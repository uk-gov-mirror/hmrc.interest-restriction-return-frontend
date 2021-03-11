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
import models.{FullReturnModel, OtherInvestorGroupElections}
import models.returnModels.{AgentDetailsModel, UTRModel}
import models.sections.AboutReturnSectionModel
import org.scalacheck.Gen
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsObject, JsValue, Json}

import java.time.LocalDate

class FullReturnModelWritesSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {
  "The AboutReturnSectionModel section of a Full Return" when {
    val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value
    val ultimateParent  = ultimateParentCompanySectionModel.sample.value
    val groupRatioElection = electionsSectionModel.sample.value
    val fullReturn = FullReturnModel(aboutReturn,ultimateParent,groupRatioElection)

    "Mapping to an accepted interest-restriction-returns payload" when {
      "Mapping the About Your Return section" should {
        "contain the `appointedReportingCompany`" in {
          val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

          (mappedAboutReturn \ "appointedReportingCompany").as[Boolean] mustEqual aboutReturn.appointedReportingCompany
        }

        "contain the `agentDetails`" when {
          "there is no agent" in {
            val fullReturn = models.FullReturnModel(aboutReturn.copy(agentDetails = AgentDetailsModel(false,None)),ultimateParent,groupRatioElection)

            val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedAboutReturn \ "agentDetails" \ "agentActingOnBehalfOfCompany").as[Boolean] mustEqual false
            (mappedAboutReturn \ "agentDetails" \ "agentName").asOpt[String] mustEqual None
          }

          "there is an agent" in {
            val fullReturn = models.FullReturnModel(aboutReturn.copy(agentDetails = AgentDetailsModel(true,Some("Bob"))),ultimateParent,groupRatioElection)

            val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedAboutReturn \ "agentDetails" \ "agentActingOnBehalfOfCompany").as[Boolean] mustEqual true
            (mappedAboutReturn \ "agentDetails" \ "agentName").asOpt[String] mustEqual Some("Bob")
          }
        }

        "contain the `submissionType`" when {
          "it is a revised return" in {
            val fullReturn = models.FullReturnModel(aboutReturn.copy(isRevisingReturn = true),ultimateParent,groupRatioElection)

            val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedAboutReturn \ "submissionType").as[String] mustEqual "revised"
          }

          "it is an original return" in {
            val fullReturn = models.FullReturnModel(aboutReturn.copy(isRevisingReturn = false),ultimateParent,groupRatioElection)

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

      "Mapping the Parent Company" when {
        "The Reporting Company is the same as The Parent Company" should {
          val fullReturn = models.FullReturnModel(aboutReturn = aboutReturn, ultimateParentCompany = ultimateParent.copy(reportingCompanySameAsParent = true),groupRatioElection)

          "Have a company name" in {
            val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedAboutReturn \ "parentCompany" \ "ultimateParent" \ "companyName").as[String] mustEqual fullReturn.aboutReturn.companyName.name
          }

          "Have a ctutr if one available" in {
            val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedAboutReturn \ "parentCompany" \ "ultimateParent" \ "ctutr").as[String] mustEqual fullReturn.aboutReturn.ctutr.utr
          }
        }

        "The Reporting Company is not the same as The Parent Company" when {
          "We are not deemed parent" should {
            val fullReturn = models.FullReturnModel(aboutReturn = aboutReturn,
              ultimateParentCompany = ultimateParent.copy(reportingCompanySameAsParent = false,Some(false),
                parentCompanies = Seq(deemedParentSectionModel.sample.value)),groupRatioElection)


            "Have a company name" in {
              val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

              (mappedAboutReturn \ "parentCompany" \ "ultimateParent" \ "companyName").as[String] mustEqual fullReturn.ultimateParentCompany.parentCompanies.head.companyName.name
            }

            "Have a ctutr if one available" in {
              val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

              (mappedAboutReturn \ "parentCompany" \ "ultimateParent" \ "ctutr").asOpt[UTRModel] mustEqual fullReturn.ultimateParentCompany.parentCompanies.head.ctutr
            }

            "Have a sautr if one available" in {
              val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

              (mappedAboutReturn \ "parentCompany" \ "ultimateParent" \ "sautr").asOpt[UTRModel] mustEqual fullReturn.ultimateParentCompany.parentCompanies.head.sautr
            }

            "Have a country of incorporation if one available" in {
              val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

              (mappedAboutReturn \ "parentCompany" \ "ultimateParent" \ "countryOfIncorporation").asOpt[String] mustEqual
                fullReturn.ultimateParentCompany.parentCompanies.head.countryOfIncorporation.map(c=>c.code)
            }
          }

          "We are deemed parent" should {
            val fullReturn = models.FullReturnModel(aboutReturn = aboutReturn,
              ultimateParentCompany = ultimateParent.copy(reportingCompanySameAsParent = false,Some(true),
                parentCompanies = Seq(deemedParentSectionModel.sample.value,deemedParentSectionModel.sample.value,deemedParentSectionModel.sample.value)),groupRatioElection)

            "Have a company name in all deemed parents" in {
              val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

              (mappedAboutReturn \ "parentCompany" \ "deemedParent" \0\ "companyName").as[String] mustEqual fullReturn.ultimateParentCompany.parentCompanies(0).companyName.name
              (mappedAboutReturn \ "parentCompany" \ "deemedParent" \1\ "companyName").as[String] mustEqual fullReturn.ultimateParentCompany.parentCompanies(1).companyName.name
            }

            "Have a ctutr if one available in all deemed parents" in {
              val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

              (mappedAboutReturn \ "parentCompany" \ "deemedParent" \0\ "ctutr").asOpt[UTRModel] mustEqual fullReturn.ultimateParentCompany.parentCompanies(0).ctutr
              (mappedAboutReturn \ "parentCompany" \ "deemedParent" \1\ "ctutr").asOpt[UTRModel] mustEqual fullReturn.ultimateParentCompany.parentCompanies(1).ctutr
            }

            "Have a sautr if one available in all deemed parents" in {
              val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

              (mappedAboutReturn \ "parentCompany" \ "deemedParent" \0\ "sautr").asOpt[UTRModel] mustEqual fullReturn.ultimateParentCompany.parentCompanies(0).sautr
              (mappedAboutReturn \ "parentCompany" \ "deemedParent" \1\ "sautr").asOpt[UTRModel] mustEqual fullReturn.ultimateParentCompany.parentCompanies(1).sautr
            }

            "Have a country of incorporation if one available in all deemed parents" in {
              val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

              (mappedAboutReturn \ "parentCompany" \ "deemedParent" \0\ "countryOfIncorporation").asOpt[String] mustEqual
                fullReturn.ultimateParentCompany.parentCompanies(0).countryOfIncorporation.map(c=>c.code)
              (mappedAboutReturn \ "parentCompany" \ "deemedParent" \1\ "countryOfIncorporation").asOpt[String] mustEqual
                fullReturn.ultimateParentCompany.parentCompanies(1).countryOfIncorporation.map(c=>c.code)
            }
          }
        }
      }

      "Mapping the Elections Section" should {
        "Whether if the group ratio is elected" in {
          val mappedElection: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

          (mappedElection \ "groupLevelElections" \ "groupRatio" \ "isElected").as[Boolean] mustEqual groupRatioElection.groupRatioIsElected
        }

        "When mapping the group ratio blended" should {
          "have whether if it is elected" in {
            val groupRatioBlended = groupRatioBlendedModel.sample.value
            val mappedElection: JsValue = Json.toJson(FullReturnModel(aboutReturn,ultimateParent,
              groupRatioElection.copy(groupRatioBlended = Some(groupRatioBlended))))(FullReturnModel.writes)

            (mappedElection \ "groupLevelElections" \ "groupRatio" \ "groupRatioBlended" \ "isElected").as[Boolean] mustEqual groupRatioBlended.isElected
          }

          "When mapping the investor groups" should {
            val groupRatioBlendedWithInvestorGroups = groupRatioBlendedModel.sample.value.copy(investorGroups = Some(List(investorGroupModel.sample.value,investorGroupModel.sample.value)))

            "have the investor name" in {
              val mappedElection: JsValue = Json.toJson(fullReturn.copy(elections = electionsSectionModel.sample.value.copy(groupRatioBlended = Some(groupRatioBlendedWithInvestorGroups))))(FullReturnModel.writes)

              (mappedElection \ "groupLevelElections" \ "groupRatio" \
                "groupRatioBlended" \ "investorGroups" \ 0 \ "groupName").as[String] mustEqual groupRatioBlendedWithInvestorGroups.investorGroups.value.head.investorName
            }

            "have the elections" in {
              val investorGroups = groupRatioBlendedModel.sample.value.copy(investorGroups = Some(List(investorGroupModel.sample.value.copy(otherInvestorGroupElections = Some(Set(arbitraryOtherInvestorGroupElections.arbitrary.sample.value))))))
              val mappedElection: JsValue = Json.toJson(fullReturn.copy(elections = electionsSectionModel.sample.value.copy(groupRatioBlended = Some(investorGroups))))(FullReturnModel.writes)

              (mappedElection \ "groupLevelElections" \ "groupRatio" \
                "groupRatioBlended" \ "investorGroups" \ 0 \ "elections" \ 0).as[OtherInvestorGroupElections] mustEqual investorGroups.investorGroups.value.head.otherInvestorGroupElections.value.head
            }
          }

          "When mapping the non consolidated investments" should {
            "have whether if its elected" in {
              val mappedElection: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

              (mappedElection \ "groupLevelElections" \ "interestAllowanceNonConsolidatedInvestment" \ "isElected").as[Boolean] mustEqual groupRatioElection.nonConsolidatedInvestmentsIsElected
            }

            "have the non consolidated investments" in {
              val nonConsolidatedInvestments = Seq("test1","test2")
              val mappedElection: JsValue = Json.toJson(fullReturn.copy(elections = electionsSectionModel.sample.value.copy(nonConsolidatedInvestmentNames = Some(nonConsolidatedInvestments))))(FullReturnModel.writes)


              (mappedElection \ "groupLevelElections" \ "interestAllowanceNonConsolidatedInvestment" \ "nonConsolidatedInvestments" \ 0 \ "investmentName").as[String] mustEqual nonConsolidatedInvestments.head
              (mappedElection \ "groupLevelElections" \ "interestAllowanceNonConsolidatedInvestment" \ "nonConsolidatedInvestments" \ 1 \ "investmentName").as[String] mustEqual nonConsolidatedInvestments.last
            }
          }
        }
      }
    }
  }
}
