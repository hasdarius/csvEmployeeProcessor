package employee.processor.config

object AppConfigurationConstants {

  val InputFolder: String = getEnvironmentVariable("INPUT_FOLDER_LOCATION")
  val OutputFolder: String = getEnvironmentVariable("OUTPUT_FOLDER_LOCATION")
  val SparkMaster: String = sys.env.getOrElse("SPARK_MASTER", "local[*]")
  val CsvDelimiter: String = sys.env.getOrElse("DELIMITER", ",")

  private def getEnvironmentVariable(environmentVariableName: String): String = {
    sys.env.get(environmentVariableName) match {
      case Some(value) => value
      case None => throw new IllegalStateException(s"External property $environmentVariableName is not present")
    }
  }

}
