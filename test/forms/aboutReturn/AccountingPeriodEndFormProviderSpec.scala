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

import forms.behaviours.DateBehaviours

class AccountingPeriodEndFormProviderSpec extends DateBehaviours {

  val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate
  def form(startDate: LocalDate) = new AccountingPeriodEndFormProvider().apply(startDate)

  ".value" should {

    val validData = datesBetween(
      min = LocalDate.of(2000, 1, 1),
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(form(now), "value", validData)

    behave like mandatoryDateField(form(now), "value", "accountingPeriodEnd.error.required.all")
  }

  "end date" should {

    val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate

    "pass validation if 1 day after the start date" in {
      form(startDate = now).fillAndValidate(now.plusDays(1)).errors.length mustBe 0
    }

    "pass validation if 18 months after the start date" in {
      form(startDate = now).fillAndValidate(now.plusMonths(18)).errors.length mustBe 0
    }

    "pass validation if 17 months and 27 days after the start date" in {
      form(startDate = now).fillAndValidate(now.plusMonths(17).plusDays(27)).errors.length mustBe 0
    }

    "fail validation if 18 months and 1 day after the start date" in {
      form(startDate = now).fillAndValidate(now.plusMonths(18).plusDays(1)).errors.length mustBe 1
    }

    "fail validation if both are the same date in"  in {
      form(startDate = now).fillAndValidate(now).errors.length mustBe 1
    }

    "fail validation if after start date"  in {
      form(startDate = now).fillAndValidate(now.minusDays(1)).errors.length mustBe 1
    }

  }
}
