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

import models.NetTaxInterestIncomeOrExpense.NetTaxInterestIncome
import models.returnModels.fullReturn.UkCompanyModel
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.SummaryListRowHelper

class CheckTotalsHelper extends SummaryListRowHelper with CurrencyFormatter {

  def constructTotalsTable(ukCompanies: Seq[UkCompanyModel])(implicit messages: Messages): Seq[SummaryListRow] = {
    val derivedData = calculateSums(ukCompanies)

    val numberOfUkCompanies = Some(summaryListRow(
      messages("derivedCompany.t1"),
      derivedData.ukCompaniesLength.toString,
      visuallyHiddenText = None,
      (controllers.ukCompanies.routes.UkCompaniesReviewAnswersListController.onPageLoad(),messages("site.review"))))

    val aggregateTaxEBITDA = Some(summaryListRow(
      messages("derivedCompany.t2"),
      currencyFormat(derivedData.aggregateEbitda),
      visuallyHiddenText = None,
      (controllers.checkTotals.routes.ReviewTaxEBITDAController.onPageLoad(),messages("site.review"))))

    val aggregateNetTaxInterest = if(derivedData.aggregateInterest>=0){
      Some(summaryListRow(
        messages("derivedCompany.t3-income"),
        currencyFormat(derivedData.aggregateInterest),
        visuallyHiddenText = None,
        (controllers.checkTotals.routes.ReviewNetTaxInterestController.onPageLoad(),messages("site.review"))))
    } else {
      Some(summaryListRow(
        messages("derivedCompany.t3-expense"),
        currencyFormat(derivedData.aggregateInterest.abs),
        visuallyHiddenText = None,
        (controllers.checkTotals.routes.ReviewNetTaxInterestController.onPageLoad(),messages("site.review"))))
    }
    val aggregateAllocatedRestrictions = derivedData.restrictions match {
      case Some(r) => Some(summaryListRow(
        messages("derivedCompany.t4"),
        currencyFormat(r),
        visuallyHiddenText = None,
        (controllers.routes.UnderConstructionController.onPageLoad(),messages("site.review"))))
      case None => None
    }
    val aggregateAllocatedReactivations = derivedData.reactivations match {
      case Some(r) => Some(summaryListRow(
        messages("derivedCompany.t5"),
        currencyFormat(r),
        visuallyHiddenText = None,
        (controllers.checkTotals.routes.ReviewReactivationsController.onPageLoad(),messages("site.review"))))
      case None => None
    }
    Seq(numberOfUkCompanies,aggregateTaxEBITDA,aggregateNetTaxInterest,aggregateAllocatedRestrictions,aggregateAllocatedReactivations).flatten
  }

  def calculateSums(ukCompanies: Seq[UkCompanyModel]): UKCompanyDerivedData = {
    val companyLength: Int = ukCompanies.length
    val oSum: Seq[BigDecimal] => Option[BigDecimal] = {
      case sum if sum.isEmpty => None
      case sum => Some(sum.sum)
    }
    val aggregateInterest: BigDecimal = (for {
      ukCompany <- ukCompanies
      incomeOrExpense <- ukCompany.netTaxInterestIncomeOrExpense
      netTaxInterest <- ukCompany.netTaxInterest
      amount = if(incomeOrExpense == NetTaxInterestIncome) netTaxInterest else netTaxInterest * -1
    } yield amount).sum
    val aggregateEbitda: BigDecimal = ukCompanies.flatMap(_.taxEBITDA).sum match {
      case sum if sum > 0.0 => sum
      case _ => 0.0
    }
    val restrictions: Option[BigDecimal] = oSum(ukCompanies.flatMap(_.allocatedRestrictions.map(_.totalDisallowances)))
    val reactivations: Option[BigDecimal] = oSum(ukCompanies.flatMap(_.allocatedReactivations.map(_.reactivation)))

    UKCompanyDerivedData(companyLength,aggregateEbitda,aggregateInterest,restrictions,reactivations)
  }

  case class UKCompanyDerivedData(ukCompaniesLength: Int, aggregateEbitda: BigDecimal, aggregateInterest: BigDecimal,
                                  restrictions: Option[BigDecimal], reactivations: Option[BigDecimal])
}
