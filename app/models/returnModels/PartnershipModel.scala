package models.returnModels

import play.api.libs.json.Json

case class PartnershipModel(partnershipName: String,
                            sautr:Option[UTRModel])

object PartnershipModel {

  implicit val writes = Json.writes[PartnershipModel]
}