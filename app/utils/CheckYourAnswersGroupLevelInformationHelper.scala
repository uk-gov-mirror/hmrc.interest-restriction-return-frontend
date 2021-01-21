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

import controllers.groupLevelInformation.routes
import models.{CheckMode, UserAnswers}
import pages.groupLevelInformation._
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersGroupLevelInformationHelper(val userAnswers: UserAnswers)
                                                 (implicit val messages: Messages) extends CheckYourAnswersHelper {

  def returnContainEstimates: Option[SummaryListRow] =
    answer(ReturnContainEstimatesPage, routes.ReturnContainEstimatesController.onPageLoad(CheckMode))

  def groupInterestAllowance: Option[SummaryListRow] =
    monetaryAnswer(GroupInterestAllowancePage, routes.GroupInterestAllowanceController.onPageLoad(CheckMode))

  def groupInterestCapacity: Option[SummaryListRow] =
    monetaryAnswer(GroupInterestCapacityPage, routes.GroupInterestCapacityController.onPageLoad(CheckMode))

  def groupSubjectToRestrictions: Option[SummaryListRow] =
    answer(GroupSubjectToRestrictionsPage, routes.GroupSubjectToRestrictionsController.onPageLoad(CheckMode))

  def interestReactivationsCap: Option[SummaryListRow] =
    monetaryAnswer(InterestReactivationsCapPage, routes.InterestReactivationsCapController.onPageLoad(CheckMode))

  def groupSubjectToReactivations: Option[SummaryListRow] =
    answer(GroupSubjectToReactivationsPage, routes.GroupSubjectToReactivationsController.onPageLoad(CheckMode))

  def interestAllowanceBroughtForward: Option[SummaryListRow] =
    monetaryAnswer(InterestAllowanceBroughtForwardPage, routes.InterestAllowanceBroughtForwardController.onPageLoad(CheckMode))

  def disallowedAmount: Option[SummaryListRow] =
    monetaryAnswer(DisallowedAmountPage, routes.DisallowedAmountController.onPageLoad(CheckMode))

  def angie: Option[SummaryListRow] =
    monetaryAnswer(EnterANGIEPage, routes.EnterANGIEController.onPageLoad(CheckMode))

  def qngie: Option[SummaryListRow] =
    monetaryAnswer(EnterQNGIEPage, routes.EnterQNGIEController.onPageLoad(CheckMode))

  def groupEBITDA: Option[SummaryListRow] =
    monetaryAnswer(GroupEBITDAPage, routes.GroupEBITDAController.onPageLoad(CheckMode))

  def groupRatioPercentage: Option[SummaryListRow] =
    answer(GroupRatioPercentagePage, routes.GroupRatioPercentageController.onPageLoad(CheckMode))
>>>>>>> CIR-903 CIR-941 CIR-942 CIR-932 CIR-931 CIR-1186 CIR-1187 - Add CYA and nav

  val rows: Seq[SummaryListRow] = Seq(
    groupSubjectToRestrictions,
    groupSubjectToReactivations,
    interestAllowanceBroughtForward
    interestReactivationsCap,
    disallowedAmount,
    interestAllowanceBroughtForward,
    groupInterestAllowance,
    groupInterestCapacity,
    angie,
    qngie,
    groupEBITDA,
    groupRatioPercentage,
    returnContainEstimates
  ).flatten
}
