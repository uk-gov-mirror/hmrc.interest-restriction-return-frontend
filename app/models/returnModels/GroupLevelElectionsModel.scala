package models.returnModels

import play.api.libs.json.Json

case class GroupLevelElectionsModel(groupRatio: GroupRatioModel,
                                    interestAllowanceAlternativeCalculation: Boolean,
                                    interestAllowanceNonConsolidatedInvestment:NonConsolidatedInvestmentElectionModel,
                                    interestAllowanceConsolidatedPartnership: ConsolidatedPartnershipModel)

object GroupLevelElectionsModel {

  implicit val writes = Json.writes[GroupLevelElectionsModel]
}