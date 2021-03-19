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

import forms.behaviours.DecimalFieldBehaviours
import play.api.data.FormError

class RestrictionAmountSameAPFormProviderSpec extends DecimalFieldBehaviours {

  ".value" must {

    val fieldName = "value"
    val minimum = 0
    val maximum = 999999999999999.99
    val validDataGenerator = decimalInRangeWithCommas(minimum, maximum)

    val netTaxInterestExpense : BigDecimal = 999999999999999.99 + 9
    val lowNetTaxInterestExpense : BigDecimal = 123

    val formWithMaxNetTaxInterestExpense = new RestrictionAmountSameAPFormProvider()(netTaxInterestExpense)
    val formlowNetTaxInterestExpense = new RestrictionAmountSameAPFormProvider()(lowNetTaxInterestExpense)

    behave like fieldThatBindsValidData(
      formWithMaxNetTaxInterestExpense,
      fieldName,
      validDataGenerator
    )

    behave like decimalField(
      formWithMaxNetTaxInterestExpense,
      fieldName,
      nonNumericError  = FormError(fieldName, "restrictionAmountSameAP.error.nonNumeric"),
      invalidNumericError = FormError(fieldName, "restrictionAmountSameAP.error.invalidNumeric")
    )

    behave like decimalFieldWithRange(
      formWithMaxNetTaxInterestExpense,
      fieldName,
      minimum       = minimum,
      maximum       = maximum,
      expectedError = FormError(fieldName, "restrictionAmountSameAP.error.outOfRange", Seq(minimum, maximum))
    )

    behave like mandatoryField(
      formWithMaxNetTaxInterestExpense,
      fieldName,
      requiredError = FormError(fieldName, "restrictionAmountSameAP.error.required")
    )

    behave like decimalFieldWithMaximum(
      formlowNetTaxInterestExpense,
      fieldName,
      maximum       = lowNetTaxInterestExpense,
      expectedError = FormError(fieldName, "restrictionAmountSameAP.error.expenseAmount")
    )
  }
}
