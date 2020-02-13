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
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import controllers.elections.{routes => electionRoutes}
import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import models.{CheckMode, NormalMode, UserAnswers}
import pages.Page
import pages.aboutReturn._
import pages.startReturn.FullOrAbbreviatedReturnPage

class AboutReturnNavigatorSpec extends SpecBase {

  val navigator = new AboutReturnNavigator

  "AboutReturnNavigator" when {

    "in Normal mode" must {

      "from the Revising Return page" should {

        "go to the Revision Information page when yes selected to revising a return" ignore {

          val userAnswers = emptyUserAnswers.set(RevisingReturnPage, true).get

          navigator.nextPage(RevisingReturnPage, NormalMode, userAnswers) mustBe
            ??? //TODO Link to Revision Information Page when implemented
        }

        "go to the Infrastructure Company Election page when no selected to revising a return" in {

          val userAnswers = emptyUserAnswers.set(RevisingReturnPage, false).get

          navigator.nextPage(RevisingReturnPage, NormalMode, userAnswers) mustBe
            aboutReturnRoutes.InfrastructureCompanyElectionController.onPageLoad(NormalMode)
        }
      }

      "from the Infrastructure Company Election page" should {

        "go to the Return Contains Estimates page when Full Return is being submitted" in {

          val fullOrAbbreviatedAnswer = emptyUserAnswers.set(FullOrAbbreviatedReturnPage, Full).get

          navigator.nextPage(InfrastructureCompanyElectionPage, NormalMode, fullOrAbbreviatedAnswer) mustBe
            aboutReturnRoutes.ReturnContainEstimatesController.onPageLoad(NormalMode)
        }

        "go to the abbreviated return section when Abbreviated Return is being submitted" ignore {

          val fullOrAbbreviatedAnswer = emptyUserAnswers.set(FullOrAbbreviatedReturnPage, Abbreviated).get

          navigator.nextPage(InfrastructureCompanyElectionPage, NormalMode, fullOrAbbreviatedAnswer) mustBe
            ??? //TODO Link to abbreviated return section when implemented
        }
      }

      "from the Return Contains Estimates page" should {

        "go to the Group Subject to Restrictions page" in {

          navigator.nextPage(ReturnContainEstimatesPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)
        }
      }

      "from the Group Subject to Restrictions page" should {

        "if user answer is Yes/True go to the Interest Allowance Brought Forwards page" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, value = true).get

          navigator.nextPage(GroupSubjectToRestrictionsPage, NormalMode, userAnswers) mustBe
            aboutReturnRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
        }

        "if user answer is No/False go to the Group Subject to Reactivations page" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, value = false).get

          navigator.nextPage(GroupSubjectToRestrictionsPage, NormalMode, userAnswers) mustBe
            aboutReturnRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
        }
      }

      "from the Group Subject to Reactivations page" should {

        "go to the Interest Reactivations Cap page when the group is subject to reactivations" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToReactivationsPage, true).get

          navigator.nextPage(GroupSubjectToReactivationsPage, NormalMode, userAnswers) mustBe
            aboutReturnRoutes.InterestReactivationsCapController.onPageLoad(NormalMode)
        }

        "go to the Interest Allowance Brought Forwards page when the group is subject to reactivations" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToReactivationsPage, false).get

          navigator.nextPage(GroupSubjectToReactivationsPage, NormalMode, userAnswers) mustBe
            aboutReturnRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
        }
      }

      "from the Interest Reactivations Cap page" should {

        "go to the Interest Allowance Brought Forwards page" in {

          navigator.nextPage(InterestReactivationsCapPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
        }
      }

      "from the Interest Allowance Brought Forwards page" should {

        "go to the Group Interest Allowance page" in {

          navigator.nextPage(InterestAllowanceBroughtForwardPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.GroupInterestAllowanceController.onPageLoad(NormalMode)
        }
      }

      "from the Group Interest Allowance page" should {

        "go to the Group Interest Capacity page" in {

          navigator.nextPage(GroupInterestAllowancePage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.GroupInterestCapacityController.onPageLoad(NormalMode)
        }
      }

      "from the Group Interest Capacity page" should {

        "go to the next section page" in {

          navigator.nextPage(GroupInterestCapacityPage, NormalMode, emptyUserAnswers) mustBe
            electionRoutes.GroupRatioElectionController.onPageLoad(NormalMode)
        }
      }

      "in Check mode" must {

        "go to CheckYourAnswers from a page that doesn't exist in the edit route map" ignore {

          case object UnknownPage extends Page
          navigator.nextPage(UnknownPage, CheckMode, UserAnswers("id")) mustBe
            ??? //TODO: Add Check Your Answers for section (future story)
        }
      }
    }
  }
}
