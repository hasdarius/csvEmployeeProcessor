package employee.processor.model

import java.time.{LocalDate, Period}

case class Employee(name: String, dateOfBirth: LocalDate) {

  def getDecade: Int = dateOfBirth.getYear / 10 * 10
  def getAge: Int = dateOfBirth.until(LocalDate.now()).getYears
}
