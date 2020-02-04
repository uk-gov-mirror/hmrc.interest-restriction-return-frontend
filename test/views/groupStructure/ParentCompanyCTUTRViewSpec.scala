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

import assets.messages.groupStructure.{ParentCompanyCTUTRMessages, ParentCompanySAUTRMessages}
import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.groupStructure.routes
import forms.groupStructure.ParentCompanyCTUTRFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.BaseSelectors
import views.behaviours.StringViewBehaviours
import views.html.groupStructure.ParentCompanyCTUTRView

class ParentCompanyCTUTRViewSpec extends StringViewBehaviours {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = "parentCompanyCTUTR"

  val form = new ParentCompanyCTUTRFormProvider()()
  val section = Some(messages("section.groupStructure"))

  s"ParentCompanyCTUTR view" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[ParentCompanyCTUTRView](Some(emptyUserAnswers))
      view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(form), messageKeyPrefix, section = section)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.groupStructure)

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(applyView(form))

    behave like stringPage(form, applyView, messageKeyPrefix, routes.ParentCompanyCTUTRController.onSubmit(NormalMode).url, section = section)

    lazy val document = asDocument(applyView(form))

    "have a hint" which {

      lazy val hint = document.select(Selectors.hint)

      "has the correct text" in {
        hint.text mustBe ParentCompanyCTUTRMessages.hint
      }

      "has a link to find a lost UTR" which {

        val hintLink = hint.select("a")

        "has the correct URL" in {
          hintLink.attr("href") mustBe frontendAppConfig.findLostUTRUrl
        }

        "has the correct link text" in {
          hintLink.text mustBe ParentCompanyCTUTRMessages.hintLink
        }
      }
    }


  }
}