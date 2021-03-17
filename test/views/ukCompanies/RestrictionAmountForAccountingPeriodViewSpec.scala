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

package views.ukCompanies

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.ukCompanies.routes
import forms.ukCompanies.RestrictionAmountForAccountingPeriodFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.DecimalViewBehaviours
import views.html.ukCompanies.RestrictionAmountForAccountingPeriodView

import java.time.LocalDate

class RestrictionAmountForAccountingPeriodViewSpec extends DecimalViewBehaviours  {

  val messageKeyPrefix = "restrictionAmountForAccountingPeriod"
  val companyName = "Company 1"
  val section = Some(companyName)
  val form = new RestrictionAmountForAccountingPeriodFormProvider()()
  val companyIdx = 1
  val restrictionIdx = 1
  val postAction = routes.RestrictionAmountForAccountingPeriodController.onSubmit(companyIdx, restrictionIdx, NormalMode)
  val endDate = LocalDate.of(2021, 1, 1)

  "RestrictionAmountForAccountingPeriodView" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[RestrictionAmountForAccountingPeriodView](Some(emptyUserAnswers))
      view.apply(form, companyName, endDate, postAction)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(form), messageKeyPrefix, section = section, headingArgs = Seq(endDate.toString))

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), companyName)

    behave like decimalPage(form, applyView, messageKeyPrefix, postAction.url, section = section, headingArgs = Seq(endDate.toString))

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(applyView(form))
  }
}
