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

import models.NetTaxInterestIncomeOrExpense.NetTaxInterestIncome
import models.returnModels.fullReturn.UkCompanyModel
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.SummaryListRowHelper

class CheckTotalsHelper extends SummaryListRowHelper with CurrencyFormatter {

  def constructTotalsTable(ukCompanies: Seq[UkCompanyModel])(implicit messages: Messages): Seq[SummaryListRow] = {
    val derivedData = calculateSums(ukCompanies)
    val numberOfUkCompanies = Some(summaryListRow(messages("derivedCompany.t1"),
      derivedData.ukCompaniesLength.toString,(controllers.routes.UnderConstructionController.onPageLoad(),messages("derivedCompany.review"))))
    val aggregateTaxEBITDA = Some(summaryListRow(messages("derivedCompany.t2"),
      currencyFormat(derivedData.aggregateEbitda),(controllers.routes.UnderConstructionController.onPageLoad(),messages("derivedCompany.review"))))
    val aggregateNetTaxInterest = Some(summaryListRow(messages("derivedCompany.t3"),
      currencyFormat(derivedData.aggregateInterest),(controllers.routes.UnderConstructionController.onPageLoad(),messages("derivedCompany.review"))))

    val aggregateAllocatedRestrictions = derivedData.restrictions match {
      case Some(r) => Some(summaryListRow(messages("derivedCompany.t4"),
        currencyFormat(r),(controllers.routes.UnderConstructionController.onPageLoad(),messages("derivedCompany.review"))))
      case None => None
    }
    val aggregateAllocatedReactivations = derivedData.reactivations match {
      case Some(r) => Some(summaryListRow(messages("derivedCompany.t5"),
        currencyFormat(r),(controllers.routes.UnderConstructionController.onPageLoad(),messages("derivedCompany.review"))))
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
    val aggregateEbitda: BigDecimal = ukCompanies.flatMap(_.taxEBITDA).sum
    val restrictions: Option[BigDecimal] = oSum(ukCompanies.flatMap(_.allocatedRestrictions.flatMap(_.totalDisallowances)))
    val reactivations: Option[BigDecimal] = oSum(ukCompanies.flatMap(_.allocatedReactivations.map(_.currentPeriodReactivation)))

    UKCompanyDerivedData(companyLength,aggregateEbitda,aggregateInterest,restrictions,reactivations)
  }

  case class UKCompanyDerivedData(ukCompaniesLength: Int, aggregateEbitda: BigDecimal, aggregateInterest: BigDecimal,
                                  restrictions: Option[BigDecimal], reactivations: Option[BigDecimal])
}
