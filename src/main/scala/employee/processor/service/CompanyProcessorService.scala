package employee.processor.service

import employee.processor.config.SparkConfiguration.sparkContext
import employee.processor.model.{AggregatedData, Employee}
import employee.processor.utils.FileUtils.{readDepartmentData, readEmployeeData}
import org.apache.spark.rdd.RDD

object CompanyProcessorService {

  def extractTransformCompanyData(): Stream[AggregatedData[String, String]] = {
    val employeeDataPerDepartment = readDepartmentData()
    val employeeDataPerCompany = ("Overall", readEmployeeData())

    (employeeDataPerCompany #:: employeeDataPerDepartment)
      .flatMap(prepareAggregatedData)
  }

  private def prepareAggregatedData(departmentData: (String, RDD[Employee])): Stream[AggregatedData[String, String]] = {

    val department = departmentData._1
    val employees = departmentData._2
    val curriedDistribution = constructDistribution(employees) _

    Stream(
      AggregatedData(department = department, fileName = "BornDecadeDistribution", content = curriedDistribution(_.getYearDecade)),
      AggregatedData(department = department, fileName = "YearDecadeDistribution", content = curriedDistribution(_.getBornDecade)),
      AggregatedData(department = department, fileName = "AgeDistribution", content = curriedDistribution(_.getAge)),
      AggregatedData(department = department, fileName = "OtherMetrics", content = computeAgeStatistics(employees))
    )
  }

  private def constructDistribution(employees: RDD[Employee])
                                   (keyFunction: Employee => Int): RDD[(String, String)] = {
    employees
      .map(employee => (keyFunction(employee), 1L))
      .reduceByKey(_ + _)
      .map(tuple => (tuple._1.toString, tuple._2.toString))
  }

  private def computeAgeStatistics(employees: RDD[Employee]): RDD[(String, String)] = {
    val ageRdd = employees.map(_.getAge)
    sparkContext.parallelize(
      Stream(
        ("AverageAge", ageRdd.mean().toString),
        ("StandardAgeDeviation", ageRdd.stdev().toString),
        ("NumberOfEmployees", employees.count().toString)
      )
    )
  }

}
