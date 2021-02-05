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
import assets.messages.{BaseMessages, CheckAnswersGroupLevelInformationMessages}
import base.SpecBase
import controllers.groupLevelInformation.{routes => groupLevelInformationRoutes}
import models.{CheckMode, UserAnswers}
import pages.groupLevelInformation._
import viewmodels.SummaryListRowHelper

class CheckYourAnswersGroupLevelInformationHelperSpec extends SpecBase with BaseConstants with SummaryListRowHelper with CurrencyFormatter {

  val helper = new CheckYourAnswersGroupLevelInformationHelper(
    UserAnswers("id")
      .set(ReturnContainEstimatesPage, false).get
      .set(GroupInterestAllowancePage, groupInterestAllowance).get
      .set(GroupInterestCapacityPage, groupInterestCapacity).get
      .set(GroupSubjectToRestrictionsPage, true).get
      .set(GroupSubjectToReactivationsPage, false).get
      .set(InterestReactivationsCapPage, interestReactivationCap).get
      .set(DisallowedAmountPage, disallowedAmount).get
      .set(InterestAllowanceBroughtForwardPage, interestAllowanceBroughtForward).get
      .set(EnterANGIEPage, angie).get
      .set(EnterQNGIEPage, qngie).get
      .set(GroupEBITDAPage, ebitda).get
      .set(GroupRatioPercentagePage, groupRatioPercentage).get
  )

  "Check Your Answers Helper" must {

    "For the GroupSubjectToRestrictions answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupSubjectToRestrictions mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.groupSubjectToRestrictions,
          BaseMessages.yes,
          visuallyHiddenText = None,
          groupLevelInformationRoutes.GroupSubjectToRestrictionsController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the GroupSubjectToReactivations answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupSubjectToReactivations mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.groupSubjectToReactivations,
          BaseMessages.no,
          visuallyHiddenText = None,
          groupLevelInformationRoutes.GroupSubjectToReactivationsController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the InterestReactivationsCap answer" must {

      "have a correctly formatted summary list row" in {

        helper.interestReactivationsCap mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.interestReactivationsCap,
          currencyFormat(interestReactivationCap),
          visuallyHiddenText = None,
          groupLevelInformationRoutes.InterestReactivationsCapController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the InterestAllowanceBroughtForward answer" must {

      "have a correctly formatted summary list row" in {

        helper.interestAllowanceBroughtForward mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.interestAllowanceBroughtForward,
          currencyFormat(interestAllowanceBroughtForward),
          visuallyHiddenText = None,
          groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the GroupInterestAllowance answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupInterestAllowance mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.groupInterestAllowance,
          currencyFormat(groupInterestAllowance),
          visuallyHiddenText = None,
          groupLevelInformationRoutes.GroupInterestAllowanceController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the GroupInterestCapacity answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupInterestCapacity mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.groupInterestCapacity,
          currencyFormat(groupInterestCapacity),
          visuallyHiddenText = None,
          groupLevelInformationRoutes.GroupInterestCapacityController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ANGIE answer" must {

      "have a correctly formatted summary list row" in {

        helper.angie mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.angie,
          currencyFormat(angie),
          visuallyHiddenText = None,
          groupLevelInformationRoutes.EnterANGIEController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the QNGIE answer" must {

      "have a correctly formatted summary list row" in {

        helper.qngie mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.qngie,
          currencyFormat(qngie),
          visuallyHiddenText = None,
          groupLevelInformationRoutes.EnterQNGIEController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the GroupEBITDA answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupEBITDA mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.groupEBITDA,
          currencyFormat(ebitda),
          visuallyHiddenText = None,
          groupLevelInformationRoutes.GroupEBITDAController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the DisallowedAmount answer" must {

      "have a correctly formatted summary list row" in {

        helper.disallowedAmount mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.disallowedAmount,
          currencyFormat(disallowedAmount),
          visuallyHiddenText = None,
          groupLevelInformationRoutes.DisallowedAmountController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the Group Ratio Percentage answer" must {

      "have a correctly formatted summary list row" in {

        helper.groupRatioPercentage mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.groupRatioPercentage,
          groupRatioPercentage.toString,
          visuallyHiddenText = None,
          groupLevelInformationRoutes.GroupRatioPercentageController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ReturnContainEstimates answer" must {

      "have a correctly formatted summary list row" in {

        helper.returnContainEstimates mustBe Some(summaryListRow(
          CheckAnswersGroupLevelInformationMessages.returnContainEstimates,
          BaseMessages.no,
          visuallyHiddenText = None,
          groupLevelInformationRoutes.ReturnContainEstimatesController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }
  }
}
