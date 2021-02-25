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
import forms.ukCompanies.CompanyEstimatedFiguresFormProvider
import models.{CompanyEstimatedFigures, NormalMode}
import play.api.Application
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.CheckboxViewBehaviours
import views.html.ukCompanies.CompanyEstimatedFiguresView
import controllers.ukCompanies.routes.CompanyEstimatedFiguresController

class CompanyEstimatedFiguresViewSpec extends CheckboxViewBehaviours[CompanyEstimatedFigures] {

  val messageKeyPrefix = "companyEstimatedFigures"
  val section = Some(messages("section.ukCompanies"))
  val form = new CompanyEstimatedFiguresFormProvider()()

    "CompanyEstimatedFiguresView" must {

      val view = viewFor[CompanyEstimatedFiguresView](Some(emptyUserAnswers))

      def checkboxes(form: Form[Set[CompanyEstimatedFigures]]) = CompanyEstimatedFigures.values.map(CompanyEstimatedFigures.estimatedFigureToCheckboxItem(form, _))

      def applyView(form: Form[Set[CompanyEstimatedFigures]]): HtmlFormat.Appendable = {
        val view = viewFor[CompanyEstimatedFiguresView](Some(emptyUserAnswers))
        view.apply(form, NormalMode, checkboxes(form), CompanyEstimatedFiguresController.onSubmit(1, NormalMode))(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.ukCompanies)

      behave like checkboxPage(form, applyView, messageKeyPrefix, checkboxes(form), messages("section.ukCompanies"))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
  }
}
