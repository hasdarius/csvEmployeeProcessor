package employee.processor

import employee.processor.config.SparkConfiguration.sparkContext
import employee.processor.service.CompanyProcessorService.extractTransformCompanyData
import employee.processor.utils.FileUtils.{cleanSparkResultsOutputFolder, writeToFileUsingSpark, writeToFileWithoutSpark}

object Main {
  def main(args: Array[String]): Unit = {
    cleanSparkResultsOutputFolder()
    val aggregatedData = extractTransformCompanyData()
    aggregatedData.foreach(data => writeToFileWithoutSpark(data))
    sparkContext.stop()
  }
}