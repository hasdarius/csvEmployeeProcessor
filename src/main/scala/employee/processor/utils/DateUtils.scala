package employee.processor.utils

import employee.processor.config.ExternalPropertiesConfiguration.DateFormat
import org.joda.time.LocalDate
import org.joda.time.format.{DateTimeFormat, DateTimeFormatter}

import scala.util.{Failure, Success, Try}

object DateUtils {

  private val dateFormatter: DateTimeFormatter = DateTimeFormat.forPattern(DateFormat)

  def parseDate(dateString: String,
                dateFormatter: DateTimeFormatter = dateFormatter): Option[LocalDate] = {
    Try(dateFormatter.parseLocalDate(dateString)) match {
      case Success(date) => Some(date)
      case Failure(_) => None // fail silently, only get rows from which data can be parsed
    }
  }

}
