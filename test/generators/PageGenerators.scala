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
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.startReturn._
import pages.ukCompanies._

trait PageGenerators {

  implicit lazy val arbitraryRestrictionAmountSameAPPage: Arbitrary[RestrictionAmountSameAPPage.type] =
    Arbitrary(RestrictionAmountSameAPPage)

  implicit lazy val arbitraryCompanyAccountingPeriodSameAsGroupPage: Arbitrary[CompanyAccountingPeriodSameAsGroupPage.type] =
    Arbitrary(CompanyAccountingPeriodSameAsGroupPage)

  implicit lazy val arbitraryAddRestrictionPage: Arbitrary[AddRestrictionPage.type] =
    Arbitrary(AddRestrictionPage)

  implicit lazy val arbitraryPartnershipDeletionConfirmationPage: Arbitrary[PartnershipDeletionConfirmationPage.type] =
    Arbitrary(PartnershipDeletionConfirmationPage)

  implicit lazy val arbitraryPartnershipsReviewAnswersListPage: Arbitrary[PartnershipsReviewAnswersListPage.type] =
    Arbitrary(PartnershipsReviewAnswersListPage)

  implicit lazy val arbitraryAddAnReactivationQueryPage: Arbitrary[AddAnReactivationQueryPage.type] =
    Arbitrary(AddAnReactivationQueryPage)

  implicit lazy val arbitraryReactivationAmountPage: Arbitrary[ReactivationAmountPage.type] =
    Arbitrary(ReactivationAmountPage)

  implicit lazy val arbitraryAccountingPeriodStartPage: Arbitrary[AccountingPeriodStartPage.type] =
    Arbitrary(AccountingPeriodStartPage)

  implicit lazy val arbitraryAccountingPeriodEndPage: Arbitrary[AccountingPeriodEndPage.type] =
    Arbitrary(AccountingPeriodEndPage)

  implicit lazy val arbitraryReviewAndCompletePage: Arbitrary[ReviewAndCompletePage.type] =
    Arbitrary(ReviewAndCompletePage)

  implicit lazy val arbitraryCheckAnswersUkCompanyPage: Arbitrary[CheckAnswersUkCompanyPage.type] =
    Arbitrary(CheckAnswersUkCompanyPage)

  implicit lazy val arbitraryInvestorGroupsDeletionConfirmationPage: Arbitrary[InvestorGroupsDeletionConfirmationPage.type] =
    Arbitrary(InvestorGroupsDeletionConfirmationPage)

  implicit lazy val arbitraryInvestmentsDeletionConfirmationPage: Arbitrary[InvestmentsDeletionConfirmationPage.type] =
    Arbitrary(InvestmentsDeletionConfirmationPage)

  implicit lazy val arbitraryConsentingCompanyPage: Arbitrary[ConsentingCompanyPage.type] =
    Arbitrary(ConsentingCompanyPage)

  implicit lazy val arbitraryInvestmentNamePage: Arbitrary[InvestmentNamePage.type] =
    Arbitrary(InvestmentNamePage)

  implicit lazy val arbitraryEnterCompanyTaxEBITDAPage: Arbitrary[EnterCompanyTaxEBITDAPage.type] =
    Arbitrary(EnterCompanyTaxEBITDAPage)

  implicit lazy val arbitraryDeletionConfirmationPage: Arbitrary[DeletionConfirmationPage.type] =
    Arbitrary(DeletionConfirmationPage)

  implicit lazy val arbitraryNetTaxInterestIncomeOrExpensePage: Arbitrary[NetTaxInterestIncomeOrExpensePage.type] =
    Arbitrary(NetTaxInterestIncomeOrExpensePage)

  implicit lazy val arbitraryCompanyDetailsPage: Arbitrary[CompanyDetailsPage.type] =
    Arbitrary(CompanyDetailsPage)

  implicit lazy val arbitraryNetTaxInterestAmountPage: Arbitrary[NetTaxInterestAmountPage.type] =
    Arbitrary(NetTaxInterestAmountPage)

  implicit lazy val arbitraryPartnershipSAUTRPage: Arbitrary[PartnershipSAUTRPage.type] =
    Arbitrary(PartnershipSAUTRPage)

  implicit lazy val arbitraryIsUkPartnershipPage: Arbitrary[IsUkPartnershipPage.type] =
    Arbitrary(IsUkPartnershipPage)

  implicit lazy val arbitraryPartnershipNamePage: Arbitrary[PartnershipNamePage.type] =
    Arbitrary(PartnershipNamePage)

  implicit lazy val arbitraryInvestorRatioMethodPage: Arbitrary[InvestorRatioMethodPage.type] =
    Arbitrary(InvestorRatioMethodPage)

  implicit lazy val arbitraryInvestorGroupNamePage: Arbitrary[InvestorGroupNamePage.type] =
    Arbitrary(InvestorGroupNamePage)

  implicit lazy val arbitraryAddInvestorGroupPage: Arbitrary[AddInvestorGroupPage.type] =
    Arbitrary(AddInvestorGroupPage)

  implicit lazy val arbitraryOtherInvestorGroupElectionsPage: Arbitrary[OtherInvestorGroupElectionsPage.type] =
    Arbitrary(OtherInvestorGroupElectionsPage)

  implicit lazy val arbitraryGroupEBITDAPage: Arbitrary[GroupEBITDAPage.type] =
    Arbitrary(GroupEBITDAPage)

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

  implicit lazy val arbitraryCountryOfIncorporationPage: Arbitrary[CountryOfIncorporationPage.type] =
    Arbitrary(CountryOfIncorporationPage)

  implicit lazy val arbitraryEnterANGIEPage: Arbitrary[EnterANGIEPage.type] =
    Arbitrary(EnterANGIEPage)

  implicit lazy val arbitraryGroupRatioElectionPage: Arbitrary[GroupRatioElectionPage.type] =
    Arbitrary(GroupRatioElectionPage)

  implicit lazy val arbitraryParentCompanyCTUTRPage: Arbitrary[ParentCompanyCTUTRPage.type] =
    Arbitrary(ParentCompanyCTUTRPage)

  implicit lazy val arbitraryParentCompanySAUTRPage: Arbitrary[ParentCompanySAUTRPage.type] =
    Arbitrary(ParentCompanySAUTRPage)

  implicit lazy val arbitraryPayTaxInUkPage: Arbitrary[PayTaxInUkPage.type] =
    Arbitrary(PayTaxInUkPage)

  implicit lazy val arbitraryLimitedLiabilityPartnershipPage: Arbitrary[LimitedLiabilityPartnershipPage.type] =
    Arbitrary(LimitedLiabilityPartnershipPage)

  implicit lazy val arbitraryParentCompanyNamePage: Arbitrary[ParentCompanyNamePage.type] =
    Arbitrary(ParentCompanyNamePage)

  implicit lazy val arbitraryDeemedParentPage: Arbitrary[HasDeemedParentPage.type] =
    Arbitrary(HasDeemedParentPage)

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
