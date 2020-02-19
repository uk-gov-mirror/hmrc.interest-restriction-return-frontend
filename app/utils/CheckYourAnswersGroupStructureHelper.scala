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

  def localRegistrationNumber(id: Int): Option[SummaryListRow] =
    answer(LocalRegistrationNumberPage, groupStructureRoutes.LocalRegistrationNumberController.onPageLoad(id, CheckMode))

  def registeredForTaxInAnotherCountry(id: Int): Option[SummaryListRow] =
    answer(RegisteredForTaxInAnotherCountryPage, groupStructureRoutes.RegisteredForTaxInAnotherCountryController.onPageLoad(id, CheckMode))

  def countryOfIncorporation(id: Int): Option[SummaryListRow] =
    answer(CountryOfIncorporationPage, groupStructureRoutes.CountryOfIncorporationController.onPageLoad(id, CheckMode))

  def parentCRN(id: Int): Option[SummaryListRow] =
    answer(ParentCRNPage, groupStructureRoutes.ParentCRNController.onPageLoad(id, CheckMode))

  def parentCompanyCTUTR(id: Int): Option[SummaryListRow] =
    answer(ParentCompanyCTUTRPage, groupStructureRoutes.ParentCompanyCTUTRController.onPageLoad(id, CheckMode))

  def parentCompanySAUTR(id: Int): Option[SummaryListRow] =
    answer(ParentCompanySAUTRPage, groupStructureRoutes.ParentCompanySAUTRController.onPageLoad(id, CheckMode))

  def payTaxInUk(id: Int): Option[SummaryListRow] =
    answer(PayTaxInUkPage, groupStructureRoutes.PayTaxInUkController.onPageLoad(id, CheckMode))

  def reportingCompanySameAsParent: Option[SummaryListRow] =
    answer(ReportingCompanySameAsParentPage, groupStructureRoutes.ReportingCompanySameAsParentController.onPageLoad(CheckMode))

  def limitedLiabilityPartnership(id: Int): Option[SummaryListRow] =
    answer(LimitedLiabilityPartnershipPage, groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(id, CheckMode))

  def registeredCompaniesHouse(id: Int): Option[SummaryListRow] =
    answer(RegisteredCompaniesHousePage, groupStructureRoutes.RegisteredCompaniesHouseController.onPageLoad(id, CheckMode))

  def parentCompanyName(id: Int): Option[SummaryListRow] =
    answer(ParentCompanyNamePage, groupStructureRoutes.ParentCompanyNameController.onPageLoad(id, CheckMode))

  def deemedParent: Option[SummaryListRow] =
    answer(DeemedParentPage, groupStructureRoutes.DeemedParentController.onPageLoad(CheckMode))

  def rows(id: Int): Seq[SummaryListRow] = Seq(
    reportingCompanySameAsParent,
    deemedParent,
    parentCompanyName(id),
    payTaxInUk(id),
    limitedLiabilityPartnership(id),
    parentCompanyCTUTR(id),
    parentCompanySAUTR(id),
    registeredCompaniesHouse(id),
    parentCRN(id),
    registeredForTaxInAnotherCountry(id),
    countryOfIncorporation(id),
    localRegistrationNumber(id)
  ).flatten
}
