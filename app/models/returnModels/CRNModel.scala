package models.returnModels

import play.api.libs.json.Json

case class CRNModel(crn: String)

object CRNModel {

  implicit val writes = Json.writes[CRNModel]
}