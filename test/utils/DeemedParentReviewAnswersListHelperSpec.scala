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

import base.SpecBase
import pages.ultimateParentCompany._
import viewmodels.SummaryListRowHelper
import assets.constants.DeemedParentConstants._
import assets.messages.BaseMessages


class DeemedParentReviewAnswersListHelperSpec extends SpecBase with SummaryListRowHelper with CurrencyFormatter {

  "DeemedParentReviewAnswersListHelper.rows" when {

    "given a list of deemed companies" should {

      "return the correct summary list row models" in {

        val helper = new DeemedParentReviewAnswersListHelper(
          emptyUserAnswers
            .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).get
            .set(DeemedParentPage, deemedParentModelUkPartnership, Some(2)).get
            .set(DeemedParentPage, deemedParentModelNonUkCompany, Some(3)).get
        )

        helper.rows mustBe Seq(
          summaryListRow(
            deemedParentModelUkCompany.companyName.name,
            deemedParentModelUkCompany.utr.get.utr,
            controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onPageLoad(1) -> BaseMessages.review,
            controllers.ultimateParentCompany.routes.DeletionConfirmationController.onPageLoad(1) -> BaseMessages.delete
          ),
          summaryListRow(
            deemedParentModelUkPartnership.companyName.name,
            deemedParentModelUkPartnership.utr.get.utr,
            controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onPageLoad(2) -> BaseMessages.review,
            controllers.ultimateParentCompany.routes.DeletionConfirmationController.onPageLoad(2) -> BaseMessages.delete
          ),
          summaryListRow(
            deemedParentModelNonUkCompany.companyName.name,
            "",
            controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onPageLoad(3) -> BaseMessages.review,
            controllers.ultimateParentCompany.routes.DeletionConfirmationController.onPageLoad(3) -> BaseMessages.delete
          )
        )
      }
    }
  }
}
