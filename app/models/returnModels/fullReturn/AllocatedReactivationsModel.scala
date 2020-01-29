package models.returnModels.fullReturn

import play.api.libs.json.Json

case class AllocatedReactivationsModel(ap1NetDisallowances: BigDecimal,
                                       currentPeriodReactivation: BigDecimal)

object AllocatedReactivationsModel {
  implicit val writes = Json.writes[AllocatedReactivationsModel]
}

