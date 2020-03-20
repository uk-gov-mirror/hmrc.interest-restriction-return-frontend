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

package views.groupLevelInformation

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.groupLevelInformation.routes
import forms.groupLevelInformation.InterestReactivationsCapFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.Twirl
import views.behaviours.DecimalViewBehaviours
import views.html.groupLevelInformation.InterestReactivationsCapView

class InterestReactivationsCapViewSpec extends DecimalViewBehaviours {

  val messageKeyPrefix = "interestReactivationsCap"
  val section = Some(messages("section.groupLevelInformation"))
  val form = new InterestReactivationsCapFormProvider()()

  Seq(Twirl).foreach { templatingSystem =>

    s"InterestReactivationsCap ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[InterestReactivationsCapView](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.groupLevelInformation)

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like decimalPage(form, applyView, messageKeyPrefix, routes.InterestReactivationsCapController.onSubmit(NormalMode).url, section = section)

      behave like pageWithSaveForLater(applyView(form))

      behave like currencyPage(applyView)
    }
  }
}
