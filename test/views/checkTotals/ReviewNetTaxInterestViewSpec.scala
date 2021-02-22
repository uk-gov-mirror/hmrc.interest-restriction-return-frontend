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

package views.checkTotals

import assets.constants.BaseConstants
import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.BaseMessages._
import assets.messages.SectionHeaderMessages
import models.NetTaxInterestIncomeOrExpense.{NetTaxInterestExpense, NetTaxInterestIncome}
import pages.checkTotals.ReviewNetTaxInterestPage
import pages.ukCompanies.UkCompaniesPage
import play.twirl.api.HtmlFormat
import utils.ReviewNetTaxInterestHelper
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class ReviewNetTaxInterestViewSpec extends ViewBehaviours with BaseConstants {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"$ReviewNetTaxInterestPage.checkYourAnswers"
  val derivedNetTaxInterestSubheading = s"$ReviewNetTaxInterestPage.checkYourAnswers.subheading"
  val derivedNetTaxInterestHeading = s"$ReviewNetTaxInterestPage.checkYourAnswers.heading"

  val userAnswers = emptyUserAnswers
    .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
    .set(UkCompaniesPage, ukCompanyModelReactivationMaxExpense, Some(2)).get
    .set(UkCompaniesPage, ukCompanyModelMin, Some(3)).get


  val checkYourAnswersHelper = new ReviewNetTaxInterestHelper(userAnswers)

  s"CheckYourAnswer view" must {

    def applyView(): HtmlFormat.Appendable = {
      val view = viewFor[CheckYourAnswersView](Some(userAnswers))
      view.apply(checkYourAnswersHelper.rows, ReviewNetTaxInterestPage, onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(), messageKeyPrefix, section = Some(SectionHeaderMessages.checkTotals))

    behave like pageWithBackLink(applyView())

    behave like pageWithSubHeading(applyView(), derivedNetTaxInterestSubheading)

    behave like pageWithHeading(applyView(), derivedNetTaxInterestHeading)

    behave like pageWithSubmitButton(applyView(), continue)

    behave like pageWithSaveForLater(applyView())

    implicit lazy val document = asDocument(applyView())

    checkYourAnswersRowChecks(
      ukCompanyModelMax.companyDetails.companyName -> s"${currency(netTaxInterestExpense)} $NetTaxInterestExpense",
      ukCompanyModelReactivationMaxExpense.companyDetails.companyName -> s"${currency(netTaxInterestExpense)} $NetTaxInterestExpense",
      ukCompanyModelMin.companyDetails.companyName -> s"${currency(netTaxInterestIncome)} $NetTaxInterestIncome"
    )
  }
}
