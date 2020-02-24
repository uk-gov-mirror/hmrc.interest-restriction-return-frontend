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

package navigation

import assets.constants.DeemedParentConstants._
import base.SpecBase
import controllers.groupStructure.{routes => groupStructureRoutes}
import models._
import pages.groupStructure.{CheckAnswersGroupStructurePage, CountryOfIncorporationPage, DeemedParentPage, HasDeemedParentPage, LimitedLiabilityPartnershipPage, LocalRegistrationNumberPage, ParentCRNPage, ParentCompanyCTUTRPage, ParentCompanyNamePage, ParentCompanySAUTRPage, PayTaxInUkPage, RegisteredCompaniesHousePage, RegisteredForTaxInAnotherCountryPage, ReportingCompanySameAsParentPage}

class GroupStructureNavigatorSpec extends SpecBase {

  val navigator = new GroupStructureNavigator

  "GroupStructureNavigator" when {

    "for each possible 1 value (when handling multiple deemed parents)" must {

      "in Normal mode" must {

        "from the ReportingCompanySameAsParentPage" should {

          "go to the DeemedParentPage when given false" in {

            val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, false).success.value

            navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.HasDeemedParentController.onPageLoad(NormalMode)
          }

          "go to the Revising Return page when given true" in {

            val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, true).success.value

            navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, userAnswers, Some(1)) mustBe
              controllers.aboutReturn.routes.RevisingReturnController.onPageLoad(NormalMode)
          }

          "go to the ReportingCompanySameAsParentPage if not answered" in {

            navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
          }
        }

        "from the DeemedParentPage" should {

          "go to the ParentCompanyNamePage when false is selected" in {

            val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, false).success.value

            navigator.nextPage(HasDeemedParentPage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.ParentCompanyNameController.onPageLoad(1, NormalMode)
          }

          "go to the ParentCompanyNamePage when given true" in {

            val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, true).success.value

            navigator.nextPage(HasDeemedParentPage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.ParentCompanyNameController.onPageLoad(1, NormalMode)
          }

          "go to the ParentCompanyPage" in {

            navigator.nextPage(HasDeemedParentPage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.ParentCompanyNameController.onPageLoad(1, NormalMode)
          }
        }

        "from the ParentCompanyNamePage" should {

          "go to the PayTaxInUkPage" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelMin, Some(1)).success.value

            navigator.nextPage(ParentCompanyNamePage, NormalMode, userAnswers) mustBe
              groupStructureRoutes.PayTaxInUkController.onPageLoad(1, NormalMode)
          }
        }

        "from the PayTaxInUkPage" should {

          "go to the LimitedLiabilityPartnershipPage when given true" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value

            navigator.nextPage(PayTaxInUkPage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(1, NormalMode)
          }

          "go to the Registered For Tax in Another country page when given false" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value

            navigator.nextPage(PayTaxInUkPage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.RegisteredForTaxInAnotherCountryController.onPageLoad(1, NormalMode)
          }

          "go to the PayTaxInUkPage if not answered" in {

            navigator.nextPage(PayTaxInUkPage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.PayTaxInUkController.onPageLoad(1, NormalMode)
          }
        }

        "from the LimitedLiabilityPartnershipPage" should {

          "go to the ParentCompanySAUTRPage when given true" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelUkPartnership, Some(1)).success.value

            navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.ParentCompanySAUTRController.onPageLoad(1, NormalMode)
          }

          "go to the ParentCompanyCTUTRPage when given false" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value

            navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.ParentCompanyCTUTRController.onPageLoad(1, NormalMode)
          }

          "go to the LimitedLiabilityPartnershipPage when no answer" in {

            navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(1, NormalMode)
          }
        }

        "from the ParentCompanyCTUTRPage" should {

          "go to the RegisteredCompaniesHousePage" in {

            navigator.nextPage(ParentCompanyCTUTRPage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.RegisteredCompaniesHouseController.onPageLoad(1, NormalMode)
          }
        }

        "from the RegisteredCompaniesHousePage" should {

          "go to the ParentCRNController when given true" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value

            navigator.nextPage(RegisteredCompaniesHousePage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.ParentCRNController.onPageLoad(1, NormalMode)
          }

          "go to the RevisingReturnController when given false" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelUkCompany.copy(registeredCompaniesHouse = Some(false)), Some(1)).success.value

            navigator.nextPage(RegisteredCompaniesHousePage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }

          "go to the RegisteredCompaniesHousePage when no answer" in {

            navigator.nextPage(RegisteredCompaniesHousePage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.RegisteredCompaniesHouseController.onPageLoad(1, NormalMode)
          }
        }

        "from the ParentCompanySAUTRPage" should {

          "go to the ParentCRNPage" in {

            navigator.nextPage(ParentCompanySAUTRPage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.ParentCRNController.onPageLoad(1, NormalMode)
          }
        }

        "from the ParentCRNPage" should {

          "go to the RevisingReturnPage" in {

            navigator.nextPage(ParentCRNPage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }
        }

        "from the Registered For Tax In Another country page" should {

          "go to the Country of Incorporation page when true" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value

            navigator.nextPage(RegisteredForTaxInAnotherCountryPage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.CountryOfIncorporationController.onPageLoad(1, NormalMode)
          }

          "go to the Check Your Answers page when false" in {

            val userAnswers = emptyUserAnswers
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value

            navigator.nextPage(RegisteredForTaxInAnotherCountryPage, NormalMode, userAnswers, Some(1)) mustBe
              groupStructureRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }
        }

        "from the Country of Incorporation page" should {

          "go to the Local CRN page" in {

            navigator.nextPage(CountryOfIncorporationPage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.LocalRegistrationNumberController.onPageLoad(1, NormalMode)
          }
        }

        "from the Local CRN page" should {

          "go to the Check Your Answers page" in {

            navigator.nextPage(LocalRegistrationNumberPage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
          }
        }

        "from the check answers page" should {

          "go to the DeemedParentReviewAnswersList page when given true" in {

            val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, true).success.value

            navigator.nextPage(CheckAnswersGroupStructurePage, NormalMode, userAnswers) mustBe
              groupStructureRoutes.DeemedParentReviewAnswersListController.onPageLoad()
          }

          "go to the revisingReturn page when given false" in {

            val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, false).success.value

            navigator.nextPage(CheckAnswersGroupStructurePage, NormalMode, userAnswers) mustBe
              controllers.aboutReturn.routes.RevisingReturnController.onPageLoad(NormalMode)
          }

          "go to the HasDeemedParent page no given answers for HasDeemedParentPage" in {

            navigator.nextPage(CheckAnswersGroupStructurePage, NormalMode, emptyUserAnswers) mustBe
              groupStructureRoutes.HasDeemedParentController.onPageLoad(NormalMode)
          }
        }
      }

      "in Check mode" must {

        "go to group structure check your answers" in {

          navigator.nextPage(HasDeemedParentPage, CheckMode, emptyUserAnswers) mustBe
            groupStructureRoutes.CheckAnswersGroupStructureController.onPageLoad(1)
        }
      }
    }
  }
}
