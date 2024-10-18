package model

import employee.processor.model.Employee

import java.time.LocalDate
import org.scalatest.funsuite.AnyFunSuite

class EmployeeTest extends AnyFunSuite {
  private val Today: LocalDate = LocalDate.now()
  private val OneYearAgo: LocalDate = LocalDate.now().minusYears(1)
  private val OneYearAgoDecade: Int = OneYearAgo.getYear / 10 * 10
  private val CurrentYear: Int = Today.getYear

  test("Rafael Nadal - should return correct decade and age") {
    val employee = Employee("Rafael Nadal", LocalDate.of(1986, 6, 3))
    assert(employee.getDecade == 1980)
    assert(employee.getAge == CurrentYear - 1986)
  }

  test("Neymar - should return correct decade and age") {
    val employee = Employee("Neymar", LocalDate.of(1992, 2, 5))
    assert(employee.getDecade == 1990)
    assert(employee.getAge == CurrentYear - 1992)
  }

  test("Lamine Yamal - should return correct decade and age") {
    val employee = Employee("Lamine Yamal", LocalDate.of(2007, 9, 13))
    assert(employee.getDecade == 2000)
    assert(employee.getAge == CurrentYear - 2007)
  }

  test("Has Darius - should return correct decade and age") {
    val employee = Employee("Has Darius", LocalDate.of(2000, 4, 17))
    assert(employee.getDecade == 2000)
    assert(employee.getAge == CurrentYear - 2000)
  }

  test("Yesterday was birthdate - should return correct decade and age") {
    val employee = Employee("Has Darius", OneYearAgo minusDays 1)
    assert(employee.getDecade == OneYearAgoDecade)
    assert(employee.getAge == 1)
  }

  test("Tommorow is birthday - should return correct decade and age") {
    val employee = Employee("Has Darius", OneYearAgo plusDays 1)
    assert(employee.getDecade == OneYearAgoDecade)
    assert(employee.getAge == 0)
  }
}
