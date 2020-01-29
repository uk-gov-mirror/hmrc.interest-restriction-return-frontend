package models.returnModels

import play.api.libs.json.Json

case class AuthorisingCompanyModel(companyName: CompanyNameModel,
                                   utr: UTRModel,
                                   consenting: Option[Boolean])

object AuthorisingCompanyModel {

  implicit val writes = Json.writes[AuthorisingCompanyModel]
}