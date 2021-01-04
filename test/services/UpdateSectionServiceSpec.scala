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

package services

import assets.constants.BaseConstants
import assets.constants.fullReturn.UkCompanyConstants.ukCompanyModelMax
import base.SpecBase
import connectors.mocks.MockCRNValidationConnector
import models.SectionStatus.{Completed, InProgress}
import models.returnModels.{ReviewAndCompleteModel, SectionState}
import pages.aboutReturn.{CheckAnswersReportingCompanyPage, ReportingCompanyNamePage}
import pages.groupLevelInformation.InfrastructureCompanyElectionPage
import pages.checkTotals.ReviewNetTaxInterestPage
import pages.elections.{CheckAnswersElectionsPage, IsUkPartnershipPage}
import pages.ultimateParentCompany.{CheckAnswersGroupStructurePage, DeemedParentPage, HasDeemedParentPage, LimitedLiabilityPartnershipPage, ReportingCompanySameAsParentPage}
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.ukCompanies.{CheckAnswersUkCompanyPage, ConsentingCompanyPage, DerivedCompanyPage, UkCompaniesPage}


class UpdateSectionServiceSpec extends SpecBase with MockCRNValidationConnector with BaseConstants {

  object TestUpdateSectionStateService extends UpdateSectionStateService

  "UpdateSectionService.updateState()" when {

    "for the ukCompanies section" when {

      "given a page in the middle of the section" must {

        "return the section updated with InProgress" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
            .set(ConsentingCompanyPage, true).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, ConsentingCompanyPage)
          result mustBe ReviewAndCompleteModel(ukCompanies = SectionState(InProgress, Some(ConsentingCompanyPage)))
        }
      }

      "given the final page of the section" must {

        "return the section updated with Completed" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
            .set(UkCompaniesPage, ukCompanyModelMax).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, CheckAnswersUkCompanyPage)
          result mustBe ReviewAndCompleteModel(ukCompanies = SectionState(Completed, Some(CheckAnswersUkCompanyPage)))
        }
      }
    }

    "for the aboutReturn section" when {

      "given a page in the middle of the section" must {

        "return the section updated with InProgress" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
            .set(ReportingCompanyNamePage, "reporting company").get

          val result = TestUpdateSectionStateService.updateState(userAnswers, ReportingCompanyNamePage)
          result mustBe ReviewAndCompleteModel(aboutReturn = SectionState(InProgress, Some(ReportingCompanyNamePage)))
        }
      }

      "given the final page of the section" must {

        "return the section updated with Completed" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, CheckAnswersReportingCompanyPage)
          result mustBe ReviewAndCompleteModel(aboutReturn = SectionState(Completed, Some(CheckAnswersReportingCompanyPage)))
        }
      }
    }

    "for the elections section" when {

      "given a page in the middle of the section" must {

        "return the section updated with InProgress" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
            .set(IsUkPartnershipPage, true).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, IsUkPartnershipPage)
          result mustBe ReviewAndCompleteModel(elections = SectionState(InProgress, Some(IsUkPartnershipPage)))
        }
      }

      "given the final page of the section" must {

        "return the section updated with Completed" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, CheckAnswersElectionsPage)
          result mustBe ReviewAndCompleteModel(elections = SectionState(Completed, Some(CheckAnswersElectionsPage)))
        }
      }
    }

    "for the groupLevelInformation section" when {

      "given a page in the middle of the section" must {

        "return the section updated with InProgress" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
            .set(InfrastructureCompanyElectionPage, true).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, InfrastructureCompanyElectionPage)
          result mustBe ReviewAndCompleteModel(groupLevelInformation = SectionState(InProgress, Some(InfrastructureCompanyElectionPage)))
        }
      }
    }

    "for the ultimateParentCompany section" when {

      "given a page in the middle of the section" must {

        "return the section updated with InProgress" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
            .set(LimitedLiabilityPartnershipPage, true).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, LimitedLiabilityPartnershipPage)
          result mustBe ReviewAndCompleteModel(ultimateParentCompany = SectionState(InProgress, Some(LimitedLiabilityPartnershipPage)))
        }
      }

      "given the final page of the section" must {

        "return the section updated with Completed" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, CheckAnswersGroupStructurePage)
          result mustBe ReviewAndCompleteModel(ultimateParentCompany = SectionState(Completed, Some(CheckAnswersGroupStructurePage)))
        }
      }

      "given the HasDeemedParent page answered true and given DeemedParent page" must {

        "return the section updated with Completed" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
            .set(HasDeemedParentPage, true).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, DeemedParentPage)
          result mustBe ReviewAndCompleteModel(ultimateParentCompany = SectionState(Completed, Some(DeemedParentPage)))
        }
      }

      "given the ReportingCompanySameAsParent page answered true" must {

        "return the section updated with Completed" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
            .set(ReportingCompanySameAsParentPage, true).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, ReportingCompanySameAsParentPage)
          result mustBe ReviewAndCompleteModel(ultimateParentCompany = SectionState(Completed, Some(ReportingCompanySameAsParentPage)))
        }
      }
    }

    "for the checkTotals section" when {

      "given a page in the middle of the section" must {

        "return the section updated with InProgress" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, ReviewNetTaxInterestPage)
          result mustBe ReviewAndCompleteModel(checkTotals = SectionState(InProgress, Some(ReviewNetTaxInterestPage)))
        }
      }

      "given the final page of the section" must {

        "return the section updated with Completed" in {

          val userAnswers = emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get

          val result = TestUpdateSectionStateService.updateState(userAnswers, DerivedCompanyPage)
          result mustBe ReviewAndCompleteModel(checkTotals = SectionState(Completed, Some(DerivedCompanyPage)))
        }
      }
    }

  }
}
