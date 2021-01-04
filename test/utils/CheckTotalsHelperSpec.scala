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
import assets.constants.fullReturn.AllocatedReactivationsConstants.{reactivation, _}
import assets.constants.fullReturn.AllocatedRestrictionsConstants._
import assets.constants.fullReturn.FullReturnConstants.{fullReturnModelMax, fullReturnModelMin, fullReturnNetTaxExpenseModelMax, fullReturnNetTaxIncomeModelMax}
import assets.constants.fullReturn.UkCompanyConstants.{netTaxInterestIncome, _}
import assets.messages.BaseMessages
import base.SpecBase
import models.NetTaxInterestIncomeOrExpense.{NetTaxInterestExpense, NetTaxInterestIncome}
import viewmodels.SummaryListRowHelper
import assets.messages.checkTotals.CheckTotalsMessages

class CheckTotalsHelperSpec extends SpecBase with BaseConstants with SummaryListRowHelper with CurrencyFormatter {

  val helper = new CheckTotalsHelper

  "calculateSums" when {

    "deriving the numberOfUkCompanies when given one or multiple companies" in {

      val result = helper.calculateSums(fullReturnModelMin.ukCompanies)

      result.ukCompaniesLength mustBe 1

      val result1 = helper.calculateSums(fullReturnModelMax.ukCompanies)

      result1.ukCompaniesLength mustBe 2

    }

    "deriving the aggregateNetTaxInterest" when {

      "income is bigger" in {

        val result = helper.calculateSums(fullReturnNetTaxIncomeModelMax.ukCompanies)

        result.aggregateInterest mustBe ((3 * netTaxInterestIncome) - netTaxInterestExpense)
      }

      "expense is bigger" in {

        val result = helper.calculateSums(fullReturnNetTaxExpenseModelMax.ukCompanies)

        result.aggregateInterest mustBe (netTaxInterestIncome - (3 * netTaxInterestExpense))
      }

      "income and expense are equal" in {

        val result = helper.calculateSums(fullReturnModelMax.ukCompanies)

        result.aggregateInterest mustBe 0
      }
    }

    "deriving the aggregateTaxEBITDA" when {

      "one company has a taxEBITDA" in {

        val result = helper.calculateSums(fullReturnModelMin.ukCompanies)

        result.aggregateEbitda mustBe taxEBITDA
      }

      "multiple companies have a taxEBITDA" in {

        val result = helper.calculateSums(fullReturnModelMax.ukCompanies)

        result.aggregateEbitda mustBe (2 * taxEBITDA)
      }
    }

    "deriving the aggregateAllocatedRestrictions" when {

      "no companies have a allocatedRestrictions" in {

        val result = helper.calculateSums(fullReturnModelMax.ukCompanies)

        result.aggregateEbitda mustBe (2 * taxEBITDA)
      }

      "one company has a allocatedRestrictions" in {

        val result = helper.calculateSums(fullReturnModelMax.ukCompanies)

        result.restrictions.get mustBe totalDisallowances
      }

      "multiple companies have a allocatedRestrictions" in {

        val result = helper.calculateSums(fullReturnNetTaxExpenseModelMax.ukCompanies)

        result.restrictions.get mustBe 3 * totalDisallowances
      }
    }

    "deriving the aggregateAllocatedReactivations" when {

      "no companies have a allocatedRestrictions" in {

        val result = helper.calculateSums(fullReturnModelMin.ukCompanies)

        result.restrictions mustBe None
      }

      "one company has a allocatedRestrictions" in {

        val result = helper.calculateSums(fullReturnModelMax.ukCompanies)

        result.reactivations mustBe Some(reactivation)
      }

      "multiple companies have a allocatedRestrictions" in {

        val result = helper.calculateSums(fullReturnNetTaxExpenseModelMax.ukCompanies)

        result.reactivations mustBe Some(3 * reactivation)
      }
    }
  }

  "constructTotalsTable" when {

    "must contain the correct summary list rows with the correct links" in {

      val result = helper.constructTotalsTable(Seq(ukCompanyModelMax))

      result mustBe
        Seq(
          summaryListRow(
            CheckTotalsMessages.t1,
            "1",
            controllers.ukCompanies.routes.UkCompaniesReviewAnswersListController.onPageLoad() -> BaseMessages.review
          ),
          summaryListRow(
            CheckTotalsMessages.t2,
            currencyFormat(taxEBITDA),
            controllers.checkTotals.routes.ReviewTaxEBITDAController.onPageLoad() -> BaseMessages.review
          ),
          summaryListRow(
            CheckTotalsMessages.t3Expense,
            currencyFormat(netTaxInterestExpense),
            controllers.checkTotals.routes.ReviewNetTaxInterestController.onPageLoad() -> BaseMessages.review
          ),
          summaryListRow(
            CheckTotalsMessages.t4,
            currencyFormat(totalDisallowances),
            controllers.routes.UnderConstructionController.onPageLoad() -> BaseMessages.review
          ),
          summaryListRow(
            CheckTotalsMessages.t5,
            currencyFormat(reactivation),
            controllers.checkTotals.routes.ReviewReactivationsController.onPageLoad() -> BaseMessages.review
          )
        )
    }

    val model = fullReturnModelMin.ukCompanies.head

    "given the first 3 mandatory fields, should not display the last 2 optional fields" in {

      val result = helper.constructTotalsTable(Seq(model))

      result.length mustBe 3
    }

    "given the first 3 mandatory fields, and 'reactivations', should hide 'restrictions'" in {

      val result = helper.constructTotalsTable(Seq(model.copy(allocatedReactivations = Some(allocatedReactivationsModel))))

      result.length mustBe 4
      result(3).key.content.asHtml.toString() mustBe "Total reactivations"
    }

    "given the first 3 mandatory fields, and 'restrictions', should hide 'reactivations'" in {

      val result = helper.constructTotalsTable(Seq(model.copy(allocatedRestrictions = Some(allocatedRestrictionsModel))))

      result.length mustBe 4
      result(3).key.content.asHtml.toString() mustBe "Total disallowed amount"
    }

    "given the first 3 mandatory fields, 'restrictions' and 'reactivations', should hide nothing" in {

      val result = helper.constructTotalsTable(Seq(model.copy(allocatedRestrictions = Some(allocatedRestrictionsModel),
        allocatedReactivations = Some(allocatedReactivationsModel))))

      result.length mustBe 5
    }

    "say net tax interest income if it is income" in {

      val result = helper.constructTotalsTable(Seq(model.copy(netTaxInterestIncomeOrExpense = Some(NetTaxInterestIncome),
        netTaxInterest = Some(100.0))))

      result(2).key.content.asHtml.toString() mustBe "Aggregate net tax-interest income"
      result(2).value.content.asHtml.toString() mustBe "&pound;100"
    }

    "say net tax interest expense if it is expense" in {

      val result = helper.constructTotalsTable(Seq(model.copy(netTaxInterestIncomeOrExpense = Some(NetTaxInterestExpense),
        netTaxInterest = Some(100.0))))

      result(2).key.content.asHtml.toString() mustBe "Aggregate net tax-interest expense"
      result(2).value.content.asHtml.toString() mustBe "&pound;100"
    }
  }
}
