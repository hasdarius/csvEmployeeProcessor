package employee.processor.model

import org.joda.time.{LocalDate, Period}


case class Employee(name: String,
                    dateOfBirth: LocalDate) {
  def getBornDecade: Int = dateOfBirth.getYear / 10 * 10

  def getYearDecade: Int = getAge / 10 * 10

  def getAge: Int = new Period(dateOfBirth, LocalDate.now()).getYears
}
