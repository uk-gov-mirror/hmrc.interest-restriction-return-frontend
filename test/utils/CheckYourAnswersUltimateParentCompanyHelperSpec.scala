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

package utils

import assets.constants.BaseConstants
import assets.constants.DeemedParentConstants.deemedParentModelMax
import assets.messages.{BaseMessages, CheckAnswersUltimateParentCompanyMessages}
import base.SpecBase
import controllers.ultimateParentCompany.{routes => ultimateParentCompanyRoutes}
import models.CheckMode
import pages.ultimateParentCompany._
import viewmodels.SummaryListRowHelper

class CheckYourAnswersUltimateParentCompanyHelperSpec extends SpecBase with BaseConstants with SummaryListRowHelper with CurrencyFormatter {

  val helper = new CheckYourAnswersUltimateParentCompanyHelper(
    emptyUserAnswers
      .set(ReportingCompanySameAsParentPage, false).success.value
      .set(HasDeemedParentPage, false).success.value
      .set(DeemedParentPage, deemedParentModelMax, Some(1)).success.value
  )

  "Check Your Answers Helper" must {

    "For the ReportingCompanySameAsParent answer" must {

      "have a correctly formatted summary list row" in {

        helper.reportingCompanySameAsParent mustBe Some(summaryListRow(
          CheckAnswersUltimateParentCompanyMessages.reportingCompanySameAsParent,
          BaseMessages.no,
          ultimateParentCompanyRoutes.ReportingCompanySameAsParentController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the DeemedParent answer" must {

      "have a correctly formatted summary list row" in {

        helper.deemedParent mustBe Some(summaryListRow(
          CheckAnswersUltimateParentCompanyMessages.deemedParent,
          BaseMessages.no,
          ultimateParentCompanyRoutes.HasDeemedParentController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCompanyName answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCompanyName(1) mustBe Some(summaryListRow(
          CheckAnswersUltimateParentCompanyMessages.parentCompanyName,
          companyNameModel.name,
          ultimateParentCompanyRoutes.ParentCompanyNameController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the PayTaxInUk answer" must {

      "have a correctly formatted summary list row" in {

        helper.payTaxInUk(1) mustBe Some(summaryListRow(
          CheckAnswersUltimateParentCompanyMessages.payTaxInUk,
          BaseMessages.yes,
          ultimateParentCompanyRoutes.PayTaxInUkController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the LimitedLiabilityPartnership answer" must {

      "have a correctly formatted summary list row" in {

        helper.limitedLiabilityPartnership(1) mustBe Some(summaryListRow(
          CheckAnswersUltimateParentCompanyMessages.limitedLiabilityPartnership,
          BaseMessages.yes,
          ultimateParentCompanyRoutes.LimitedLiabilityPartnershipController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCompanyCTUTR answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCompanyCTUTR(1) mustBe Some(summaryListRow(
          CheckAnswersUltimateParentCompanyMessages.parentCompanyCTUTR,
          ctutrModel.utr,
          ultimateParentCompanyRoutes.ParentCompanyCTUTRController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ParentCompanySAUTR answer" must {

      "have a correctly formatted summary list row" in {

        helper.parentCompanySAUTR(1) mustBe Some(summaryListRow(
          CheckAnswersUltimateParentCompanyMessages.parentCompanySAUTR,
          sautrModel.utr,
          ultimateParentCompanyRoutes.ParentCompanySAUTRController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the CountryOfIncorporation answer" must {

      "have a correctly formatted summary list row" in {

        helper.countryOfIncorporation(1) mustBe Some(summaryListRow(
          CheckAnswersUltimateParentCompanyMessages.registeredCountry,
          nonUkCountryCode.country,
          ultimateParentCompanyRoutes.CountryOfIncorporationController.onPageLoad(1, CheckMode) -> BaseMessages.changeLink
        ))
      }
    }
  }
}
