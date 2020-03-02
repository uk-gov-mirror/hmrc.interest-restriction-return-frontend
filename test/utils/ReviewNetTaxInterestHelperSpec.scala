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

import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.BaseMessages
import base.SpecBase
import models.CheckMode
import models.NetTaxInterestIncomeOrExpense.{NetTaxInterestExpense, NetTaxInterestIncome}
import pages.ukCompanies.UkCompaniesPage
import viewmodels.SummaryListRowHelper


class ReviewNetTaxInterestHelperSpec extends SpecBase with SummaryListRowHelper with CurrencyFormatter {

  "ReviewNetTaxInterestHelper.rows" when {

    "given a list of uk companies" should {

      "return the correct summary list row models" in {

        val helper = new ReviewNetTaxInterestHelper(
          emptyUserAnswers
            .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
            .set(UkCompaniesPage, ukCompanyModelReactivationMaxExpense, Some(2)).get
            .set(UkCompaniesPage, ukCompanyModelMin, Some(3)).get
        )

        helper.rows mustBe Seq(
          summaryListRow(
            ukCompanyModelMax.companyDetails.companyName,
            s"${currencyFormat(netTaxInterestExpense)} $NetTaxInterestExpense",
            controllers.ukCompanies.routes.NetTaxInterestIncomeOrExpenseController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
          ),
          summaryListRow(
            ukCompanyModelReactivationMaxExpense.companyDetails.companyName,
            s"${currencyFormat(netTaxInterestExpense)} $NetTaxInterestExpense",
            controllers.ukCompanies.routes.NetTaxInterestIncomeOrExpenseController.onPageLoad(2, CheckMode) -> BaseMessages.changeLink
          ),
          summaryListRow(
            ukCompanyModelMin.companyDetails.companyName,
            s"${currencyFormat(netTaxInterestIncome)} $NetTaxInterestIncome",
            controllers.ukCompanies.routes.NetTaxInterestIncomeOrExpenseController.onPageLoad(3, CheckMode) -> BaseMessages.changeLink
          )
        )
      }
    }
  }
}