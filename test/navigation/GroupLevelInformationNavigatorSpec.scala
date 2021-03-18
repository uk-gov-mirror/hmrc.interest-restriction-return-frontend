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

package navigation

import base.SpecBase
import controllers.groupLevelInformation.{routes => groupLevelInformationRoutes}
import controllers.ukCompanies.{routes => ukCompaniesRoutes}
import models.{CheckMode, EstimatedFigures, NormalMode, UserAnswers}
import pages.groupLevelInformation._
import pages.elections.GroupRatioElectionPage

class GroupLevelInformationNavigatorSpec extends SpecBase {

  val navigator = new GroupLevelInformationNavigator

  "GroupLevelInformationNavigator" when {

    "in Normal mode" must {

      "from the Group Subject to Restrictions page" should {

        "if user answer is Yes/True go to the Disallowed Amount page" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, value = true).get

          navigator.nextPage(GroupSubjectToRestrictionsPage, NormalMode, userAnswers) mustBe
            groupLevelInformationRoutes.DisallowedAmountController.onPageLoad(NormalMode)
        }

        "if user answer is No/False go to the Group Subject to Reactivations page" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, value = false).get

          navigator.nextPage(GroupSubjectToRestrictionsPage, NormalMode, userAnswers) mustBe
            groupLevelInformationRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
        }
      }

      "from the Group Subject to Reactivations page" should {

        "go to the Interest Reactivations Cap page when the group is subject to reactivations" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToReactivationsPage, true).get

          navigator.nextPage(GroupSubjectToReactivationsPage, NormalMode, userAnswers) mustBe
            groupLevelInformationRoutes.InterestReactivationsCapController.onPageLoad(NormalMode)
        }

        "go to the Interest Allowance Brought Forwards page when the group is subject to reactivations" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToReactivationsPage, false).get

          navigator.nextPage(GroupSubjectToReactivationsPage, NormalMode, userAnswers) mustBe
            groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
        }
      }

      "from the Interest Reactivations Cap page" should {

        "go to the Interest Allowance Brought Forwards page" in {

          navigator.nextPage(InterestReactivationsCapPage, NormalMode, emptyUserAnswers) mustBe
            groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
        }
      }

      "from the Disallowed Amount page" should {

        "go to the Interest Allowance Brought Forwards page" in {

          navigator.nextPage(DisallowedAmountPage, NormalMode, emptyUserAnswers) mustBe
            groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
        }
      }

      "from the Interest Allowance Brought Forwards page" should {

        "go to the Group Interest Allowance page" in {

          navigator.nextPage(InterestAllowanceBroughtForwardPage, NormalMode, emptyUserAnswers) mustBe
            groupLevelInformationRoutes.GroupInterestAllowanceController.onPageLoad(NormalMode)
        }
      }

      "from the Group Interest Allowance page" should {

        "go to the Group Interest Capacity page" in {

          navigator.nextPage(GroupInterestAllowancePage, NormalMode, emptyUserAnswers) mustBe
            groupLevelInformationRoutes.GroupInterestCapacityController.onPageLoad(NormalMode)
        }
      }

      "from the Group Interest Capacity page" should {

        "go to the next section page" in {

          navigator.nextPage(GroupInterestCapacityPage, NormalMode, emptyUserAnswers) mustBe
            groupLevelInformationRoutes.EnterANGIEController.onPageLoad(NormalMode)
        }
      }

      "from the ANGIE page" when {
        "group ratio election page was set to true" should {
          "go to the next section page" in {
            val userAnswers = emptyUserAnswers.set(GroupRatioElectionPage, true).get
            navigator.nextPage(EnterANGIEPage, NormalMode, userAnswers) mustBe
              groupLevelInformationRoutes.EnterQNGIEController.onPageLoad(NormalMode)
          }
        }

        "group ratio election page was set to false" should {
          "go to the next section page" in {
            val userAnswers = emptyUserAnswers.set(GroupRatioElectionPage, false).get
            navigator.nextPage(EnterANGIEPage, NormalMode, userAnswers) mustBe
              groupLevelInformationRoutes.ReturnContainEstimatesController.onPageLoad(NormalMode)
          }
        }

      }

      "from the QNGIE page" should {
        "go to the next section page" in {
          navigator.nextPage(EnterQNGIEPage, NormalMode, emptyUserAnswers) mustBe
            groupLevelInformationRoutes.GroupEBITDAController.onPageLoad(NormalMode)
        }
      }

      "from the GroupEBITDA page" should {
        "go to the next section page" in {
          navigator.nextPage(GroupEBITDAPage, NormalMode, emptyUserAnswers) mustBe
            groupLevelInformationRoutes.GroupRatioPercentageController.onPageLoad(NormalMode)
        }
      }

      "from the Group Ratio Percentage page" should {
        "go to the next section page" in {
          navigator.nextPage(GroupRatioPercentagePage, NormalMode, emptyUserAnswers) mustBe
            groupLevelInformationRoutes.ReturnContainEstimatesController.onPageLoad(NormalMode)
        }
      }

      "from the Return Contains Estimates page" should {
        "go to the Check Answers page where false" in {
          val userAnswers = emptyUserAnswers.set(ReturnContainEstimatesPage, false).get
          navigator.nextPage(ReturnContainEstimatesPage, NormalMode, userAnswers) mustBe
            groupLevelInformationRoutes.CheckAnswersGroupLevelController.onPageLoad()
        }

        "go to the EstimatedFigures page where true" in {
          val userAnswers = emptyUserAnswers.set(ReturnContainEstimatesPage, true).get
          navigator.nextPage(ReturnContainEstimatesPage, NormalMode, userAnswers) mustBe
            groupLevelInformationRoutes.EstimatedFiguresController.onPageLoad(NormalMode)
        }
      }

      "from the Check Answers page" should {

        "go to the Adding UK Companies page" in {
          navigator.nextPage(CheckAnswersGroupLevelPage, NormalMode, emptyUserAnswers) mustBe
            ukCompaniesRoutes.AboutAddingUKCompaniesController.onPageLoad()
        }
      }

      "in Check mode" must {

        "from the Return Contains Estimates page" should {
          "go to the Check Answers page where false" in {
            val userAnswers = emptyUserAnswers.set(ReturnContainEstimatesPage, false).get
            navigator.nextPage(ReturnContainEstimatesPage, CheckMode, userAnswers) mustBe
              groupLevelInformationRoutes.CheckAnswersGroupLevelController.onPageLoad()
          }

          "go to the Check Answers page where true and EstimatedFigures is populated" in {
            val userAnswers =
              for {
                ua <- emptyUserAnswers.set(ReturnContainEstimatesPage, true)
                finalUa <- ua.set(EstimatedFiguresPage, EstimatedFigures.values.toSet)
              } yield finalUa
            navigator.nextPage(ReturnContainEstimatesPage, CheckMode, userAnswers.get) mustBe
              groupLevelInformationRoutes.CheckAnswersGroupLevelController.onPageLoad()
          }

          "go to the EstimatedFigures page where true and EstimatedFigures is not populated" in {
            val userAnswers = emptyUserAnswers.set(ReturnContainEstimatesPage, true).get
            navigator.nextPage(ReturnContainEstimatesPage, CheckMode, userAnswers) mustBe
              groupLevelInformationRoutes.EstimatedFiguresController.onPageLoad(CheckMode)
          }
        }

        "from GroupSubjectToRestrictionsPage" should {

          "when set to true and DisallowedAmountPage already set go back to the CYA page " in {
            val userAnswers = for {
              ua <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, true)
              finalUa <- ua.set(DisallowedAmountPage, BigDecimal(123))
            } yield finalUa

            navigator.nextPage(GroupSubjectToRestrictionsPage, CheckMode, userAnswers.get) mustBe
              groupLevelInformationRoutes.CheckAnswersGroupLevelController.onPageLoad()
          }

          "when set to false and GroupSubjectToReactivationsPage already set go back to the CYA page" in {
            val userAnswers = for {
              ua <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
              finalUa <- ua.set(GroupSubjectToReactivationsPage, true)
            } yield finalUa

            navigator.nextPage(GroupSubjectToRestrictionsPage, CheckMode, userAnswers.get) mustBe
              groupLevelInformationRoutes.CheckAnswersGroupLevelController.onPageLoad()
          }

          "when set to true and DisallowedAmountPage not already set go to DisallowedAmountPage" in {
            val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, true)

            navigator.nextPage(GroupSubjectToRestrictionsPage, CheckMode, userAnswers.get) mustBe
              groupLevelInformationRoutes.DisallowedAmountController.onPageLoad(NormalMode)
          }

          "when set to false and GroupSubjectToReactivationsPage not already set go to GroupSubjectToReactivationsPage" in {
            val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)

            navigator.nextPage(GroupSubjectToRestrictionsPage, CheckMode, userAnswers.get) mustBe
              groupLevelInformationRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
          }
        }

        "from GroupSubjectToReactivationsPage" should {

          "when set to true and data for the section is not complete then go to the InterestReactivationsCapPage" in {
            val userAnswers = emptyUserAnswers.set(GroupSubjectToReactivationsPage, true)

            navigator.nextPage(GroupSubjectToReactivationsPage, CheckMode, userAnswers.get) mustBe
              groupLevelInformationRoutes.InterestReactivationsCapController.onPageLoad(NormalMode)
          }

          "when set to true and data for the section is complete then go back to the CYA page" in {
            val userAnswers = UserAnswers("id")
              .set(GroupRatioElectionPage, false).get
              .set(GroupSubjectToRestrictionsPage, false).get
              .set(GroupSubjectToReactivationsPage, true).get
              .set(InterestReactivationsCapPage,BigDecimal(5)).get
              .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
              .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
              .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
              .set(EnterANGIEPage, BigDecimal(123.12)).get
              .set(ReturnContainEstimatesPage, false).get


            navigator.nextPage(GroupSubjectToReactivationsPage, CheckMode, userAnswers) mustBe
              groupLevelInformationRoutes.CheckAnswersGroupLevelController.onPageLoad()
          }

          "when set to false and data for section is complete then go back to the CYA page" in {
            val userAnswers = UserAnswers("id")
              .set(GroupRatioElectionPage, false).get
              .set(GroupSubjectToRestrictionsPage, false).get
              .set(GroupSubjectToReactivationsPage, false).get
              .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
              .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
              .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
              .set(EnterANGIEPage, BigDecimal(123.12)).get
              .set(ReturnContainEstimatesPage, false).get

            navigator.nextPage(GroupSubjectToReactivationsPage, CheckMode, userAnswers) mustBe
              groupLevelInformationRoutes.CheckAnswersGroupLevelController.onPageLoad()
          }

          "when set to false and data for the section is not complete then go back to interest allowance brought forward" in {
            val userAnswers = emptyUserAnswers.set(GroupSubjectToReactivationsPage, false)

            navigator.nextPage(GroupSubjectToReactivationsPage, CheckMode, userAnswers.get) mustBe
              groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
          }
        }
      }
    }
  }
}
