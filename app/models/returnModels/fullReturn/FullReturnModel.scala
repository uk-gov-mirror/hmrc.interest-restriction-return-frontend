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

package models.returnModels.fullReturn

import models.returnModels._
import play.api.libs.json.Json

case class FullReturnModel(agentDetails: AgentDetailsModel,
                           reportingCompany: ReportingCompanyModel,
                           parentCompany: Option[ParentCompanyModel],
                           publicInfrastructure: Boolean,
                           groupCompanyDetails: GroupCompanyDetailsModel,
                           submissionType: SubmissionType,
                           revisedReturnDetails: Option[String],
                           groupLevelElections: GroupLevelElectionsModel,
                           ukCompanies: Seq[UkCompanyModel],
                           angie: Option[BigDecimal],
                           returnContainsEstimates: Boolean,
                           groupSubjectToInterestRestrictions: Boolean,
                           groupSubjectToInterestReactivation: Boolean,
                           totalReactivation: BigDecimal,
                           groupLevelAmount: GroupLevelAmountModel,
                           adjustedGroupInterest: Option[AdjustedGroupInterestModel]) {

  val oSum: Seq[BigDecimal] => Option[BigDecimal] = {
    case x if x.isEmpty => None
    case x => Some(x.sum)
  }

  val numberOfUkCompanies: Int = ukCompanies.length
  val aggregateNetTaxInterestIncome: BigDecimal = ukCompanies.map(_.netTaxInterestIncome).sum
  val aggregateNetTaxInterestExpense: BigDecimal = ukCompanies.map(_.netTaxInterestExpense).sum
  val aggregateTaxEBITDA: BigDecimal = ukCompanies.map(_.taxEBITDA).sum
  val aggregateAllocatedRestrictions: Option[BigDecimal] = oSum(ukCompanies.flatMap(_.allocatedRestrictions.flatMap(_.totalDisallowances)))
  val aggregateAllocatedReactivations: Option[BigDecimal] = oSum(ukCompanies.flatMap(_.allocatedReactivations.map(_.currentPeriodReactivation)))
}

object FullReturnModel {

  implicit val writes = Json.writes[FullReturnModel]
}