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

class ReactivationAmountFormProviderSpec extends DecimalFieldBehaviours {

  val form = new ReactivationAmountFormProvider()()

  ".value" must {

    val fieldName = "value"

    val minimum: BigDecimal = 0
    val maximum: BigDecimal = 999999999999999.99

    val validDataGenerator = decimalInRangeWithCommas(minimum, maximum)

    val nonNumericError  = FormError(fieldName, "reactivationAmount.error.nonNumeric")
    val invalidNumericError = FormError(fieldName, "reactivationAmount.error.invalidNumeric")
    val outOfRangeError = FormError(fieldName, "reactivationAmount.error.outOfRange", Seq(minimum, maximum))

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      validDataGenerator
    )

    "not bind non-numeric numbers" in {

      forAll(nonNumerics -> "nonNumeric") {
        nonNumeric =>
          val result = form.bind(Map(fieldName -> nonNumeric))
          result.errors mustEqual Seq(nonNumericError)
      }
    }

    "not bind invalid decimals (over 2dp)" in {
      val result = form.bind(Map(fieldName -> "12.123"))
      result.errors mustEqual Seq(invalidNumericError)
    }

    "value is less than the minimum" in {

      val result = form.bind(Map(fieldName -> (minimum - 0.01).toString))
      result.errors mustEqual Seq(outOfRangeError)
    }

    "value is greater than the maximum" in {

      val result = form.bind(Map(fieldName -> (maximum + 0.01).toString))
      result.errors mustEqual Seq(outOfRangeError)
    }

    s"not bind outside the range $minimum to $maximum" when {

      "value is greater than the maximum" in {

        val result = form.bind(Map(fieldName -> (maximum + 0.01).toString))
        result.errors mustEqual Seq(outOfRangeError)
      }

      "value is less than the minimum" in {

        val result = form.bind(Map(fieldName -> (minimum - 0.01).toString))
        result.errors mustEqual Seq(outOfRangeError)
      }
    }

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, "reactivationAmount.error.required")
    )
  }
}
