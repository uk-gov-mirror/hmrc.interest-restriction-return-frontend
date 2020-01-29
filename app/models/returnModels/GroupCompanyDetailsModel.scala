package models.returnModels

import play.api.libs.json.Json

case class GroupCompanyDetailsModel(totalCompanies: Int,
                                    accountingPeriod: AccountingPeriodModel)

object GroupCompanyDetailsModel {

  implicit val writes = Json.writes[GroupCompanyDetailsModel]
}