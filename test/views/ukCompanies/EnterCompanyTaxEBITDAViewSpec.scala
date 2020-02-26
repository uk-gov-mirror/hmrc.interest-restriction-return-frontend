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

package views.ukCompanies

import assets.constants.fullReturn.UkCompanyConstants.companyNameModel
import assets.messages.BaseMessages
import assets.messages.ukCompanies.CompanyTaxEBITDAMessages
import controllers.ukCompanies.routes
import forms.ukCompanies.EnterCompanyTaxEBITDAFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.DecimalViewBehaviours
import views.html.ukCompanies.EnterCompanyTaxEBITDAView

class EnterCompanyTaxEBITDAViewSpec extends DecimalViewBehaviours  {

  val messageKeyPrefix = "enterCompanyTaxEBITDA"
  val form = new EnterCompanyTaxEBITDAFormProvider()()
  val view = viewFor[EnterCompanyTaxEBITDAView]()
  val section = Some(CompanyTaxEBITDAMessages.subheading(companyNameModel.name))

  "EnterCompanyTaxEBITDAView" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      view.apply(form, NormalMode, companyNameModel.name, onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(form), messageKeyPrefix, section = section)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), section.get)

    behave like decimalPage(form, applyView, messageKeyPrefix, routes.EnterCompanyTaxEBITDAController.onSubmit(NormalMode).url, section = section)

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(applyView(form))
  }
}
