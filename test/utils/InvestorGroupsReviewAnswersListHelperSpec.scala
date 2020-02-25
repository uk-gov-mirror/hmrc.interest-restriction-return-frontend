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

import assets.constants.InvestorGroupConstants._
import assets.messages.BaseMessages
import assets.messages.elections.InvestorGroupsReviewAnswersListMessages
import base.SpecBase
import controllers.elections.routes
import models.NormalMode
import pages.elections.InvestorGroupsPage
import viewmodels.SummaryListRowHelper


class InvestorGroupsReviewAnswersListHelperSpec extends SpecBase with SummaryListRowHelper with CurrencyFormatter {

  "InvestorGroupsReviewAnswersListHelper.rows" when {

    "given a list of investor groups" should {

      "return the correct summary list row models" in {

        val helper = new InvestorGroupsReviewAnswersListHelper(
          emptyUserAnswers
            .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).get
            .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(2)).get
            .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(3)).get
        )

        helper.rows mustBe Seq(
          summaryListRow(
            investorGroupsGroupRatioModel.investorName,
            InvestorGroupsReviewAnswersListMessages.groupRatio,
            routes.InvestorGroupNameController.onPageLoad(1, NormalMode) -> BaseMessages.changeLink,
            routes.InvestorGroupsDeletionConfirmationController.onPageLoad(1) -> BaseMessages.delete
          ),
          summaryListRow(
            investorGroupsFixedRatioModel.investorName,
            InvestorGroupsReviewAnswersListMessages.fixedRatio,
            routes.InvestorGroupNameController.onPageLoad(2, NormalMode) -> BaseMessages.changeLink,
            routes.InvestorGroupsDeletionConfirmationController.onPageLoad(2) -> BaseMessages.delete
          ),
          summaryListRow(
            investorGroupsFixedRatioModel.investorName,
            InvestorGroupsReviewAnswersListMessages.fixedRatio,
            routes.InvestorGroupNameController.onPageLoad(3, NormalMode) -> BaseMessages.changeLink,
            routes.InvestorGroupsDeletionConfirmationController.onPageLoad(3) -> BaseMessages.delete
          )
        )
      }
    }
  }
}
