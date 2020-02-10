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

import base.SpecBase
import controllers.groupStructure.{routes => groupStructureRoutes}
import models._
import pages.groupStructure.{CheckAnswersGroupStructurePage, DeemedParentPage, LimitedLiabilityPartnershipPage, ParentCRNPage, ParentCompanyCTUTRPage, ParentCompanyNamePage, ParentCompanySAUTRPage, PayTaxInUkPage, RegisteredCompaniesHousePage, ReportingCompanySameAsParentPage}

class GroupStructureNavigatorSpec extends SpecBase {

  val navigator = new GroupStructureNavigator

  "GroupStructureNavigator" when {

    "in Normal mode" must {

      "from the ReportingCompanyNamePage" should {

        "go to the DeemedParentpage when given false" in {

          val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, false).success.value

          navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, userAnswers) mustBe
            groupStructureRoutes.DeemedParentController.onPageLoad(NormalMode)
        }

        "go to the under construction page when given true" in {

          val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, true).success.value

          navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, userAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }

        "go to the ReportingCompanySameAsParentPage" in {

          navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
        }
      }

      "from the DeemedParentPage" should {

        "go to the ParentCompanyNamePage when given false" in {

          val userAnswers = emptyUserAnswers.set(DeemedParentPage, false).success.value

          navigator.nextPage(DeemedParentPage, NormalMode, userAnswers) mustBe
            groupStructureRoutes.ParentCompanyNameController.onPageLoad(NormalMode)
        }

        "go to the under construction page when given true" in {

          val userAnswers = emptyUserAnswers.set(DeemedParentPage, true).success.value

          navigator.nextPage(DeemedParentPage, NormalMode, userAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }

        "go to the DeemedParentPage" in {

          navigator.nextPage(DeemedParentPage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.DeemedParentController.onPageLoad(NormalMode)
        }
      }

      "from the ParentCompanyNamePage" should {

        "go to the PayTaxInUkPage when given false" in {

          navigator.nextPage(ParentCompanyNamePage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.PayTaxInUkController.onPageLoad(NormalMode)
        }
      }

      "from the PayTaxInUkPage" should {

        "go to the LimitedLiabilityPartnershipPage when given false" in {

          val userAnswers = emptyUserAnswers.set(PayTaxInUkPage, true).success.value

          navigator.nextPage(PayTaxInUkPage, NormalMode, userAnswers) mustBe
            groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(NormalMode)
        }

        "go to the under construction page when given true" in {

          val userAnswers = emptyUserAnswers.set(PayTaxInUkPage, false).success.value

          navigator.nextPage(PayTaxInUkPage, NormalMode, userAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }

        "go to the PayTaxInUkPage" in {

          navigator.nextPage(PayTaxInUkPage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.PayTaxInUkController.onPageLoad(NormalMode)
        }
      }

      "from the LimitedLiabilityPartnershipPage" should {

        "go to the ParentCompanySAUTRPage when given false" in {

          val userAnswers = emptyUserAnswers.set(LimitedLiabilityPartnershipPage, true).success.value

          navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, userAnswers) mustBe
            groupStructureRoutes.ParentCompanySAUTRController.onPageLoad(NormalMode)
        }

        "go to the ParentCompanyCTUTRPage when given true" in {

          val userAnswers = emptyUserAnswers.set(LimitedLiabilityPartnershipPage, false).success.value

          navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, userAnswers) mustBe
            groupStructureRoutes.ParentCompanyCTUTRController.onPageLoad(NormalMode)
        }

        "go to the PayTaxInUkPage" in {

          navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(NormalMode)
        }
      }

      "from the ParentCompanyCTUTRPage" should {

        "go to the RegisteredCompaniesHousePage when given false" in {

          navigator.nextPage(ParentCompanyCTUTRPage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.RegisteredCompaniesHouseController.onPageLoad(NormalMode)
        }
      }

      "from the RegisteredCompaniesHousePage" should {

        "go to the RegisteredCompaniesHousePage when given false" in {

          navigator.nextPage(RegisteredCompaniesHousePage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.ParentCRNController.onPageLoad(NormalMode)
        }
      }

      "from the ParentCompanySAUTRPage" should {

        "go to the RegisteredCompaniesHousePage when given false" in {

          navigator.nextPage(ParentCompanySAUTRPage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.ParentCRNController.onPageLoad(NormalMode)
        }
      }

      "from the ParentCRNPage" should {

        "go to the RegisteredCompaniesHousePage when given false" in {

          navigator.nextPage(ParentCRNPage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.CheckAnswersGroupStructureController.onPageLoad()
        }
      }

      "from the check answers page" should {

        "go to the underConstruction page" in {

          navigator.nextPage(CheckAnswersGroupStructurePage, NormalMode, emptyUserAnswers) mustBe
          controllers.routes.UnderConstructionController.onPageLoad()
        }
      }
    }

    "in Check mode" must {

      "go to group structure check your answers" in {

        navigator.nextPage(DeemedParentPage, CheckMode, emptyUserAnswers) mustBe
          groupStructureRoutes.CheckAnswersGroupStructureController.onPageLoad()
      }
    }
  }
}
