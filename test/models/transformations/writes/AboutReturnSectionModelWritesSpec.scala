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

import assets.constants.AgentDetailsConstants
import generators.ModelGenerators
import models.FullOrAbbreviatedReturn
import models.FullOrAbbreviatedReturn.Full
import models.returnModels.{AgentDetailsModel, CompanyNameModel, UTRModel}
import models.sections.AboutReturnSectionModel
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsDefined, JsValue, Json}
import org.scalacheck.Arbitrary.arbitrary

class AboutReturnSectionModelWritesSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {
  "An AboutReturnSectionModel object" when {
    "Mapping to an accepted interest-restriction-returns payload" should {
      "contain the `appointedReportingCompany`" in {
        val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value

        val mappedAboutReturn: JsValue = Json.toJson(aboutReturn)(AboutReturnSectionModel.writes)

        (mappedAboutReturn \ "appointedReportingCompany").as[Boolean] mustEqual aboutReturn.appointedReportingCompany
      }

      "contain the `agentDetails`" when {
        "there is no agent" in {
          val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value.copy(agentDetails = AgentDetailsModel(false,None))

          val mappedAboutReturn: JsValue = Json.toJson(aboutReturn)(AboutReturnSectionModel.writes)

          (mappedAboutReturn \ "agentDetails" \ "agentActingOnBehalfOfCompany").as[Boolean] mustEqual aboutReturn.agentDetails.agentActingOnBehalfOfCompany
          (mappedAboutReturn \ "agentDetails" \ "agentName").asOpt[String] mustEqual None
        }

        "there is an agent" in {
          val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value.copy(agentDetails = AgentDetailsModel(true,Some("Bob")))

          val mappedAboutReturn: JsValue = Json.toJson(aboutReturn)(AboutReturnSectionModel.writes)

          (mappedAboutReturn \ "agentDetails" \ "agentActingOnBehalfOfCompany").as[Boolean] mustEqual aboutReturn.agentDetails.agentActingOnBehalfOfCompany
          (mappedAboutReturn \ "agentDetails" \ "agentName").asOpt[String] mustEqual aboutReturn.agentDetails.agentName
        }
      }

      "contain the `submissionType`" when {
        "it is a revised return" in {
          val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value.copy(isRevisingReturn = true)

          val mappedAboutReturn: JsValue = Json.toJson(aboutReturn)(AboutReturnSectionModel.writes)

          (mappedAboutReturn \ "submissionType").as[String] mustEqual "revised"
        }

        "it is an original return" in {
          val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value.copy(isRevisingReturn = false)

          val mappedAboutReturn: JsValue = Json.toJson(aboutReturn)(AboutReturnSectionModel.writes)

          (mappedAboutReturn \ "submissionType").as[String] mustEqual "original"
        }
      }

      "contain the `revisedReturnDetails`" in {
        val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value

        val mappedAboutReturn: JsValue = Json.toJson(aboutReturn)(AboutReturnSectionModel.writes)

        (mappedAboutReturn \ "revisedReturnDetails").asOpt[String] mustEqual aboutReturn.revisedReturnDetails
      }

      "contain the `reportingCompany`" in {
        val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value

        val mappedAboutReturn: JsValue = Json.toJson(aboutReturn)(AboutReturnSectionModel.writes)

        (mappedAboutReturn \ "reportingCompany" \ "companyName").as[String] mustEqual aboutReturn.companyName.name
        (mappedAboutReturn \ "reportingCompany" \ "ctutr").as[String] mustEqual aboutReturn.ctutr.utr
      }
    }
  }
}
