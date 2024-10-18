package employee.processor

import employee.processor.config.ExternalConfiguration

object Main {
  def main(args: Array[String]): Unit = {
    println(ExternalConfiguration.getInputFolder)
    println(ExternalConfiguration.getOutputFolder)
  }
}