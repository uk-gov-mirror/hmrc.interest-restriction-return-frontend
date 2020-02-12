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

package generators

import org.scalacheck.Arbitrary
import pages._
import pages.aboutReportingCompany._
import pages.aboutReturn._
import pages.elections._
import pages.groupStructure._
import pages.startReturn._

trait PageGenerators {

  implicit lazy val arbitraryInterestAllowanceConsolidatedPshipElectionPage: Arbitrary[InterestAllowanceConsolidatedPshipElectionPage.type] =
    Arbitrary(InterestAllowanceConsolidatedPshipElectionPage)

  implicit lazy val arbitraryElectedInterestAllowanceConsolidatedPshipBeforePage: Arbitrary[ElectedInterestAllowanceConsolidatedPshipBeforePage.type] =
    Arbitrary(ElectedInterestAllowanceConsolidatedPshipBeforePage)

  implicit lazy val arbitraryInterestAllowanceNonConsolidatedInvestmentsElectionPage: Arbitrary[InterestAllowanceNonConsolidatedInvestmentsElectionPage.type] =
    Arbitrary(InterestAllowanceNonConsolidatedInvestmentsElectionPage)

  implicit lazy val arbitraryGroupRatioPercentagePage: Arbitrary[GroupRatioPercentagePage.type] =
    Arbitrary(GroupRatioPercentagePage)

  implicit lazy val arbitraryInterestAllowanceAlternativeCalcElectionPage: Arbitrary[InterestAllowanceAlternativeCalcElectionPage.type] =
    Arbitrary(InterestAllowanceAlternativeCalcElectionPage)

  implicit lazy val arbitraryElectedInterestAllowanceAlternativeCalcBeforePage: Arbitrary[ElectedInterestAllowanceAlternativeCalcBeforePage.type] =
    Arbitrary(ElectedInterestAllowanceAlternativeCalcBeforePage)

  implicit lazy val arbitraryGroupEBITDAChargeableGainsElectionPage: Arbitrary[GroupEBITDAChargeableGainsElectionPage.type] =
    Arbitrary(GroupEBITDAChargeableGainsElectionPage)

  implicit lazy val arbitraryElectedGroupEBITDABeforePage: Arbitrary[ElectedGroupEBITDABeforePage.type] =
    Arbitrary(ElectedGroupEBITDABeforePage)

  implicit lazy val arbitraryGroupRatioBlendedElectionPage: Arbitrary[GroupRatioBlendedElectionPage.type] =
    Arbitrary(GroupRatioBlendedElectionPage)

  implicit lazy val arbitraryEnterQNGIEPage: Arbitrary[EnterQNGIEPage.type] =
    Arbitrary(EnterQNGIEPage)

  implicit lazy val arbitraryLocalRegistrationNumberPage: Arbitrary[LocalRegistrationNumberPage.type] =
    Arbitrary(LocalRegistrationNumberPage)

  implicit lazy val arbitraryCountryOfIncorporationPage: Arbitrary[CountryOfIncorporationPage.type] =
    Arbitrary(CountryOfIncorporationPage)

  implicit lazy val arbitraryEnterANGIEPage: Arbitrary[EnterANGIEPage.type] =
    Arbitrary(EnterANGIEPage)

  implicit lazy val arbitraryGroupRatioElectionPage: Arbitrary[GroupRatioElectionPage.type] =
    Arbitrary(GroupRatioElectionPage)

  implicit lazy val arbitraryRegisteredForTaxInAnotherCountryPage: Arbitrary[RegisteredForTaxInAnotherCountryPage.type] =
    Arbitrary(RegisteredForTaxInAnotherCountryPage)

  implicit lazy val arbitraryParentCRNPage: Arbitrary[ParentCRNPage.type] =
    Arbitrary(ParentCRNPage)

  implicit lazy val arbitraryParentCompanyCTUTRPage: Arbitrary[ParentCompanyCTUTRPage.type] =
    Arbitrary(ParentCompanyCTUTRPage)

  implicit lazy val arbitraryParentCompanySAUTRPage: Arbitrary[ParentCompanySAUTRPage.type] =
    Arbitrary(ParentCompanySAUTRPage)

  implicit lazy val arbitraryPayTaxInUkPage: Arbitrary[PayTaxInUkPage.type] =
    Arbitrary(PayTaxInUkPage)

  implicit lazy val arbitraryLimitedLiabilityPartnershipPage: Arbitrary[LimitedLiabilityPartnershipPage.type] =
    Arbitrary(LimitedLiabilityPartnershipPage)

  implicit lazy val arbitraryRegisteredCompaniesHousePage: Arbitrary[RegisteredCompaniesHousePage.type] =
    Arbitrary(RegisteredCompaniesHousePage)

  implicit lazy val arbitraryParentCompanyNamePage: Arbitrary[ParentCompanyNamePage.type] =
    Arbitrary(ParentCompanyNamePage)

  implicit lazy val arbitraryDeemedParentPage: Arbitrary[DeemedParentPage.type] =
    Arbitrary(DeemedParentPage)

  implicit lazy val arbitraryContinueSavedReturnPage: Arbitrary[ContinueSavedReturnPage.type] =
    Arbitrary(ContinueSavedReturnPage)

  implicit lazy val arbitraryCheckAnswersReportingCompanyPage: Arbitrary[CheckAnswersReportingCompanyPage.type] =
    Arbitrary(CheckAnswersReportingCompanyPage)

  implicit lazy val arbitraryReturnContainEstimatesPage: Arbitrary[ReturnContainEstimatesPage.type] =
    Arbitrary(ReturnContainEstimatesPage)

  implicit lazy val arbitraryGroupInterestAllowancePage: Arbitrary[GroupInterestAllowancePage.type] =
    Arbitrary(GroupInterestAllowancePage)

  implicit lazy val arbitraryGroupInterestCapacityPage: Arbitrary[GroupInterestCapacityPage.type] =
    Arbitrary(GroupInterestCapacityPage)

  implicit lazy val arbitraryGroupSubjectToRestrictionsPage: Arbitrary[GroupSubjectToRestrictionsPage.type] =
    Arbitrary(GroupSubjectToRestrictionsPage)

  implicit lazy val arbitraryReportingCompanyAppointedPage: Arbitrary[ReportingCompanyAppointedPage.type] =
    Arbitrary(ReportingCompanyAppointedPage)

  implicit lazy val arbitraryReportingCompanyNamePage: Arbitrary[ReportingCompanyNamePage.type] =
    Arbitrary(ReportingCompanyNamePage)

  implicit lazy val arbitraryReportingCompanyResultPage: Arbitrary[ReportingCompanyRequiredPage.type] =
    Arbitrary(ReportingCompanyRequiredPage)

  implicit lazy val arbitraryReportingCompanyCTUTRPage: Arbitrary[ReportingCompanyCTUTRPage.type] =
    Arbitrary(ReportingCompanyCTUTRPage)

  implicit lazy val arbitraryReportingCompanyCRNPage: Arbitrary[ReportingCompanyCRNPage.type] =
    Arbitrary(ReportingCompanyCRNPage)

  implicit lazy val arbitraryInterestReactivationsCapPage: Arbitrary[InterestReactivationsCapPage.type] =
    Arbitrary(InterestReactivationsCapPage)

  implicit lazy val arbitraryInterestAllowanceBroughtForwardPage: Arbitrary[InterestAllowanceBroughtForwardPage.type] =
    Arbitrary(InterestAllowanceBroughtForwardPage)

  implicit lazy val arbitraryGroupSubjectToReactivationsPage: Arbitrary[GroupSubjectToReactivationsPage.type] =
    Arbitrary(GroupSubjectToReactivationsPage)

  implicit lazy val arbitraryFullOrAbbreviatedReturnPage: Arbitrary[FullOrAbbreviatedReturnPage.type] =
    Arbitrary(FullOrAbbreviatedReturnPage)

  implicit lazy val arbitraryRevisingReturnPage: Arbitrary[RevisingReturnPage.type] =
    Arbitrary(RevisingReturnPage)

  implicit lazy val arbitraryAgentNamePage: Arbitrary[AgentNamePage.type] =
    Arbitrary(AgentNamePage)

  implicit lazy val arbitraryAgentActingOnBehalfOfCompanyPage: Arbitrary[AgentActingOnBehalfOfCompanyPage.type] =
    Arbitrary(AgentActingOnBehalfOfCompanyPage)

  implicit lazy val arbitraryInfrastructureCompanyElectionPage: Arbitrary[InfrastructureCompanyElectionPage.type] =
    Arbitrary(InfrastructureCompanyElectionPage)
}
