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
import pages.ukCompanies.{EnterCompanyTaxEBITDAPage, UkCompaniesDeletionConfirmationPage, _}

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

        "Check your answers page" in {

          navigator.nextPage(ConsentingCompanyPage, NormalMode, emptyUserAnswers) mustBe
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
    }

    "in Check mode" must {

      "go to Under Construction" in {
        navigator.nextPage(CheckAnswersUkCompanyPage, CheckMode, emptyUserAnswers) mustBe
          routes.CheckAnswersUkCompanyController.onPageLoad(1)
      }
    }

    ".addCompany(numOfCompanies: Int)" must {

      "return a call to add the next UK Company by going to the Company Details page for the next IDX" in {
        navigator.addCompany(1) mustBe routes.CompanyDetailsController.onPageLoad(2, NormalMode)
      }
    }
  }
}