/*
 * Copyright 2021 HM Revenue & Customs
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

package assets.constants.fullReturn

import assets.constants.AgentDetailsConstants._
import assets.constants.GroupCompanyDetailsConstants._
import assets.constants.GroupLevelElectionsConstants._
import assets.constants.ParentCompanyConstants._
import assets.constants.ReportingCompanyConstants._
import assets.constants.fullReturn.AdjustedGroupInterestConstants._
import assets.constants.fullReturn.GroupLevelAmountConstants._
import assets.constants.fullReturn.UkCompanyConstants._
import models.returnModels._
import play.api.libs.json.{JsObject, Json}

object FullReturnConstants {

  val ackRef = "ackRef"
  val revisedReturnDetails = "some details"
  val angie: BigDecimal = 1.11
  val totalReactivations: BigDecimal = ukCompanyModelReactivationMaxIncome.allocatedReactivations.foldLeft[BigDecimal](0) {
    (total, company) => total + company.reactivation
  }

  val fullReturnJsonMax: JsObject = Json.obj(
    "agentDetails" -> agentDetailsJsonMax,
    "reportingCompany" -> reportingCompanyJson,
    "parentCompany" -> parentCompanyJsonUltUkCompany,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Revised,
    "revisedReturnDetails" -> revisedReturnDetails,
    "groupLevelElections" -> groupLevelElectionsJsonMax,
    "ukCompanies" -> Seq(ukCompanyJsonMax, ukCompanyJsonMin),
    "angie" -> angie,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "groupLevelAmount" -> groupLevelAmountJson,
    "adjustedGroupInterest" -> adjustedGroupInterestJson
  )

  val fullReturnJsonMin: JsObject = Json.obj(
    "agentDetails" -> agentDetailsJsonMin,
    "reportingCompany" -> reportingCompanyJson,
    "publicInfrastructure" -> true,
    "groupCompanyDetails" -> groupCompanyDetailsJson,
    "submissionType" -> Original,
    "ukCompanies" -> Seq(ukCompanyJsonMin),
    "groupLevelElections" -> groupLevelElectionsJsonMin,
    "returnContainsEstimates" -> true,
    "groupSubjectToInterestRestrictions" -> false,
    "groupSubjectToInterestReactivation" -> true,
    "totalReactivation" -> totalReactivations,
    "groupLevelAmount" -> groupLevelAmountJson
  )
}
