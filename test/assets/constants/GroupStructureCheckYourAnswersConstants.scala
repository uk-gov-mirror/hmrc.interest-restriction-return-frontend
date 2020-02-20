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

package assets.constants

import pages.groupStructure._
import views.behaviours.ViewBehaviours

trait GroupStructureCheckYourAnswersConstants extends ViewBehaviours with BaseConstants {

  val userAnswersUKCompany = emptyUserAnswers
    .set(ReportingCompanySameAsParentPage, false).get
    .set(HasDeemedParentPage, false).get
    .set(ParentCompanyNamePage, ultimateParentCompanyUK.companyName.toString).get
    .set(PayTaxInUkPage, true).get
    .set(LimitedLiabilityPartnershipPage, false).get
    .set(ParentCompanyCTUTRPage, ultimateParentCompanyUK.ctutr.toString).get
    .set(RegisteredCompaniesHousePage, true).get
    .set(ParentCRNPage, ultimateParentCompanyUK.crn.toString).get

  val userAnswersForeignRegisteredCompany = emptyUserAnswers
    .set(ReportingCompanySameAsParentPage, false).get
    .set(HasDeemedParentPage, false).get
    .set(ParentCompanyNamePage, ultimateParentCompanyForeign.companyName.toString).get
    .set(PayTaxInUkPage, false).get
    .set(RegisteredForTaxInAnotherCountryPage, true).get
    .set(CountryOfIncorporationPage, ultimateParentCompanyForeign.countryOfIncorporation.get.country).get
    .set(LocalRegistrationNumberPage, ultimateParentCompanyForeign.nonUkCrn.get).get

  val userAnswersForeignNotRegisteredCompany = emptyUserAnswers
    .set(ReportingCompanySameAsParentPage, false).get
    .set(HasDeemedParentPage, false).get
    .set(ParentCompanyNamePage, ultimateParentCompanyForeign.companyName.toString).get
    .set(PayTaxInUkPage, false).get
    .set(RegisteredForTaxInAnotherCountryPage, false).get

  val userAnswersUKLLP = emptyUserAnswers
    .set(ReportingCompanySameAsParentPage, false).get
    .set(HasDeemedParentPage, false).get
    .set(ParentCompanyNamePage, ultimateParentUKLLP.companyName.toString).get
    .set(PayTaxInUkPage, true).get
    .set(LimitedLiabilityPartnershipPage, true).get
    .set(ParentCompanySAUTRPage, ultimateParentUKLLP.sautr.toString).get
    .set(RegisteredCompaniesHousePage, true).get
    .set(ParentCRNPage, ultimateParentUKLLP.crn.toString).get

  val userAnswersUKCompanyMin = emptyUserAnswers
    .set(ReportingCompanySameAsParentPage, false).get
    .set(HasDeemedParentPage, false).get
    .set(ParentCompanyNamePage, ultimateParentCompanyUKMin.companyName.toString).get
    .set(PayTaxInUkPage, true).get
    .set(LimitedLiabilityPartnershipPage, false).get
    .set(ParentCompanyCTUTRPage, ultimateParentCompanyUKMin.ctutr.toString).get
    .set(RegisteredCompaniesHousePage, false).get
    .set(ParentCRNPage, ultimateParentCompanyUKMin.crn.toString).get

  val userAnswersUKLLPMin = emptyUserAnswers
    .set(ReportingCompanySameAsParentPage, false).get
    .set(HasDeemedParentPage, false).get
    .set(ParentCompanyNamePage, ultimateParentUKLLPMin.companyName.toString).get
    .set(PayTaxInUkPage, true).get
    .set(LimitedLiabilityPartnershipPage, true).get
    .set(ParentCompanySAUTRPage, ultimateParentUKLLPMin.sautr.toString).get
    .set(RegisteredCompaniesHousePage, false).get
    .set(ParentCRNPage, ultimateParentUKLLPMin.crn.toString).get
}
