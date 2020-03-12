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

package utils

import assets.constants.BaseConstants
import assets.constants.InvestorGroupConstants._
import assets.constants.NonConsolidatedInvestmentConstants._
import assets.constants.PartnershipsConstants._
import assets.messages.{BaseMessages, CheckAnswersElectionsMessages}
import base.SpecBase
import controllers.elections.{routes => electionsRoutes}
import models.{CheckMode, UserAnswers}
import pages.elections._
import viewmodels.SummaryListRowHelper

class CheckYourAnswersElectionsHelperSpec extends SpecBase with BaseConstants with SummaryListRowHelper with CurrencyFormatter {

  val userAnswers = UserAnswers("id")
    .set(GroupRatioElectionPage, true).get
    .set(EnterANGIEPage, angie).get
    .set(EnterQNGIEPage, qngie).get
    .set(GroupEBITDAPage, ebitda).get
    .set(GroupRatioPercentagePage, groupRatioPercentage).get
    .set(GroupRatioBlendedElectionPage, true).get
    .set(AddInvestorGroupPage, true).get
    .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).get
    .set(ElectedGroupEBITDABeforePage, false).get
    .set(GroupEBITDAChargeableGainsElectionPage, true).get
    .set(ElectedInterestAllowanceAlternativeCalcBeforePage, false).get
    .set(InterestAllowanceAlternativeCalcElectionPage, true).get
    .set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, true).get
    .set(InvestmentNamePage, investmentName, Some(1)).get
    .set(ElectedInterestAllowanceConsolidatedPshipBeforePage, false).get
    .set(InterestAllowanceConsolidatedPshipElectionPage, true).get
    .set(PartnershipsPage, partnershipModelUK, Some(1)).get

  val helper = new CheckYourAnswersElectionsHelper(userAnswers)

  "Check Your Answers Helper" must {

    "For the GroupRatioElection answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupRatioElection mustBe Some(summaryListRow(
          messages("groupRatioElection.checkYourAnswersLabel"),
          BaseMessages.yes,
          electionsRoutes.GroupRatioElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the EnterANGIE answer" must {

      "have a correctly formatted summary list row" in {

        helper.enterANGIE mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.angie,
          currencyFormat(angie),
          electionsRoutes.EnterANGIEController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the EnterQNGIE answer" must {

      "have a correctly formatted summary list row" in {

        helper.enterQNGIE mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.qngie,
          currencyFormat(qngie),
          electionsRoutes.EnterQNGIEController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the GroupEBITDA answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupEBITDA mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.ebitda,
          currencyFormat(ebitda),
          electionsRoutes.GroupEBITDAController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the GroupRatioPercentage answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupRatioPercentage mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.groupRatioPercentage,
          s"$groupRatioPercentage%",
          electionsRoutes.GroupRatioPercentageController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the GroupRatioBlendedElection answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupRatioBlendedElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.groupRatioBlended,
          BaseMessages.yes,
          electionsRoutes.GroupRatioBlendedElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    //TODO:      update the Change Link to go to the review page (when created)
    "For the Investor Groups Added" must {

      "have a correctly formatted summary list row when one added" in {

        helper.investorGroupsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.investorGroupsHeading,
          CheckAnswersElectionsMessages.investorGroupsValue(1),
          controllers.routes.UnderConstructionController.onPageLoad() -> CheckAnswersElectionsMessages.investorGroupsReview
        ))
      }

      "have a correctly formatted summary list row when multiple added" in {

        val helper = new CheckYourAnswersElectionsHelper(userAnswers
          .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(2)).get
          .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(3)).get)

        helper.investorGroupsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.investorGroupsHeading,
          CheckAnswersElectionsMessages.investorGroupsValue(3),
          controllers.routes.UnderConstructionController.onPageLoad() -> CheckAnswersElectionsMessages.investorGroupsReview
        ))
      }
    }

    "For Elected Group EBITDA Before" must {

      "have a correctly formatted summary list row" in {

        helper.electedGroupEBITDABefore mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.electedGroupEBITDABefore,
          BaseMessages.no,
          electionsRoutes.ElectedGroupEBITDABeforeController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Group EBITDA Chargeable Gains Election" must {

      "have a correctly formatted summary list row" in {

        helper.groupEBITDAChargeableGainsElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.groupEBITDAElection,
          BaseMessages.yes,
          electionsRoutes.GroupEBITDAChargeableGainsElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Elected Interest Allowance Alternative Calc Before" must {

      "have a correctly formatted summary list row" in {

        helper.electedInterestAllowanceAlternativeCalcBefore mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.electedInterestAllowanceAlternativeCalcBefore,
          BaseMessages.no,
          electionsRoutes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Interest Allowance Alternative Calc Election" must {

      "have a correctly formatted summary list row" in {

        helper.interestAllowanceAlternativeCalcElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.interestAllowanceAlternativeCalcElection,
          BaseMessages.yes,
          electionsRoutes.InterestAllowanceAlternativeCalcElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Interest Allowance Non Consolidated Investments Election" must {

      "have a correctly formatted summary list row" in {

        helper.interestAllowanceNonConsolidatedInvestmentsElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.interestAllowanceNonConsolidatedElection,
          BaseMessages.yes,
          electionsRoutes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    //TODO:      update the Change Link to go to the review page (when created)
    "For the Non Consolidated Investments listed" must {

      "have a correctly formatted summary list row when one added" in {

        helper.nonConsolidatedInvestmentsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.nonConsolidatedInvestmentsHeading,
          CheckAnswersElectionsMessages.nonConsolidatedInvestmentsValue(1),
          controllers.routes.UnderConstructionController.onPageLoad() -> CheckAnswersElectionsMessages.nonConsolidatedInvestmentsReview
        ))
      }

      "have a correctly formatted summary list row when multiple added" in {

        val helper = new CheckYourAnswersElectionsHelper(userAnswers
          .set(InvestmentNamePage, investmentName, Some(2)).get
          .set(InvestmentNamePage, investmentName, Some(3)).get)

        helper.nonConsolidatedInvestmentsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.nonConsolidatedInvestmentsHeading,
          CheckAnswersElectionsMessages.nonConsolidatedInvestmentsValue(3),
          controllers.routes.UnderConstructionController.onPageLoad() -> CheckAnswersElectionsMessages.nonConsolidatedInvestmentsReview
        ))
      }
    }

    "For Elected Interest Allowance Consolidated Pship Before" must {

      "have a correctly formatted summary list row" in {

        helper.electedInterestAllowanceConsolidatedPshipBefore mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.electedInterestAllowanceConsolidatedPshipBefore,
          BaseMessages.no,
          electionsRoutes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Interest Allowance Consolidated Pship Election" must {

      "have a correctly formatted summary list row" in {

        helper.interestAllowanceConsolidatedPshipElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.interestAllowanceConsolidatedPshipElection,
          BaseMessages.yes,
          electionsRoutes.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    //TODO:      update the Change Link to go to the review page (when created)
    "For the Consolidated Partnerships listed" must {

      "have a correctly formatted summary list row when one added" in {

        helper.consolidatedPartnershipsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.consolidatedPartnershipsHeading,
          CheckAnswersElectionsMessages.consolidatedPartnershipsValue(1),
          controllers.routes.UnderConstructionController.onPageLoad() -> CheckAnswersElectionsMessages.consolidatedPartnershipsReview
        ))
      }

      "have a correctly formatted summary list row when multiple added" in {

        val helper = new CheckYourAnswersElectionsHelper(userAnswers
          .set(PartnershipsPage, partnershipModelUK, Some(2)).get
          .set(PartnershipsPage, partnershipModelNonUk, Some(3)).get)

        helper.consolidatedPartnershipsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.consolidatedPartnershipsHeading,
          CheckAnswersElectionsMessages.consolidatedPartnershipsValue(3),
          controllers.routes.UnderConstructionController.onPageLoad() -> CheckAnswersElectionsMessages.consolidatedPartnershipsReview
        ))
      }
    }


  }
}
