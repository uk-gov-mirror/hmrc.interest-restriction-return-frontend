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

import java.time.LocalDate
import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data.Form

class AccountingPeriodEndFormProvider @Inject() extends Mappings {

  def apply(startDate: LocalDate): Form[LocalDate] =
    Form(
      "value" -> localDate(
        invalidKey     = "accountingPeriodEnd.error.invalid",
        allRequiredKey = "accountingPeriodEnd.error.required.all",
        twoRequiredKey = "accountingPeriodEnd.error.required.two",
        requiredKey    = "accountingPeriodEnd.error.required"
      )
        .verifying("accountingPeriodEnd.error.range", endDate =>
          endDate.isAfter(startDate) && !endDate.minusMonths(18).isAfter(startDate))

    )
}
