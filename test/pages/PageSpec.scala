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

import base.SpecBase
import pages.aboutReportingCompany._
import pages.aboutReturn._
import pages.elections._
import pages.groupStructure._
import pages.startReturn._
import play.api.libs.json.{JsString, Json}

class PageSpec extends SpecBase {

  "Page" must {

    "Have a mapping between all possible pages (as String) to pages" in {
      val expected = Map(
        EnterQNGIEPage.toString -> EnterQNGIEPage,
        EnterANGIEPage.toString -> EnterANGIEPage,
        GroupRatioElectionPage.toString -> GroupRatioElectionPage,
        LocalRegistrationNumberPage.toString -> LocalRegistrationNumberPage,
        CountryOfIncorporationPage.toString -> CountryOfIncorporationPage,
        RegisteredForTaxInAnotherCountryPage.toString -> RegisteredForTaxInAnotherCountryPage,
        EnterANGIEPage.toString -> EnterANGIEPage,
        GroupRatioElectionPage.toString -> GroupRatioElectionPage,
        ParentCompanySAUTRPage.toString -> ParentCompanySAUTRPage,
        PayTaxInUkPage.toString -> PayTaxInUkPage,
        LimitedLiabilityPartnershipPage.toString -> LimitedLiabilityPartnershipPage,
        RegisteredCompaniesHousePage.toString -> RegisteredCompaniesHousePage,
        ParentCRNPage.toString -> ParentCRNPage,
        ParentCompanyCTUTRPage.toString -> ParentCompanyCTUTRPage,
        ParentCompanyNamePage.toString -> ParentCompanyNamePage,
        DeemedParentPage.toString -> DeemedParentPage,
        ConfirmationPage.toString -> ConfirmationPage,
        ContinueSavedReturnPage.toString -> ContinueSavedReturnPage,
        ReportingCompanySameAsParentPage.toString -> ReportingCompanySameAsParentPage,
        ReportingCompanyNamePage.toString -> ReportingCompanyNamePage,
        ReportingCompanyCTUTRPage.toString -> ReportingCompanyCTUTRPage,
        ReportingCompanyCRNPage.toString -> ReportingCompanyCRNPage,
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
        IndexPage.toString -> IndexPage
      )

      Page.pages mustBe expected
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
