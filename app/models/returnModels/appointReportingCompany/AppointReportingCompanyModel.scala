package models.returnModels.appointReportingCompany

import play.api.libs.json.{JsPath, Json}
import models.returnModels._

case class AppointReportingCompanyModel(agentDetails: AgentDetailsModel,
                                        reportingCompany: ReportingCompanyModel,
                                        authorisingCompanies: Seq[AuthorisingCompanyModel],
                                        isReportingCompanyAppointingItself: Boolean,
                                        identityOfAppointingCompany: Option[IdentityOfCompanySubmittingModel],
                                        ultimateParentCompany: Option[UltimateParentModel],
                                        accountingPeriod: AccountingPeriodModel,
                                        declaration: Boolean)

object AppointReportingCompanyModel{

  implicit val writes = Json.writes[AppointReportingCompanyModel]
}