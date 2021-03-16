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
import java.time.LocalDate
import forms.ukCompanies.CompanyAccountingPeriodEndDateFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.ukCompanies.CompanyAccountingPeriodEndDateView
import controllers.ukCompanies.routes
import models.returnModels.AccountingPeriodModel
import pages.aboutReturn.AccountingPeriodPage
import pages.ukCompanies.UkCompaniesPage
import assets.constants.fullReturn.UkCompanyConstants.ukCompanyModelMax

class CompanyAccountingPeriodEndDateViewSpec extends QuestionViewBehaviours[LocalDate] {

  val periodOfAccount = AccountingPeriodModel(LocalDate.of(2017,1,1), LocalDate.of(2018,1,1))
  val userAnswers = (for {
    ua  <- emptyUserAnswers.set(AccountingPeriodPage, periodOfAccount)
    ua2 <- ua.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
  } yield ua2).get

  val messageKeyPrefix = "companyAccountingPeriodEndDate"
  val section = Some(messages("section.ukCompanies"))
  val form = new CompanyAccountingPeriodEndDateFormProvider()(1, 1, userAnswers)
  val companyIdx = 1
  val restrictionIdx = 1
  val postAction = routes.CompanyAccountingPeriodEndDateController.onSubmit(companyIdx, restrictionIdx, NormalMode)

    "CompanyAccountingPeriodEndDateView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
        val view = viewFor[CompanyAccountingPeriodEndDateView](Some(emptyUserAnswers))
        view.apply(form, "Company 1", "first", postAction)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), "companyAccountingPeriodEndDate.first", section = Some("Company 1"))

      behave like pageWithSubHeading(applyView(form), "Company 1")

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
