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

package views.groupStructure

import assets.constants.BaseConstants
import assets.messages.groupStructure.LocalRegistrationNumberMessages
import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.groupStructure.routes
import forms.groupStructure.LocalRegistrationNumberFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.BaseSelectors
import views.behaviours.StringViewBehaviours
import views.html.groupStructure.LocalRegistrationNumberView
class LocalRegistrationNumberViewSpec extends StringViewBehaviours with BaseConstants {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = "localRegistrationNumber"
  val section = Some(messages("section.groupStructure"))
  val form = new LocalRegistrationNumberFormProvider()()

  "LocalRegistrationNumberView" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[LocalRegistrationNumberView](Some(emptyUserAnswers))
      view.apply(form, NormalMode, companyNameModel.name)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(
      view = applyView(form),
      messageKeyPrefix = messageKeyPrefix,
      section = section,
      headingArgs = Seq(companyNameModel.name)
    )

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.groupStructure)

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like stringPage(form, applyView, messageKeyPrefix, routes.LocalRegistrationNumberController.onSubmit(NormalMode).url)

    behave like pageWithSaveForLater(applyView(form))

    lazy val document = asDocument(applyView(form))

    "have a hint" which {

      lazy val hint = document.select(Selectors.hint)

      "has the correct text" in {
        hint.text mustBe LocalRegistrationNumberMessages.hint
      }
    }

    "have " which {

      lazy val hint = document.select(Selectors.label)

      "has the correct text" in {
        hint.text mustBe LocalRegistrationNumberMessages.hint
      }
    }
  }
}
