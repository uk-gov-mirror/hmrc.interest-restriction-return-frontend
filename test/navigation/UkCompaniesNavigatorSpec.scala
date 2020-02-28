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
import models._
import controllers.ukCompanies.routes
import pages.ukCompanies._

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

        "Under Construction page" in {

          navigator.nextPage(ConsentingCompanyPage, NormalMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }
      }
    }

    "in Check mode" must {

      //TODO: Update as part of future CYA story
      "go to Under Construction" in {
        navigator.nextPage(EnterCompanyTaxEBITDAPage, CheckMode, emptyUserAnswers) mustBe
          controllers.routes.UnderConstructionController.onPageLoad()
      }
    }
  }
}
