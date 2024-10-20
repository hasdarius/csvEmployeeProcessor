package employee.processor.config

import com.typesafe.config.ConfigFactory
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}


object ExternalAppConfiguration {

  val InputFolder: String = getEnvironmentVariable("INPUT_FOLDER_LOCATION")
  val OutputFolder: String = getEnvironmentVariable("OUTPUT_FOLDER_LOCATION")
  val SparkMaster: String = sys.env.getOrElse("SPARK_MASTER", "local[*]")
  val SparkAppName: String = sys.env.getOrElse("SPARK_APP_NAME", "CsvEmployeeProcessor")
  val FileType: String = sys.env.getOrElse("FILE_TYPE", ".csv")
  val Delimiter: String = sys.env.getOrElse("DELIMITER", ",")
  val CustomDateFormatter: DateTimeFormatter = DateTimeFormat.forPattern(sys.env.getOrElse("DATE_FORMAT", "yyyy-MM-dd"))

  private def getEnvironmentVariable(environmentVariableName: String): String = {
    sys.env.get(environmentVariableName) match {
      case Some(value) => value
      case None => throw new IllegalStateException(s"External property $environmentVariableName is not present")
    }
  }

}
