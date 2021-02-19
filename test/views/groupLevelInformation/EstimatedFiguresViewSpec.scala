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

package views.groupLevelInformation

import assets.messages.{BaseMessages, SectionHeaderMessages}
import forms.groupLevelInformation.EstimatedFiguresFormProvider
import models.{EstimatedFigures, NormalMode}
import play.api.Application
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.CheckboxViewBehaviours
import views.html.groupLevelInformation.EstimatedFiguresView
import controllers.groupLevelInformation.routes.EstimatedFiguresController

class EstimatedFiguresViewSpec extends CheckboxViewBehaviours[EstimatedFigures] {

  val messageKeyPrefix = "estimatedFigures"
  val section = Some(messages("section.groupLevelInformation"))
  val form = new EstimatedFiguresFormProvider()()

    "EstimatedFiguresView" must {

      val view = viewFor[EstimatedFiguresView](Some(emptyUserAnswers))

      def checkboxes(form: Form[Set[EstimatedFigures]]) = EstimatedFigures.values.map(EstimatedFigures.estimatedFigureToCheckboxItem(form, _))

      def applyView(form: Form[Set[EstimatedFigures]]): HtmlFormat.Appendable = {
        val view = viewFor[EstimatedFiguresView](Some(emptyUserAnswers))
        view.apply(form, NormalMode, checkboxes(form))(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.groupLevelInformation)

      behave like checkboxPage(form, applyView, messageKeyPrefix, checkboxes(form), messages("section.groupLevelInformation"))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
  }
}
