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

package sectionstatus

import pages.groupLevelInformation._
import pages.elections._
import base.SpecBase
import models.SectionStatus._
import assets.constants.BaseConstants
import models.UserAnswers

class GroupLevelInformationSectionStatusSpec extends SpecBase with BaseConstants {

  "getStatus" must {
    "return NotStarted" when {
      "no data from the section has been entered" in {
        val userAnswers = emptyUserAnswers
        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual NotStarted
      }
    }
    "return InProgress" when {
      "Subject to restrictions but total disallowed amount missing" in {
        val userAnswers = UserAnswers("id")
          .set(GroupRatioElectionPage, false).get
          .set(GroupSubjectToRestrictionsPage, true).get
          .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
          .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
          .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
          .set(EnterANGIEPage, BigDecimal(123.12)).get
          .set(ReturnContainEstimatesPage, false).get

        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "Group ratio elected but QNGIE/EBITDA/Percentage missing" in {
        val userAnswers = UserAnswers("id")
          .set(GroupRatioElectionPage, true).get
          .set(GroupSubjectToRestrictionsPage, true).get
          .set(DisallowedAmountPage, BigDecimal(123.12)).get
          .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
          .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
          .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
          .set(EnterANGIEPage, BigDecimal(123.12)).get
          .set(ReturnContainEstimatesPage, false).get

        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "Subject to reactivations but reactivations cap missing" in {
        val userAnswers = UserAnswers("id")
          .set(GroupSubjectToRestrictionsPage, false).get
          .set(GroupSubjectToReactivationsPage, true).get
          .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
          .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
          .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
          .set(GroupRatioElectionPage, true).get
          .set(EnterANGIEPage, BigDecimal(123.12)).get
          .set(EnterQNGIEPage, BigDecimal(123.12)).get
          .set(GroupEBITDAPage, BigDecimal(123.12)).get
          .set(GroupRatioPercentagePage, BigDecimal(70)).get
          .set(ReturnContainEstimatesPage, false).get

        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual InProgress
      }
    }
    "return Completed" when {
      "Subject to restrictions and group ratio not elected" in {
        val userAnswers = UserAnswers("id")
          .set(GroupRatioElectionPage, false).get
          .set(GroupSubjectToRestrictionsPage, true).get
          .set(DisallowedAmountPage, BigDecimal(123.12)).get
          .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
          .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
          .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
          .set(EnterANGIEPage, BigDecimal(123.12)).get
          .set(ReturnContainEstimatesPage, false).get

        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "Subject to restrictions and group ratio elected" in {
        val userAnswers = UserAnswers("id")
          .set(GroupRatioElectionPage, true).get
          .set(GroupSubjectToRestrictionsPage, true).get
          .set(DisallowedAmountPage, BigDecimal(123.12)).get
          .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
          .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
          .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
          .set(EnterANGIEPage, BigDecimal(123.12)).get
          .set(EnterQNGIEPage, BigDecimal(123.12)).get
          .set(GroupEBITDAPage, BigDecimal(123.12)).get
          .set(GroupRatioPercentagePage, BigDecimal(70)).get
          .set(ReturnContainEstimatesPage, false).get

        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "Not subject to restrictions and not subject to reactivations and group ratio elected" in {
        val userAnswers = UserAnswers("id")
          .set(GroupRatioElectionPage, true).get
          .set(GroupSubjectToRestrictionsPage, false).get
          .set(GroupSubjectToReactivationsPage, false).get
          .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
          .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
          .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
          .set(EnterANGIEPage, BigDecimal(123.12)).get
          .set(EnterQNGIEPage, BigDecimal(123.12)).get
          .set(GroupEBITDAPage, BigDecimal(123.12)).get
          .set(GroupRatioPercentagePage, BigDecimal(70)).get
          .set(ReturnContainEstimatesPage, false).get

        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "Not subject to restrictions and not subject to reactivations and group ratio not elected" in {
        val userAnswers = UserAnswers("id")
          .set(GroupRatioElectionPage, false).get
          .set(GroupSubjectToRestrictionsPage, false).get
          .set(GroupSubjectToReactivationsPage, false).get
          .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
          .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
          .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
          .set(EnterANGIEPage, BigDecimal(123.12)).get
          .set(ReturnContainEstimatesPage, false).get

        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "Subject to reactivations and group ratio elected" in {
        val userAnswers = UserAnswers("id")
          .set(GroupRatioElectionPage, true).get
          .set(GroupSubjectToRestrictionsPage, false).get
          .set(GroupSubjectToReactivationsPage, true).get
          .set(InterestReactivationsCapPage, BigDecimal(123.12)).get
          .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
          .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
          .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
          .set(EnterANGIEPage, BigDecimal(123.12)).get
          .set(EnterQNGIEPage, BigDecimal(123.12)).get
          .set(GroupEBITDAPage, BigDecimal(123.12)).get
          .set(GroupRatioPercentagePage, BigDecimal(70)).get
          .set(ReturnContainEstimatesPage, false).get

        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "Subject to reactivations and group ratio not elected" in {
        val userAnswers = UserAnswers("id")
          .set(GroupRatioElectionPage, false).get
          .set(GroupSubjectToRestrictionsPage, false).get
          .set(GroupSubjectToReactivationsPage, true).get
          .set(InterestReactivationsCapPage, BigDecimal(123.12)).get
          .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
          .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
          .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
          .set(EnterANGIEPage, BigDecimal(123.12)).get
          .set(ReturnContainEstimatesPage, false).get

        GroupLevelInformationSectionStatus.getStatus(userAnswers) mustEqual Completed
      }
    }
  }
}