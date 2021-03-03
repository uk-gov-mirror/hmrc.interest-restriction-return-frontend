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
          visuallyHiddenText = None,
          electionsRoutes.GroupRatioElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the GroupRatioBlendedElection answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupRatioBlendedElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.groupRatioBlended,
          BaseMessages.yes,
          visuallyHiddenText = None,
          electionsRoutes.GroupRatioBlendedElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the Investor Groups Added" must {

      "have a correctly formatted summary list row when one added" in {

        helper.investorGroupsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.investorGroupsHeading,
          CheckAnswersElectionsMessages.investorGroupsValue(1),
          visuallyHiddenText = None,
          electionsRoutes.InvestorGroupsReviewAnswersListController.onPageLoad() -> CheckAnswersElectionsMessages.investorGroupsReview
        ))
      }

      "have a correctly formatted summary list row when multiple added" in {

        val helper = new CheckYourAnswersElectionsHelper(userAnswers
          .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(2)).get
          .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(3)).get)

        helper.investorGroupsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.investorGroupsHeading,
          CheckAnswersElectionsMessages.investorGroupsValue(3),
          visuallyHiddenText = None,
          electionsRoutes.InvestorGroupsReviewAnswersListController.onPageLoad() -> CheckAnswersElectionsMessages.investorGroupsReview
        ))
      }
    }

    "For Elected Group EBITDA Before" must {

      "have a correctly formatted summary list row" in {

        helper.electedGroupEBITDABefore mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.electedGroupEBITDABefore,
          BaseMessages.no,
          visuallyHiddenText = None,
          electionsRoutes.ElectedGroupEBITDABeforeController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Group EBITDA Chargeable Gains Election" must {

      "have a correctly formatted summary list row" in {

        helper.groupEBITDAChargeableGainsElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.groupEBITDAElection,
          BaseMessages.yes,
          visuallyHiddenText = None,
          electionsRoutes.GroupEBITDAChargeableGainsElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Elected Interest Allowance Alternative Calc Before" must {

      "have a correctly formatted summary list row" in {

        helper.electedInterestAllowanceAlternativeCalcBefore mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.electedInterestAllowanceAlternativeCalcBefore,
          BaseMessages.no,
          visuallyHiddenText = None,
          electionsRoutes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Interest Allowance Alternative Calc Election" must {

      "have a correctly formatted summary list row" in {

        helper.interestAllowanceAlternativeCalcElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.interestAllowanceAlternativeCalcElection,
          BaseMessages.yes,
          visuallyHiddenText = None,
          electionsRoutes.InterestAllowanceAlternativeCalcElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Interest Allowance Non Consolidated Investments Election" must {

      "have a correctly formatted summary list row" in {

        helper.interestAllowanceNonConsolidatedInvestmentsElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.interestAllowanceNonConsolidatedElection,
          BaseMessages.yes,
          visuallyHiddenText = None,
          electionsRoutes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the Non Consolidated Investments listed" must {

      "have a correctly formatted summary list row when one added" in {

        helper.nonConsolidatedInvestmentsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.nonConsolidatedInvestmentsHeading,
          CheckAnswersElectionsMessages.nonConsolidatedInvestmentsValue(1),
          visuallyHiddenText = None,
          electionsRoutes.InvestmentsReviewAnswersListController.onPageLoad() -> CheckAnswersElectionsMessages.nonConsolidatedInvestmentsReview
        ))
      }

      "have a correctly formatted summary list row when multiple added" in {

        val helper = new CheckYourAnswersElectionsHelper(userAnswers
          .set(InvestmentNamePage, investmentName, Some(2)).get
          .set(InvestmentNamePage, investmentName, Some(3)).get)

        helper.nonConsolidatedInvestmentsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.nonConsolidatedInvestmentsHeading,
          CheckAnswersElectionsMessages.nonConsolidatedInvestmentsValue(3),
          visuallyHiddenText = None,
          electionsRoutes.InvestmentsReviewAnswersListController.onPageLoad() -> CheckAnswersElectionsMessages.nonConsolidatedInvestmentsReview
        ))
      }
    }

    "For Elected Interest Allowance Consolidated Pship Before" must {

      "have a correctly formatted summary list row" in {

        helper.electedInterestAllowanceConsolidatedPshipBefore mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.electedInterestAllowanceConsolidatedPshipBefore,
          BaseMessages.no,
          visuallyHiddenText = None,
          electionsRoutes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For Interest Allowance Consolidated Pship Election" must {

      "have a correctly formatted summary list row" in {

        helper.interestAllowanceConsolidatedPshipElection mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.interestAllowanceConsolidatedPshipElection,
          BaseMessages.yes,
          visuallyHiddenText = None,
          electionsRoutes.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the Consolidated Partnerships listed" must {

      "have a correctly formatted summary list row when one added" in {

        helper.consolidatedPartnershipsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.consolidatedPartnershipsHeading,
          CheckAnswersElectionsMessages.consolidatedPartnershipsValue(1),
          visuallyHiddenText = None,
          electionsRoutes.PartnershipsReviewAnswersListController.onPageLoad() -> CheckAnswersElectionsMessages.consolidatedPartnershipsReview
        ))
      }

      "have a correctly formatted summary list row when multiple added" in {

        val helper = new CheckYourAnswersElectionsHelper(userAnswers
          .set(PartnershipsPage, partnershipModelUK, Some(2)).get
          .set(PartnershipsPage, partnershipModelNonUk, Some(3)).get)

        helper.consolidatedPartnershipsRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.consolidatedPartnershipsHeading,
          CheckAnswersElectionsMessages.consolidatedPartnershipsValue(3),
          visuallyHiddenText = None,
          electionsRoutes.PartnershipsReviewAnswersListController.onPageLoad() -> CheckAnswersElectionsMessages.consolidatedPartnershipsReview
        ))
      }

      "do not display partnerships row when there are none" in {

        val helper = new CheckYourAnswersElectionsHelper(userAnswers
          .set(ElectedInterestAllowanceConsolidatedPshipBeforePage, true).get
          .set(InterestAllowanceConsolidatedPshipElectionPage, false).get
          .remove(PartnershipsPage).get)

        val partnerRow = helper.consolidatedPartnershipsRow
        partnerRow mustBe None
      }

      "display partnerships row when partnerships are not removed" in {
        val partnerRow = helper.consolidatedPartnershipsRow

        partnerRow mustBe Some(summaryListRow(
          CheckAnswersElectionsMessages.consolidatedPartnershipsHeading,
          CheckAnswersElectionsMessages.consolidatedPartnershipsValue(1),
          visuallyHiddenText = None,
          electionsRoutes.PartnershipsReviewAnswersListController.onPageLoad() -> CheckAnswersElectionsMessages.consolidatedPartnershipsReview))
      }
    }
  }
}
