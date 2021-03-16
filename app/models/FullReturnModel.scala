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

package models

import models.returnModels.{AgentDetailsModel, DeemedParentModel}
import models.sections._
import play.api.libs.functional.syntax._
import play.api.libs.json.{JsObject, JsPath, Json, Writes}

case class FullReturnModel(aboutReturn: AboutReturnSectionModel,
                           ultimateParentCompany: UltimateParentCompanySectionModel,
                           elections: ElectionsSectionModel,
                           groupLevelInformation: GroupLevelInformationSectionModel,
                           ukCompanies: UkCompaniesSectionModel)

object FullReturnModel {
  val revisedReturn = "revised"
  val originalReturn = "original"

  val writes: Writes[FullReturnModel] = (
    (JsPath \ "appointedReportingCompany").write[Boolean] and
      (JsPath \ "agentDetails").write[AgentDetailsModel] and
      (JsPath \ "submissionType").write[String] and
      (JsPath \ "revisedReturnDetails").writeNullable[String] and
      (JsPath \ "reportingCompany").write[JsObject] and
      (JsPath \ "groupCompanyDetails" \ "accountingPeriod").write[JsObject] and
      (JsPath \ "parentCompany").write[JsObject] and
      (JsPath \ "groupLevelElections").write[JsObject] and
      (JsPath \ "angie").write[BigDecimal] and
      (JsPath \ "groupLevelAmount").write[JsObject] and
      (JsPath \ "adjustedGroupInterest").writeNullable[JsObject] and
      (JsPath \ "groupSubjectToInterestRestrictions").write[Boolean] and
      (JsPath \ "groupSubjectToInterestReactivation").write[Boolean]
    ) (
    fullReturn => (
      fullReturn.aboutReturn.appointedReportingCompany,
      fullReturn.aboutReturn.agentDetails,
      if (fullReturn.aboutReturn.isRevisingReturn) revisedReturn else originalReturn,
      fullReturn.aboutReturn.revisedReturnDetails,
      Json.obj(
        "companyName" -> fullReturn.aboutReturn.companyName,
        "ctutr" -> fullReturn.aboutReturn.ctutr,
        "sameAsUltimateParent" -> fullReturn.ultimateParentCompany.reportingCompanySameAsParent
      ),
      Json.obj(
        "startDate" -> fullReturn.aboutReturn.periodOfAccount.startDate,
        "endDate" -> fullReturn.aboutReturn.periodOfAccount.endDate
      ),
      toParentCompany(fullReturn),
      toGroupLevelElections(fullReturn),
      fullReturn.groupLevelInformation.groupRatioJourney.angie,
      toGroupLevelAmount(fullReturn),
      toAdjustedGroupInterest(fullReturn),
      fullReturn.groupLevelInformation.restrictionReactivationJourney.subjectToRestrictions,
      fullReturn.groupLevelInformation.restrictionReactivationJourney.subjectToReactivations.fold(false)(isSubjectToReactivations => isSubjectToReactivations)
    )
  )

  private def toAdjustedGroupInterest(fullReturn: FullReturnModel) = {
    if (fullReturn.elections.groupRatioIsElected) {
      Some(Json.obj(
        "qngie" -> fullReturn.groupLevelInformation.groupRatioJourney.qngie,
        "groupEBITDA" -> fullReturn.groupLevelInformation.groupRatioJourney.groupEBITDA,
        "groupRatio" -> fullReturn.groupLevelInformation.groupRatioJourney.groupRatioPercentage
      ))
    } else {
      None
    }
  }

  private def toGroupLevelAmount(fullReturn: FullReturnModel) = {
    Json.obj(
      "interestAllowanceBroughtForward" -> fullReturn.groupLevelInformation.interestAllowanceBroughtForward,
      "interestAllowanceForPeriod" -> fullReturn.groupLevelInformation.interestAllowanceForReturnPeriod,
      "interestCapacityForPeriod" -> fullReturn.groupLevelInformation.interestCapacityForReturnPeriod,
      "interestReactivationCap" -> fullReturn.groupLevelInformation.restrictionReactivationJourney.reactivationCap.fold(BigDecimal(0))(amount => amount)
    )
  }

  private def toGroupLevelElections(fullReturn: FullReturnModel) = {
    Json.obj(
      "groupRatio" -> Json.obj(
        "isElected" -> fullReturn.elections.groupRatioIsElected,
        "groupRatioBlended" -> Json.obj(
          "isElected" -> fullReturn.elections.groupRatioBlended.map(groupRatio => groupRatio.isElected),
          "investorGroups" -> fullReturn.elections.groupRatioBlended.flatMap(groupRatio => groupRatio.investorGroups.map(investors => {
            investors.map(investor => Json.obj(
              "groupName" -> investor.investorName,
              "elections" -> investor.otherInvestorGroupElections.map(elections => elections.map(election => election))))}))),
          "groupEBITDAChargeableGains" -> fullReturn.elections.groupEBITDAChargeableGainsIsElected),
      "interestAllowanceNonConsolidatedInvestment" -> Json.obj(
          "isElected" -> fullReturn.elections.nonConsolidatedInvestmentsIsElected,
          "nonConsolidatedInvestments" -> fullReturn.elections.nonConsolidatedInvestmentNames.map(investments=>investments.map(investment =>
                Json.obj("investmentName" -> investment)))),
      "interestAllowanceAlternativeCalculation" -> fullReturn.elections.interestAllowanceAlternativeCalcIsElected.fold(false)(isElected => isElected),
      "interestAllowanceConsolidatedPartnership" -> Json.obj(
          "isElected" -> fullReturn.elections.consolidatedPartnerships.fold(false)(partnership=>partnership.isElected),
          "consolidatedPartnerships" -> fullReturn.elections.consolidatedPartnerships.flatMap(consolidatedPartnership => consolidatedPartnership.consolidatedPartnerships.map(partnerhips=>partnerhips.map(partnership=>{
            Json.obj("partnershipName" -> partnership.name,
                     "sautr" -> partnership.sautr.map(sautr=>sautr.utr))}))))
    )
  }

  private def toParentCompany(fullReturn: FullReturnModel) = {
    if (fullReturn.ultimateParentCompany.reportingCompanySameAsParent) {
      Json.obj("ultimateParent" -> Json.obj("companyName" -> fullReturn.aboutReturn.companyName,
        "ctutr" -> fullReturn.aboutReturn.ctutr))
    } else {
      fullReturn.ultimateParentCompany.hasDeemedParent match {
        case Some(true) => Json.obj("deemedParent" -> fullReturn.ultimateParentCompany.parentCompanies.map(parent => {
          companyDetails(parent)}))
        case _ => Json.obj("ultimateParent" -> companyDetails(fullReturn.ultimateParentCompany.parentCompanies.head))
      }
    }
  }

  private def companyDetails(parent: DeemedParentModel) : JsObject = {
    Json.obj("companyName" -> parent.companyName.name,
      "ctutr" -> parent.ctutr,
      "sautr" -> parent.sautr,
      "countryOfIncorporation" -> parent.countryOfIncorporation.map(c => c.code))
  }
}