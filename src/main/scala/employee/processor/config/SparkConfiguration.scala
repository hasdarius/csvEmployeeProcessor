package employee.processor.config

import employee.processor.config.ExternalPropertiesConfiguration.{SparkAppName, SparkMaster}
import org.apache.spark.{SparkConf, SparkContext}

object SparkConfiguration {
  val sparkContext: SparkContext = new SparkContext(
    new SparkConf()
      .setAppName(SparkAppName)
      .setMaster(SparkMaster)
  )
}
