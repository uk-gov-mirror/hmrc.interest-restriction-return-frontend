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
import controllers.elections.routes
import models._
import pages.elections._

class ElectionsNavigatorSpec extends SpecBase {

  val navigator = new ElectionsNavigator

  "ElectionsNavigator" when {

    "in Normal mode" must {

      "from the GroupRatioElectionPage" should {

        "go to the Enter ANGIE page" in {

          navigator.nextPage(GroupRatioElectionPage, NormalMode, emptyUserAnswers) mustBe
            routes.EnterANGIEController.onPageLoad(NormalMode)
        }
      }

      "from the EnterANGIEPage" should {

        "go to the Enter QNGIE page when Group Ratio is elected" in {

          val userAnswers = emptyUserAnswers.set(GroupRatioElectionPage, true).success.value

          navigator.nextPage(EnterANGIEPage, NormalMode, userAnswers) mustBe
            routes.EnterQNGIEController.onPageLoad(NormalMode)
        }

        "go to the Elected Interest Allowance Alternative Calc Before page when Group Ratio is NOT elected" in {

          val userAnswers = emptyUserAnswers.set(GroupRatioElectionPage, false).success.value

          navigator.nextPage(EnterANGIEPage, NormalMode, userAnswers) mustBe
            routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode)
        }

        "go to the Group Ratio Election page when there is no answer for Group Ratio Election" in {

          navigator.nextPage(EnterANGIEPage, NormalMode, emptyUserAnswers) mustBe
            routes.GroupRatioElectionController.onPageLoad(NormalMode)
        }
      }

      "from the EnterQNGIEPage" should {

        "go to the GroupEBITDA page" in {

          navigator.nextPage(EnterQNGIEPage, NormalMode, emptyUserAnswers) mustBe
            routes.GroupEBITDAController.onPageLoad(NormalMode)
        }
      }

      "from the EnterQNGIEPage" should {

        "go to the GroupRatioPercentage page" in {

          navigator.nextPage(GroupEBITDAPage, NormalMode, emptyUserAnswers) mustBe
            routes.GroupRatioPercentageController.onPageLoad(NormalMode)
        }
      }

      "from the GroupRatioPercentage page" should {

        "go to the Group Ratio Blended Election page" in {

          navigator.nextPage(GroupRatioPercentagePage, NormalMode, emptyUserAnswers) mustBe
            routes.GroupRatioBlendedElectionController.onPageLoad(NormalMode)
        }
      }

      "from the GroupRatioBlendedElection page" should {

        "go to the Elected Group EBITDA Chargeable Gains Before page" in {

          navigator.nextPage(GroupRatioBlendedElectionPage, NormalMode, emptyUserAnswers) mustBe
            routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode)
        }
      }

      "from the ElectedGroupEBITDABefore page" should {

        "go to the Group EBITDA Chargeable Gains Election page when answer is false" in {

          val userAnswers = emptyUserAnswers.set(ElectedGroupEBITDABeforePage, false).success.value

          navigator.nextPage(ElectedGroupEBITDABeforePage, NormalMode, userAnswers) mustBe
            routes.GroupEBITDAChargeableGainsElectionController.onPageLoad(NormalMode)
        }

        "go to the Elected Interest Allowance Alternative Calculation page when answer is true" in {

          val userAnswers = emptyUserAnswers.set(ElectedGroupEBITDABeforePage, true).success.value

          navigator.nextPage(ElectedGroupEBITDABeforePage, NormalMode, userAnswers) mustBe
            routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode)
        }

        "go to the Elected Group EBITDA Before page when there's no answer" in {

          navigator.nextPage(ElectedGroupEBITDABeforePage, NormalMode, emptyUserAnswers) mustBe
            routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode)
        }
      }

      "from the GroupEBITDAChargeableGainsElection page" should {

        "go to the Elected Interest Allowance Alternative Calculation Before page" in {

          navigator.nextPage(GroupEBITDAChargeableGainsElectionPage, NormalMode, emptyUserAnswers) mustBe
            routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode)
        }
      }

      "from the ElectedInterestAllowanceAlternativeCalcBefore page" should {

        "go to the Interest Allowance Non Consolidated Investments page when answer is true" in {

          val userAnswers = emptyUserAnswers.set(ElectedInterestAllowanceAlternativeCalcBeforePage, true).success.value

          navigator.nextPage(ElectedInterestAllowanceAlternativeCalcBeforePage, NormalMode, userAnswers) mustBe
            routes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(NormalMode)
        }

        "go to the Interest Allowance Alternative Calculation page when answer is false" in {

          val userAnswers = emptyUserAnswers.set(ElectedInterestAllowanceAlternativeCalcBeforePage, false).success.value

          navigator.nextPage(ElectedInterestAllowanceAlternativeCalcBeforePage, NormalMode, userAnswers) mustBe
            routes.InterestAllowanceAlternativeCalcElectionController.onPageLoad(NormalMode)
        }

        "go to the Elected Interest Allowance Alternative Calc Before page when there's no answer" in {

          navigator.nextPage(ElectedInterestAllowanceAlternativeCalcBeforePage, NormalMode, emptyUserAnswers) mustBe
            routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode)
        }
      }

      "from the InterestAllowanceAlternativeCalcElectionPage page" should {

        "go to the Interest Allowance Non-Consolidated Investments page" in {

          navigator.nextPage(InterestAllowanceAlternativeCalcElectionPage, NormalMode, emptyUserAnswers) mustBe
            routes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(NormalMode)
        }
      }

      "from the InterestAllowanceNonConsolidatedInvestmentsElection page" should {

        "go to the Elected Interest Allowance Consolidated Partnership Before page" in {

          navigator.nextPage(InterestAllowanceNonConsolidatedInvestmentsElectionPage, NormalMode, emptyUserAnswers) mustBe
            routes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(NormalMode)
        }
      }

      "from the ElectedInterestAllowanceConsolidatedPshipBefore page" should {

        "go to the Interest Allowance Consolidated Partnership Election page when answer is false" in {

          val userAnswers = emptyUserAnswers.set(ElectedInterestAllowanceConsolidatedPshipBeforePage, false).success.value

          navigator.nextPage(ElectedInterestAllowanceConsolidatedPshipBeforePage, NormalMode, userAnswers) mustBe
            routes.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(NormalMode)
        }

        //TODO: Update with Check Your Answers page when built
        "go to the Under Construction page when answer is true" in {

          val userAnswers = emptyUserAnswers.set(ElectedInterestAllowanceConsolidatedPshipBeforePage, true).success.value

          navigator.nextPage(ElectedInterestAllowanceConsolidatedPshipBeforePage, NormalMode, userAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }

        "go to the Elected Interest Allowance Consolidated Partnership Before page when answer there's no answer" in {

          navigator.nextPage(ElectedInterestAllowanceConsolidatedPshipBeforePage, NormalMode, emptyUserAnswers) mustBe
            routes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(NormalMode)
        }
      }

      "from the InterestAllowanceConsolidatedPshipElection page" should {

        //TODO: Update with Check Your Answers page when built
        "go to the Under Construction page" in {

          navigator.nextPage(InterestAllowanceConsolidatedPshipElectionPage, NormalMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }
      }
    }

    "in Check mode" must {

      "go to the Under Construction page" in {

        navigator.nextPage(InterestAllowanceConsolidatedPshipElectionPage, CheckMode, emptyUserAnswers) mustBe
          controllers.routes.UnderConstructionController.onPageLoad()
      }
    }
  }
}
