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

package pages

import pages.aboutReportingCompany._
import pages.aboutReturn._
import pages.checkTotals.ReviewTaxEBITDAPage
import pages.elections._
import pages.groupStructure._
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.startReturn._
import pages.ukCompanies._
import play.api.libs.json.{JsPath, JsString, Reads, Writes}

import scala.language.implicitConversions

trait Page

object Page {

  implicit def toString(page: Page): String = page.toString

  val pages: Map[String, Page] = Map(
    PartnershipsPage.toString -> PartnershipsPage,
    PartnershipsReviewAnswersListPage.toString -> PartnershipsReviewAnswersListPage,
    AddAnReactivationQueryPage.toString -> AddAnReactivationQueryPage,
    ReactivationAmountPage.toString -> ReactivationAmountPage,
    AccountingPeriodStartPage.toString -> AccountingPeriodStartPage,
    AccountingPeriodEndPage.toString -> AccountingPeriodEndPage,
    ReviewAndCompletePage.toString -> ReviewAndCompletePage,
    CheckAnswersUkCompanyPage.toString -> CheckAnswersUkCompanyPage,
    UkCompaniesDeletionConfirmationPage.toString -> UkCompaniesDeletionConfirmationPage,
    ReviewTaxEBITDAPage.toString -> ReviewTaxEBITDAPage,
    UkCompaniesPage.toString -> UkCompaniesPage,
    CompanyDetailsPage.toString -> CompanyDetailsPage,
    InvestorGroupsDeletionConfirmationPage.toString -> InvestorGroupsDeletionConfirmationPage,
    InvestorGroupsPage.toString -> InvestorGroupsPage,
    InvestmentsDeletionConfirmationPage.toString -> InvestmentsDeletionConfirmationPage,
    InvestmentNamePage.toString -> InvestmentNamePage,
    ConsentingCompanyPage.toString -> ConsentingCompanyPage,
    EnterCompanyTaxEBITDAPage.toString -> EnterCompanyTaxEBITDAPage,
    DeletionConfirmationPage.toString -> DeletionConfirmationPage,
    NetTaxInterestIncomeOrExpensePage.toString -> NetTaxInterestIncomeOrExpensePage,
    NetTaxInterestAmountPage.toString -> NetTaxInterestAmountPage,
    PartnershipSAUTRPage.toString -> PartnershipSAUTRPage,
    IsUkPartnershipPage.toString -> IsUkPartnershipPage,
    PartnershipNamePage.toString -> PartnershipNamePage,
    InvestorRatioMethodPage.toString -> InvestorRatioMethodPage,
    InvestorGroupNamePage.toString -> InvestorGroupNamePage,
    AddInvestorGroupPage.toString -> AddInvestorGroupPage,
    OtherInvestorGroupElectionsPage.toString -> OtherInvestorGroupElectionsPage,
    GroupEBITDAPage.toString -> GroupEBITDAPage,
    InterestAllowanceConsolidatedPshipElectionPage.toString -> InterestAllowanceConsolidatedPshipElectionPage,
    ElectedInterestAllowanceConsolidatedPshipBeforePage.toString -> ElectedInterestAllowanceConsolidatedPshipBeforePage,
    InterestAllowanceNonConsolidatedInvestmentsElectionPage.toString -> InterestAllowanceNonConsolidatedInvestmentsElectionPage,
    GroupRatioPercentagePage.toString -> GroupRatioPercentagePage,
    InterestAllowanceAlternativeCalcElectionPage.toString -> InterestAllowanceAlternativeCalcElectionPage,
    ElectedInterestAllowanceAlternativeCalcBeforePage.toString -> ElectedInterestAllowanceAlternativeCalcBeforePage,
    GroupEBITDAChargeableGainsElectionPage.toString -> GroupEBITDAChargeableGainsElectionPage,
    ElectedGroupEBITDABeforePage.toString -> ElectedGroupEBITDABeforePage,
    GroupRatioBlendedElectionPage.toString -> GroupRatioBlendedElectionPage,
    EnterQNGIEPage.toString -> EnterQNGIEPage,
    EnterANGIEPage.toString -> EnterANGIEPage,
    GroupRatioElectionPage.toString -> GroupRatioElectionPage,
    CountryOfIncorporationPage.toString -> CountryOfIncorporationPage,
    ParentCompanySAUTRPage.toString -> ParentCompanySAUTRPage,
    PayTaxInUkPage.toString -> PayTaxInUkPage,
    LimitedLiabilityPartnershipPage.toString -> LimitedLiabilityPartnershipPage,
    ParentCompanyCTUTRPage.toString -> ParentCompanyCTUTRPage,
    ParentCompanyNamePage.toString -> ParentCompanyNamePage,
    HasDeemedParentPage.toString -> HasDeemedParentPage,
    ConfirmationPage.toString -> ConfirmationPage,
    ContinueSavedReturnPage.toString -> ContinueSavedReturnPage,
    ReportingCompanySameAsParentPage.toString -> ReportingCompanySameAsParentPage,
    ReportingCompanyNamePage.toString -> ReportingCompanyNamePage,
    ReportingCompanyCTUTRPage.toString -> ReportingCompanyCTUTRPage,
    AgentActingOnBehalfOfCompanyPage.toString -> AgentActingOnBehalfOfCompanyPage,
    AgentNamePage.toString -> AgentNamePage,
    FullOrAbbreviatedReturnPage.toString -> FullOrAbbreviatedReturnPage,
    ReportingCompanyAppointedPage.toString -> ReportingCompanyAppointedPage,
    ReportingCompanyRequiredPage.toString -> ReportingCompanyRequiredPage,
    GroupInterestAllowancePage.toString -> GroupInterestAllowancePage,
    GroupInterestCapacityPage.toString -> GroupInterestCapacityPage,
    GroupSubjectToReactivationsPage.toString -> GroupSubjectToReactivationsPage,
    GroupSubjectToRestrictionsPage.toString -> GroupSubjectToRestrictionsPage,
    InfrastructureCompanyElectionPage.toString -> InfrastructureCompanyElectionPage,
    InterestAllowanceBroughtForwardPage.toString -> InterestAllowanceBroughtForwardPage,
    InterestReactivationsCapPage.toString -> InterestReactivationsCapPage,
    RevisingReturnPage.toString -> RevisingReturnPage,
    ReturnContainEstimatesPage.toString -> ReturnContainEstimatesPage,
    IndexPage.toString -> IndexPage,
    DeemedParentPage.toString -> DeemedParentPage
  )

  val allQuestionPages = pages.values.collect{ case a: QuestionPage[_] => a}.toList

  def apply(page: String): Page = pages(page)

  def unapply(arg: Page): String = pages.map(_.swap).apply(arg)

  implicit val reads: Reads[Page] = JsPath.read[String].map(apply)
  implicit val writes: Writes[Page] = Writes { page => JsString(unapply(page)) }
}
