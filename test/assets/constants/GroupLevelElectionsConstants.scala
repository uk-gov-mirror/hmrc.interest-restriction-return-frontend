/*
 * Copyright 2020 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package assets.constants

import assets.constants.GroupRatioConstants._
import assets.constants.NonConsolidatedInvestmentElectionConstants._
import assets.constants.ConsolidatedPartnershipConstants._
import models.returnModels.GroupLevelElectionsModel
import play.api.libs.json.Json


object GroupLevelElectionsConstants {

  val groupLevelElectionsJsonMax = Json.obj(
    "groupRatio" -> groupRatioJsonMax,
    "interestAllowanceAlternativeCalculation" -> true,
    "interestAllowanceNonConsolidatedInvestment" -> nonConsolidatedInvestmentJsonMax,
    "interestAllowanceConsolidatedPartnership" -> consolidatedPartnershipsJsonMax
  )

  val groupLevelElectionsModelMax = GroupLevelElectionsModel(
    groupRatio = groupRatioModelMax,
    interestAllowanceAlternativeCalculation = true,
    interestAllowanceNonConsolidatedInvestment = nonConsolidatedInvestmentModelMax,
    interestAllowanceConsolidatedPartnership = consolidatedPartnershipsModelMax
  )

  val groupLevelElectionsJsonMin = Json.obj(
    "groupRatio" -> groupRatioJsonMin,
    "interestAllowanceAlternativeCalculation" -> true,
    "interestAllowanceNonConsolidatedInvestment" -> nonConsolidatedInvestmentJsonMin,
    "interestAllowanceConsolidatedPartnership" -> consolidatedPartnershipsJsonMin
  )

  val groupLevelElectionsModelMin = GroupLevelElectionsModel(
    groupRatio = groupRatioModelMin,
    interestAllowanceAlternativeCalculation = true,
    interestAllowanceNonConsolidatedInvestment = nonConsolidatedInvestmentModelMin,
    interestAllowanceConsolidatedPartnership = consolidatedPartnershipsModelMin
  )
}