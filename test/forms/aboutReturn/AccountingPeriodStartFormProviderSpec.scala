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

import forms.aboutReportingCompany.AccountingPeriodStartFormProvider
import forms.behaviours.DateBehaviours

class AccountingPeriodStartFormProviderSpec extends DateBehaviours {

  val form = new AccountingPeriodStartFormProvider()()

  ".value" should {


    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(form, "value", validData)

    behave like mandatoryDateField(form, "value", "accountingPeriodStart.error.required.all")
  }

  "start date" should {

    val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate

    "pass validation if in the present" in {
      form.fillAndValidate(now).errors.length mustBe 0
    }

    "pass validation if in the past" in {
      form.fillAndValidate(now.minusDays(1L)).errors.length mustBe 0
    }

    "pass validation if several thousand years in the past" in {
      form.fillAndValidate(now.minusYears(4510L)).errors.length mustBe 0
    }

    "fail validation if 1 day in the future" in {
      form.fillAndValidate(now.plusDays(1L)).errors.length mustBe 1
    }

  }
}
