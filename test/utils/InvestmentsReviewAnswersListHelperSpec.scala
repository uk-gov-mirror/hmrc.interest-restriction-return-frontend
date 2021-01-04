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

import assets.constants.NonConsolidatedInvestmentConstants._
import assets.messages.BaseMessages
import controllers.elections.routes
import base.SpecBase
import models.CheckMode
import pages.elections.InvestmentNamePage
import viewmodels.SummaryListRowHelper


class InvestmentsReviewAnswersListHelperSpec extends SpecBase with SummaryListRowHelper with CurrencyFormatter {

  "InvestmentsReviewAnswersListHelper.rows" when {

    "given a list of investments" should {

      "return the correct summary list row models" in {

        val helper = new InvestmentsReviewAnswersListHelper(
          emptyUserAnswers
            .set(InvestmentNamePage, investmentName, Some(1)).get
            .set(InvestmentNamePage, investmentName, Some(2)).get
            .set(InvestmentNamePage, investmentName, Some(3)).get
        )

        helper.rows mustBe Seq(
          summaryListRow(
            investmentName,
            "",
            routes.InvestmentNameController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink,
            routes.InvestmentsDeletionConfirmationController.onPageLoad(1) -> BaseMessages.delete
          ),
          summaryListRow(
            investmentName,
            "",
            routes.InvestmentNameController.onPageLoad(2, CheckMode) -> BaseMessages.changeLink,
            routes.InvestmentsDeletionConfirmationController.onPageLoad(2) -> BaseMessages.delete
          ),
          summaryListRow(
            investmentName,
            "",
            routes.InvestmentNameController.onPageLoad(3, CheckMode) -> BaseMessages.changeLink,
            routes.InvestmentsDeletionConfirmationController.onPageLoad(3) -> BaseMessages.delete
          )
        )
      }
    }
  }
}
