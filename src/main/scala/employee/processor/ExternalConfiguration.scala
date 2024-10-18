package employee.processor

import com.typesafe.config.ConfigFactory

object ExternalConfiguration {
  private val configuration = ConfigFactory.load()
  private val INPUT_FOLDER_PROPERTY = "processor.input.folder"
  private val OUTPUT_FOLDER_PROPERTY = "processor.output.folder"
  def getInputFolder: String = configuration.getString(INPUT_FOLDER_PROPERTY)
  def getOutputFolder: String = configuration.getString(OUTPUT_FOLDER_PROPERTY)

}
