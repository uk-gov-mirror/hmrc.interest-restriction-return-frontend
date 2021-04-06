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

import models.Section
import pages.groupLevelInformation._
import pages.checkTotals.{ReviewNetTaxInterestPage, ReviewTaxEBITDAPage}
import pages.elections.{IsUkPartnershipPage, _}
import pages.ultimateParentCompany._
import pages.reviewAndComplete._
import pages.aboutReturn._
import pages.ukCompanies._
import play.api.libs.json.{JsPath, JsString, Reads, Writes}
import models._

import scala.language.implicitConversions

trait Page

object Page {
  
  implicit val reads: Reads[Page] = JsPath.read[String].map(apply)
  implicit val writes: Writes[Page] = Writes { page => JsString(unapply(page)) }

  implicit def toString(page: Page): String = page.toString

  lazy val aboutReturnSectionPages: Seq[Page] = List(
    AgentActingOnBehalfOfCompanyPage,
    AgentNamePage,
    FullOrAbbreviatedReturnPage,
    ReportingCompanyAppointedPage,
    ReportingCompanyRequiredPage,
    AccountingPeriodPage,
    ReportingCompanyNamePage,
    ReportingCompanyCTUTRPage,
    RevisingReturnPage,
    IndexPage,
    CheckAnswersAboutReturnPage,
    TellUsWhatHasChangedPage
  )

  lazy val groupLevelInformationSectionPages: Seq[Page] = List(
    GroupInterestAllowancePage,
    GroupInterestCapacityPage,
    GroupSubjectToReactivationsPage,
    GroupSubjectToRestrictionsPage,
    InterestAllowanceBroughtForwardPage,
    InterestReactivationsCapPage,
    ReturnContainEstimatesPage,
    EstimatedFiguresPage,
    DisallowedAmountPage,
    EnterANGIEPage,
    EnterQNGIEPage,
    GroupRatioPercentagePage,
    GroupEBITDAPage,
    CheckAnswersGroupLevelPage
  )

  lazy val ukCompaniesSectionPages: Seq[Page] = List(
    UkCompaniesPage,
    CheckAnswersUkCompanyPage,
    CompanyDetailsPage,
    ConsentingCompanyPage,
    EnterCompanyTaxEBITDAPage,
    AddNetTaxInterestPage,
    NetTaxInterestIncomeOrExpensePage,
    NetTaxInterestAmountPage,
    ReactivationAmountPage,
    UkCompaniesDeletionConfirmationPage,
    AddRestrictionPage,
    CompanyAccountingPeriodSameAsGroupPage,
    RestrictionAmountSameAPPage,
    CompanyQICElectionPage,
    CompanyEstimatedFiguresPage,
    CompanyContainsEstimatesPage
  )

  lazy val electionsSectionPages: Seq[Page] = List(
    AddInvestorGroupPage,
    ElectedGroupEBITDABeforePage,
    ElectedInterestAllowanceAlternativeCalcBeforePage,
    ElectedInterestAllowanceConsolidatedPshipBeforePage,
    GroupEBITDAChargeableGainsElectionPage,
    GroupRatioBlendedElectionPage,
    InterestAllowanceAlternativeCalcElectionPage,
    InterestAllowanceConsolidatedPshipElectionPage,
    InterestAllowanceNonConsolidatedInvestmentsElectionPage,
    InvestmentNamePage,
    InvestmentsDeletionConfirmationPage,
    InvestmentsReviewAnswersListPage,
    CheckAnswersElectionsPage,
    InvestorGroupNamePage,
    InvestorGroupsDeletionConfirmationPage,
    InvestorGroupsPage,
    InvestorRatioMethodPage,
    IsUkPartnershipPage,
    OtherInvestorGroupElectionsPage,
    PartnershipSAUTRPage,
    PartnershipNamePage,
    GroupRatioElectionPage,
    PartnershipsPage,
    PartnershipDeletionConfirmationPage,
    PartnershipsReviewAnswersListPage
  )

  lazy val ultimateParentCompanySectionPages: Seq[Page] = List(
    CheckAnswersGroupStructurePage,
    DeletionConfirmationPage,
    CountryOfIncorporationPage,
    ParentCompanySAUTRPage,
    PayTaxInUkPage,
    LimitedLiabilityPartnershipPage,
    ParentCompanyCTUTRPage,
    ParentCompanyNamePage,
    HasDeemedParentPage,
    ReportingCompanySameAsParentPage,
    DeemedParentPage
  )

  lazy val checkTotalsSectionPages: Seq[Page] = List(
    DerivedCompanyPage,
    ReviewTaxEBITDAPage,
    ReviewNetTaxInterestPage
  )

  lazy val reviewAndCompleteSectionPages: Seq[Page] = List(
    ReviewAndCompletePage,
    DeclarationPage
  )

  lazy val sections = Map(
    Section.AboutReturn -> aboutReturnSectionPages,
    Section.GroupLevelInformation -> groupLevelInformationSectionPages,
    Section.UkCompanies -> ukCompaniesSectionPages,
    Section.Elections -> electionsSectionPages,
    Section.CheckTotals -> checkTotalsSectionPages,
    Section.ReviewAndComplete -> reviewAndCompleteSectionPages,
    Section.UltimateParentCompany -> ultimateParentCompanySectionPages
  )

  
  lazy val pages: Map[String, Page] = sections.flatMap{
    section => section._2.map(page => page.toString -> page)
  } ++ Map(
    ConfirmationPage.toString -> ConfirmationPage,
    ContinueSavedReturnPage.toString -> ContinueSavedReturnPage
  )

  val allPagesFromSubjectToReactivations: Map[String, Page] = (groupLevelInformationSectionPages.filterNot(page => page == GroupSubjectToRestrictionsPage ||
    page == GroupSubjectToReactivationsPage ||
    page == DisallowedAmountPage ) ++ ukCompaniesSectionPages).map(page => page.toString -> page).toMap

  lazy val allQuestionPages: Seq[QuestionPage[_]] = pages.values.collect{ case a: QuestionPage[_] => a}.toList

  def apply(page: String): Page = pages(page)

  def unapply(arg: Page): String = pages.map(_.swap).apply(arg)

}
