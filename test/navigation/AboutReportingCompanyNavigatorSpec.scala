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
import controllers.aboutReportingCompany.{routes => aboutReportingCompanyRoutes}
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import controllers.groupStructure.{routes => groupStructureRoutes}
import models._
import pages.aboutReportingCompany._
import pages.aboutReturn.RevisingReturnPage

class AboutReportingCompanyNavigatorSpec extends SpecBase {

  val navigator = new AboutReportingCompanyNavigator

  "AboutReportingCompanyNavigator" when {

    "in Normal mode" must {

      "from the ReportingCompanyNamePage" should {

        "go to the ReportingCompanyCTUTRPage" in {
          navigator.nextPage(ReportingCompanyNamePage, NormalMode, emptyUserAnswers) mustBe
            aboutReportingCompanyRoutes.ReportingCompanyCTUTRController.onPageLoad(NormalMode)
        }
      }

      "from the Revising Return page" should {

        "go to the Revision Information page when yes selected to revising a return" in {

          val userAnswers = emptyUserAnswers.set(RevisingReturnPage, true).get

          navigator.nextPage(RevisingReturnPage, NormalMode, userAnswers) mustBe controllers.routes.UnderConstructionController.onPageLoad()
        }

        "go to the Reporting company name page when no selected to revising a return" in {

          val userAnswers = emptyUserAnswers.set(RevisingReturnPage, false).get

          navigator.nextPage(RevisingReturnPage, NormalMode, userAnswers) mustBe
            aboutReportingCompanyRoutes.ReportingCompanyNameController.onPageLoad(NormalMode)
        }
      }

      "from the ReportingCompanyCTUTRPage" should {

        "go to the AccountingPeriodStartPage" in {
          navigator.nextPage(ReportingCompanyCTUTRPage, NormalMode, emptyUserAnswers) mustBe
            aboutReportingCompanyRoutes.AccountingPeriodStartController.onPageLoad(NormalMode)
        }
      }

      "from the AccountingPeriodStartPage" should {

        "go to the AccountingPeriodEndPage" in {
          navigator.nextPage(AccountingPeriodStartPage, NormalMode, emptyUserAnswers) mustBe
            aboutReportingCompanyRoutes.AccountingPeriodEndController.onPageLoad(NormalMode)
        }
      }

      "from the AccountingPeriodEndPage" should {

        "go to the CheckAnswersReportingCompany" in {
          navigator.nextPage(AccountingPeriodEndPage, NormalMode, emptyUserAnswers) mustBe
            aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
        }
      }

      "from the CheckAnswersReportingCompanyPage" should {

        "go to the next section" in {
          navigator.nextPage(CheckAnswersReportingCompanyPage, NormalMode, emptyUserAnswers) mustBe
            groupStructureRoutes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
        }
      }
    }

    "in Check mode" must {

      "from ReportingCompanyCTUTRPage go to Reporting Company CheckYourAnswers " in {
        navigator.nextPage(ReportingCompanyCTUTRPage, CheckMode, emptyUserAnswers) mustBe
          aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
      }

      "from AccountingPeriodStartPage go to Reporting Company CheckYourAnswers" in {
        navigator.nextPage(AccountingPeriodStartPage, CheckMode, emptyUserAnswers) mustBe
          aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
      }

      "from AccountingPeriodEndPage go to Reporting Company CheckYourAnswers" in {
        navigator.nextPage(AccountingPeriodEndPage, CheckMode, emptyUserAnswers) mustBe
          aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
      }

      "from the Revising Return page" should {

        "go to the Revision Information page when yes selected to revising a return" in {

          val userAnswers = emptyUserAnswers.set(RevisingReturnPage, true).get

          navigator.nextPage(RevisingReturnPage, CheckMode, userAnswers) mustBe controllers.routes.UnderConstructionController.onPageLoad()
        }

        "go to Reporting Company CheckYourAnswers" in {

          val userAnswers = emptyUserAnswers.set(RevisingReturnPage, false).get

          navigator.nextPage(RevisingReturnPage, CheckMode, userAnswers) mustBe
            aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
        }
      }
    }
  }
}
