package models.returnModels

import play.api.libs.json.Json

case class NonConsolidatedInvestmentModel(nonConsolidatedInvestment: String)

object NonConsolidatedInvestmentModel {

  implicit val writes = Json.writes[NonConsolidatedInvestmentModel]
}