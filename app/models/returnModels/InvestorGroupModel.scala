package models.returnModels

import play.api.libs.json.Json

case class InvestorGroupModel(investorName: String)

object InvestorGroupModel {

  implicit val writes = Json.writes[InvestorGroupModel]
}