package models.returnModels

import play.api.libs.json.Json

case class NonConsolidatedInvestmentElectionModel(isElected: Boolean,
                                                  nonConsolidatedInvestments: Option[Seq[NonConsolidatedInvestmentModel]])

object NonConsolidatedInvestmentElectionModel {

  implicit val writes = Json.writes[NonConsolidatedInvestmentElectionModel]
}