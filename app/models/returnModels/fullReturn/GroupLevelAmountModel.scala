package models.returnModels.fullReturn

import play.api.libs.json.Json

case class GroupLevelAmountModel(interestReactivationCap: Option[BigDecimal],
                                 interestAllowanceForward: BigDecimal,
                                 interestAllowanceForPeriod: BigDecimal,
                                 interestCapacityForPeriod: BigDecimal)

object GroupLevelAmountModel {

  implicit val writes = Json.writes[GroupLevelAmountModel]

}