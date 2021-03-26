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

import assets.constants.fullReturn.UkCompanyConstants._
import base.SpecBase
import controllers.ukCompanies.routes
import models._
import models.CompanyEstimatedFigures._
import pages.groupLevelInformation.{GroupSubjectToReactivationsPage, GroupSubjectToRestrictionsPage}
import models.FullOrAbbreviatedReturn._
import models.returnModels.AccountingPeriodModel
import pages.aboutReturn.{FullOrAbbreviatedReturnPage, AccountingPeriodPage}
import pages.ukCompanies.{EnterCompanyTaxEBITDAPage, UkCompaniesDeletionConfirmationPage, _}
import java.time.LocalDate

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

        "to the ConsentingCompanyPage" in {

          navigator.nextPage(CompanyDetailsPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.ConsentingCompanyController.onPageLoad(1, NormalMode)
        }
      }

      "go from the EnterCompanyTaxEBITDAPage" should {

        "to the AddNetTaxInterestPage" in {

          navigator.nextPage(EnterCompanyTaxEBITDAPage, NormalMode, emptyUserAnswers, Some(1)) mustBe
            routes.AddNetTaxInterestController.onPageLoad(1, NormalMode)
        }
      }

      "go from the AddNetTaxInterestPage" should {

        "where add net tax interest is yes" should {

          "go to the NetTaxInterestIncomeOrExpensePage" in {
            val userAnswers = (for {
              finalUa <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1))
            } yield finalUa).get

            navigator.nextPage(AddNetTaxInterestPage, NormalMode, userAnswers, Some(1)) mustBe
              routes.NetTaxInterestIncomeOrExpenseController.onPageLoad(1, NormalMode)
          }

        }

        "where add net tax interest is no" should {

          "where reactivations are yes" should {

            "go to the AddAnReactivationsPage" in {
              val userAnswers = (for {
                re <- emptyUserAnswers.set(GroupSubjectToReactivationsPage, true)
                finalUa <- re.set(
                  UkCompaniesPage,
                  ukCompanyModelReactivationMaxIncome.copy(
                    addNetTaxInterest = Some(false),
                    netTaxInterestIncomeOrExpense = None
                  ),
                  Some(1)
                )
              } yield finalUa).get

              navigator.nextPage(AddNetTaxInterestPage, NormalMode, userAnswers, Some(1)) mustBe
                routes.AddAnReactivationQueryController.onPageLoad(1, NormalMode)
            }

          }

          "where reactivations are no" should {

            "go to the ContainsEstimatesPage" in {
              val userAnswers = (for {
                re <- emptyUserAnswers.set(GroupSubjectToReactivationsPage, false)
                finalUa <- re.set(
                  UkCompaniesPage,
                  ukCompanyModelReactivationMaxIncome.copy(
                    addNetTaxInterest = Some(false),
                    netTaxInterestIncomeOrExpense = None
                  ),
                  Some(1)
                )
              } yield finalUa).get

              navigator.nextPage(AddNetTaxInterestPage, NormalMode, userAnswers, Some(1)) mustBe
                routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
            }

          }

          "where restrictions are yes" should {

            "go to the ContainsEstimatesPage" in {
              val userAnswers = (for {
                re <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, true)
                finalUa <- re.set(
                  UkCompaniesPage,
                  ukCompanyModelReactivationMaxIncome.copy(
                    addNetTaxInterest = Some(false),
                    netTaxInterestIncomeOrExpense = None
                  ),
                  Some(1)
                )
              } yield finalUa).get

              navigator.nextPage(AddNetTaxInterestPage, NormalMode, userAnswers, Some(1)) mustBe
                routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
            }
          }

          "where restrictions and reactivations are both no" should {

            "go to the ContainsEstimatesPage" in {
              val userAnswers = (for {
                re <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
                ree <- re.set(GroupSubjectToReactivationsPage, false)
                finalUa <- ree.set(
                  UkCompaniesPage,
                  ukCompanyModelReactivationMaxIncome.copy(
                    addNetTaxInterest = Some(false),
                    netTaxInterestIncomeOrExpense = None
                  ),
                  Some(1)
                )
              } yield finalUa).get

              navigator.nextPage(AddNetTaxInterestPage, NormalMode, userAnswers, Some(1)) mustBe
                routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
            }

          }

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
            ua <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, true)
            finalUa <- ua.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
        }

        "where interest is income and group is subject to reactivations, go to the AddAnReactivationQueryPage" in {
          val userAnswers = (for {
            ua <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
            ua2 <- ua.set(GroupSubjectToReactivationsPage, true)
            finalUa <- ua2.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.AddAnReactivationQueryController.onPageLoad(1, NormalMode)
        }

        "where interest is income and group is NOT subject to restrictions or reactivations, go to the CompanyContainsEstimatesPage" in {
          val userAnswers = (for {
            ua <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
            ua2 <- ua.set(GroupSubjectToReactivationsPage, false)
            finalUa <- ua2.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
        }

        "where interest is expense and group is subject to restrictions, go to the AddRestrictionPage" in {
          val userAnswers = (for {
            ua <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, true)
            finalUa <- ua.set(UkCompaniesPage, ukCompanyModelReactivationMaxExpense, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.AddRestrictionController.onPageLoad(1, NormalMode)
        }

        "where interest is expense and group is subject to reactivations, go to the AddAnReactivationQueryPage" in {
          val userAnswers = (for {
            ua <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
            ua2 <- ua.set(GroupSubjectToReactivationsPage, true)
            finalUa <- ua2.set(UkCompaniesPage, ukCompanyModelReactivationMaxExpense, Some(1))
          } yield finalUa).get

          navigator.nextPage(NetTaxInterestAmountPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.AddAnReactivationQueryController.onPageLoad(1, NormalMode)
        }

        "where interest is expense and group is NOT subject to restrictions or reactivations, go to the CompanyContainsEstimatesPage" in {
          val userAnswers = (for {
            ua <- emptyUserAnswers.set(GroupSubjectToRestrictionsPage, false)
            ua2 <- ua.set(GroupSubjectToReactivationsPage, false)
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

        "CompanyQICElectionController when multiple subject to restrictions are false" in {

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
            controllers.checkTotals.routes.DerivedCompanyController.onPageLoad()
        }
      }

      "from the AddRestrictionPage" should {
        "go to the CompanyAccountingPeriodSameAsGroup when set to true" in {
          val company = ukCompanyModelMax.copy(restriction = Some(true))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).success.value

          navigator.nextPage(AddRestrictionPage, NormalMode, userAnswers) mustBe
            controllers.ukCompanies.routes.CompanyAccountingPeriodSameAsGroupController.onPageLoad(1, NormalMode)
        }

        "go to the CompanyContainsEstimatesPage when set to false" in {
          val company = ukCompanyModelMax.copy(restriction = Some(false))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).success.value

          navigator.nextPage(AddRestrictionPage, NormalMode, userAnswers) mustBe
            routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
        }
      }

      "from the CompanyAccountingPeriodSameAsGroupPage" should {

        "go to the RestrictionAmountSameAPController when true" in {
          val company = ukCompanyModelMax.copy(accountPeriodSameAsGroup = Some(true))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).success.value

          navigator.nextPage(CompanyAccountingPeriodSameAsGroupPage, NormalMode, userAnswers) mustBe
            controllers.ukCompanies.routes.RestrictionAmountSameAPController.onPageLoad(1, NormalMode)
        }

        "go to the first CompanyAccountingPeriodEndDate page when false" in {
          val company = ukCompanyModelMax.copy(accountPeriodSameAsGroup = Some(false))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).success.value

          //TODO update to correct route in 1350
          navigator.nextPage(CompanyAccountingPeriodSameAsGroupPage, NormalMode, userAnswers) mustBe
            routes.CompanyAccountingPeriodEndDateController.onPageLoad(1, 1, NormalMode)
        }
      }

      "from the RestrictionAmountSameAPPage" should {
        "go to CompanyContainsEstimatesController" in {
          navigator.nextPage(RestrictionAmountSameAPPage, NormalMode, emptyUserAnswers) mustBe
            controllers.ukCompanies.routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
        }
      }

      "from the RestrictionAmountForAccountingPeriodPage" should {
        "go to CheckRestrictionController" in {
          navigator.nextRestrictionPage(RestrictionAmountForAccountingPeriodPage(1, 1), NormalMode, emptyUserAnswers) mustBe
            controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1, 1)
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

      "from the CompanyQICElectionPage" should {
        "go to the EnterCompanyTaxEBITDAController when true & in full return" in {
          val userAnswers = emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).success.value
            .set(CompanyQICElectionPage, true).success.value

          navigator.nextPage(CompanyQICElectionPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.EnterCompanyTaxEBITDAController.onPageLoad(1, NormalMode)
        }

        "go to the EnterCompanyTaxEBITDAController when false & in full return" in {
          val userAnswers = emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).success.value
            .set(CompanyQICElectionPage, false).success.value

          navigator.nextPage(CompanyQICElectionPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.EnterCompanyTaxEBITDAController.onPageLoad(1, NormalMode)
        }

        "go to the CheckAnswersUkCompanyController when true & in abbreviated return" in {
          val userAnswers = emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Abbreviated).success.value
            .set(CompanyQICElectionPage, true).success.value

          navigator.nextPage(CompanyQICElectionPage, NormalMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }

        "go to the CheckAnswersUkCompanyController when false & in abbreviated return" in {
          val userAnswers = emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Abbreviated).success.value
            .set(CompanyQICElectionPage, false).success.value

          navigator.nextPage(CompanyQICElectionPage, NormalMode, userAnswers, Some(1)) mustBe
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

      "from AddNetTaxInterestPage" should {
        "if true route to NetTaxInterestIncomeOrExpenseController" in {

          val model = ukCompanyModelReactivationTrue.copy(addNetTaxInterest = Some(true))
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, model, Some(1)).success.value
          navigator.nextPage(AddNetTaxInterestPage, CheckMode, userAnswers, Some(1)) mustBe
            routes.NetTaxInterestIncomeOrExpenseController.onPageLoad(1, NormalMode)
        }

        "if false route to UkCheckYourAnswers" in {
          val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationFalse, Some(1)).success.value
          navigator.nextPage(AddNetTaxInterestPage, CheckMode, userAnswers, Some(1)) mustBe
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

      "from the CompanyQICElectionPage" should {
        "go to the CheckAnswersUkCompanyController when false" in {
          val userAnswers = emptyUserAnswers.set(CompanyQICElectionPage, false).success.value

          navigator.nextPage(CompanyQICElectionPage, CheckMode, userAnswers, Some(1)) mustBe
            routes.CheckAnswersUkCompanyController.onPageLoad(1)
        }

        "go to the CheckAnswersUkCompanyController when true" in {
          val userAnswers = emptyUserAnswers.set(CompanyQICElectionPage, true).success.value

          navigator.nextPage(CompanyQICElectionPage, CheckMode, userAnswers, Some(1)) mustBe
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

      ".addCompany(numOfCompanies: Int)" must {

        "return a call to add the next UK Company by going to the Company Details page for the next IDX" in {
          navigator.addCompany(1) mustBe routes.CompanyDetailsController.onPageLoad(2, NormalMode)
        }
      }
    }
  }
  "nextRestrictionPage" must {

    "in Normal mode" must {

      "for AddRestrictionAmountPage" when {

        "set to true" must {
          "Navigate to the RestrictionAmountForAccountingPeriodPage" in {
            val page = AddRestrictionAmountPage(1, 1)
            val userAnswers = emptyUserAnswers.set(page, true).get
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.RestrictionAmountForAccountingPeriodController.onPageLoad(1, 1, NormalMode)
          }
        }

        "set to false" must {
          "Navigate to the CheckRestrictionPage" in {
            val page = AddRestrictionAmountPage(1, 1)
            val userAnswers = emptyUserAnswers.set(page, false).get
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.CheckRestrictionController.onPageLoad(1, 1)
          }
        }

      }

      "for AddAnotherAccountingPeriodPage" when {

        val ap1EndDate = LocalDate.of(2017, 3, 1)
        val ap2EndDate = LocalDate.of(2017, 9, 1)

        "is set to true and only one accounting period has been added" must {
          "Navigate to the second end date page" in {

            val page = AddAnotherAccountingPeriodPage(1)

            val userAnswers = (for {
              ua  <- emptyUserAnswers.set(page, true)
              ua2 <- ua.set(CompanyAccountingPeriodEndDatePage(1,1), ap1EndDate)
            } yield ua2).get
            
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.CompanyAccountingPeriodEndDateController.onPageLoad(1, 2, NormalMode)
          }
        }

        "is set to true and two accounting periods have been added" must {
          "Navigate to the second end date page" in {

            val page = AddAnotherAccountingPeriodPage(1)

            val userAnswers = (for {
              ua  <- emptyUserAnswers.set(page, true)
              ua2 <- ua.set(CompanyAccountingPeriodEndDatePage(1,1), ap1EndDate)
              ua3 <- ua2.set(CompanyAccountingPeriodEndDatePage(1,2), ap2EndDate)
            } yield ua3).get
            
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.CompanyAccountingPeriodEndDateController.onPageLoad(1, 3, NormalMode)
          }
        }

        "is set to false" must {
          "Navigate to the company contains estimates page" in {

            val page = AddAnotherAccountingPeriodPage(1)

            val userAnswers = (for {
              ua  <- emptyUserAnswers.set(page, false)
              ua2 <- ua.set(CompanyAccountingPeriodEndDatePage(1,1), ap1EndDate)
              ua3 <- ua2.set(CompanyAccountingPeriodEndDatePage(1,2), ap2EndDate)
            } yield ua2).get
            
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
          }
        }

      }

      "for RestrictionDeletionConfirmationPage" must {

        "Navigate to the ReviewCompanyRestrictionsPage" in {
          val page = RestrictionDeletionConfirmationPage(1, 1)
          navigator.nextRestrictionPage(page, NormalMode, emptyUserAnswers) mustBe
            routes.ReviewCompanyRestrictionsController.onPageLoad(1)
        }

      }

      "for CheckRestrictionPage" must {

        "Navigate to the ReviewCompanyRestrictionsPage" in {
          val page = CheckRestrictionPage(1, 1)
          navigator.nextRestrictionPage(page, NormalMode, emptyUserAnswers) mustBe
            routes.ReviewCompanyRestrictionsController.onPageLoad(1)
        }

      }

      "for CompanyAccountingPeriodEndDatePage" must {

        "Navigate to the AddRestrictionAmountPage" in {
          val page = CompanyAccountingPeriodEndDatePage(1, 1)
          navigator.nextRestrictionPage(page, NormalMode, emptyUserAnswers) mustBe
            routes.AddRestrictionAmountController.onPageLoad(1, 1, NormalMode)
        }

      }
      
      "for ReviewCompanyRestrictionsPage" when {

        val poaStartDate = LocalDate.of(2020,1,1)
        val poaEndDate = LocalDate.of(2021,1,1)
        val poa = AccountingPeriodModel(poaStartDate, poaEndDate)

        "A single restriction has been added, and the end date doesn't exceed the group PoA" must {
          "Navigate to the AddAnotherAccountingPeriod page" in {

            val userAnswers = (for {
              ua      <- emptyUserAnswers.set(AccountingPeriodPage, poa)
              ua2     <- ua.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2020,2,1))
            } yield ua2).get

            val page = ReviewCompanyRestrictionsPage(1)
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.AddAnotherAccountingPeriodController.onPageLoad(1, NormalMode)
          }
        }

        "Two restrictions have been added, and the end dates don't exceed the group PoA" must {
          "Navigate to the AddAnotherAccountingPeriod page" in {

            val userAnswers = (for {
              ua      <- emptyUserAnswers.set(AccountingPeriodPage, poa)
              ua2     <- ua.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2020,2,1))
              ua3     <- ua2.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.of(2020,3,1))
            } yield ua3).get

            val page = ReviewCompanyRestrictionsPage(1)
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.AddAnotherAccountingPeriodController.onPageLoad(1, NormalMode)
          }
        }

        "Three restrictions have been added, and the end dates don't exceed the group PoA" must {
          "Navigate to the CompanyContainsEstimates page" in {

            val userAnswers = (for {
              ua      <- emptyUserAnswers.set(AccountingPeriodPage, poa)
              ua2     <- ua.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2020,2,1))
              ua3     <- ua2.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.of(2020,3,1))
              ua4     <- ua3.set(CompanyAccountingPeriodEndDatePage(1, 3), LocalDate.of(2020,4,1))
            } yield ua4).get

            val page = ReviewCompanyRestrictionsPage(1)
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
          }
        }

        "One restrictions has been added, and the end date exceeds the group PoA" must {
          "Navigate to the CompanyContainsEstimates page" in {

            val userAnswers = (for {
              ua      <- emptyUserAnswers.set(AccountingPeriodPage, poa)
              ua2     <- ua.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2021,2,1))
            } yield ua2).get

            val page = ReviewCompanyRestrictionsPage(1)
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
          }
        }

        "Two restrictions have been added, and the end date exceeds the group PoA" must {
          "Navigate to the CompanyContainsEstimates page" in {

            val userAnswers = (for {
              ua      <- emptyUserAnswers.set(AccountingPeriodPage, poa)
              ua2     <- ua.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2020,2,1))
              ua3     <- ua2.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.of(2021,2,1))
            } yield ua3).get

            val page = ReviewCompanyRestrictionsPage(1)
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
          }
        }

        "Three restrictions have been added, and the end date exceeds the group PoA" must {
          "Navigate to the CompanyContainsEstimates page" in {

            val userAnswers = (for {
              ua      <- emptyUserAnswers.set(AccountingPeriodPage, poa)
              ua2     <- ua.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.of(2020,2,1))
              ua3     <- ua2.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.of(2020,6,1))
              ua4     <- ua3.set(CompanyAccountingPeriodEndDatePage(1, 3), LocalDate.of(2021,2,1))
            } yield ua4).get

            val page = ReviewCompanyRestrictionsPage(1)
            navigator.nextRestrictionPage(page, NormalMode, userAnswers) mustBe
              routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode)
          }
        }

      }

    }

    "in Check mode" must {

      "for AddRestrictionAmountPage" must {

        "Navigate to the UnderConstructionController" in {
          val page = AddRestrictionAmountPage(1, 1)
          navigator.nextRestrictionPage(page, CheckMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }

      }

      "for AddAnotherAccountingPeriodPage" must {

        "Navigate to the UnderConstructionController" in {
          val page = AddAnotherAccountingPeriodPage(1)
          navigator.nextRestrictionPage(page, CheckMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }

      }

    }

  }
}
