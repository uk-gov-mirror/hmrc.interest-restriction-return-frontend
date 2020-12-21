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

package views.aboutReturn

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.aboutReturn.routes
import forms.aboutReturn.TellUsWhatHasChangedFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.StringViewBehaviours
import views.html.aboutReturn.TellUsWhatHasChangedView

class TellUsWhatHasChangedViewSpec extends StringViewBehaviours  {

  val messageKeyPrefix = "tellUsWhatHasChanged"
  val section = Some(messages("section.aboutReturn"))
  val form = new TellUsWhatHasChangedFormProvider()()

    "TellUsWhatHasChangedView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
          val view = viewFor[TellUsWhatHasChangedView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.aboutReturn)

      behave like stringPage(form, applyView, messageKeyPrefix, routes.TellUsWhatHasChangedController.onSubmit(NormalMode).url, section = section, textArea = true)

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
