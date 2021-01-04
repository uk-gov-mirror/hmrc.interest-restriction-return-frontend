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

import assets.constants.UkCompanyCheckYourAnswersConstants
import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.{CheckAnswersUkCompanyMessages, SectionHeaderMessages}
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
  val section = Some(messages("section.ukCompanies"))
  val ukCompaniesSubheading = s"$UkCompanies.checkYourAnswers.subheading"
  val ukCompaniesHeading = s"$UkCompanies.checkYourAnswers.heading"

  val view = injector.instanceOf[CheckYourAnswersView]

  def applyView(checkYourAnswersHelper: CheckYourAnswersUkCompanyHelper)(): HtmlFormat.Appendable = {
    view.apply(
      checkYourAnswersHelper.rows(1),
      UkCompanies,
      onwardRoute,
      Seq(addPossessive(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)),
      "ukCompanies.checkYourAnswers.button"
    )(fakeRequest, messages, frontendAppConfig
    )
  }

  "CheckAnswersUkCompanyView" when {

    "net tax amount is and income" must {

      val checkYourAnswersHelper = new CheckYourAnswersUkCompanyHelper(userAnswersUKCompanyNetTaxReactivationIncome)

      behave like normalPage(
        view = applyView(checkYourAnswersHelper)(),
        messageKeyPrefix = messageKeyPrefix,
        headingArgs = Seq(addPossessive(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)),
        section = Some(SectionHeaderMessages.ukCompanies)
        )

      behave like pageWithBackLink (applyView(checkYourAnswersHelper)())

      behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ukCompaniesSubheading)

      behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), confirmCompany)

      behave like pageWithSaveForLater (applyView(checkYourAnswersHelper)())

      implicit lazy val document: Document = asDocument(applyView(checkYourAnswersHelper)())

      checkYourAnswersRowChecks(
        CheckAnswersUkCompanyMessages.companyName -> ukCompanyModelReactivationMaxIncome.companyDetails.companyName,
        CheckAnswersUkCompanyMessages.companyCTUTR -> ukCompanyModelReactivationMaxIncome.companyDetails.ctutr,
        CheckAnswersUkCompanyMessages.consenting -> "Yes",
        CheckAnswersUkCompanyMessages.taxEBITDA -> currency(taxEBITDA),
        CheckAnswersUkCompanyMessages.netTaxInterest -> s"${currency(netTaxInterestIncome)} ${CheckAnswersUkCompanyMessages.income}"
      )
    }

    "net tax amount is an expense" must {

      val checkYourAnswersHelper = new CheckYourAnswersUkCompanyHelper(userAnswersUKCompanyNetTaxReactivationExpense)

      behave like normalPage(
        view = applyView(checkYourAnswersHelper)(),
        messageKeyPrefix = messageKeyPrefix,
        headingArgs = Seq(addPossessive(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)),
        section = Some(SectionHeaderMessages.ukCompanies)
      )

      behave like pageWithBackLink (applyView(checkYourAnswersHelper)())

      behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ukCompaniesSubheading)

      behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), confirmCompany)

      behave like pageWithSaveForLater (applyView(checkYourAnswersHelper)())

      implicit lazy val document: Document = asDocument(applyView(checkYourAnswersHelper)())

      checkYourAnswersRowChecks(
        CheckAnswersUkCompanyMessages.companyName -> ukCompanyModelReactivationMaxIncome.companyDetails.companyName,
        CheckAnswersUkCompanyMessages.companyCTUTR -> ukCompanyModelReactivationMaxIncome.companyDetails.ctutr,
        CheckAnswersUkCompanyMessages.consenting -> "Yes",
        CheckAnswersUkCompanyMessages.taxEBITDA -> currency(taxEBITDA),
        CheckAnswersUkCompanyMessages.netTaxInterest -> s"${currency(netTaxInterestExpense)} ${CheckAnswersUkCompanyMessages.expense}"
      )


    }


  }
}
