package models.returnModels

import play.api.libs.json.Json

case class ConsolidatedPartnershipModel(isElected: Boolean,
                                        consolidatedPartnerships: Option[Seq[PartnershipModel]]
                                       )

object ConsolidatedPartnershipModel {

  implicit val writes = Json.writes[ConsolidatedPartnershipModel]
}