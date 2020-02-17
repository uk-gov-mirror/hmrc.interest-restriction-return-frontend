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

package utils

import assets.constants.BaseConstants
import assets.messages.{BaseMessages, CheckAnswersElectionsMessages, CheckAnswersGroupStructureMessages}
import base.SpecBase
import controllers.groupStructure.{routes => groupStructureRoutes}
import models.{CheckMode, UserAnswers}
import pages.elections._
import pages.groupStructure.{CountryOfIncorporationPage, DeemedParentPage, LimitedLiabilityPartnershipPage, LocalRegistrationNumberPage, ParentCRNPage, ParentCompanyCTUTRPage, ParentCompanyNamePage, ParentCompanySAUTRPage, PayTaxInUkPage, RegisteredCompaniesHousePage, RegisteredForTaxInAnotherCountryPage, ReportingCompanySameAsParentPage}
import viewmodels.SummaryListRowHelper

class CheckYourAnswersGroupStructureHelperSpec extends SpecBase with BaseConstants with SummaryListRowHelper with CurrencyFormatter {

  val helper = new CheckYourAnswersGroupStructureHelper(
    UserAnswers("id")
      .set(ReportingCompanySameAsParentPage, false).get
      .set(DeemedParentPage, false).get
      .set(ParentCompanyNamePage, companyNameModel.name).get
      .set(PayTaxInUkPage, true).get
      .set(LimitedLiabilityPartnershipPage, true).get
      .set(ParentCompanyCTUTRPage, ctutrModel.ctutr).get
      .set(ParentCompanySAUTRPage, sautrModel.ctutr).get
      .set(RegisteredCompaniesHousePage, true).get
      .set(ParentCRNPage, crnModel.crn).get
      .set(RegisteredForTaxInAnotherCountryPage, true).get
      .set(CountryOfIncorporationPage, nonUkCountryCode.country).get
      .set(LocalRegistrationNumberPage, nonUkCrn).get
  )

  "Check Your Answers Helper" must {

    "For the ReportingCompanySameAsParent answer" must {

      "have a correctly formatted summary list row" in {

        helper.reportingCompanySameAsParent mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.reportingCompanySameAsParent,
          BaseMessages.no,
          groupStructureRoutes.ReportingCompanySameAsParentController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the DeemedParent answer" must {

      "have a correctly formatted summary list row" in {

        helper.deemedParent mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.deemedParent,
          BaseMessages.no,
          groupStructureRoutes.DeemedParentController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCompanyName answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCompanyName mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.parentCompanyName,
          companyNameModel.name,
          groupStructureRoutes.ParentCompanyNameController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the PayTaxInUk answer" must {

      "have a correctly formatted summary list row" in {

        helper.payTaxInUk mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.payTaxInUk,
          BaseMessages.yes,
          groupStructureRoutes.PayTaxInUkController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the LimitedLiabilityPartnership answer" must {

      "have a correctly formatted summary list row" in {

        helper.limitedLiabilityPartnership mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.limitedLiabilityPartnership,
          BaseMessages.yes,
          groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCompanyCTUTR answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCompanyCTUTR mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.parentCompanyCTUTR,
          ctutrModel.ctutr,
          groupStructureRoutes.ParentCompanyCTUTRController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCompanySAUTR answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCompanySAUTR mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.parentCompanySAUTR,
          sautrModel.ctutr,
          groupStructureRoutes.ParentCompanySAUTRController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the RegisteredCompaniesHouse answer" must {

      "have a correctly formatted summary list row" in {

        helper.registeredCompaniesHouse mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse,
          BaseMessages.yes,
          groupStructureRoutes.RegisteredCompaniesHouseController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCRN answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCRN mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.parentCRN,
          crnModel.crn,
          groupStructureRoutes.ParentCRNController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the RegisteredForTaxInAnotherCountry answer" must {

      "have a correctly formatted summary list row" in {

        helper.registeredForTaxInAnotherCountry mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.registeredForTaxInAnotherCountry,
          BaseMessages.yes,
          groupStructureRoutes.RegisteredForTaxInAnotherCountryController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the CountryOfIncorporation answer" must {

      "have a correctly formatted summary list row" in {

        helper.countryOfIncorporation mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.registeredCountry,
          nonUkCountryCode.country,
          groupStructureRoutes.CountryOfIncorporationController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the LocalRegistrationNumber answer" must {

      "have a correctly formatted summary list row" in {

        helper.localRegistrationNumber mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.localCRN,
          nonUkCrn,
          groupStructureRoutes.LocalRegistrationNumberController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }
  }
}
