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
import controllers.groupStructure.{routes => groupRoutes}
import models._
import pages.groupStructure.{DeemedParentPage, LimitedLiabilityPartnershipPage, ParentCRNPage, ParentCompanyCTUTRPage, ParentCompanyNamePage, ParentCompanySAUTRPage, PayTaxInUkPage, RegisteredCompaniesHousePage, ReportingCompanySameAsParentPage}

class GroupStructureNavigatorSpec extends SpecBase {

  val navigator = new GroupStructureNavigator

  "GroupStructureNavigator" when {

    "in Normal mode" must {

      "from the ReportingCompanyNamePage" should {

        "go to the DeemedParentpage when given false" in {

          val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, false).success.value

          navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, userAnswers) mustBe
            groupRoutes.DeemedParentController.onPageLoad(NormalMode)
        }

        "go to the under construction page when given true" in {

          val userAnswers = emptyUserAnswers.set(ReportingCompanySameAsParentPage, true).success.value

          navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, userAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }

        "go to the ReportingCompanySameAsParentPage" in {

          navigator.nextPage(ReportingCompanySameAsParentPage, NormalMode, emptyUserAnswers) mustBe
            groupRoutes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
        }
      }

      "from the DeemedParentPage" should {

        "go to the ParentCompanyNamePage when given false" in {

          val userAnswers = emptyUserAnswers.set(DeemedParentPage, false).success.value

          navigator.nextPage(DeemedParentPage, NormalMode, userAnswers) mustBe
            groupRoutes.ParentCompanyNameController.onPageLoad(NormalMode)
        }
      }

      "from the ParentCompanyNamePage" should {

        "go to the PayTaxInUkPage when given false" in {

          navigator.nextPage(ParentCompanyNamePage, NormalMode, emptyUserAnswers) mustBe
            groupRoutes.PayTaxInUkController.onPageLoad(NormalMode)
        }
      }

      "from the PayTaxInUkPage" should {

        "go to the LimitedLiabilityPartnershipPage when given false" in {

          val userAnswers = emptyUserAnswers.set(PayTaxInUkPage, true).success.value

          navigator.nextPage(PayTaxInUkPage, NormalMode, userAnswers) mustBe
            groupRoutes.LimitedLiabilityPartnershipController.onPageLoad(NormalMode)
        }

        "go to the under construction page when given true" in {

          val userAnswers = emptyUserAnswers.set(PayTaxInUkPage, false).success.value

          navigator.nextPage(PayTaxInUkPage, NormalMode, userAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }

        "go to the PayTaxInUkPage" in {

          navigator.nextPage(PayTaxInUkPage, NormalMode, emptyUserAnswers) mustBe
            groupRoutes.PayTaxInUkController.onPageLoad(NormalMode)
        }
      }

      "from the LimitedLiabilityPartnershipPage" should {

        "go to the ParentCompanySAUTRPage when given false" in {

          val userAnswers = emptyUserAnswers.set(LimitedLiabilityPartnershipPage, true).success.value

          navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, userAnswers) mustBe
            groupRoutes.ParentCompanySAUTRController.onPageLoad(NormalMode)
        }

        "go to the ParentCompanyCTUTRPage when given true" in {

          val userAnswers = emptyUserAnswers.set(LimitedLiabilityPartnershipPage, false).success.value

          navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, userAnswers) mustBe
            groupRoutes.ParentCompanyCTUTRController.onPageLoad(NormalMode)
        }

        "go to the PayTaxInUkPage" in {

          navigator.nextPage(LimitedLiabilityPartnershipPage, NormalMode, emptyUserAnswers) mustBe
            groupRoutes.LimitedLiabilityPartnershipController.onPageLoad(NormalMode)
        }
      }

      "from the ParentCompanyCTUTRPage" should {

        "go to the RegisteredCompaniesHousePage when given false" in {

          navigator.nextPage(ParentCompanyCTUTRPage, NormalMode, emptyUserAnswers) mustBe
            groupRoutes.RegisteredCompaniesHouseController.onPageLoad(NormalMode)
        }
      }

      "from the RegisteredCompaniesHousePage" should {

        "go to the RegisteredCompaniesHousePage when given false" in {

          navigator.nextPage(RegisteredCompaniesHousePage, NormalMode, emptyUserAnswers) mustBe
            groupRoutes.ParentCRNController.onPageLoad(NormalMode)
        }
      }

      "from the ParentCompanySAUTRPage" should {

        "go to the RegisteredCompaniesHousePage when given false" in {

          navigator.nextPage(ParentCompanySAUTRPage, NormalMode, emptyUserAnswers) mustBe
            groupRoutes.ParentCRNController.onPageLoad(NormalMode)
        }
      }

      "from the ParentCRNPage" should {

        "go to the RegisteredCompaniesHousePage when given false" in {

          navigator.nextPage(ParentCRNPage, NormalMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }
      }
    }

    "in Check mode" must {

      "go to Reporting Company Under construction" in {

        navigator.nextPage(DeemedParentPage, CheckMode, emptyUserAnswers) mustBe
          controllers.routes.UnderConstructionController.onPageLoad()
      }
    }
  }
}
