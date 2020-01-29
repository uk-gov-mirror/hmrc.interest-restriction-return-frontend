package models.returnModels

import play.api.libs.json.Json

case class ReportingCompanyModel(companyName: CompanyNameModel,
                                 ctutr: UTRModel,
                                 crn: CRNModel,
                                 sameAsUltimateParent: Boolean)

object ReportingCompanyModel {

  implicit val writes = Json.writes[ReportingCompanyModel]
}