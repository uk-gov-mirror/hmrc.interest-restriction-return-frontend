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
import controllers.ukCompanies.routes
import models._
import models.CompanyEstimatedFigures._
import pages.ukCompanies.{EnterCompanyTaxEBITDAPage, UkCompaniesDeletionConfirmationPage, _}
import assets.constants.fullReturn.UkCompanyConstants._
import pages.groupLevelInformation.{GroupSubjectToReactivationsPage, GroupSubjectToRestrictionsPage}

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

      "from the NetTaxInterestAmountPage" should {

        "where interest is income and group is subject to restrictions, go to the CompanyContainsEstimatesPage" in {
          val userAnswers = (for {
            ua      <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, true)
            finalUa <- ua.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
        }

        "where interest is income and group is subject to reactivations, go to the AddAnReactivationQueryPage" in {
          val userAnswers = (for {
            ua      <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
            ua2     <- ua.set(GroupSubjectToReactivationsPage, true)
            finalUa <- ua2.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.AddAnReactivationQueryController.onPageLoad(1, NormalMode)
        }

        "where interest is income and group is NOT subject to restrictions or reactivations, go to the CompanyContainsEstimatesPage" in {
          val userAnswers = (for {
            ua      <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
            ua2     <- ua.set(GroupSubjectToReactivationsPage, false)
            finalUa <- ua2.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
        }

        "where interest is expense and group is subject to restrictions, go to the AddRestrictionPage" in {
          val userAnswers = (for {
            ua      <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, true)
            finalUa <- ua.set(UkCompaniesPage, ukCompanyModelReactivationMaxExpense, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.AddRestrictionController.onPageLoad(1, NormalMode)
        }

        "where interest is expense and group is subject to reactivations, go to the AddAnReactivationQueryPage" in {
          val userAnswers = (for {
            ua      <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
            ua2     <- ua.set(GroupSubjectToReactivationsPage, true)
            finalUa <- ua2.set(UkCompaniesPage, ukCompanyModelReactivationMaxExpense, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.AddAnReactivationQueryController.onPageLoad(1, NormalMode)
        }

        "where interest is expense and group is NOT subject to restrictions or reactivations, go to the CompanyContainsEstimatesPage" in {
          val userAnswers = (for {
            ua      <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
            ua2     <- ua.set(GroupSubjectToReactivationsPage, false)
            finalUa <- ua2.set(UkCompaniesPage, ukCompanyModelReactivationMaxExpense, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
        }
      }

      "go from the ConsentingCompanyPage to the" should {

        "CompanyQICElectionController when subject to restrictions is true" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, true).success.value

          navigator.nextPage(ConsentingCompanyPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyQICElectionController.onPageLoad(1, NormalMode)
        }

        "CompanyQICElectionController when multiple subject to restrictions are false"  in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false).success.value
              .set(GroupSubjectToReactivationsPage, false).success.value

          navigator.nextPage(ConsentingCompanyPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyQICElectionController.onPageLoad(1, NormalMode)
        }

        "CompanyQICElectionController when multiple subject to restrictions are mixed" in {

          val userAnswers = emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false).success.value
            .set(GroupSubjectToReactivationsPage, true).success.value

          navigator.nextPage(ConsentingCompanyPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyQICElectionController.onPageLoad(1, NormalMode)
        }
      }

      "go from the AddareactivationqueryPage to the" should {

        "if yes to the reactivationamountpage" in {

          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationTrue, Some(1)).success.value

          navigator.nextPage(AddAnReactivationQueryPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.ReactivationAmountController.onPageLoad(1, NormalMode)
        }

        "if no to the CompanyContainsEstimatesPage" in {

          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationFalse, Some(1)).success.value

          navigator.nextPage(AddAnReactivationQueryPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
        }
      }

      "from the ReactivationAmount" should {

        "navigate to the CompanyContainsEstimates page" in {

          navigator.nextPage(ReactivationAmountPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
        }
      }

      "from the UkCompanies Deletion Confirmation Page" should {

        "go to the UkCompanies List View page" in {
          navigator.nextPage(UkCompaniesDeletionConfirmationPage, NormalMode, emptyUserAnswers) mustBe
            routes.UkCompaniesReviewAnswersListController.onPageLoad()
        }
      }

      "from the UkCompaniesPage" should {

        "go to the Check your Answers page" in {
          navigator.nextPage(UkCompaniesPage, NormalMode, emptyUserAnswers) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
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

      "from the RestrictionAmountSameAPPage" should {

        //TODO: Update as part of routing subtask
        "go to the Under Construction page" in {
          navigator.nextPage(RestrictionAmountSameAPPage, NormalMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }
      }

      "from the CompanyEstimatedFiguresPage" should {
        "go to the CYA page where it's set to false" in {
          val company = ukCompanyModelMin.copy(containsEstimates = Some(false))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).success.value

          navigator.nextPage(CompanyContainsEstimatesPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }

        "go to the CompanyEstimatedFiguresPage where it's set to true" in {
          val company = ukCompanyModelMin.copy(containsEstimates = Some(true))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).success.value

          navigator.nextPage(CompanyContainsEstimatesPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyEstimatedFiguresController.onPageLoad(1, NormalMode)
        }
      }

      "from the CompanyEstimatedFiguresPage" should {
        "go to the CYA page" in {
          navigator.nextPage(CompanyEstimatedFiguresPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }
      }
    }

    "in Check mode" must {

      "for any page go to CheckAnswersUKCompany" in {
        navigator.nextPage(CheckAnswersUkCompanyPage, CheckMode, emptyUserAnswers) mustBe
          routes.CheckAnswersUkCompanyController.onPageLoad(1)
      }

      "from the CompanyContainsEstimatesPage" should {
        "go to the CYA page where it's set to false" in {
          val company = ukCompanyModelMin.copy(containsEstimates = Some(false))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).success.value

          navigator.nextPage(CompanyContainsEstimatesPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }

        "go to the CompanyEstimatedFiguresPage where it's set to true and the next page is not set" in {
          val company = ukCompanyModelMin.copy(containsEstimates = Some(true))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).success.value

          navigator.nextPage(CompanyContainsEstimatesPage, CheckMode, userAnswers, Some(1)) mustBe
            routes.CompanyEstimatedFiguresController.onPageLoad(1, CheckMode)
        }

        "go to the CYA page where it's set to true and the next page is set" in {
          val company = ukCompanyModelMin.copy(containsEstimates = Some(true), estimatedFigures = Some(Set(TaxEbitda)))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).success.value

          navigator.nextPage(CompanyContainsEstimatesPage, CheckMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }
      } 

      "from the AddAnReactivationQueryPage" should {

        "if yes and the ReactivationAmountPage is not set, to the ReactivationAmountPage" in {

          val model = ukCompanyModelReactivationTrue.copy(allocatedReactivations = None)
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, model, Some(1)).success.value

          navigator.nextPage(AddAnReactivationQueryPage, CheckMode, userAnswers, Some(1)) mustBe
            routes.ReactivationAmountController.onPageLoad(1, CheckMode)
        }

        "if yes and the ReactivationAmountPage is set, to the CYA Page" in {

          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationTrue, Some(1)).success.value

          navigator.nextPage(AddAnReactivationQueryPage, CheckMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }

        "if no to the CYA Page" in {

          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationFalse, Some(1)).success.value

          navigator.nextPage(AddAnReactivationQueryPage, CheckMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }
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
