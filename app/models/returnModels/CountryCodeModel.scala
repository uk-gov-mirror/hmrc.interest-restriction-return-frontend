package models.returnModels

import play.api.libs.json.Json

case class CountryCodeModel(code: String)

object CountryCodeModel {

  implicit val writes = Json.writes[CountryCodeModel]
}