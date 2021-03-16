/*
 * Copyright 2021 HM Revenue & Customs
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

package forms.ukCompanies

import java.time.{LocalDate, ZoneOffset}

import forms.behaviours.DateBehaviours

class CompanyAccountingPeriodEndDateFormProviderSpec extends DateBehaviours {

  val minDate = LocalDate.of(2017, 1, 1)
  val minDateString = "01 01 2017"
  val form = new CompanyAccountingPeriodEndDateFormProvider()(minDate)

  ".value" should {

    val validData = datesBetween(
      min = minDate,
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(form, "value", validData)

    behave like mandatoryDateField(form, "value", "companyAccountingPeriodEndDate.error.required.all")

    behave like dateFieldWithMin(form, "value", minDate.plusDays(1), "End date must be after 01 01 2017")
  }
}
