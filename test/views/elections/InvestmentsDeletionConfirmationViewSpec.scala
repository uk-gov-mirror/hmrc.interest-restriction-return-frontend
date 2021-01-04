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

package views.elections

import assets.constants.NonConsolidatedInvestmentConstants.investmentName
import assets.messages.{BaseMessages, SectionHeaderMessages}
import forms.elections.InvestmentsDeletionConfirmationFormProvider
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.elections.InvestmentsDeletionConfirmationView

class InvestmentsDeletionConfirmationViewSpec extends YesNoViewBehaviours  {

  val messageKeyPrefix = "investmentsDeletionConfirmation"
  val section = Some(SectionHeaderMessages.elections)
  val view = viewFor[InvestmentsDeletionConfirmationView]()
  val form = new InvestmentsDeletionConfirmationFormProvider()()

    "InvestmentsDeletionConfirmationView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = view.apply(form, onwardRoute, investmentName)(fakeRequest, messages, frontendAppConfig)

      behave like normalPage(applyView(form), messageKeyPrefix, section = section, headingArgs = Seq(investmentName))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.elections)

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(form, applyView, messageKeyPrefix, onwardRoute.url, section = section, headingArgs = Seq(investmentName))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.continue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
