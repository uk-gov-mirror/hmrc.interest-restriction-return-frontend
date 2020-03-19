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
import controllers.ukCompanies.routes
import models._
import pages.checkTotals.ReviewReactivationsPage
import pages.ukCompanies.{EnterCompanyTaxEBITDAPage, UkCompaniesDeletionConfirmationPage, _}
import assets.constants.fullReturn.UkCompanyConstants._
import pages.aboutReturn.{GroupSubjectToReactivationsPage, GroupSubjectToRestrictionsPage}

class UkCompaniesNavigatorSpec extends SpecBase {

  val navigator = new UkCompaniesNavigator

  "UkCompaniesNavigator" when {

    "in Normal mode" must {

      "go from the AboutAddingUKCompaniesPage" should {

        "to the CompanyDetailsPage" in {

          navigator.nextPage(AboutAddingUKCompaniesPage, NormalMode, emptyUserAnswers) mustBe
            routes.CompanyDetailsController.onPageLoad(1, NormalMode)
        }
      }

      "go from the CompanyDetailsPage" should {

        "to the EnterCompanyTaxEBITDAPage" in {

          navigator.nextPage(CompanyDetailsPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.EnterCompanyTaxEBITDAController.onPageLoad(1, NormalMode)
        }
      }

      "go from the EnterCompanyTaxEBITDAPage" should {

        "to the NetTaxInterestIncomeOrExpensePage" in {

          navigator.nextPage(EnterCompanyTaxEBITDAPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.NetTaxInterestIncomeOrExpenseController.onPageLoad(1, NormalMode)
        }
      }

      "go from the NetTaxInterestIncomeOrExpensePage" should {

        "to the NetTaxInterestAmountPage" in {

          navigator.nextPage(NetTaxInterestIncomeOrExpensePage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.NetTaxInterestAmountController.onPageLoad(1, NormalMode)
        }
      }

      "go from the NetTaxInterestAmountPage" should {

        "to the ConsentingCompanyPage" in {

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.ConsentingCompanyController.onPageLoad(1, NormalMode)
        }
      }

      "go from the ConsentingCompanyPage to the" should {

        "Add a checkanswers page for subject to restrictions" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, true).success.value

          navigator.nextPage(ConsentingCompanyPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }

        "Add a checkanswers page for not subject to reactivations" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false).success.value
              .set(GroupSubjectToReactivationsPage, false).success.value

          navigator.nextPage(ConsentingCompanyPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }

        "Add a checkanswers page for subject to reactivations" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false).success.value
            .set(GroupSubjectToReactivationsPage, true).success.value

          navigator.nextPage(ConsentingCompanyPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.AddAnReactivationQueryController.onPageLoad(1, NormalMode)
        }
      }

      "go from the AddareactivationqueryPage to the" should {

        "if yes to the reactivationamountpage" in {

          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationTrue, Some(1)).success.value

          navigator.nextPage(AddAnReactivationQueryPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.ReactivationAmountController.onPageLoad(1, NormalMode)
        }

        "if no to the checkyouranswerspage" in {

          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationFalse, Some(1)).success.value

          navigator.nextPage(AddAnReactivationQueryPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }
      }

      "go from the ReactivationAmount to the" should {

        "Check your answers page" in {

          navigator.nextPage(ReactivationAmountPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }
      }

      "from the UkCompanies Deletion Confirmation Page" should {

        "go to the UkCompanies List View page" in {
          navigator.nextPage(UkCompaniesDeletionConfirmationPage, NormalMode, emptyUserAnswers) mustBe
            routes.UkCompaniesReviewAnswersListController.onPageLoad()
        }
      }

      "from the UkCompaniesPage" should {

        "go to the DerivedCompany page" in {
          navigator.nextPage(UkCompaniesPage, NormalMode, emptyUserAnswers) mustBe
            controllers.checkTotals.routes.DerivedCompanyController.onPageLoad()
        }
      }

      "from the AddRestrictionPage" should {

        //TODO: Update as part of routing subtask
        "go to the Under Construction page" in {
          navigator.nextPage(AddRestrictionPage, NormalMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }
      }

      "from the CompanyAccountingPeriodSameAsGroupPage" should {

        //TODO: Update as part of routing subtask
        "go to the Under Construction page" in {
          navigator.nextPage(CompanyAccountingPeriodSameAsGroupPage, NormalMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }
      }
    }

    "in Check mode" must {

      "for any page go to CheckAnswersUKCompany" in {
        navigator.nextPage(CheckAnswersUkCompanyPage, CheckMode, emptyUserAnswers) mustBe
          routes.CheckAnswersUkCompanyController.onPageLoad(1)
      }
    }

    "in Review mode" must {

      "for the ReactivationAmount page got to Review Reactivations page" in {
        navigator.nextPage(ReactivationAmountPage, ReviewMode, emptyUserAnswers) mustBe
          controllers.checkTotals.routes.ReviewReactivationsController.onPageLoad()
      }

      "for the NetTaxInterestAmount page got to Review Reactivations page" in {
        navigator.nextPage(NetTaxInterestAmountPage, ReviewMode, emptyUserAnswers) mustBe
          controllers.checkTotals.routes.ReviewNetTaxInterestController.onPageLoad()
      }

      "for the EnterCompanyTaxEBITDA page got to Review Reactivations page" in {
        navigator.nextPage(EnterCompanyTaxEBITDAPage, ReviewMode, emptyUserAnswers) mustBe
          controllers.checkTotals.routes.ReviewTaxEBITDAController.onPageLoad()
      }

      "for any other page got to Review & Complete page" in {
        navigator.nextPage(CompanyDetailsPage, ReviewMode, emptyUserAnswers) mustBe
          controllers.reviewAndComplete.routes.ReviewAndCompleteController.onPageLoad()
      }
    }

    ".addCompany(numOfCompanies: Int)" must {

      "return a call to add the next UK Company by going to the Company Details page for the next IDX" in {
        navigator.addCompany(1) mustBe routes.CompanyDetailsController.onPageLoad(2, NormalMode)
      }
    }
  }
}
