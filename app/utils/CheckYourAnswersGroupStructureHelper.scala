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

import controllers.groupStructure.{routes => groupStructureRoutes}
import models.{CheckMode, UserAnswers}
import pages.groupStructure._
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersGroupStructureHelper(val userAnswers: UserAnswers)
                                          (implicit val messages: Messages) extends CheckYourAnswersHelper {

  def localRegistrationNumber: Option[SummaryListRow] =
    answer(LocalRegistrationNumberPage, groupStructureRoutes.LocalRegistrationNumberController.onPageLoad(CheckMode))

  def registeredForTaxInAnotherCountry: Option[SummaryListRow] =
    answer(RegisteredForTaxInAnotherCountryPage, groupStructureRoutes.RegisteredForTaxInAnotherCountryController.onPageLoad(CheckMode))

  def countryOfIncorporation: Option[SummaryListRow] =
    answer(CountryOfIncorporationPage, groupStructureRoutes.CountryOfIncorporationController.onPageLoad(CheckMode))

  def parentCRN: Option[SummaryListRow] =
    answer(ParentCRNPage, groupStructureRoutes.ParentCRNController.onPageLoad(CheckMode))

  def parentCompanyCTUTR: Option[SummaryListRow] =
    answer(ParentCompanyCTUTRPage, groupStructureRoutes.ParentCompanyCTUTRController.onPageLoad(CheckMode))

  def parentCompanySAUTR: Option[SummaryListRow] =
    answer(ParentCompanySAUTRPage, groupStructureRoutes.ParentCompanySAUTRController.onPageLoad(CheckMode))

  def payTaxInUk: Option[SummaryListRow] =
    answer(PayTaxInUkPage, groupStructureRoutes.PayTaxInUkController.onPageLoad(CheckMode))

  def reportingCompanySameAsParent: Option[SummaryListRow] =
    answer(ReportingCompanySameAsParentPage, groupStructureRoutes.ReportingCompanySameAsParentController.onPageLoad(CheckMode))

  def limitedLiabilityPartnership: Option[SummaryListRow] =
    answer(LimitedLiabilityPartnershipPage, groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(CheckMode))

  def registeredCompaniesHouse: Option[SummaryListRow] =
    answer(RegisteredCompaniesHousePage, groupStructureRoutes.RegisteredCompaniesHouseController.onPageLoad(CheckMode))

  def parentCompanyName: Option[SummaryListRow] =
    answer(ParentCompanyNamePage, groupStructureRoutes.ParentCompanyNameController.onPageLoad(CheckMode))

  def deemedParent: Option[SummaryListRow] =
    answer(DeemedParentPage, groupStructureRoutes.DeemedParentController.onPageLoad(CheckMode))

  override val rows: Seq[SummaryListRow] = Seq(
    reportingCompanySameAsParent,
    deemedParent,
    parentCompanyName,
    payTaxInUk,
    limitedLiabilityPartnership,
    parentCompanyCTUTR,
    parentCompanySAUTR,
    registeredCompaniesHouse,
    parentCRN,
    registeredForTaxInAnotherCountry,
    countryOfIncorporation,
    localRegistrationNumber
  ).flatten
}
