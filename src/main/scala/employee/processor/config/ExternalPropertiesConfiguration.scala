package employee.processor.config

import com.typesafe.config.{Config, ConfigFactory}
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

import scala.util.{Failure, Success, Try}


object ExternalPropertiesConfiguration {

  private val config: Config = ConfigFactory.load()

  val InputFolder: String = getEnvironmentVariable("app.input-folder-location")
  val OutputFolder: String = getEnvironmentVariable("app.output-folder-location")
  val SparkMaster: String = getOrDefaultVariable("app.spark_master", "local[*]")
  val SparkAppName: String = getOrDefaultVariable("app.spark_name", "CsvEmployeeProcessor")
  val FileType: String = getOrDefaultVariable("app.file_type", ".csv")
  val Delimiter: String = getOrDefaultVariable("app.delimiter", ",")
  val DateFormat: String = getOrDefaultVariable("app.date_format", "yyyy-MM-dd")

  private def getEnvironmentVariable(variableName: String): String = {
    Try(config.getString(variableName)) match {
      case Success(value) => value
      case Failure(_) => throw new IllegalStateException(s"External property $variableName is not present")
    }
  }

  private def getOrDefaultVariable(variableName: String,
                                   defaultValue: String) = {
    Try(config.getString(variableName)) match {
      case Success(value) => value
      case Failure(_) => defaultValue
    }
  }

}
