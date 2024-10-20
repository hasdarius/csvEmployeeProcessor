package employee.processor.utils

import employee.processor.config.ExternalAppConfiguration.CustomDateFormatter
import org.joda.time.LocalDate
import org.joda.time.format.DateTimeFormatter

import scala.util.{Failure, Success, Try}

object DateUtils {
  def parseDate(dateString: String,
                dateFormatter: DateTimeFormatter = CustomDateFormatter): Option[LocalDate] = {
    Try(dateFormatter.parseLocalDate(dateString)) match {
      case Success(date) => Some(date)
      case Failure(_) => None // fail silently, only get rows from which data can be parsed
    }
  }

}
