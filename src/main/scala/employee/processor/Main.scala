package employee.processor

object Main {
  def main(args: Array[String]): Unit = {
    println(ExternalConfiguration.getInputFolder)
    println(ExternalConfiguration.getOutputFolder)
  }
}