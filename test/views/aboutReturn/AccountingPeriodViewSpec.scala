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

package views.aboutReturn

import java.time.LocalDate

import assets.messages.BaseMessages
import assets.messages.SectionHeaderMessages._
import forms.aboutReturn.AccountingPeriodFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.aboutReturn.AccountingPeriodView
import models.returnModels.AccountingPeriodModel

class AccountingPeriodViewSpec extends QuestionViewBehaviours[AccountingPeriodModel] {

  val messageKeyPrefix = "accountingPeriod"
  val section = Some(messages("section.aboutReturn"))
  val form = new AccountingPeriodFormProvider()()

    "AccountingPeriodView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[AccountingPeriodView](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithSubHeading(applyView(form), aboutReturn)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
