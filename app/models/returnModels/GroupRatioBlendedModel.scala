package models.returnModels

import play.api.libs.json.Json

case class GroupRatioBlendedModel(isElected: Boolean,
                                  investorGroups: Option[Seq[InvestorGroupModel]])

object GroupRatioBlendedModel {

  implicit val writes = Json.writes[GroupRatioBlendedModel]
}