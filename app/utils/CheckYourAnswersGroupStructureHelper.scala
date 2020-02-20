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

  def localRegistrationNumber(idx: Int): Option[SummaryListRow] =
    answer(LocalRegistrationNumberPage, groupStructureRoutes.LocalRegistrationNumberController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def registeredForTaxInAnotherCountry(idx: Int): Option[SummaryListRow] =
    answer(RegisteredForTaxInAnotherCountryPage, groupStructureRoutes.RegisteredForTaxInAnotherCountryController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def countryOfIncorporation(idx: Int): Option[SummaryListRow] =
    answer(CountryOfIncorporationPage, groupStructureRoutes.CountryOfIncorporationController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def parentCRN(idx: Int): Option[SummaryListRow] =
    answer(ParentCRNPage, groupStructureRoutes.ParentCRNController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def parentCompanyCTUTR(idx: Int): Option[SummaryListRow] =
    answer(ParentCompanyCTUTRPage, groupStructureRoutes.ParentCompanyCTUTRController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def parentCompanySAUTR(idx: Int): Option[SummaryListRow] =
    answer(ParentCompanySAUTRPage, groupStructureRoutes.ParentCompanySAUTRController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def payTaxInUk(idx: Int): Option[SummaryListRow] =
    answer(PayTaxInUkPage, groupStructureRoutes.PayTaxInUkController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def reportingCompanySameAsParent: Option[SummaryListRow] =
    answer(ReportingCompanySameAsParentPage, groupStructureRoutes.ReportingCompanySameAsParentController.onPageLoad(CheckMode))

  def limitedLiabilityPartnership(idx: Int): Option[SummaryListRow] =
    answer(LimitedLiabilityPartnershipPage, groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def registeredCompaniesHouse(idx: Int): Option[SummaryListRow] =
    answer(RegisteredCompaniesHousePage, groupStructureRoutes.RegisteredCompaniesHouseController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def parentCompanyName(idx: Int): Option[SummaryListRow] =
    answer(ParentCompanyNamePage, groupStructureRoutes.ParentCompanyNameController.onPageLoad(idx, CheckMode), idx = Some(idx))

  def deemedParent: Option[SummaryListRow] =
    answer(HasDeemedParentPage, groupStructureRoutes.DeemedParentController.onPageLoad(CheckMode))

  def rows(idx: Int): Seq[SummaryListRow] = Seq(
    reportingCompanySameAsParent,
    deemedParent,
    parentCompanyName(idx),
    payTaxInUk(idx),
    limitedLiabilityPartnership(idx),
    parentCompanyCTUTR(idx),
    parentCompanySAUTR(idx),
    registeredCompaniesHouse(idx),
    parentCRN(idx),
    registeredForTaxInAnotherCountry(idx),
    countryOfIncorporation(idx),
    localRegistrationNumber(idx)
  ).flatten
}
