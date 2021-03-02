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

package views.ukCompanies

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.ukCompanies.routes
import forms.ukCompanies.CompanyContainsEstimatesFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.ukCompanies.CompanyContainsEstimatesView
import controllers.ukCompanies.routes.CompanyContainsEstimatesController

class CompanyContainsEstimatesViewSpec extends YesNoViewBehaviours  {

  val messageKeyPrefix = "companyContainsEstimates"
  val section = Some(messages("section.ukCompanies"))
  val form = new CompanyContainsEstimatesFormProvider()()

    "CompanyContainsEstimatesView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[CompanyContainsEstimatesView](Some(emptyUserAnswers))
        view.apply(form, NormalMode, "Company 1", CompanyContainsEstimatesController.onSubmit(1, NormalMode))(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = Some("Company 1"))

      behave like pageWithSubHeading(applyView(form), "Company 1")

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(form, applyView, messageKeyPrefix, routes.CompanyContainsEstimatesController.onSubmit(1, NormalMode).url, section = Some("Company 1"))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
