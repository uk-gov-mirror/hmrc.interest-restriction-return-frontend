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