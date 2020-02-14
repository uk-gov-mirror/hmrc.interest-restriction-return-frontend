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

import assets.constants.{BaseConstants, ElectionsCheckYourAnswersConstants}
import assets.messages.BaseMessages.saveAndContinue
import assets.messages.{CheckAnswersElectionsMessages, SectionHeaderMessages}
import models.Section.Elections
import org.jsoup.nodes.Document
import play.twirl.api.HtmlFormat
import utils.{CheckYourAnswersElectionsHelper, CurrencyFormatter}
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersElectionsViewSpec extends ViewBehaviours with BaseConstants with ElectionsCheckYourAnswersConstants with CurrencyFormatter {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"$Elections.checkYourAnswers"
  val subheading = s"$Elections.checkYourAnswers.subheading"
  val heading = s"$Elections.checkYourAnswers.heading"

  val view = injector.instanceOf[CheckYourAnswersView]

  def applyView(checkYourAnswersHelper: CheckYourAnswersElectionsHelper)(): HtmlFormat.Appendable = {
    view.apply(checkYourAnswersHelper, Elections, onwardRoute)(fakeRequest, messages, frontendAppConfig)
  }

  "CheckYourAnswer view" when {

    "Maximum values are provided" must {

      val checkYourAnswersHelper = new CheckYourAnswersElectionsHelper(allElections)

      behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.elections))

      behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

      behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), subheading)

      behave like pageWithHeading(applyView(checkYourAnswersHelper)(), heading)

      behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

      behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

      implicit lazy val document = asDocument(applyView(checkYourAnswersHelper)())

      rowChecks(Seq(
        CheckAnswersElectionsMessages.groupRatioElection -> "Yes",
        CheckAnswersElectionsMessages.angie -> currencyFormat(angie),
        CheckAnswersElectionsMessages.qngie -> currencyFormat(qngie),
        CheckAnswersElectionsMessages.ebitda -> currencyFormat(ebitda),
        CheckAnswersElectionsMessages.groupRatioPercentage -> s"$groupRatioPercentage%",
        CheckAnswersElectionsMessages.groupRatioBlended -> "Yes",
        CheckAnswersElectionsMessages.investorGroupsHeading -> CheckAnswersElectionsMessages.investorGroupsValue(1),
        CheckAnswersElectionsMessages.electedGroupEBITDABefore -> "No",
        CheckAnswersElectionsMessages.groupEBITDAElection -> "Yes",
        CheckAnswersElectionsMessages.electedInterestAllowanceAlternativeCalcBefore -> "No",
        CheckAnswersElectionsMessages.interestAllowanceAlternativeCalcElection -> "No",
        CheckAnswersElectionsMessages.interestAllowanceNonConsolidatedElection -> "Yes",
        CheckAnswersElectionsMessages.nonConsolidatedInvestmentsHeading -> CheckAnswersElectionsMessages.nonConsolidatedInvestmentsValue(1),
        CheckAnswersElectionsMessages.electedInterestAllowanceConsolidatedPshipBefore -> "No",
        CheckAnswersElectionsMessages.interestAllowanceConsolidatedPshipElection -> "Yes",
        CheckAnswersElectionsMessages.consolidatedPartnershipsHeading -> CheckAnswersElectionsMessages.consolidatedPartnershipsValue(1)
      ))
    }

    "Minimum values are provided" must {

      val checkYourAnswersHelper = new CheckYourAnswersElectionsHelper(minElections)

      behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.elections))

      behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

      behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), subheading)

      behave like pageWithHeading(applyView(checkYourAnswersHelper)(), heading)

      behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

      behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

      implicit lazy val document = asDocument(applyView(checkYourAnswersHelper)())

      rowChecks(Seq(
        CheckAnswersElectionsMessages.groupRatioElection -> "No",
        CheckAnswersElectionsMessages.angie -> currencyFormat(angie),
        CheckAnswersElectionsMessages.electedInterestAllowanceAlternativeCalcBefore -> "Yes",
        CheckAnswersElectionsMessages.interestAllowanceNonConsolidatedElection -> "No",
        CheckAnswersElectionsMessages.electedInterestAllowanceConsolidatedPshipBefore -> "Yes",
        CheckAnswersElectionsMessages.consolidatedPartnershipsHeading -> CheckAnswersElectionsMessages.consolidatedPartnershipsValue(1)
      ))
    }
  }


  def rowChecks(expectedRowData: Seq[(String, String)])(implicit document: Document): Unit = {

    expectedRowData.zipWithIndex.foreach { case ((heading, value), i) =>

      val rowPosition = i + 1

      s"have the correct answer row at position $rowPosition" which {

        s"should have the correct heading of '$heading'" in {
          document.select(Selectors.checkAnswersHeading(rowPosition)).text mustBe heading
        }

        s"should have the correct value of '$value'" in {
          document.select(Selectors.checkAnswersAnswerValue(rowPosition)).text mustBe value
        }
      }
    }
  }
}