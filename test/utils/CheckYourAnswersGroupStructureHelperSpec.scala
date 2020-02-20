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
import assets.constants.DeemedParentConstants.deemedParentModelMax
import assets.messages.{BaseMessages, CheckAnswersGroupStructureMessages}
import base.SpecBase
import controllers.groupStructure.{routes => groupStructureRoutes}
import models.CheckMode
import pages.groupStructure._
import viewmodels.SummaryListRowHelper

class CheckYourAnswersGroupStructureHelperSpec extends SpecBase with BaseConstants with SummaryListRowHelper with CurrencyFormatter {

  val helper = new CheckYourAnswersGroupStructureHelper(
    emptyUserAnswers
      .set(ReportingCompanySameAsParentPage, false).success.value
      .set(HasDeemedParentPage, false).success.value
      .set(DeemedParentPage, deemedParentModelMax, Some(1)).success.value
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

        helper.parentCompanyName(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.parentCompanyName,
          companyNameModel.name,
          groupStructureRoutes.ParentCompanyNameController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the PayTaxInUk answer" must {

      "have a correctly formatted summary list row" in {

        helper.payTaxInUk(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.payTaxInUk,
          BaseMessages.yes,
          groupStructureRoutes.PayTaxInUkController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the LimitedLiabilityPartnership answer" must {

      "have a correctly formatted summary list row" in {

        helper.limitedLiabilityPartnership(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.limitedLiabilityPartnership,
          BaseMessages.yes,
          groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCompanyCTUTR answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCompanyCTUTR(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.parentCompanyCTUTR,
          ctutrModel.utr,
          groupStructureRoutes.ParentCompanyCTUTRController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCompanySAUTR answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCompanySAUTR(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.parentCompanySAUTR,
          sautrModel.utr,
          groupStructureRoutes.ParentCompanySAUTRController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the RegisteredCompaniesHouse answer" must {

      "have a correctly formatted summary list row" in {

        helper.registeredCompaniesHouse(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse,
          BaseMessages.yes,
          groupStructureRoutes.RegisteredCompaniesHouseController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCRN answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCRN(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.parentCRN,
          crnModel.crn,
          groupStructureRoutes.ParentCRNController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the RegisteredForTaxInAnotherCountry answer" must {

      "have a correctly formatted summary list row" in {

        helper.registeredForTaxInAnotherCountry(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.registeredForTaxInAnotherCountry,
          BaseMessages.yes,
          groupStructureRoutes.RegisteredForTaxInAnotherCountryController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the CountryOfIncorporation answer" must {

      "have a correctly formatted summary list row" in {

        helper.countryOfIncorporation(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.registeredCountry,
          nonUkCountryCode.country,
          groupStructureRoutes.CountryOfIncorporationController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the LocalRegistrationNumber answer" must {

      "have a correctly formatted summary list row" in {

        helper.localRegistrationNumber(1) mustBe Some(summaryListRow(
          CheckAnswersGroupStructureMessages.localCRN,
          nonUkCrn,
          groupStructureRoutes.LocalRegistrationNumberController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }
  }
}
