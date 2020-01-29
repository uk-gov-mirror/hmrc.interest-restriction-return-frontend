package models.returnModels

import play.api.libs.json.Json

case class UTRModel(utr: String)

object UTRModel {

  implicit val writes = Json.writes[UTRModel]
}