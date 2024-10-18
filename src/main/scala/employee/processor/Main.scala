package employee.processor

import employee.processor.config.AppConfigurationConstants.{InputFolder, OutputFolder, SparkMaster}
import org.apache.spark.{SparkConf, SparkContext}

object Main {
  def main(args: Array[String]): Unit = {
    val sparkContext: SparkContext = new SparkContext(
      new SparkConf()
        .setAppName("CsvEmployeeProcessor")
        .setMaster(SparkMaster)
    )

    val files = sparkContext.textFile(s"$InputFolder/*.csv")
    files.foreach(f => {
      println(f)
    })
  }
}