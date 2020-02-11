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

import forms.behaviours.StringFieldBehaviours
import play.api.data.FormError

class ReportingCompanyCRNFormProviderSpec extends StringFieldBehaviours {

  val requiredKey = "reportingCompanyCRN.error.required"
  val invalidFormatKey = "reportingCompanyCRN.error.invalidFormat"
  val maxLength = 8
  val regexPattern = "^([0-9]{8})|([A-Za-z]{2}[0-9]{6})$"

  val form = new ReportingCompanyCRNFormProvider()()

  ".value" must {

    val fieldName = "value"

    behave like fieldThatBindsValidData(
      form,
      fieldName,
      stringsWithMaxLength(maxLength)
    )

    behave like mandatoryField(
      form,
      fieldName,
      requiredError = FormError(fieldName, requiredKey)
    )


    "error when given an invalid format for the CRN" in {

      val result = form.bind(Map(fieldName -> "InvalidCRN")).apply(fieldName)
      result.errors(0) mustEqual FormError(fieldName, invalidFormatKey, Seq(regexPattern))
    }
  }
}