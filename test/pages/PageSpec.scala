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

package pages

import base.SpecBase
import pages.aboutReturn._
import pages.groupLevelInformation._
import pages.checkTotals.{ReviewNetTaxInterestPage, ReviewTaxEBITDAPage}
import pages.elections._
import pages.ultimateParentCompany._
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.aboutReturn._
import pages.ukCompanies._
import play.api.libs.json.{JsString, Json}

class PageSpec extends SpecBase {

  "Page" must {

    "Have a mapping between all possible pages (as String) to pages" in {

      val expected = Map(
    QICElectionPage.toString -> QICElectionPage,
    TellUsWhatHasChangedPage.toString -> TellUsWhatHasChangedPage,
    RestrictionAmountSameAPPage.toString -> RestrictionAmountSameAPPage,
    CompanyAccountingPeriodSameAsGroupPage.toString -> CompanyAccountingPeriodSameAsGroupPage,
        AddRestrictionPage.toString -> AddRestrictionPage,
        AgentActingOnBehalfOfCompanyPage.toString -> AgentActingOnBehalfOfCompanyPage,
        AgentNamePage.toString -> AgentNamePage,
        FullOrAbbreviatedReturnPage.toString -> FullOrAbbreviatedReturnPage,
        ReportingCompanyAppointedPage.toString -> ReportingCompanyAppointedPage,
        ReportingCompanyRequiredPage.toString -> ReportingCompanyRequiredPage,
        AccountingPeriodPage.toString -> AccountingPeriodPage,
        ReportingCompanyNamePage.toString -> ReportingCompanyNamePage,
        ReportingCompanyCTUTRPage.toString -> ReportingCompanyCTUTRPage,
        CheckAnswersReportingCompanyPage.toString -> CheckAnswersReportingCompanyPage,
        RevisingReturnPage.toString -> RevisingReturnPage,
        IndexPage.toString -> IndexPage,
        GroupInterestAllowancePage.toString -> GroupInterestAllowancePage,
        GroupInterestCapacityPage.toString -> GroupInterestCapacityPage,
        GroupSubjectToReactivationsPage.toString -> GroupSubjectToReactivationsPage,
        GroupSubjectToRestrictionsPage.toString -> GroupSubjectToRestrictionsPage,
        QICElectionPage.toString -> QICElectionPage,
        InterestAllowanceBroughtForwardPage.toString -> InterestAllowanceBroughtForwardPage,
        InterestReactivationsCapPage.toString -> InterestReactivationsCapPage,
        ReturnContainEstimatesPage.toString -> ReturnContainEstimatesPage,
        UkCompaniesPage.toString -> UkCompaniesPage,
        CheckAnswersUkCompanyPage.toString -> CheckAnswersUkCompanyPage,
        CompanyDetailsPage.toString -> CompanyDetailsPage,
        ConsentingCompanyPage.toString -> ConsentingCompanyPage,
        EnterCompanyTaxEBITDAPage.toString -> EnterCompanyTaxEBITDAPage,
        NetTaxInterestIncomeOrExpensePage.toString -> NetTaxInterestIncomeOrExpensePage,
        NetTaxInterestAmountPage.toString -> NetTaxInterestAmountPage,
        ReactivationAmountPage.toString -> ReactivationAmountPage,
        UkCompaniesDeletionConfirmationPage.toString -> UkCompaniesDeletionConfirmationPage,
        AddInvestorGroupPage.toString -> AddInvestorGroupPage,
        ElectedGroupEBITDABeforePage.toString -> ElectedGroupEBITDABeforePage,
        ElectedInterestAllowanceAlternativeCalcBeforePage.toString -> ElectedInterestAllowanceAlternativeCalcBeforePage,
        ElectedInterestAllowanceConsolidatedPshipBeforePage.toString -> ElectedInterestAllowanceConsolidatedPshipBeforePage,
        EnterQNGIEPage.toString -> EnterQNGIEPage,
        EnterANGIEPage.toString -> EnterANGIEPage,
        GroupEBITDAChargeableGainsElectionPage.toString -> GroupEBITDAChargeableGainsElectionPage,
        GroupEBITDAPage.toString -> GroupEBITDAPage,
        GroupRatioBlendedElectionPage.toString -> GroupRatioBlendedElectionPage,
        GroupRatioPercentagePage.toString -> GroupRatioPercentagePage,
        InterestAllowanceAlternativeCalcElectionPage.toString -> InterestAllowanceAlternativeCalcElectionPage,
        InterestAllowanceConsolidatedPshipElectionPage.toString -> InterestAllowanceConsolidatedPshipElectionPage,
        InterestAllowanceNonConsolidatedInvestmentsElectionPage.toString -> InterestAllowanceNonConsolidatedInvestmentsElectionPage,
        InvestmentNamePage.toString -> InvestmentNamePage,
        InvestmentsDeletionConfirmationPage.toString -> InvestmentsDeletionConfirmationPage,
        InvestmentsReviewAnswersListPage.toString -> InvestmentsReviewAnswersListPage,
        CheckAnswersElectionsPage.toString -> CheckAnswersElectionsPage,
        InvestorGroupNamePage.toString -> InvestorGroupNamePage,
        InvestorGroupsDeletionConfirmationPage.toString -> InvestorGroupsDeletionConfirmationPage,
        InvestorGroupsPage.toString -> InvestorGroupsPage,
        InvestorRatioMethodPage.toString -> InvestorRatioMethodPage,
        IsUkPartnershipPage.toString -> IsUkPartnershipPage,
        OtherInvestorGroupElectionsPage.toString -> OtherInvestorGroupElectionsPage,
        PartnershipSAUTRPage.toString -> PartnershipSAUTRPage,
        PartnershipNamePage.toString -> PartnershipNamePage,
        GroupRatioElectionPage.toString -> GroupRatioElectionPage,
        CheckAnswersGroupStructurePage.toString -> CheckAnswersGroupStructurePage,
        DeletionConfirmationPage.toString -> DeletionConfirmationPage,
        CountryOfIncorporationPage.toString -> CountryOfIncorporationPage,
        ParentCompanySAUTRPage.toString -> ParentCompanySAUTRPage,
        PayTaxInUkPage.toString -> PayTaxInUkPage,
        LimitedLiabilityPartnershipPage.toString -> LimitedLiabilityPartnershipPage,
        ParentCompanyCTUTRPage.toString -> ParentCompanyCTUTRPage,
        ParentCompanyNamePage.toString -> ParentCompanyNamePage,
        HasDeemedParentPage.toString -> HasDeemedParentPage,
        ReportingCompanySameAsParentPage.toString -> ReportingCompanySameAsParentPage,
        DeemedParentPage.toString -> DeemedParentPage,
        DerivedCompanyPage.toString -> DerivedCompanyPage,
        ReviewTaxEBITDAPage.toString -> ReviewTaxEBITDAPage,
        ReviewNetTaxInterestPage.toString -> ReviewNetTaxInterestPage,
        ReviewAndCompletePage.toString -> ReviewAndCompletePage,
        ConfirmationPage.toString -> ConfirmationPage,
        ContinueSavedReturnPage.toString -> ContinueSavedReturnPage,
        PartnershipsPage.toString -> PartnershipsPage,
        PartnershipDeletionConfirmationPage.toString -> PartnershipDeletionConfirmationPage,
        PartnershipsReviewAnswersListPage.toString -> PartnershipsReviewAnswersListPage
      )

      expected.foreach { kv => Page.pages.find(_ == kv) mustBe Some(kv) }
      Page.pages.foreach { kv => expected.find(_ == kv) mustBe Some(kv) }
    }

    "be able to construct a Page from its name" in {
      Page.pages.foreach {
        case (pageString, page) => Page(pageString) mustBe page
      }
    }

    "be able to unapply a Page to its name" in {
      Page.pages.foreach {
        case (pageString, page) => Page.unapply(page) mustBe pageString
      }
    }

    "serialise to JSON correctly" in {
      Page.pages.foreach {
        case (pageString, page) => Json.toJson(page) mustBe JsString(pageString)
      }
    }

    "deserialise from JSON correctly" in {
      Page.pages.foreach {
        case (pageString, page) => JsString(pageString).as[Page] mustBe page
      }
    }
  }
}
