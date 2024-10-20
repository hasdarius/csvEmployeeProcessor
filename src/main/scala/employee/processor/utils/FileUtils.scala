package employee.processor.utils

import employee.processor.config.ExternalAppConfiguration._
import employee.processor.config.SparkConfiguration.sparkContext
import employee.processor.model.{AggregatedData, Employee}
import employee.processor.utils.DateUtils.parseDate
import org.apache.hadoop.fs.{FileSystem, Path}
import org.apache.spark.rdd.RDD

import java.nio.charset.StandardCharsets
import java.nio.file.{Files, Paths}
import scala.reflect.io.File


object FileUtils {

  def cleanSparkResultsOutputFolder(): Boolean = {
    val fs: FileSystem = FileSystem.get(sparkContext.hadoopConfiguration)
    val path = new Path(OutputFolder)
    fs.delete(path, true)
  }

  /**
   * Method that read data from each file in a folder
   *
   * @param filePath  the folder location
   * @param fileType  the type of the file
   * @param delimiter the delimiter used in the file
   * @return data containing tuples of department names and employee data corresponding to the departments
   */
  def readDepartmentData(filePath: String = InputFolder,
                         fileType: String = FileType,
                         delimiter: String = Delimiter): Stream[(String, RDD[Employee])] = {

    val fs = FileSystem.get(sparkContext.hadoopConfiguration)
    val status = fs.listStatus(new Path(filePath)).toStream

    status
      .map(fileStatus => fileStatus.getPath.toString)
      .filter(fileName => fileName.endsWith(fileType))
      .map(departmentFile => (extractFileName(departmentFile, fileType), readEmployeeData(departmentFile, delimiter)))
  }

  /**
   * Method that reads all data from given path, no matter if it is folder or file.
   * THis method can be used to read both data from a department file or to
   * read data efficiently from all department files located in a folder.
   *
   * @param filePath  the file path
   * @param delimiter the delimiter used in the file(s)
   * @return employee data
   */
  def readEmployeeData(filePath: String = InputFolder,
                       delimiter: String = Delimiter): RDD[Employee] = {
    sparkContext
      .textFile(filePath)
      .flatMap(line => readLine(line, delimiter))

  }

  /**
   * Method that writes aggregated data to a location using Spark,
   *
   * @param aggregatedData the aggregated data metrics
   * @param outputFolder   the output folder
   * @param delimiter      the delimiter to be used to reconstruct the line
   */
  def writeToFileUsingSpark[T1, T2](aggregatedData: AggregatedData[T1, T2],
                                    outputFolder: String = OutputFolder,
                                    delimiter: String = Delimiter): Unit = {

    val fileLocation = constructOutputPath(aggregatedData, outputFolder)

    aggregatedData
      .content
      .map(tuple => String.join(delimiter, tuple._1.toString, tuple._2.toString))
      .repartition(1)
      .saveAsTextFile(fileLocation)
  }

  /**
   * Method that writes aggregated data to a location without using Spark.
   * This may be a viable option as the aggregated data is not that large
   *
   * @param aggregatedData the aggregated data metrics
   * @param outputFolder   the output folder
   * @param delimiter      the delimiter to be used to reconstruct the line
   */
  def writeToFileWithoutSpark[T1, T2](aggregatedData: AggregatedData[T1, T2],
                                      outputFolder: String = OutputFolder,
                                      delimiter: String = Delimiter,
                                      fileType: String = FileType): Unit = {
    val fileLocation = constructOutputPath(aggregatedData, outputFolder) + fileType
    val outputPath = Paths.get(fileLocation)

    if (Files.notExists(outputPath.getParent)) {
      Files.createDirectories(outputPath.getParent)
    }

    val content = aggregatedData
      .content
      .map(tuple => s"${tuple._1}$delimiter${tuple._2}")
      .collect()
      .mkString(System.lineSeparator())

    Files.write(outputPath, content.getBytes(StandardCharsets.UTF_8))
  }

  private def constructOutputPath[T2, T1](aggregatedData: AggregatedData[T1, T2], outputFolder: String) = {
    String.join(File.separator, outputFolder, aggregatedData.department, aggregatedData.fileName)
  }

  private def extractFileName(filePath: String,
                                    fileType: String): String = {
    filePath
      .split(File.separator)
      .last
      .stripSuffix(fileType)
  }


  private def readLine(line: String,
                       delimiter: String): Option[Employee] = {
    line.split(delimiter) match {
      case Array(name, dateOfBirth) => parseDate(dateOfBirth).map(date => Employee(name, date))
      case _ => None
    }
  }

}
