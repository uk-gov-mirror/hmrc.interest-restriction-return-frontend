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

package generators

import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import models.InvestorRatioMethod.GroupRatioMethod
import models._
import models.returnModels._
import models.sections.{AboutReturnSectionModel, ElectionsSectionModel, GroupLevelInformationSectionModel, GroupRatioJourneyModel, RestrictionReactivationJourneyModel, UkCompaniesSectionModel, UkCompanyJourneyModel, UltimateParentCompanySectionModel}
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}

import java.time.LocalDate

trait ModelGenerators {

  implicit lazy val arbitraryCompanyEstimatedFigures: Arbitrary[CompanyEstimatedFigures] =
    Arbitrary {
      Gen.oneOf(CompanyEstimatedFigures.values.toSeq)
    }

  implicit lazy val arbitraryEstimatedFigures: Arbitrary[EstimatedFigures] =
    Arbitrary {
      Gen.oneOf(EstimatedFigures.values.toSeq)
    }

  implicit lazy val arbitraryNetTaxInterestIncomeOrExpense: Arbitrary[NetTaxInterestIncomeOrExpense] =
    Arbitrary {
      Gen.oneOf(NetTaxInterestIncomeOrExpense.values)
    }

  implicit lazy val arbitraryInvestorRatioMethod: Arbitrary[InvestorRatioMethod] =
    Arbitrary {
      Gen.oneOf(InvestorRatioMethod.values)
    }

  implicit lazy val arbitraryOtherInvestorGroupElections: Arbitrary[OtherInvestorGroupElections] =
    Arbitrary {
      Gen.oneOf(OtherInvestorGroupElections.values(GroupRatioMethod))
    }

  implicit lazy val arbitraryContinueSavedReturn: Arbitrary[ContinueSavedReturn] =
    Arbitrary {
      Gen.oneOf(ContinueSavedReturn.values)
    }

  implicit lazy val arbitraryFullOrAbbreviatedReturn: Arbitrary[FullOrAbbreviatedReturn] =
    Arbitrary {
      Gen.oneOf(FullOrAbbreviatedReturn.values)
    }

  implicit lazy val arbitraryInvestorGroups: Arbitrary[InvestorGroupModel] = Arbitrary {
    for {
      name  <- arbitrary[String]
      ratioMethod <- arbitrary[InvestorRatioMethod]
      otherElections <- arbitrary[OtherInvestorGroupElections]
    } yield InvestorGroupModel(name, Some(ratioMethod), Some(Set(otherElections)))
  }

  implicit lazy val arbitraryCompanyDetailsModel: Arbitrary[CompanyDetailsModel] =
    Arbitrary {
      for {
        name <- arbitrary[String]
        utr <- arbitrary[String]
      } yield  CompanyDetailsModel(CompanyNameModel(name), UTRModel(utr))
    }

  //implicit val localDateArb = Arbitrary(localDateGen)

  def localDateGen: Gen[LocalDate] = {
    val rangeStart = LocalDate.MIN.toEpochDay
    val currentYear = LocalDate.now().getYear
    val rangeEnd = LocalDate.of(currentYear, 1, 1).toEpochDay
    Gen.choose(rangeStart, rangeEnd).map(i => LocalDate.ofEpochDay(i))
  }
  implicit lazy val aboutReturnSectionModel: Gen[AboutReturnSectionModel] =
      for {
        hasAppointedReportingCompany <- arbitrary[Boolean]
        agentOnBehalfCompany <- arbitrary[Boolean]
        agentName <- arbitrary[Option[String]]
        fullOrAbbr <- Gen.oneOf(Full, Abbreviated)
        isRevising <- arbitrary[Boolean]
        revisedReturnDetails <- arbitrary[Option[String]]
        companyName <- arbitrary[String]
        ctUtr <- arbitrary[String]
        date <- localDateGen
      } yield AboutReturnSectionModel(hasAppointedReportingCompany, AgentDetailsModel(agentOnBehalfCompany, agentName),
        fullOrAbbr, isRevising, revisedReturnDetails, CompanyNameModel(companyName), UTRModel(ctUtr), AccountingPeriodModel(date, date))

  implicit lazy val utrModel : Gen[UTRModel] = arbitrary[String].map(utr => UTRModel(utr))

  implicit lazy val cuontryCodeModel : Gen[CountryCodeModel] =
    for{
      code <- arbitrary[String]
      country <- arbitrary[String]
    } yield CountryCodeModel(code,country)

  implicit lazy val deemedParentSectionModel : Gen[DeemedParentModel] =
    for {
      companyName <- arbitrary[String]
      ctUtr <- Gen.option(utrModel)
      saUtr <- Gen.option(utrModel)
      country <- Gen.option(cuontryCodeModel)
      liability <- arbitrary[Option[Boolean]]
      payTaxInUk <- arbitrary[Option[Boolean]]
      isSameAsParent <- arbitrary[Option[Boolean]]
    } yield DeemedParentModel(CompanyNameModel(companyName),ctUtr,saUtr,country,liability,payTaxInUk,isSameAsParent)

  implicit lazy val ultimateParentCompanySectionModel : Gen[UltimateParentCompanySectionModel] =
    for {
      isSameAsParent <- arbitrary[Boolean]
      hasDeemedParent <- arbitrary[Option[Boolean]]
      parentCompanies <- Gen.listOf(deemedParentSectionModel)
    } yield UltimateParentCompanySectionModel(isSameAsParent,hasDeemedParent,parentCompanies)


  implicit lazy val investorGroupModel : Gen[InvestorGroupModel] =
    for {
      name <- arbitrary[String]
      ratioMethod <- Gen.option(arbitraryInvestorRatioMethod.arbitrary)
      otherInvestments <- Gen.option(Gen.listOf(arbitraryOtherInvestorGroupElections.arbitrary))
    } yield InvestorGroupModel(name,ratioMethod,otherInvestments.map(c=>c.toSet))

  implicit lazy val groupRatioBlendedModel : Gen[GroupRatioBlendedModel] =
    for {
      isElected <- arbitrary[Boolean]
      blendedModel <- Gen.option(Gen.listOf(investorGroupModel))
    } yield GroupRatioBlendedModel(isElected,blendedModel)


  implicit lazy val partnershipModel : Gen[PartnershipModel] =
    for {
      name <- arbitrary[String]
      isUkPartnership <- Gen.option(Gen.oneOf(IsUKPartnershipOrPreferNotToAnswer.values))
      saUtr <- Gen.option(arbitrary[String])
    } yield PartnershipModel(name,isUkPartnership,saUtr.map(c=>UTRModel(c)))

  implicit lazy val consolidatedPartnershipModel : Gen[ConsolidatedPartnershipModel] =
    for {
      isElected <- arbitrary[Boolean]
      consolidated <- Gen.option(Gen.listOf(partnershipModel))
    } yield ConsolidatedPartnershipModel(isElected,consolidated)

  implicit lazy val electionsSectionModel : Gen[ElectionsSectionModel] =
    for {
      isGroupRatioElected <- arbitrary[Boolean]
      blended <- Gen.option(groupRatioBlendedModel)
      ebitdaActive <- arbitrary[Option[Boolean]]
      ebitdaIsElected <- arbitrary[Option[Boolean]]
      calcActive <- arbitrary[Boolean]
      calcIsElectec <- arbitrary[Option[Boolean]]
      nonConsoElec <- arbitrary[Boolean]
      nonConsolidatedInvestments <- Gen.option(Gen.listOf(arbitrary[String]))
      isPartnershipActive <- arbitrary[Boolean]
      partnership <- Gen.option(consolidatedPartnershipModel)
    } yield ElectionsSectionModel(isGroupRatioElected,blended,ebitdaActive,ebitdaIsElected,calcActive,
      calcIsElectec,nonConsoElec,nonConsolidatedInvestments,isPartnershipActive,partnership)

  implicit lazy val restrictionReactivationJourneyModel : Gen[RestrictionReactivationJourneyModel] = {
    for {
      hasRestrictions <- arbitrary[Boolean]
      totalDisallowedAmount <- Gen.option(arbitrary[BigDecimal])
      subjectToReactivations <- Gen.option(arbitrary[Boolean])
      cap <- Gen.option(arbitrary[BigDecimal])
    } yield RestrictionReactivationJourneyModel(hasRestrictions,totalDisallowedAmount,subjectToReactivations,cap)
  }

  implicit lazy val groupRatioJourneyModel : Gen[GroupRatioJourneyModel] = {
    for {
      angie <- arbitrary[BigDecimal]
      qngie <- Gen.option(arbitrary[BigDecimal])
      ebitda <- Gen.option(arbitrary[BigDecimal])
      groupRatioPercentage <- Gen.option(arbitrary[BigDecimal])
    } yield GroupRatioJourneyModel(angie,qngie,ebitda,groupRatioPercentage)
  }

  implicit lazy val groupLevelInformationSectionModel : Gen[GroupLevelInformationSectionModel] =
    for {
      restrictionReactivationJourney <- restrictionReactivationJourneyModel
      interestAllowanceBroughtForward <- arbitrary[BigDecimal]
      interestAllowanceForReturnPeriod <- arbitrary[BigDecimal]
      interestCapacityForReturnPeriod <- arbitrary[BigDecimal]
      groupRatioJourney <- groupRatioJourneyModel
      estimates <- arbitrary[Boolean]
      groupRatioElection <- arbitrary[Boolean]
    } yield GroupLevelInformationSectionModel(restrictionReactivationJourney,interestAllowanceBroughtForward,interestAllowanceForReturnPeriod,interestCapacityForReturnPeriod,groupRatioJourney,estimates,groupRatioElection)

  implicit lazy val ukCompanyJourneyModel: Gen[UkCompanyJourneyModel] =
    for {
      companyDetails <- arbitraryCompanyDetailsModel.arbitrary
      taxEbitda <- Gen.option(arbitrary[BigDecimal])
      netTaxInterestIncome <- Gen.option(arbitrary[BigDecimal])
      netTaxInterestExpense <- Gen.option(arbitrary[BigDecimal])
      reactivationAmount <- Gen.option(arbitrary[BigDecimal])
    } yield UkCompanyJourneyModel(companyDetails,taxEbitda,netTaxInterestIncome,netTaxInterestExpense,reactivationAmount)


  implicit lazy val ukCompaniesSectionModel: Gen[UkCompaniesSectionModel] =
    for {
      ukCompanies <- Gen.listOf(ukCompanyJourneyModel)
      fullOrAbbreviated <- arbitraryFullOrAbbreviatedReturn.arbitrary
      restrictions <- Gen.option(arbitrary[Boolean])
      reactivations <- Gen.option(arbitrary[Boolean])
    } yield UkCompaniesSectionModel(ukCompanies,fullOrAbbreviated,restrictions,reactivations)
}
