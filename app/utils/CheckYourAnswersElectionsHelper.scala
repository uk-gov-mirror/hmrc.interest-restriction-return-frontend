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

import controllers.elections.{routes => electionsRoutes}
import models.{CheckMode, UserAnswers}
import pages.elections._
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersElectionsHelper(val userAnswers: UserAnswers)
                                     (implicit val messages: Messages) extends CheckYourAnswersHelper {

  def groupEBITDA: Option[SummaryListRow] =
    monetaryAnswer(GroupEBITDAPage, electionsRoutes.GroupEBITDAController.onPageLoad(CheckMode))

  def interestAllowanceConsolidatedPshipElection: Option[SummaryListRow] =
    answer(InterestAllowanceConsolidatedPshipElectionPage, electionsRoutes.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(CheckMode))

  def electedInterestAllowanceConsolidatedPshipBefore: Option[SummaryListRow] =
    answer(ElectedInterestAllowanceConsolidatedPshipBeforePage, electionsRoutes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(CheckMode))

  def groupRatioPercentage: Option[SummaryListRow] =
    percentageAnswer(GroupRatioPercentagePage, electionsRoutes.GroupRatioPercentageController.onPageLoad(CheckMode))

  def interestAllowanceNonConsolidatedInvestmentsElection: Option[SummaryListRow] =
    answer(InterestAllowanceNonConsolidatedInvestmentsElectionPage, electionsRoutes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(CheckMode))

  def interestAllowanceAlternativeCalcElection: Option[SummaryListRow] =
    answer(InterestAllowanceAlternativeCalcElectionPage, electionsRoutes.InterestAllowanceAlternativeCalcElectionController.onPageLoad(CheckMode))

  def electedInterestAllowanceAlternativeCalcBefore: Option[SummaryListRow] =
    answer(ElectedInterestAllowanceAlternativeCalcBeforePage, electionsRoutes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(CheckMode))

  def groupEBITDAChargeableGainsElection: Option[SummaryListRow] =
    answer(GroupEBITDAChargeableGainsElectionPage, electionsRoutes.GroupEBITDAChargeableGainsElectionController.onPageLoad(CheckMode))

  def electedGroupEBITDABefore: Option[SummaryListRow] =
    answer(ElectedGroupEBITDABeforePage, electionsRoutes.ElectedGroupEBITDABeforeController.onPageLoad(CheckMode))

  def groupRatioBlendedElection: Option[SummaryListRow] =
    answer(GroupRatioBlendedElectionPage, electionsRoutes.GroupRatioBlendedElectionController.onPageLoad(CheckMode))

  def enterQNGIE: Option[SummaryListRow] =
    monetaryAnswer(EnterQNGIEPage, electionsRoutes.EnterQNGIEController.onPageLoad(CheckMode))

  def enterANGIE: Option[SummaryListRow] =
    monetaryAnswer(EnterANGIEPage, electionsRoutes.EnterANGIEController.onPageLoad(CheckMode))

  def groupRatioElection: Option[SummaryListRow] =
    answer(GroupRatioElectionPage, electionsRoutes.GroupRatioElectionController.onPageLoad(CheckMode))

  //TODO: This is a placeholder row. It needs to be updated to calculate number of investors held in UserAnswers
  def investorGroupsRow(implicit messages: Messages): Option[SummaryListRow] = {
    if(!userAnswers.get(AddInvestorGroupPage).contains(true)) None else {
      val numberOfInvestorGroupsAdded = 1 //TODO: This will need to be calculated from list held in UserAnswers in future
      val valueMsgSuffix = if (numberOfInvestorGroupsAdded > 1) "plural" else "singular"
      Some(summaryListRow(
        Messages("investorGroupsAdded.checkYourAnswers.label"),
        Messages(s"investorGroupsAdded.checkYourAnswers.value.$valueMsgSuffix", numberOfInvestorGroupsAdded),
        controllers.routes.UnderConstructionController.onPageLoad() -> Messages("investorGroupsAdded.checkYourAnswers.review")
      ))
    }
  }

  //TODO: This is a placeholder row. It needs to be updated to calculate number of partnerships held in UserAnswers
  def consolidatedPartnershipsRow(implicit messages: Messages): Option[SummaryListRow] = {
    val addedPships = userAnswers.get(ElectedInterestAllowanceConsolidatedPshipBeforePage).contains(true) ||
      userAnswers.get(InterestAllowanceConsolidatedPshipElectionPage).contains(true)
    if (!addedPships) None else {
      val numberOfPartnershipsAdded = 1 //TODO: This will need to be calculated from list held in UserAnswers in future
      val valueMsgSuffix = if(numberOfPartnershipsAdded > 1) "plural" else "singular"
      Some(summaryListRow(
        Messages("consolidatedPartnershipsAdded.checkYourAnswers.label"),
        Messages(s"consolidatedPartnershipsAdded.checkYourAnswers.value.$valueMsgSuffix", numberOfPartnershipsAdded),
        controllers.routes.UnderConstructionController.onPageLoad() -> Messages("consolidatedPartnershipsAdded.checkYourAnswers.review")
      ))
    }
  }

  //TODO: This is a placeholder row. It needs to be updated to calculate number of nonConsolidatedInvestments held in UserAnswers
  def nonConsolidatedInvestmentsRow(implicit messages: Messages): Option[SummaryListRow] = {
    if(!userAnswers.get(InterestAllowanceNonConsolidatedInvestmentsElectionPage).contains(true)) None else {
      val numberOfInvestmentsAdded = 1 //TODO: This will need to be calculated from list held in UserAnswers in future
      val valueMsgSuffix = if(numberOfInvestmentsAdded > 1) "plural" else "singular"
      Some(summaryListRow(
        Messages("nonConsolidatedInvestmentsAdded.checkYourAnswers.label"),
        Messages(s"nonConsolidatedInvestmentsAdded.checkYourAnswers.value.$valueMsgSuffix", numberOfInvestmentsAdded),
        controllers.routes.UnderConstructionController.onPageLoad() -> Messages("nonConsolidatedInvestmentsAdded.checkYourAnswers.review")
      ))
    }
  }

   val rows: Seq[SummaryListRow] = Seq(
    groupRatioElection,
    enterANGIE,
    enterQNGIE,
    groupEBITDA,
    groupRatioPercentage,
    groupRatioBlendedElection,
    investorGroupsRow,
    electedGroupEBITDABefore,
    groupEBITDAChargeableGainsElection,
    electedInterestAllowanceAlternativeCalcBefore,
    interestAllowanceAlternativeCalcElection,
    interestAllowanceNonConsolidatedInvestmentsElection,
    nonConsolidatedInvestmentsRow,
    electedInterestAllowanceConsolidatedPshipBefore,
    interestAllowanceConsolidatedPshipElection,
    consolidatedPartnershipsRow
  ).flatten
}
