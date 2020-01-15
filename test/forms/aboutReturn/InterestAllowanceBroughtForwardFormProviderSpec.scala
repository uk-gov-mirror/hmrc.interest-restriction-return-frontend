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

import forms.behaviours.DecimalFieldBehaviours
import play.api.data.FormError

class InterestAllowanceBroughtForwardFormProviderSpec extends DecimalFieldBehaviours {

  val form = new InterestAllowanceBroughtForwardFormProvider()()

  ".value" must {

    val fieldName = "value"

    val minimum = 0
    val maximum = 999999999999.99

    val validDataGenerator = decimalInRangeWithCommas(minimum, maximum)

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validDataGenerator
    )

    behave like decimalField(
      form,
      fieldName,
      nonNumericError  = FormError(fieldName, "interestAllowanceBroughtForward.error.nonNumeric"),
      invalidNumericError = FormError(fieldName, "interestAllowanceBroughtForward.error.invalidNumeric")
    )

    behave like decimalFieldWithRange(
      form,
      fieldName,
      minimum       = minimum,
      maximum       = maximum,
      expectedError = FormError(fieldName, "interestAllowanceBroughtForward.error.outOfRange", Seq(minimum, maximum))
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, "interestAllowanceBroughtForward.error.required")
    )
  }
}