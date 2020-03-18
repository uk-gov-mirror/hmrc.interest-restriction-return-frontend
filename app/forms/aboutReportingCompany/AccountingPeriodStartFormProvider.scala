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

package forms.aboutReportingCompany

import java.time.{Instant, LocalDate, ZoneOffset}
import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class AccountingPeriodStartFormProvider @Inject() extends Mappings {

  def apply(): Form[LocalDate] = {
    val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate
    Form(
      "value" -> localDate(
        invalidKey = "accountingPeriodStart.error.invalid",
        allRequiredKey = "accountingPeriodStart.error.required.all",
        twoRequiredKey = "accountingPeriodStart.error.required.two",
        requiredKey = "accountingPeriodStart.error.required"
      ).verifying("accountingPeriodStart.error.range.above", period => !period.isAfter(now)
      ).verifying("accountingPeriodStart.error.range.below", period => !period.isBefore(LocalDate.parse("2000-01-01")))
    )
  }
}
