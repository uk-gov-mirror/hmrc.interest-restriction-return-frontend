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
import forms.UTRFormValidation
import forms.mappings.Constraints
import javax.inject.Inject
import models.ukCompanies.CompanyDetails
import play.api.data.Form
import play.api.data.Forms._


class CompanyDetailsFormProvider @Inject() extends Constraints with UTRFormValidation {

  def apply(): Form[CompanyDetails] =
    Form(mapping(
      "companyNameValue" -> nonEmptyText,
      "ctutrValue" -> nonEmptyText
      .verifying(maxLength(160, "companyDetails.companyName.error.length"))
      .verifying(regexp("^[0-9]{10}$", "companyDetails.ctutr.error.length"))
      .verifying(checksum("companyDetails.ctutr.error.checksum"))
      )(CompanyDetails.apply)(CompanyDetails.unapply)
    )
}
