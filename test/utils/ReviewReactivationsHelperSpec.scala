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

import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.BaseMessages
import base.SpecBase
import models.ReviewMode
import pages.ukCompanies.UkCompaniesPage
import viewmodels.SummaryListRowHelper


class ReviewReactivationsHelperSpec extends SpecBase with SummaryListRowHelper with CurrencyFormatter {

  lazy val helper = new ReviewReactivationsHelper(
    emptyUserAnswers
      .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
      .set(UkCompaniesPage, ukCompanyModelMax, Some(2)).get
      .set(UkCompaniesPage, ukCompanyModelMax, Some(3)).get
  )

  "ReviewReactivationsHelper.rows" when {

    "given a list of uk companies" should {

      "return the correct summary list row models" in {

        helper.rows mustBe Seq(
          summaryListRow(
            ukCompanyModelMax.companyDetails.companyName,
            currencyFormat(ukCompanyModelMax.allocatedReactivations.fold[BigDecimal](0)(_.reactivation)),
            controllers.ukCompanies.routes.ReactivationAmountController.onPageLoad(1, ReviewMode) -> BaseMessages.changeLink
          ),
          summaryListRow(
            ukCompanyModelMax.companyDetails.companyName,
            currencyFormat(ukCompanyModelMax.allocatedReactivations.fold[BigDecimal](0)(_.reactivation)),
            controllers.ukCompanies.routes.ReactivationAmountController.onPageLoad(2, ReviewMode) -> BaseMessages.changeLink
          ),
          summaryListRow(
            ukCompanyModelMax.companyDetails.companyName,
            currencyFormat(ukCompanyModelMax.allocatedReactivations.fold[BigDecimal](0)(_.reactivation)),
            controllers.ukCompanies.routes.ReactivationAmountController.onPageLoad(3, ReviewMode) -> BaseMessages.changeLink
          )
        )
      }
    }
  }

  "ReviewReactivationsHelper.totalReactivations" should {

    "return the total reactivations" in {
      val expectedTotal = ukCompanyModelMax.allocatedReactivations.get.reactivation * 3
      helper.totalReactivations mustBe Some(expectedTotal)
    }
  }
}
