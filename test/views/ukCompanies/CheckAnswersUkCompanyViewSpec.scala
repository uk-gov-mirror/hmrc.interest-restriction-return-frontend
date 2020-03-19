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

package views.ukCompanies

import assets.constants.UkCompanyCheckYourAnswersConstants
import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.{CheckAnswersUkCompanyMessages}
import models.Section.UkCompanies
import org.jsoup.nodes.Document
import play.twirl.api.HtmlFormat
import utils.CheckYourAnswersUkCompanyHelper
import views.BaseSelectors
import views.ViewUtils.addPossessive
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckAnswersUkCompanyViewSpec extends ViewBehaviours with UkCompanyCheckYourAnswersConstants {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"$UkCompanies.checkYourAnswers"
  val section = Some(CheckAnswersUkCompanyMessages.subheading(companyNameModel.name))
  val ukCompaniesSubheading = CheckAnswersUkCompanyMessages.subheading(companyNameModel.name)
  val ukCompaniesHeading = s"$UkCompanies.checkYourAnswers.heading"

  val view = injector.instanceOf[CheckYourAnswersView]

  def applyView(checkYourAnswersHelper: CheckYourAnswersUkCompanyHelper)(): HtmlFormat.Appendable = {
    view.apply(
      answers = checkYourAnswersHelper.rows(1),
      section = UkCompanies,
      postAction = onwardRoute,
      subheadingMsgArgs = Seq(ukCompanyModelReactivationMaxIncome.companyDetails.companyName),
      buttonMsg = "ukCompanies.checkYourAnswers.button"
    )(fakeRequest, messages, frontendAppConfig)
  }

  "CheckAnswersUkCompanyView" when {

    "Net Tax Amount is an Income" must {

      val checkYourAnswersHelper = new CheckYourAnswersUkCompanyHelper(userAnswersUKCompanyNetTaxReactivationIncome)

      behave like normalPage(
        view = applyView(checkYourAnswersHelper)(),
        messageKeyPrefix = messageKeyPrefix,
        headingArgs = Seq(addPossessive(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)),
        section = Some(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)
      )

      behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

      behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ukCompaniesSubheading)

      behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

      behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

      implicit lazy val document: Document = asDocument(applyView(checkYourAnswersHelper)())

      checkYourAnswersRowChecks(
        CheckAnswersUkCompanyMessages.companyNameLabel -> ukCompanyModelReactivationMaxIncome.companyDetails.companyName,
        CheckAnswersUkCompanyMessages.companyCTUTRLabel -> ukCompanyModelReactivationMaxIncome.companyDetails.ctutr,
        CheckAnswersUkCompanyMessages.consentingLabel -> "Yes",
        CheckAnswersUkCompanyMessages.taxEBITDALabel -> currency(taxEBITDA),
        CheckAnswersUkCompanyMessages.netTaxInterestLabel -> "Income",
        CheckAnswersUkCompanyMessages.netTaxInterestAmountLabel -> s"${currency(netTaxInterestIncome)} ${CheckAnswersUkCompanyMessages.income}"
      )
    }

    "Net Tax amount is an Expense" must {

      val checkYourAnswersHelper = new CheckYourAnswersUkCompanyHelper(userAnswersUKCompanyNetTaxReactivationExpense)

      behave like normalPage(
        view = applyView(checkYourAnswersHelper)(),
        messageKeyPrefix = messageKeyPrefix,
        headingArgs = Seq(addPossessive(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)),
        section = Some(companyNameModel.name)
      )

      behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

      behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ukCompaniesSubheading)

      behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

      behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

      implicit lazy val document: Document = asDocument(applyView(checkYourAnswersHelper)())

      checkYourAnswersRowChecks(
        CheckAnswersUkCompanyMessages.companyNameLabel -> ukCompanyModelReactivationMaxIncome.companyDetails.companyName,
        CheckAnswersUkCompanyMessages.companyCTUTRLabel -> ukCompanyModelReactivationMaxIncome.companyDetails.ctutr,
        CheckAnswersUkCompanyMessages.consentingLabel -> "Yes",
        CheckAnswersUkCompanyMessages.taxEBITDALabel -> currency(taxEBITDA),
        CheckAnswersUkCompanyMessages.netTaxInterestLabel -> "Expense",
        CheckAnswersUkCompanyMessages.netTaxInterestAmountLabel -> s"${currency(netTaxInterestExpense)} ${CheckAnswersUkCompanyMessages.expense}"
      )
    }

    "Net Tax interest is not an Income or Expense " must {

      val checkYourAnswersHelper = new CheckYourAnswersUkCompanyHelper(userAnswersUKCompanyNetTaxNoIncomeOrExpense)

      behave like normalPage(
        view = applyView(checkYourAnswersHelper)(),
        messageKeyPrefix = messageKeyPrefix,
        headingArgs = Seq(addPossessive(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)),
        section = Some(companyNameModel.name)
      )

      behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

      behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ukCompaniesSubheading)

      behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

      behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

      implicit lazy val document: Document = asDocument(applyView(checkYourAnswersHelper)())

      checkYourAnswersRowChecks(
        CheckAnswersUkCompanyMessages.companyNameLabel -> ukCompanyModelReactivationMaxIncome.companyDetails.companyName,
        CheckAnswersUkCompanyMessages.companyCTUTRLabel -> ukCompanyModelReactivationMaxIncome.companyDetails.ctutr,
        CheckAnswersUkCompanyMessages.consentingLabel -> "Yes",
        CheckAnswersUkCompanyMessages.taxEBITDALabel -> currency(taxEBITDA),
        CheckAnswersUkCompanyMessages.netTaxInterestLabel -> "No Income Or Expense"
      )
    }

  }
}
