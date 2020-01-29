package models.returnModels.fullReturn

import play.api.libs.json.Json

case class AdjustedGroupInterestModel(qngie: BigDecimal,
                                      groupEBITDA: BigDecimal,
                                      groupRatio: BigDecimal)

object AdjustedGroupInterestModel {
  implicit val writes = Json.writes[AdjustedGroupInterestModel]
}