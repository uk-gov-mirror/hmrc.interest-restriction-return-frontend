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

import models._
import org.scalacheck.Arbitrary
import org.scalacheck.Arbitrary.arbitrary
import pages._
import pages.aboutReportingCompany.{CheckAnswersReportingCompanyPage, ReportingCompanyCRNPage, ReportingCompanyCTUTRPage, ReportingCompanyNamePage}
import pages.aboutReturn.{GroupInterestAllowancePage, GroupInterestCapacityPage, GroupSubjectToReactivationsPage, GroupSubjectToRestrictionsPage, InfrastructureCompanyElectionPage, InterestAllowanceBroughtForwardPage, InterestReactivationsCapPage, ReturnContainEstimatesPage, RevisingReturnPage}
import pages.groupStructure.{DeemedParentPage, LimitedLiabilityPartnershipPage, ParentCompanyNamePage, RegisteredCompaniesHousePage}
import pages.startReturn.{AgentActingOnBehalfOfCompanyPage, AgentNamePage, FullOrAbbreviatedReturnPage, ReportingCompanyAppointedPage, ReportingCompanyRequiredPage}
import pages.aboutReturn._
import pages.groupStructure._
import pages.startReturn._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersEntryGenerators extends PageGenerators with ModelGenerators {

  implicit lazy val arbitraryLocalRegistrationNumberUserAnswersEntry: Arbitrary[(LocalRegistrationNumberPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[LocalRegistrationNumberPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryRegisteredForTaxInAnotherCountryUserAnswersEntry: Arbitrary[(RegisteredForTaxInAnotherCountryPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RegisteredForTaxInAnotherCountryPage.type]
        value <- arbitrary[Boolean].map(Json.toJson(_))
      } yield (page, value)
    }

  implicit lazy val arbitraryParentCRNUserAnswersEntry: Arbitrary[(ParentCRNPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ParentCRNPage.type]
        value <- arbitrary[String].suchThat(_.nonEmpty).map(Json.toJson(_))
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

  implicit lazy val arbitraryRegisteredCompaniesHouseUserAnswersEntry: Arbitrary[(RegisteredCompaniesHousePage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[RegisteredCompaniesHousePage.type]
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

  implicit lazy val arbitraryDeemedParentUserAnswersEntry: Arbitrary[(DeemedParentPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[DeemedParentPage.type]
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

  implicit lazy val arbitraryCheckAnswersReportingCompanyUserAnswersEntry: Arbitrary[(CheckAnswersReportingCompanyPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[CheckAnswersReportingCompanyPage.type]
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

  implicit lazy val arbitraryReportingCompanyCRNUserAnswersEntry: Arbitrary[(ReportingCompanyCRNPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[ReportingCompanyCRNPage.type]
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

  implicit lazy val arbitraryInfrastructureCompanyElectionUserAnswersEntry: Arbitrary[(InfrastructureCompanyElectionPage.type, JsValue)] =
    Arbitrary {
      for {
        page  <- arbitrary[InfrastructureCompanyElectionPage.type]
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
