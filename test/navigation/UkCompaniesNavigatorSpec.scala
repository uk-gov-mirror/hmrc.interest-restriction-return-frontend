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
import pages.ukCompanies.{EnterCompanyTaxEBITDAPage, UkCompaniesDeletionConfirmationPage, UkCompaniesPage}

class UkCompaniesNavigatorSpec extends SpecBase {

  val navigator = new UkCompaniesNavigator

  "UkCompaniesNavigator" when {

    "in Normal mode" must {

      "from the EnterCompanyTaxEBITDAPage" should {

        //TODO: Update a part of routing sub-task
        "go to the NetTaxInterestIncomeOrExpense page" in {
          navigator.nextPage(EnterCompanyTaxEBITDAPage, NormalMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }
      }

      "from the UkCompanies List View Page" should {

        //TODO: Update a part of routing sub-task
        "go to the Under Construction page" in {
          navigator.nextPage(UkCompaniesPage, NormalMode, emptyUserAnswers) mustBe
            controllers.routes.UnderConstructionController.onPageLoad()
        }
      }

      "from the UkCompanies Deletion Confirmation Page" should {

        "go to the UkCompanies List View page" in {
          navigator.nextPage(UkCompaniesDeletionConfirmationPage, NormalMode, emptyUserAnswers) mustBe
            routes.UkCompaniesReviewAnswersListController.onPageLoad()
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

    ".addCompany(numOfCompanies: Int)" must {

      "return a call to add the next UK Company by going to the Company Details page for the next IDX" in {
        navigator.addCompany(1) mustBe routes.CompanyDetailsController.onPageLoad(2, NormalMode)
      }
    }
  }
}
