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

import assets.messages.BaseMessages
import base.SpecBase
import models.NormalMode
import viewmodels.SummaryListRowHelper
import pages.ukCompanies.{CompanyAccountingPeriodEndDatePage, RestrictionAmountForAccountingPeriodPage}
import java.time.LocalDate

class ReviewCompanyRestrictionsHelperSpec extends SpecBase with SummaryListRowHelper with CurrencyFormatter {

  "ReviewCompanyRestrictionsHelper.rows" when {

    "return the correct summary list row models" when {

      val firstDate = LocalDate.of(2020,1,1)
      val firstAmountValue = BigDecimal(111)
      val secondDate = LocalDate.of(2020,2,2)
      val secondAmountValue = BigDecimal(222)
      val thirdDate = LocalDate.of(2020,3,3)
      val thirdAmountValue = BigDecimal(333)

      "A single accounting period is entered" in {
        val userAnswers = (for {
          ua      <- emptyUserAnswers.set(CompanyAccountingPeriodEndDatePage(1, 1), firstDate)
          finalUa <- ua.set(RestrictionAmountForAccountingPeriodPage(1, 1), firstAmountValue)
        } yield finalUa).get

        val index = 1
        val helper = new ReviewCompanyRestrictionsHelper(index, userAnswers)

        helper.rows mustBe Seq(
          summaryListRow(
            "First accounting period",
            currencyFormat(firstAmountValue),
            visuallyHiddenText = None,
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 1) -> BaseMessages.review
          ),
          summaryListRow(
            "Total restrictions",
            currencyFormat(firstAmountValue),
            visuallyHiddenText = None
          )
        )
      }

      "Two accounting periods are entered" in {
        val userAnswers = (for {
          ua      <- emptyUserAnswers.set(CompanyAccountingPeriodEndDatePage(1, 1), firstDate)
          ua2     <- ua.set(RestrictionAmountForAccountingPeriodPage(1, 1), firstAmountValue)
          ua3     <- ua2.set(CompanyAccountingPeriodEndDatePage(1, 2), secondDate)
          finalUa <- ua3.set(RestrictionAmountForAccountingPeriodPage(1, 2), secondAmountValue)
        } yield finalUa).get

        val index = 1
        val helper = new ReviewCompanyRestrictionsHelper(index, userAnswers)

        helper.rows mustBe Seq(
          summaryListRow(
            "First accounting period",
            currencyFormat(firstAmountValue),
            visuallyHiddenText = None,
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 1) -> BaseMessages.review
          ),          
          summaryListRow(
            "Second accounting period",
            currencyFormat(secondAmountValue),
            visuallyHiddenText = None,
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 2) -> BaseMessages.review,
            controllers.ukCompanies.routes.RestrictionDeletionConfirmationController.onPageLoad(1, 2, NormalMode) -> BaseMessages.remove
          ),
          summaryListRow(
            "Total restrictions",
            currencyFormat(firstAmountValue + secondAmountValue),
            visuallyHiddenText = None
          )
        )
      }

      "Three accounting periods are entered" in {
        val userAnswers = (for {
          ua      <- emptyUserAnswers.set(CompanyAccountingPeriodEndDatePage(1, 1), firstDate)
          ua2     <- ua.set(RestrictionAmountForAccountingPeriodPage(1, 1), firstAmountValue)
          ua3     <- ua2.set(CompanyAccountingPeriodEndDatePage(1, 2), secondDate)
          ua4     <- ua3.set(RestrictionAmountForAccountingPeriodPage(1, 2), secondAmountValue)
          ua5     <- ua4.set(CompanyAccountingPeriodEndDatePage(1, 3), thirdDate)
          finalUa <- ua5.set(RestrictionAmountForAccountingPeriodPage(1, 3), thirdAmountValue)
        } yield finalUa).get

        val index = 1
        val helper = new ReviewCompanyRestrictionsHelper(index, userAnswers)

        helper.rows mustBe Seq(
          summaryListRow(
            "First accounting period",
            currencyFormat(firstAmountValue),
            visuallyHiddenText = None,
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 1) -> BaseMessages.review
          ),
          summaryListRow(
            "Second accounting period",
            currencyFormat(secondAmountValue),
            visuallyHiddenText = None,
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 2) -> BaseMessages.review
          ),
          summaryListRow(
            "Third accounting period",
            currencyFormat(thirdAmountValue),
            visuallyHiddenText = None,
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 3) -> BaseMessages.review,
            controllers.ukCompanies.routes.RestrictionDeletionConfirmationController.onPageLoad(1, 3, NormalMode) -> BaseMessages.remove
          ),
          summaryListRow(
            "Total restrictions",
            currencyFormat(firstAmountValue + secondAmountValue + thirdAmountValue),
            visuallyHiddenText = None
          )
        )
      }

      "Three accounting periods are entered, but the second doesn't have a restriction amount" in {
        val userAnswers = (for {
          ua      <- emptyUserAnswers.set(CompanyAccountingPeriodEndDatePage(1, 1), firstDate)
          ua2     <- ua.set(RestrictionAmountForAccountingPeriodPage(1, 1), firstAmountValue)
          ua3     <- ua2.set(CompanyAccountingPeriodEndDatePage(1, 2), secondDate)
          ua4     <- ua3.set(CompanyAccountingPeriodEndDatePage(1, 3), thirdDate)
          finalUa <- ua4.set(RestrictionAmountForAccountingPeriodPage(1, 3), thirdAmountValue)
        } yield finalUa).get

        val index = 1
        val helper = new ReviewCompanyRestrictionsHelper(index, userAnswers)

        helper.rows mustBe Seq(
          summaryListRow(
            "First accounting period",
            currencyFormat(firstAmountValue),
            visuallyHiddenText = None,
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 1) -> BaseMessages.review
          ),
          summaryListRow(
            "Second accounting period",
            currencyFormat(0),
            visuallyHiddenText = None,
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 2) -> BaseMessages.review
          ),
          summaryListRow(
            "Third accounting period",
            currencyFormat(thirdAmountValue),
            visuallyHiddenText = None,
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 3) -> BaseMessages.review,
            controllers.ukCompanies.routes.RestrictionDeletionConfirmationController.onPageLoad(1, 3, NormalMode) -> BaseMessages.remove
          ),
          summaryListRow(
            "Total restrictions",
            currencyFormat(firstAmountValue + thirdAmountValue),
            visuallyHiddenText = None
          )
        )
      }

    }
  }
}