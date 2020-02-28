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

import models.NormalMode
import models.returnModels.fullReturn.UkCompanyModel
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import viewmodels.SummaryListRowHelper

class CheckTotalsHelper extends SummaryListRowHelper {

  def constructTotalsTable(ukCompanies: Seq[UkCompanyModel]): Seq[SummaryListRow] = {
    val derivedData = calculateSums(ukCompanies)
    val numberOfUkCompanies = Some(summaryListRow("Companies added",
      derivedData.ukCompaniesLength.toString,(controllers.ukCompanies.routes.AboutAddingUKCompaniesController.onPageLoad(),"Review")))
    val aggregateTaxEBITDA = Some(summaryListRow("Aggregate Tax-EBITDA",
      derivedData.aggregateEbitda.toString(),(controllers.ukCompanies.routes.EnterCompanyTaxEBITDAController.onPageLoad(NormalMode),"Review")))
    val aggregateNetTaxInterest = Some(summaryListRow("Aggregate net tax-interest",
      derivedData.aggregateInterest.toString(),(Call("GET","http://letmegooglethat.com/?q=+Aggregate+net+tax-interest"),"TODO")))

    val aggregateAllocatedRestrictions = derivedData.restrictions match {
      case Some(r) => Some(summaryListRow("Total disallowed amount",r.toString,
        (Call("GET","http://letmegooglethat.com/?q=+Aggregate+net+tax-interest+"),"TODO")))
      case None => None
    }
    val aggregateAllocatedReactivations = derivedData.reactivations match {
      case Some(r) => Some(summaryListRow("Total reactivations",r.toString,
        (Call("GET","http://letmegooglethat.com/?q=+Total+reactivations"),"TODO")))
      case None => None
    }
    Seq(numberOfUkCompanies,aggregateTaxEBITDA,aggregateNetTaxInterest,aggregateAllocatedRestrictions,aggregateAllocatedReactivations).flatten
  }

  def calculateSums(ukCompanies: Seq[UkCompanyModel]): UKCompanyDerivedData = {
    val totalTaxInterestIncome: BigDecimal = ukCompanies.flatMap(_.netTaxInterestIncome).sum
    val totalTaxInterestExpense: BigDecimal = ukCompanies.flatMap(_.netTaxInterestExpense).sum
    val oSum: Seq[BigDecimal] => Option[BigDecimal] = {
      case sum if sum.isEmpty => None
      case sum => Some(sum.sum)
    }
    val companyLength = ukCompanies.length
    val aggregateEbitda = ukCompanies.flatMap(_.taxEBITDA).sum
    val aggregateInterest = totalTaxInterestIncome - totalTaxInterestExpense
    val restrictions = oSum(ukCompanies.flatMap(_.allocatedRestrictions.flatMap(_.totalDisallowances)))
    val reactivations = oSum(ukCompanies.flatMap(_.allocatedReactivations.map(_.currentPeriodReactivation)))
    UKCompanyDerivedData(companyLength,aggregateEbitda,aggregateInterest,restrictions,reactivations)
  }

  case class UKCompanyDerivedData(ukCompaniesLength: Int, aggregateEbitda: BigDecimal, aggregateInterest: BigDecimal,
                                  restrictions: Option[BigDecimal], reactivations: Option[BigDecimal])
}
