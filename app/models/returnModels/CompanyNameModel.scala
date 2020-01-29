package models.returnModels

import play.api.libs.json.Json

case class CompanyNameModel(name: String)

object CompanyNameModel {

  implicit val writes = Json.writes[CompanyNameModel]
}