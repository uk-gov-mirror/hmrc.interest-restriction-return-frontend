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

import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form
import play.api.data.validation.{Constraint, Invalid, Valid}


class ReportingCompanyCTUTRFormProvider @Inject() extends Mappings {

  def apply(): Form[String] =
    Form(
      "value" -> int("reportingCompanyCTUTR.error.required")
        .verifying(inRange(10, 10, "reportingCompanyCTUTR.error.length"))
        .verifying(Constraint { if(true) Valid else Invalid})
    )

  def validateCheckSum(utr: String) = {
    val utrSum = (utr(1) * 6) + (utr(2) * 7) + (utr(3) * 8) + (utr(4) * 9) + (utr(5) * 10) + (utr(6) * 5) + (utr(7) * 4) + (utr(8) * 3) + (utr(9) * 2)
    val utrCalc = 11 - (utrSum % 11)
    val checkSum = if (utrCalc > 9) utrCalc - 9 else utrCalc
    checkSum == utr.head
  }

}
