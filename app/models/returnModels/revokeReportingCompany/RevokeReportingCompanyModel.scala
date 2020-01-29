package models.returnModels.revokeReportingCompany

import models.returnModels._
import play.api.libs.json.Json

case class RevokeReportingCompanyModel(agentDetails: AgentDetailsModel,
                                       reportingCompany: ReportingCompanyModel,
                                       isReportingCompanyRevokingItself: Boolean,
                                       companyMakingRevocation: Option[IdentityOfCompanySubmittingModel],
                                       ultimateParent: Option[UltimateParentModel],
                                       accountingPeriod: AccountingPeriodModel,
                                       authorisingCompanies: Seq[AuthorisingCompanyModel],
                                       declaration: Boolean)

object RevokeReportingCompanyModel {

  implicit val writes = Json.writes[RevokeReportingCompanyModel]

}
