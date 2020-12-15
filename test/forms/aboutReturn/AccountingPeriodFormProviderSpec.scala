/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package forms.aboutReturn

import java.time.{Instant, LocalDate, ZoneOffset}

import forms.behaviours.FieldBehaviours
import play.api.data.FormError
import models.returnModels.AccountingPeriodModel
import org.scalacheck.Gen
import play.api.data.{Form, FormError}

class AccountingPeriodFormProviderSpec extends FieldBehaviours {

  val form = new AccountingPeriodFormProvider()()
  val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate
  val min = LocalDate.of(2000, 1, 1)

  "The dates in the form" should {

    val validData = datesBetween(
      min = min,
      max = now
    )

    behave like dateFields(form, validData)

    "be mandatory" in {
      val result = form.bind(Map.empty[String, String])
      result.errors must contain(FormError("startValue", "accountingPeriod.start.error.required.all"))
      result.errors must contain(FormError("endValue", "accountingPeriod.end.error.required.all"))
    }
  }

  "start date" should {

    "pass validation if in the present" in {
      val model = AccountingPeriodModel(now, now.plusDays(1L))
      fillForm(form, model).errors.length mustBe 0
    }

    "pass validation if in the past" in {
      val model = AccountingPeriodModel(now.minusDays(1L), now.plusDays(1))
      fillForm(form, model).errors.length mustBe 0
    }

    "fail validation if 1 day before the min date" in {
      val model = AccountingPeriodModel(min.minusDays(1L), min)
      val errors = fillForm(form, model).errors
      errors.length mustBe 1
      errors.head mustBe FormError("startValue", "accountingPeriod.start.error.range.below")
    }

    "fail validation if 1 day in the future" in {
      val model = AccountingPeriodModel(now.plusDays(1L), now.plusDays(2L))
      val errors = fillForm(form, model).errors
      errors.length mustBe 1
      errors.head mustBe FormError("startValue", "accountingPeriod.start.error.range.above")
    }

  }

  "end date" should {

    val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate

    "pass validation if 1 day after the start date" in {
      val model = AccountingPeriodModel(now, now.plusDays(1L))
      fillForm(form, model).errors.length mustBe 0
    }

    "pass validation if 18 months after the start date" in {
      val model = AccountingPeriodModel(now, now.plusMonths(18))
      fillForm(form, model).errors.length mustBe 0
    }

    "pass validation if 17 months and 27 days after the start date" in {
      val model = AccountingPeriodModel(now, now.plusMonths(17).plusDays(27))
      fillForm(form, model).errors.length mustBe 0
    }

    "fail validation if 18 months and 1 day after the start date" in {
      val model = AccountingPeriodModel(now, now.plusMonths(18).plusDays(1))
      fillForm(form, model).errors.length mustBe 1
    }

    "fail validation if both are the same date in"  in {
      val model = AccountingPeriodModel(now, now)
      fillForm(form, model).errors.length mustBe 1
    }

    "fail validation if before start date"  in {
      val model = AccountingPeriodModel(now, now.minusDays(1))
      fillForm(form, model).errors.length mustBe 1
    }

  }

  def dateFields(form: Form[AccountingPeriodModel], validData: Gen[LocalDate]): Unit = {
    "bind valid data" in {
      forAll(validData -> "valid date") {
        date =>
          val datePlusOneMonth = date.plusMonths(1L)        
          val result = fillForm(form, AccountingPeriodModel(date, datePlusOneMonth))
          result.value.value mustEqual AccountingPeriodModel(date, datePlusOneMonth)
      }
    }
  }

  def fillForm(form: Form[AccountingPeriodModel], model: AccountingPeriodModel): Form[AccountingPeriodModel] = {
    val data = Map(
      "startValue.day"   -> model.startDate.getDayOfMonth.toString,
      "startValue.month" -> model.startDate.getMonthValue.toString,
      "startValue.year"  -> model.startDate.getYear.toString,
      "endValue.day"   -> model.endDate.getDayOfMonth.toString,
      "endValue.month" -> model.endDate.getMonthValue.toString,
      "endValue.year"  -> model.endDate.getYear.toString
    )
    form.bind(data)
  }

}
