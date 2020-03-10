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

package views.startReturn

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.startReturn.routes
import forms.startReturn.AgentNameFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.Twirl
import views.behaviours.StringViewBehaviours
import views.html.startReturn.AgentNameView

class AgentNameViewSpec extends StringViewBehaviours {

  val messageKeyPrefix = "agentName"
  val section = Some(messages("section.startReturn"))
  val form = new AgentNameFormProvider()()

  Seq(Twirl).foreach { templatingSystem =>

    s"AgentName ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[AgentNameView](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.startReturn)

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like stringPage(form, applyView, messageKeyPrefix, routes.AgentNameController.onSubmit(NormalMode).url, section = section)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
}
