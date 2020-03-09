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

import java.time.LocalDate

import assets.messages.BaseMessages
import assets.messages.SectionHeaderMessages._
import forms.aboutReportingCompany.AccountingPeriodStartFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.aboutReportingCompany.AccountingPeriodStartView

class AccountingPeriodStartViewSpec extends QuestionViewBehaviours[LocalDate] {

  val messageKeyPrefix = "accountingPeriodStart"
  val section = Some(messages("section.startReturn"))
  val form = new AccountingPeriodStartFormProvider()()

    "AccountingPeriodStartView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[AccountingPeriodStartView](Some(emptyUserAnswers))
        view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithSubHeading(applyView(form), startReturn)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
