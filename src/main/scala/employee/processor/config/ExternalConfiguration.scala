package employee.processor.config

import com.typesafe.config.ConfigFactory

object ExternalConfiguration {
  private val configuration = ConfigFactory.load()

  def getInputFolder: String = configuration.getString("processor.input.folder")
  def getOutputFolder: String = configuration.getString("processor.output.folder")

}
