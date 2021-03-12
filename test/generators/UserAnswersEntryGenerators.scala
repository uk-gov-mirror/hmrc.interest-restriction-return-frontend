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

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import pages.aboutReturn._
import pages.groupLevelInformation._
import pages.elections._
import pages.ultimateParentCompany._
import pages.aboutReturn._
import pages.ukCompanies._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryAddRestrictionAmountUserAnswersEntry: Arbitrary[(AddRestrictionAmountPage, JsValue)] =
    Arbitrary {
      for {
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (AddRestrictionAmountPage(1, 1), value)
    }

  implicit lazy val arbitraryAddNetTaxInterestUserAnswersEntry: Arbitrary[(AddNetTaxInterestPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddNetTaxInterestPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCompanyContainsEstimatesUserAnswersEntry: Arbitrary[(CompanyContainsEstimatesPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CompanyContainsEstimatesPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCompanyEstimatedFiguresUserAnswersEntry: Arbitrary[(CompanyEstimatedFiguresPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CompanyEstimatedFiguresPage.type]
        value <- arbitrary[CompanyEstimatedFigures].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCompanyQICElectionUserAnswersEntry: Arbitrary[(CompanyQICElectionPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CompanyQICElectionPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryEstimatedFiguresUserAnswersEntry: Arbitrary[(EstimatedFiguresPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[EstimatedFiguresPage.type]
        value <- arbitrary[EstimatedFigures].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGroupEBITDAUserAnswersEntry: Arbitrary[(GroupEBITDAPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupEBITDAPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGroupRatioPercentageUserAnswersEntry: Arbitrary[(GroupRatioPercentagePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupRatioPercentagePage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDisallowedAmountUserAnswersEntry: Arbitrary[(DisallowedAmountPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DisallowedAmountPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryTellUsWhatHasChangedUserAnswersEntry: Arbitrary[(TellUsWhatHasChangedPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[TellUsWhatHasChangedPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRestrictionAmountSameAPUserAnswersEntry: Arbitrary[(RestrictionAmountSameAPPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RestrictionAmountSameAPPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCompanyAccountingPeriodSameAsGroupUserAnswersEntry: Arbitrary[(CompanyAccountingPeriodSameAsGroupPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CompanyAccountingPeriodSameAsGroupPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddRestrictionUserAnswersEntry: Arbitrary[(AddRestrictionPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddRestrictionPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnershipDeletionConfirmationUserAnswersEntry: Arbitrary[(PartnershipDeletionConfirmationPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnershipDeletionConfirmationPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnershipsReviewAnswersListUserAnswersEntry: Arbitrary[(PartnershipsReviewAnswersListPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnershipsReviewAnswersListPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddAnReactivationQueryUserAnswersEntry: Arbitrary[(AddAnReactivationQueryPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddAnReactivationQueryPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryReactivationAmountUserAnswersEntry: Arbitrary[(ReactivationAmountPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ReactivationAmountPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAccountingPeriodUserAnswersEntry: Arbitrary[(AccountingPeriodPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AccountingPeriodPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInvestorGroupsDeletionConfirmationUserAnswersEntry: Arbitrary[(InvestorGroupsDeletionConfirmationPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InvestorGroupsDeletionConfirmationPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInvestmentsDeletionConfirmationUserAnswersEntry: Arbitrary[(InvestmentsDeletionConfirmationPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InvestmentsDeletionConfirmationPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInvestmentNameUserAnswersEntry: Arbitrary[(InvestmentNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InvestmentNamePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryConsentingCompanyUserAnswersEntry: Arbitrary[(ConsentingCompanyPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ConsentingCompanyPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryEnterCompanyTaxEBITDAUserAnswersEntry: Arbitrary[(EnterCompanyTaxEBITDAPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[EnterCompanyTaxEBITDAPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDeletionConfirmationUserAnswersEntry: Arbitrary[(DeletionConfirmationPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DeletionConfirmationPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNetTaxInterestIncomeOrExpenseUserAnswersEntry: Arbitrary[(NetTaxInterestIncomeOrExpensePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[NetTaxInterestIncomeOrExpensePage.type]
        value <- arbitrary[NetTaxInterestIncomeOrExpense].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCompanyDetailsUserAnswersEntry: Arbitrary[(CompanyDetailsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CompanyDetailsPage.type]
        value <- arbitrary[CompanyDetailsModel].flatMap(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryNetTaxInterestAmountUserAnswersEntry: Arbitrary[(NetTaxInterestAmountPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[NetTaxInterestAmountPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnershipSAUTRUserAnswersEntry: Arbitrary[(PartnershipSAUTRPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnershipSAUTRPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryIsUkPartnershipUserAnswersEntry: Arbitrary[(IsUkPartnershipPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[IsUkPartnershipPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPartnershipNameUserAnswersEntry: Arbitrary[(PartnershipNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PartnershipNamePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInvestorRatioMethodUserAnswersEntry: Arbitrary[(InvestorRatioMethodPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InvestorRatioMethodPage.type]
        value <- arbitrary[InvestorRatioMethod].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInvestorGroupNameUserAnswersEntry: Arbitrary[(InvestorGroupNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InvestorGroupNamePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAddInvestorGroupUserAnswersEntry: Arbitrary[(AddInvestorGroupPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AddInvestorGroupPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryOtherInvestorGroupElectionsUserAnswersEntry: Arbitrary[(OtherInvestorGroupElectionsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[OtherInvestorGroupElectionsPage.type]
        value <- arbitrary[OtherInvestorGroupElections].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInterestAllowanceConsolidatedPshipElectionUserAnswersEntry: Arbitrary[(InterestAllowanceConsolidatedPshipElectionPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InterestAllowanceConsolidatedPshipElectionPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryElectedInterestAllowanceConsolidatedPshipBeforeUserAnswersEntry: Arbitrary[(ElectedInterestAllowanceConsolidatedPshipBeforePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ElectedInterestAllowanceConsolidatedPshipBeforePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInterestAllowanceNonConsolidatedInvestmentsElectionUserAnswersEntry: Arbitrary[(InterestAllowanceNonConsolidatedInvestmentsElectionPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InterestAllowanceNonConsolidatedInvestmentsElectionPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInterestAllowanceAlternativeCalcElectionUserAnswersEntry: Arbitrary[(InterestAllowanceAlternativeCalcElectionPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InterestAllowanceAlternativeCalcElectionPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryElectedInterestAllowanceAlternativeCalcBeforeUserAnswersEntry: Arbitrary[(ElectedInterestAllowanceAlternativeCalcBeforePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ElectedInterestAllowanceAlternativeCalcBeforePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGroupEBITDAChargeableGainsElectionUserAnswersEntry: Arbitrary[(GroupEBITDAChargeableGainsElectionPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupEBITDAChargeableGainsElectionPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryElectedGroupEBITDABeforeUserAnswersEntry: Arbitrary[(ElectedGroupEBITDABeforePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ElectedGroupEBITDABeforePage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGroupRatioBlendedElectionUserAnswersEntry: Arbitrary[(GroupRatioBlendedElectionPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupRatioBlendedElectionPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }


  implicit lazy val arbitraryEnterQNGIEUserAnswersEntry: Arbitrary[(EnterQNGIEPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[EnterQNGIEPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCountryOfIncorporationUserAnswersEntry: Arbitrary[(CountryOfIncorporationPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CountryOfIncorporationPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryEnterANGIEUserAnswersEntry: Arbitrary[(EnterANGIEPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[EnterANGIEPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGroupRatioElectionUserAnswersEntry: Arbitrary[(GroupRatioElectionPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupRatioElectionPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryParentCompanyCTUTRUserAnswersEntry: Arbitrary[(ParentCompanyCTUTRPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ParentCompanyCTUTRPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryParentCompanySAUTRUserAnswersEntry: Arbitrary[(ParentCompanySAUTRPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ParentCompanySAUTRPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryPayTaxInUkUserAnswersEntry: Arbitrary[(PayTaxInUkPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[PayTaxInUkPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryLimitedLiabilityPartnershipUserAnswersEntry: Arbitrary[(LimitedLiabilityPartnershipPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[LimitedLiabilityPartnershipPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryParentCompanyNameUserAnswersEntry: Arbitrary[(ParentCompanyNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ParentCompanyNamePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryDeemedParentUserAnswersEntry: Arbitrary[(HasDeemedParentPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[HasDeemedParentPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryContinueSavedReturnUserAnswersEntry: Arbitrary[(ContinueSavedReturnPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ContinueSavedReturnPage.type]
        value <- arbitrary[ContinueSavedReturn].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCheckAnswersAboutReturnUserAnswersEntry: Arbitrary[(CheckAnswersAboutReturnPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CheckAnswersAboutReturnPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryCheckAnswersGroupLevelUserAnswersEntry: Arbitrary[(CheckAnswersGroupLevelPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CheckAnswersGroupLevelPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryReturnContainEstimatesUserAnswersEntry: Arbitrary[(ReturnContainEstimatesPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ReturnContainEstimatesPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGroupInterestAllowanceUserAnswersEntry: Arbitrary[(GroupInterestAllowancePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupInterestAllowancePage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGroupInterestCapacityUserAnswersEntry: Arbitrary[(GroupInterestCapacityPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupInterestCapacityPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGroupSubjectToRestrictionsUserAnswersEntry: Arbitrary[(GroupSubjectToRestrictionsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupSubjectToRestrictionsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInterestReactivationsCapUserAnswersEntry: Arbitrary[(InterestReactivationsCapPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InterestReactivationsCapPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryInterestAllowanceBroughtForwardUserAnswersEntry: Arbitrary[(InterestAllowanceBroughtForwardPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InterestAllowanceBroughtForwardPage.type]
        value <- arbitrary[Int].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryReportingCompanyAppointedUserAnswersEntry: Arbitrary[(ReportingCompanyAppointedPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ReportingCompanyAppointedPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryReportingCompanyNameUserAnswersEntry: Arbitrary[(ReportingCompanyNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ReportingCompanyNamePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRevisingReturnUserAnswersEntry: Arbitrary[(RevisingReturnPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RevisingReturnPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryReportingCompanyResultUserAnswersEntry: Arbitrary[(ReportingCompanyRequiredPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ReportingCompanyRequiredPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryGroupSubjectToReactivationsUserAnswersEntry: Arbitrary[(GroupSubjectToReactivationsPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[GroupSubjectToReactivationsPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryReportingCompanyCTUTRUserAnswersEntry: Arbitrary[(ReportingCompanyCTUTRPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ReportingCompanyCTUTRPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAgentNameUserAnswersEntry: Arbitrary[(AgentNamePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AgentNamePage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryAgentActingOnBehalfOfCompanyUserAnswersEntry: Arbitrary[(AgentActingOnBehalfOfCompanyPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[AgentActingOnBehalfOfCompanyPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryFullOrAbbreviatedReturnUserAnswersEntry: Arbitrary[(FullOrAbbreviatedReturnPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[FullOrAbbreviatedReturnPage.type]
        value <- arbitrary[FullOrAbbreviatedReturn].map(Json.toJson(_))
      } yield (page, value)
    }
}
