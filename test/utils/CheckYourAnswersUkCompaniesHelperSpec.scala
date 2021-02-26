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
import assets.constants.fullReturn.AllocatedReactivationsConstants._
import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.{BaseMessages, CheckAnswersUkCompanyMessages}
import base.SpecBase
import controllers.ukCompanies.{routes => ukCompaniesRoutes}
import models.{CheckMode, CompanyEstimatedFigures}
import pages.ukCompanies._
import viewmodels.SummaryListRowHelper

class CheckYourAnswersUkCompaniesHelperSpec extends SpecBase with BaseConstants with SummaryListRowHelper with CurrencyFormatter {

  val company = ukCompanyModelMax.copy(
    containsEstimates = Some(true),
    estimatedFigures = Some(CompanyEstimatedFigures.values.toSet)
  )

  val helper = new CheckYourAnswersUkCompanyHelper(
    emptyUserAnswers
      .set(UkCompaniesPage, company, Some(1)).success.value
  )

  "Check Your Answers Helper" must {

    "For the Company Name answer" must {

      "have a correctly formatted summary list row" in {

        helper.companyName(1) mustBe Some(summaryListRow(
          CheckAnswersUkCompanyMessages.companyName,
          companyNameModel.name,
          visuallyHiddenText = None,
          ukCompaniesRoutes.CompanyDetailsController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the CT UTR answer" must {

      "have a correctly formatted summary list row" in {

        helper.ctutr(1) mustBe Some(summaryListRow(
          CheckAnswersUkCompanyMessages.companyCTUTR,
          ctutrModel.utr,
          visuallyHiddenText = None,
          ukCompaniesRoutes.CompanyDetailsController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the Consenting Company answer" must {

      "have a correctly formatted summary list row" in {

        helper.consentingCompany(1) mustBe Some(summaryListRow(
          CheckAnswersUkCompanyMessages.consenting,
          BaseMessages.yes,
          visuallyHiddenText = None,
          ukCompaniesRoutes.ConsentingCompanyController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the enterCompanyTaxEBITDA answer" must {

      "have a correctly formatted summary list row" in {

        helper.enterCompanyTaxEBITDA(1) mustBe Some(summaryListRow(
          CheckAnswersUkCompanyMessages.taxEBITDA,
          currencyFormat(taxEBITDA),
          visuallyHiddenText = None,
          ukCompaniesRoutes.EnterCompanyTaxEBITDAController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the netTaxInterestAmount answer" must {

      "have a correctly formatted summary list row" in {

        helper.netTaxInterestAmount(1) mustBe Some(summaryListRow(
          CheckAnswersUkCompanyMessages.netTaxInterest,
          currencyFormat(netTaxInterestIncome) + " Expense",
          visuallyHiddenText = None,
          ukCompaniesRoutes.NetTaxInterestAmountController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the companyReactivationAmount answer" must {

      "have a correctly formatted summary list row" in {

        helper.companyReactivationAmount(1) mustBe Some(summaryListRow(
          CheckAnswersUkCompanyMessages.reactivationAmount,
          currencyFormat(reactivation),
          visuallyHiddenText = None,
          ukCompaniesRoutes.ReactivationAmountController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the Contains Estimates answer" must {

      "have a correctly formatted summary list row" in {

        helper.containsEstimates(1) mustBe Some(summaryListRow(
          CheckAnswersUkCompanyMessages.estimates,
          BaseMessages.yes,
          visuallyHiddenText = None,
          ukCompaniesRoutes.CompanyContainsEstimatesController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the CompanyEstimatedFigures answer" must {

      "have a correctly formatted summary list row" in {
        helper.estimatedFigures(1) mustBe Some(summaryListRow(
          CheckAnswersUkCompanyMessages.estimatedFigures,
          "Tax EBITDA<br>Net tax-interest amount<br>Allocated restrictions<br>Allocated reactivations",
          visuallyHiddenText = None,
          ukCompaniesRoutes.CompanyEstimatedFiguresController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }
  }
}
