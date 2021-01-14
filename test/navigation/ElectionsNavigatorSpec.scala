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
import assets.constants.PartnershipsConstants._
import controllers.elections.routes
import models._
import models.FullOrAbbreviatedReturn._
import pages.aboutReturn.FullOrAbbreviatedReturnPage
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

      "from the GroupEBITDAPage" should {

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

      "from the GroupRatioBlendedElection page" when {

        "the answer is false" should {

          "go to the Elected Group EBITDA Chargeable Gains Before page" in {

            val userAnswers = emptyUserAnswers.set(GroupRatioBlendedElectionPage, false).success.value

            navigator.nextPage(GroupRatioBlendedElectionPage, NormalMode, userAnswers) mustBe
              routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode)
          }
        }

        "the answer is true" should {

          "go to the Add Investor Group page" in {

            val userAnswers = emptyUserAnswers.set(GroupRatioBlendedElectionPage, true).success.value

            navigator.nextPage(GroupRatioBlendedElectionPage, NormalMode, userAnswers) mustBe
              routes.AddInvestorGroupController.onPageLoad(NormalMode)
          }
        }

        "there is no answer" should {

          "go to the Group Ratio Blended Election page" in {

            navigator.nextPage(GroupRatioBlendedElectionPage, NormalMode, emptyUserAnswers) mustBe
              routes.GroupRatioBlendedElectionController.onPageLoad(NormalMode)
          }
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

        "go to the Investments page when answer is yes" in {

          val userAnswers = emptyUserAnswers.set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, true).success.value

          navigator.nextPage(InterestAllowanceNonConsolidatedInvestmentsElectionPage, NormalMode, userAnswers) mustBe
            routes.InvestmentsReviewAnswersListController.onPageLoad()
        }

        "go to the Elected Interest Allowance Consolidated Partnership Before page when answer is no" in {

          val userAnswers = emptyUserAnswers.set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, false).success.value

          navigator.nextPage(InterestAllowanceNonConsolidatedInvestmentsElectionPage, NormalMode, userAnswers) mustBe
            routes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(NormalMode)
        }

        "go to InterestAllowanceNonConsolidatedInvestmentsElection when there is no answer" in {

          navigator.nextPage(InterestAllowanceNonConsolidatedInvestmentsElectionPage, NormalMode, emptyUserAnswers) mustBe
            routes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(NormalMode)
        }
      }

      "from the ElectedInterestAllowanceConsolidatedPshipBefore page" should {

        "go to the Interest Allowance Consolidated Partnership Election page when answer is false" in {

          val userAnswers = emptyUserAnswers.set(ElectedInterestAllowanceConsolidatedPshipBeforePage, false).success.value

          navigator.nextPage(ElectedInterestAllowanceConsolidatedPshipBeforePage, NormalMode, userAnswers) mustBe
            routes.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(NormalMode)
        }

        "go to the QIC Election page when answer is true" in {

          val userAnswers = emptyUserAnswers.set(ElectedInterestAllowanceConsolidatedPshipBeforePage, true).success.value

          navigator.nextPage(ElectedInterestAllowanceConsolidatedPshipBeforePage, NormalMode, userAnswers) mustBe
            routes.QICElectionPageController.onPageLoad(NormalMode)
        }

        "go to the Elected Interest Allowance Consolidated Partnership Before page when answer there's no answer" in {

          navigator.nextPage(ElectedInterestAllowanceConsolidatedPshipBeforePage, NormalMode, emptyUserAnswers) mustBe
            routes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(NormalMode)
        }
      }

      "from the InterestAllowanceConsolidatedPshipElection page" when {

        "the answer is true" should {

          "go to the Partnerships Review Answers List page" in {

            val userAnswers = emptyUserAnswers.set(InterestAllowanceConsolidatedPshipElectionPage, true).success.value

            navigator.nextPage(InterestAllowanceConsolidatedPshipElectionPage, NormalMode, userAnswers) mustBe
              routes.PartnershipsReviewAnswersListController.onPageLoad()
          }
        }

        "the answer is false" should {

          "go to QIC Election page" in {

            val userAnswers = emptyUserAnswers.set(InterestAllowanceConsolidatedPshipElectionPage, false).success.value

            navigator.nextPage(InterestAllowanceConsolidatedPshipElectionPage, NormalMode, userAnswers) mustBe
              routes.QICElectionPageController.onPageLoad(NormalMode)
          }
        }

        "no answer is given" should {

          "go to the InterestAllowanceConsolidatedPshipElection page" in {

            navigator.nextPage(InterestAllowanceConsolidatedPshipElectionPage, NormalMode, emptyUserAnswers) mustBe
              routes.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(NormalMode)
          }
        }
      }

      "from the QICElection page" when {

        "the answer is true" should {

          "go to the check your answers page" in {

            val userAnswers = emptyUserAnswers.set(QICElectionPage, true).success.value

            navigator.nextPage(QICElectionPage, NormalMode, userAnswers) mustBe
              routes.CheckAnswersElectionsController.onPageLoad()
          }
        }

        "the answer is false" should {

          "go to the check your answers page" in {

            val userAnswers = emptyUserAnswers.set(QICElectionPage, false).success.value

            navigator.nextPage(QICElectionPage, NormalMode, userAnswers) mustBe
              routes.CheckAnswersElectionsController.onPageLoad()
          }
        }

        "no answer is given" should {

          "go to the QICElection page" in {

            navigator.nextPage(QICElectionPage, NormalMode, emptyUserAnswers) mustBe
              routes.QICElectionPageController.onPageLoad(NormalMode)
          }
        }
      }

      "from the AddInvestorGroup page" when {

        "the answer is true" should {

          "go to the Investor Group Name page" in {

            val userAnswers = emptyUserAnswers.set(AddInvestorGroupPage, true).success.value

            navigator.nextPage(AddInvestorGroupPage, NormalMode, userAnswers) mustBe
              routes.InvestorGroupsReviewAnswersListController.onPageLoad()
          }
        }

        "the answer is false" should {

          "go to the Elected Group EBITDA Before page" in {

            val userAnswers = emptyUserAnswers.set(AddInvestorGroupPage, false).success.value

            navigator.nextPage(AddInvestorGroupPage, NormalMode, userAnswers) mustBe
              routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode)
          }
        }

        "there is no answer" should {

          "go to the Add Investor Group page" in {

            navigator.nextPage(AddInvestorGroupPage, NormalMode, emptyUserAnswers) mustBe
              routes.AddInvestorGroupController.onPageLoad(NormalMode)
          }
        }
      }

      "from the InvestorGroupName page" should {

        "go to the Investor Ratio Method page" in {

          navigator.nextPage(InvestorGroupNamePage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.InvestorRatioMethodController.onPageLoad(1, NormalMode)
        }
      }

      "from the InvestorRatioMethod page" should {

        "go to the Other Investor Group Elections page" in {

          navigator.nextPage(InvestorRatioMethodPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.OtherInvestorGroupElectionsController.onPageLoad(1, NormalMode)
        }
      }

      "from the Other Investor Group Elections page" should {

        "go to the Investor Groups Review Answers List page" in {

          navigator.nextPage(OtherInvestorGroupElectionsPage, NormalMode, emptyUserAnswers) mustBe
            routes.InvestorGroupsReviewAnswersListController.onPageLoad()
        }
      }

      "from the Investor Groups Review Answers List page" should {

        "go to the Elected Group EBITDA Before page" in {

          navigator.nextPage(InvestorGroupsPage, NormalMode, emptyUserAnswers) mustBe
            routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode)
        }
      }

      "from the PartnershipName page" should {

        "go to the IsUkPartnership page" in {

          val userAnswers = emptyUserAnswers.set(
            page = PartnershipsPage,
            value = partnershipModelUK.copy(
              name = partnerName,
              isUkPartnership = None,
              sautr = None
            ),
            idx = Some(1)).success.value

          navigator.nextPage(PartnershipNamePage, NormalMode, userAnswers, Some(1)) mustBe
            routes.IsUkPartnershipController.onPageLoad(1, NormalMode)
        }
      }

      "from the IsUkPartnership page" when {

        "answer is true" should {

          "go to the PartnershipSAUTR page" in {

            val userAnswers = emptyUserAnswers.set(
              page = PartnershipsPage,
              value = partnershipModelUK.copy(
                name = partnerName,
                isUkPartnership = Some(true),
                sautr = None
              ),
              idx = Some(1)).success.value

            navigator.nextPage(IsUkPartnershipPage, NormalMode, userAnswers, Some(1)) mustBe
              routes.PartnershipSAUTRController.onPageLoad(1, NormalMode)
          }
        }

        "answer is false" should {

          "go to the PartnershipsReviewAnswersList page" in {

            val userAnswers = emptyUserAnswers.set(
              page = PartnershipsPage,
              value = partnershipModelUK.copy(
                name = partnerName,
                isUkPartnership = Some(false),
                sautr = None
              ),
              idx = Some(1)).success.value

            navigator.nextPage(IsUkPartnershipPage, NormalMode, userAnswers, Some(1)) mustBe
              routes.PartnershipsReviewAnswersListController.onPageLoad()
          }
        }

        "no answer given" should {

          "go to the IsUkPartnership page" in {

            navigator.nextPage(IsUkPartnershipPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
              routes.IsUkPartnershipController.onPageLoad(1, NormalMode)
          }
        }
      }

      "from the PartnershipSAUTR page" should {

        "go to the PartnershipsReviewAnswersList page" in {

          val userAnswers = emptyUserAnswers.set(
            page = PartnershipsPage,
            value = partnershipModelUK,
            idx = Some(1)).success.value

          navigator.nextPage(PartnershipSAUTRPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.PartnershipsReviewAnswersListController.onPageLoad()
        }
      }

      "from the PartnershipsReviewAnswersList page" should {

        "Do you need to add another partnership is" must {

          "yes, go to Partnership name page" in {

            navigator.addPartnership(1) mustBe
              routes.PartnershipNameController.onPageLoad(2, NormalMode)
          }

          "no, go to check answers page" in {

            val userAnswers = emptyUserAnswers.set(
              page = PartnershipsReviewAnswersListPage,
              value = false).success.value

            navigator.nextPage(PartnershipsReviewAnswersListPage, NormalMode, userAnswers) mustBe
              routes.QICElectionPageController.onPageLoad(NormalMode)
          }
        }

        "from the partnership deletion confirmation page" should {

          "Go to partnership review answers list page" in {

            navigator.nextPage(PartnershipDeletionConfirmationPage, NormalMode, emptyUserAnswers) mustBe
              routes.PartnershipsReviewAnswersListController.onPageLoad()

          }
        }
      }

      "from the CheckAnswersElections page" should {

        "go to the GroupSubjectToRestrictions page on full return" in {
          val userAnswers = emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).success.value

          navigator.nextPage(CheckAnswersElectionsPage, NormalMode, userAnswers) mustBe
            controllers.groupLevelInformation.routes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)
        }

        "go to the AboutAddingUKCompanies page on abbreviated return" in {
          val userAnswers = emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Abbreviated).success.value

          navigator.nextPage(CheckAnswersElectionsPage, NormalMode, userAnswers) mustBe
            controllers.groupLevelInformation.routes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)
        }
      }

      "from the Investment Name page" should {

        "go to the Investments Review Answers List page" in {

          val userAnswers = emptyUserAnswers
              .set(InvestmentNamePage, companyNameModel.name).success.value

          navigator.nextPage(InvestmentNamePage, NormalMode, userAnswers, Some(1)) mustBe
            routes.InvestmentsReviewAnswersListController.onPageLoad()
        }
      }

      "from the Investments Review Answers List page" should {

        "go to the Elected Interest Allowance Consolidated Pship Before page" in {

          navigator.nextPage(InvestmentsReviewAnswersListPage, NormalMode, emptyUserAnswers) mustBe
            routes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(NormalMode)
        }
      }


      "from the InvestmentsDeletionConfirmation page" should {

        "go to the InvestmentsReviewAnswersList page" in {

          navigator.nextPage(InvestmentsDeletionConfirmationPage, NormalMode, emptyUserAnswers) mustBe
            routes.InvestmentsReviewAnswersListController.onPageLoad()
        }
      }

      "from the Investor Groups Deletion Confirmation page" should {

        "go to the Investments Review Answers List page" in {

          navigator.nextPage(InvestorGroupsDeletionConfirmationPage, NormalMode, emptyUserAnswers) mustBe
            routes.InvestorGroupsReviewAnswersListController.onPageLoad()
        }
      }
    }

    "in Check mode" must {

      "from the Investment Name page" should {

        "go to the Investments Review Answers List page" in {

          navigator.nextPage(InvestmentNamePage, CheckMode, emptyUserAnswers) mustBe
            routes.InvestmentsReviewAnswersListController.onPageLoad()
        }
      }

      "go to the Check Answers Elections page" in {

        navigator.nextPage(InterestAllowanceConsolidatedPshipElectionPage, CheckMode, emptyUserAnswers) mustBe
          routes.CheckAnswersElectionsController.onPageLoad()
      }
    }
  }
}
