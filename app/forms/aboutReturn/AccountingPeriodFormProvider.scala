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

package forms.aboutReturn

import java.time.{Instant, ZoneOffset}
import javax.inject.Inject

import forms.mappings.Mappings
import play.api.data._
import play.api.data.Forms._
import models.returnModels.AccountingPeriodModel
import java.time.LocalDate

class AccountingPeriodFormProvider @Inject() extends Mappings {

  def apply(): Form[AccountingPeriodModel] = {
    val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate
    Form(mapping(
      "startValue" -> localDate(
        invalidKey = "accountingPeriod.start.error.invalid",
        allRequiredKey = "accountingPeriod.start.error.required.all",
        twoRequiredKey = "accountingPeriod.start.error.required.two",
        requiredKey = "accountingPeriod.start.error.required"
       ),
      "endValue" -> localDate(
        invalidKey = "accountingPeriod.end.error.invalid",
        allRequiredKey = "accountingPeriod.end.error.required.all",
        twoRequiredKey = "accountingPeriod.end.error.required.two",
        requiredKey = "accountingPeriod.end.error.required"
       )
      )(AccountingPeriodModel.apply)(AccountingPeriodModel.unapply)
      .verifying("accountingPeriod.start.error.range.above", period => !period.startDate.isAfter(now))
      .verifying("accountingPeriod.start.error.range.below", period => !period.startDate.isBefore(LocalDate.parse("2016-10-01")))
      .verifying("accountingPeriod.end.error.range", period => period.endDate.isAfter(period.startDate) && !period.endDate.minusMonths(18).isAfter(period.startDate))
      
    )
  }
}
