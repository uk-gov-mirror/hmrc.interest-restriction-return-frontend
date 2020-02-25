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

package views.elections

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.elections.routes
import forms.elections.InvestmentNameFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.StringViewBehaviours
import views.html.elections.InvestmentNameView

class InvestmentNameViewSpec extends StringViewBehaviours  {

  val messageKeyPrefix = "investmentName"
  val section = Some(SectionHeaderMessages.elections)
  val form = new InvestmentNameFormProvider()()
  val view = viewFor[InvestmentNameView]()

    "InvestmentNameView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        view.apply(form, NormalMode, onwardRoute)(fakeRequest, messages, frontendAppConfig)

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), section.get)

      behave like stringPage(form, applyView, messageKeyPrefix, onwardRoute.url, section = section)

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
