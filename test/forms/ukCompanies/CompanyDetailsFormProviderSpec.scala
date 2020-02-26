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

package forms.ukCompanies

import forms.behaviours.StringFieldBehaviours
import forms.behaviours.UTRFieldBehaviours
import play.api.data.FormError

class CompanyDetailsFormProviderSpec extends StringFieldBehaviours with UTRFieldBehaviours {

  val companyNameRequiredKey = "companyDetails.companyName.error.required"
  val companyNameLengthKey = "companyDetails.companyName.error.length"
  val companyNameMaxLength = 160
  val ctutrRequiredKey = "companyDetails.ctutr.error.required"
  val ctutrRegexpKey = "companyDetails.ctutr.error.regexp"
  val ctutrChecksumKey = "companyDetails.ctutr.error.checksum"

  val form = new CompanyDetailsFormProvider()()

  ".companyName" must {

    val companyNameField = "companyNameValue"

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
  behave like validUTR(
    form = form,
    utrFieldName = "ctutrValue",
    utrChecksumErrorKey = ctutrChecksumKey,
    utrRegexpKey = ctutrRegexpKey
  )
}
