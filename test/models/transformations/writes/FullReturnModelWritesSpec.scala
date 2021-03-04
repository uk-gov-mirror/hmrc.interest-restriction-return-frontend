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

package models.transformations.writes

import generators.ModelGenerators
import models.FullReturnModel
import models.returnModels.AgentDetailsModel
import models.sections.{AboutReturnSectionModel, UltimateParentCompanySectionModel}
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsValue, Json}

import java.time.LocalDate

class FullReturnModelWritesSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {
  "The AboutReturnSectionModel section of a Full Return" when {
    val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value
    val ultimateParent  = UltimateParentCompanySectionModel(true,None,Seq())
    val fullReturn = FullReturnModel(aboutReturn,ultimateParent)

    "Mapping to an accepted interest-restriction-returns payload" should {
      "contain the `appointedReportingCompany`" in {
        val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

        (mappedAboutReturn \ "appointedReportingCompany").as[Boolean] mustEqual aboutReturn.appointedReportingCompany
      }

      "contain the `agentDetails`" when {
        "there is no agent" in {
          val fullReturn = models.FullReturnModel(aboutReturn.copy(agentDetails = AgentDetailsModel(false,None)),ultimateParent)

          val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

          (mappedAboutReturn \ "agentDetails" \ "agentActingOnBehalfOfCompany").as[Boolean] mustEqual false
          (mappedAboutReturn \ "agentDetails" \ "agentName").asOpt[String] mustEqual None
        }

        "there is an agent" in {
          val fullReturn = models.FullReturnModel(aboutReturn.copy(agentDetails = AgentDetailsModel(true,Some("Bob"))),ultimateParent)

          val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

          (mappedAboutReturn \ "agentDetails" \ "agentActingOnBehalfOfCompany").as[Boolean] mustEqual true
          (mappedAboutReturn \ "agentDetails" \ "agentName").asOpt[String] mustEqual Some("Bob")
        }
      }

      "contain the `submissionType`" when {
        "it is a revised return" in {
          val fullReturn = models.FullReturnModel(aboutReturn.copy(isRevisingReturn = true),ultimateParent)

          val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

          (mappedAboutReturn \ "submissionType").as[String] mustEqual "revised"
        }

        "it is an original return" in {
          val fullReturn = models.FullReturnModel(aboutReturn.copy(isRevisingReturn = false),ultimateParent)

          val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

          (mappedAboutReturn \ "submissionType").as[String] mustEqual "original"
        }
      }

      "contain the `revisedReturnDetails`" in {
        val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

        (mappedAboutReturn \ "revisedReturnDetails").asOpt[String] mustEqual aboutReturn.revisedReturnDetails
      }

      "contain the `reportingCompany`" in {
        val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

        (mappedAboutReturn \ "reportingCompany" \ "companyName").as[String] mustEqual aboutReturn.companyName.name
        (mappedAboutReturn \ "reportingCompany" \ "ctutr").as[String] mustEqual aboutReturn.ctutr.utr
        (mappedAboutReturn \ "reportingCompany" \ "sameAsUltimateParent").as[Boolean] mustEqual fullReturn.ultimateParentCompany.reportingCompanySameAsParent
      }

      "contain the `accountingPeriod`" in {
        val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

        (mappedAboutReturn \ "groupCompanyDetails" \ "accountingPeriod" \ "startDate").as[LocalDate] mustEqual aboutReturn.periodOfAccount.startDate
        (mappedAboutReturn \ "groupCompanyDetails" \ "accountingPeriod" \ "endDate").as[LocalDate] mustEqual aboutReturn.periodOfAccount.endDate
      }
    }
  }
}
