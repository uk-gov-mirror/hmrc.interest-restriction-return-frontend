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

import forms.behaviours.StringFieldBehaviours
import models.CompanyDetailsModel
import play.api.data.{Form, FormError}

class CompanyDetailsFormProviderSpec extends StringFieldBehaviours {

  val companyNameRequiredKey = "companyDetails.companyName.error.required"
  val companyNameLengthKey = "companyDetails.companyName.error.length"
  val companyNameMaxLength = 160
  val ctutrRequiredKey = "companyDetails.ctutr.error.required"
  val ctutrRegexpKey = "companyDetails.ctutr.error.regexp"
  val ctutrChecksumKey = "companyDetails.ctutr.error.checksum"
  val companyNameField = "companyNameValue"
  val ctutrField = "ctutrValue"
  val companyName = "Company Name"

  val form = new CompanyDetailsFormProvider()()

  ".companyName" must {

    behave like fieldThatBindsValidData(
      form,
      companyNameField,
      stringsWithMaxLength(companyNameMaxLength)
    )

    behave like fieldWithMaxLength(
      form,
      companyNameField,
      maxLength = companyNameMaxLength,
      lengthError = FormError(companyNameField, companyNameLengthKey, Seq(companyNameMaxLength))
    )

    behave like mandatoryField(
      form,
      companyNameField,
      requiredError = FormError(companyNameField, companyNameRequiredKey)
    )
  }

  ".ctutr" must {

    behave like validUTR(
      form = form,
      utrFieldName = "ctutrValue",
      utrChecksumErrorKey = ctutrChecksumKey,
      utrRegexpKey = ctutrRegexpKey
    )
  }

  //noinspection ScalaStyle
  def validUTR(
                form: Form[_],
                utrFieldName: String = "value",
                utrChecksumErrorKey: String,
                utrRegexpKey: String
              ): Unit = {
    s"$utrFieldName" must {

      val validUTR: String = "1111111111"
      val invalidUTR: String = "1234567899"
      val validRegexp: String = "^[0-9]{10}$"
      val invalidRegexp: String = "1234"
      val whitespaceUTR: String = "     1    1     1   111    1   1  1  1        "

      "when binding a value which does match the regexp" when {

        "checksum fails" should {

          s"return the checksum error for $utrFieldName" in {
            val result = form.bind(Map(
              companyNameField -> companyName,
              utrFieldName -> invalidUTR))
            result.errors.headOption mustEqual Some(FormError(utrFieldName, utrChecksumErrorKey))
          }
        }

        "whitespace is removed before binding" when {

          "entered value contains whitespace" in {

            val result = form.bind(Map(
              companyNameField -> companyName,
              utrFieldName -> whitespaceUTR))
            result.value mustBe Some(CompanyDetailsModel(companyName, validUTR))
          }
        }

        "checksum is successful" should {

          "return the field value" in {
            val result = form.bind(Map(
              companyNameField -> companyName,
              utrFieldName -> validUTR))
            result.value mustBe Some(CompanyDetailsModel(companyName, validUTR))
          }
        }

        "when binding a value which does not match the regexp" should {

          "return the regexp error" in {
            val result = form.bind(Map(
              companyNameField -> companyName,
              utrFieldName -> invalidRegexp))
            result.errors.headOption mustEqual Some(FormError(utrFieldName, utrRegexpKey, Seq(validRegexp)))
          }
        }
      }
    }
  }

}
