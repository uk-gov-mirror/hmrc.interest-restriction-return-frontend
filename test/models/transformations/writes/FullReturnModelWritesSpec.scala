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
import models.sections.{AboutReturnSectionModel, GroupRatioJourneyModel}
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsValue, Json}

import java.time.LocalDate

class FullReturnModelWritesSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {
  "The AboutReturnSectionModel section of a Full Return" when {
    val aboutReturn : AboutReturnSectionModel = aboutReturnSectionModel.sample.value
    val ultimateParent  = ultimateParentCompanySectionModel.sample.value
    val groupRatioElection = electionsSectionModel.sample.value
    val groupLevelInfo = groupLevelInformationSectionModel.sample.value
    val fullReturn = FullReturnModel(aboutReturn,ultimateParent,groupRatioElection,groupLevelInfo)

    "Mapping to an accepted interest-restriction-returns payload" when {
      "Mapping the About Your Return section" should {
        "contain the `appointedReportingCompany`" in {
          val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

          (mappedAboutReturn \ "appointedReportingCompany").as[Boolean] mustEqual aboutReturn.appointedReportingCompany
        }

        "contain the `agentDetails`" when {
          "there is no agent" in {
            val fullReturn = models.FullReturnModel(aboutReturn.copy(agentDetails = AgentDetailsModel(false,None)),ultimateParent,groupRatioElection,groupLevelInfo)

            val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedAboutReturn \ "agentDetails" \ "agentActingOnBehalfOfCompany").as[Boolean] mustEqual false
            (mappedAboutReturn \ "agentDetails" \ "agentName").asOpt[String] mustEqual None
          }

          "there is an agent" in {
            val fullReturn = models.FullReturnModel(aboutReturn.copy(agentDetails = AgentDetailsModel(true,Some("Bob"))),ultimateParent,groupRatioElection,groupLevelInfo)

            val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedAboutReturn \ "agentDetails" \ "agentActingOnBehalfOfCompany").as[Boolean] mustEqual true
            (mappedAboutReturn \ "agentDetails" \ "agentName").asOpt[String] mustEqual Some("Bob")
          }
        }

        "contain the `submissionType`" when {
          "it is a revised return" in {
            val fullReturn = models.FullReturnModel(aboutReturn.copy(isRevisingReturn = true),ultimateParent,groupRatioElection,groupLevelInfo)

            val mappedAboutReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedAboutReturn \ "submissionType").as[String] mustEqual "revised"
          }

          "it is an original return" in {
            val fullReturn = models.FullReturnModel(aboutReturn.copy(isRevisingReturn = false),ultimateParent,groupRatioElection,groupLevelInfo)

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
          val fullReturn = models.FullReturnModel(aboutReturn = aboutReturn, ultimateParentCompany = ultimateParent.copy(reportingCompanySameAsParent = true),groupRatioElection,groupLevelInfo)

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
                parentCompanies = Seq(deemedParentSectionModel.sample.value)),groupRatioElection,groupLevelInfo)


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
                parentCompanies = Seq(deemedParentSectionModel.sample.value,deemedParentSectionModel.sample.value,deemedParentSectionModel.sample.value)),groupRatioElection,groupLevelInfo)

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
            val mappedElection: JsValue = Json.toJson(FullReturnModel(aboutReturn, ultimateParent,
              groupRatioElection.copy(groupRatioBlended = Some(groupRatioBlended)),groupLevelInfo))(FullReturnModel.writes)

            (mappedElection \ "groupLevelElections" \ "groupRatio" \ "groupRatioBlended" \ "isElected").as[Boolean] mustEqual groupRatioBlended.isElected
          }

          "When mapping the investor groups" should {
            val groupRatioBlendedWithInvestorGroups = groupRatioBlendedModel.sample.value.copy(investorGroups = Some(List(investorGroupModel.sample.value, investorGroupModel.sample.value)))

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

          "have the groupEBITDAChargeableGains when available" in {
            val mappedElection: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedElection \ "groupLevelElections" \ "groupRatio" \ "groupEBITDAChargeableGains").asOpt[Boolean] mustEqual groupRatioElection.groupEBITDAChargeableGainsIsElected
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

        "have the interestAllowanceAlternativeCalculation" in {
          val mappedElection: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

          (mappedElection \ "groupLevelElections" \ "interestAllowanceAlternativeCalculation").as[Boolean] mustEqual groupRatioElection.interestAllowanceAlternativeCalcIsElected.fold(false)(isElected => isElected)
        }

        "When mapping the interestAllowanceConsolidatedPartnership" should {
          "have whether if it is elected" in {
            val mappedElection: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

            (mappedElection \ "groupLevelElections" \ "interestAllowanceConsolidatedPartnership" \ "isElected").as[Boolean] mustEqual groupRatioElection.consolidatedPartnerships.fold(false)(partnership=>partnership.isElected)
          }

          "When mapping the consolidated partnerships" should {
            val consolidatedPartnership = Seq(partnershipModel.sample.value)
            val mappedElection: JsValue = Json.toJson(fullReturn.copy(elections = electionsSectionModel.sample.value.copy(consolidatedPartnerships = Some(consolidatedPartnershipModel.sample.value.copy(consolidatedPartnerships = Some(consolidatedPartnership))))))(FullReturnModel.writes)

            "have the name" in {
              (mappedElection \ "groupLevelElections" \ "interestAllowanceConsolidatedPartnership" \ "consolidatedPartnerships" \ 0 \ "partnershipName").as[String] mustEqual consolidatedPartnership.head.name
            }

            "have the SAUTR if available" in {
              (mappedElection \ "groupLevelElections" \ "interestAllowanceConsolidatedPartnership" \ "consolidatedPartnerships" \ 0 \ "sautr").asOpt[String] mustEqual consolidatedPartnership.head.sautr.map(c=>c.utr)
            }
          }
        }
      }

      "Mapping the Group Level Information section" should {
        val mappedReturn: JsValue = Json.toJson(fullReturn)(FullReturnModel.writes)

        "have the ANGIE" in {
          (mappedReturn \ "angie").as[BigDecimal] mustEqual groupLevelInfo.groupRatioJourney.angie
        }

        "have the groupSubjectToInterestRestrictions" in {
          (mappedReturn \ "groupSubjectToInterestRestrictions").as[Boolean] mustEqual groupLevelInfo.restrictionReactivationJourney.subjectToRestrictions
        }

        "have the groupSubjectToInterestReactivation" in {
          (mappedReturn \ "groupSubjectToInterestReactivation").as[Boolean] mustEqual groupLevelInfo.restrictionReactivationJourney.subjectToReactivations.fold(false)(c=>c)
        }

        "When mapping the groupLevelAount" should {
          "have the interest allowance brought forward" in {
            (mappedReturn \ "groupLevelAmount" \ "interestAllowanceBroughtForward").as[BigDecimal] mustEqual groupLevelInfo.interestAllowanceBroughtForward
          }

          "have the interest allowance for period" in {
            (mappedReturn \ "groupLevelAmount" \ "interestAllowanceForPeriod").as[BigDecimal] mustEqual groupLevelInfo.interestAllowanceForReturnPeriod
          }

          "have the interest capacity for period" in {
            (mappedReturn \ "groupLevelAmount" \ "interestCapacityForPeriod").as[BigDecimal] mustEqual groupLevelInfo.interestCapacityForReturnPeriod
          }

          "default interestReactivationCap to 0" when {
            "there is no reactivationCap" in {
              val restrictionJourneyModel = restrictionReactivationJourneyModel.sample.value.copy(reactivationCap = None)
              val fullReturnWithGroupLevelInformation = fullReturn.copy(groupLevelInformation =  groupLevelInformationSectionModel.sample.value.copy(restrictionReactivationJourney = restrictionJourneyModel))


              val mappedReturn : JsValue = Json.toJson(fullReturnWithGroupLevelInformation)(FullReturnModel.writes)


              (mappedReturn \ "groupLevelAmount" \ "interestReactivationCap").as[BigDecimal] mustEqual 0
            }
          }

          "map the interestReactivionCap" when {
            "we have a reactivationCap" in {
              val restrictionJourneyModel = restrictionReactivationJourneyModel.sample.value.copy(reactivationCap = Some(BigDecimal(3242313)))
              val fullReturnWithGroupLevelInformation = fullReturn.copy(groupLevelInformation =  groupLevelInformationSectionModel.sample.value.copy(restrictionReactivationJourney = restrictionJourneyModel))


              val mappedReturn : JsValue = Json.toJson(fullReturnWithGroupLevelInformation)(FullReturnModel.writes)


              (mappedReturn \ "groupLevelAmount" \ "interestReactivationCap").as[BigDecimal] mustEqual restrictionJourneyModel.reactivationCap.value
            }
          }
        }

        "When it is groupRatioElected and mapping the adjustedGroupInterest" should {
          val groupRatioJourney = GroupRatioJourneyModel(BigDecimal(5),Some(BigDecimal(325324)),Some(BigDecimal(23432324)),Some(BigDecimal(23423432)))
          val fullReturnWithGroupLevelInformation = fullReturn.copy(elections = electionsSectionModel.sample.value.copy(groupRatioIsElected = true),
            groupLevelInformation =  groupLevelInformationSectionModel.sample.value.copy(groupRatioJourney = groupRatioJourney))

          val mappedReturn : JsValue = Json.toJson(fullReturnWithGroupLevelInformation)(FullReturnModel.writes)

          "have QNGIE" in {
            (mappedReturn \ "adjustedGroupInterest" \ "qngie").asOpt[BigDecimal] mustEqual groupRatioJourney.qngie
          }

          "have groupEBITDA" in {
            (mappedReturn \ "adjustedGroupInterest" \ "groupEBITDA").asOpt[BigDecimal] mustEqual groupRatioJourney.groupEBITDA
          }

          "have groupRatio" in {
            (mappedReturn \ "adjustedGroupInterest" \ "groupRatio").asOpt[BigDecimal] mustEqual groupRatioJourney.groupRatioPercentage
          }
        }

        "When it is not groupRatioElected" should {
          "not add adjustedGroupInterest" in {
            val fullReturnNoGroupRatio = fullReturn.copy(elections = electionsSectionModel.sample.value.copy(groupRatioIsElected = false))
            val mappedReturn : JsValue = Json.toJson(fullReturnNoGroupRatio)(FullReturnModel.writes)

            (mappedReturn \ "adjustedGroupInterest").asOpt[JsValue] mustEqual None
          }
        }
      }
    }
  }
}
