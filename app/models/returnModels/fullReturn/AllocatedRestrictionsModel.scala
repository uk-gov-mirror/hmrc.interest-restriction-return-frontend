package models.returnModels.fullReturn

import java.time.LocalDate

import play.api.libs.json.Json

case class AllocatedRestrictionsModel(ap1End: Option[LocalDate],
                                      ap2End: Option[LocalDate],
                                      ap3End: Option[LocalDate],
                                      disallowanceAp1: Option[BigDecimal],
                                      disallowanceAp2: Option[BigDecimal],
                                      disallowanceAp3: Option[BigDecimal],
                                      totalDisallowances: Option[BigDecimal]
                                     )

object AllocatedRestrictionsModel {
  implicit val writes = Json.writes[AllocatedRestrictionsModel]
}
