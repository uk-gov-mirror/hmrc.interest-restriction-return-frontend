package models.returnModels

import play.api.libs.json.Json

case class GroupRatioModel(isElected: Boolean,
                           groupEBITDAChargeableGains: Option[Boolean],
                           groupRatioBlended: Option[GroupRatioBlendedModel])

object GroupRatioModel {

  implicit val writes = Json.writes[GroupRatioModel]
}