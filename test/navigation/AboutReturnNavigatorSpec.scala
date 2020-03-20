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
import controllers.groupLevelInformation.{routes => groupLevelInformationRoutes}
import controllers.ultimateParentCompany.{routes => ultimateParentCompanyRoutes}
import models._
import pages.Page
import pages.aboutReturn._
import pages.groupLevelInformation.RevisingReturnPage

class AboutReturnNavigatorSpec extends SpecBase {

  val navigator = new AboutReturnNavigator


  "AboutReturnNavigator" when {

    "in Normal mode" must {

      "from the IndexPage" should {

        "go to the ReportingCompanyAppointedPage" in {
          navigator.nextPage(ReportingCompanyAppointedPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.ReportingCompanyAppointedController.onPageLoad(NormalMode)
        }
      }

      "from the HasReportingBeenAppointedPage" should {

        "go to the AgentActingOnBehalfOfCompanyPage when answer is true" in {

          val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, true).get
          navigator.nextPage(ReportingCompanyAppointedPage, NormalMode, userAnswers) mustBe
            aboutReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode)
        }

        "go to the ReportingCompanyRequiredPage when answer is false" in {

          val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, false).get
          navigator.nextPage(ReportingCompanyAppointedPage, NormalMode, userAnswers) mustBe
            aboutReturnRoutes.ReportingCompanyRequiredController.onPageLoad
        }

        "go to the HasReportingBeenAppointedPage when answer is not set" in {
          navigator.nextPage(ReportingCompanyAppointedPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.ReportingCompanyAppointedController.onPageLoad(NormalMode)
        }
      }

      "from the AgentActingOnBehalfOfCompanyPage" should {

        "go to the AgentNamePage when answer is true" in {

          val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, true).get
          navigator.nextPage(AgentActingOnBehalfOfCompanyPage, NormalMode, userAnswers) mustBe
            aboutReturnRoutes.AgentNameController.onPageLoad(NormalMode)
        }

        "go to the FullOrAbbreviatedReturnPage when answer is false" in {

          val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, false).get
          navigator.nextPage(AgentActingOnBehalfOfCompanyPage, NormalMode, userAnswers) mustBe
            aboutReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(NormalMode)
        }

        "go to the AgentActingOnBehalfOfCompanyPage when answer is not set" in {
          navigator.nextPage(AgentActingOnBehalfOfCompanyPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode)
        }
      }

      "from the AgentNamePage" should {

        "go to the FullOrAbbreviatedReturnPage when answer is true" in {
          navigator.nextPage(AgentNamePage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(NormalMode)
        }
      }

      "from the FullOrAbbreviatedReturnPage" should {

        "go to the ReviseReturn when answer is true" in {
          navigator.nextPage(FullOrAbbreviatedReturnPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.RevisingReturnController.onPageLoad(NormalMode)
        }
      }
    }

    "in Check mode" must {

      "from the Reporting Company appointed page" when {

        "answer is set to false" should {

          "go to the Reporting Company required page in Normal Mode when answer is false" in {

            val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, false).get

            navigator.nextPage(ReportingCompanyAppointedPage, CheckMode, userAnswers) mustBe
              aboutReturnRoutes.ReportingCompanyRequiredController.onPageLoad()
          }
        }

        "answer is set to true" should {

          "go to Agent Acting on behalf of Company (in Normal Mode)" in {

            val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, true).get

            navigator.nextPage(ReportingCompanyAppointedPage, CheckMode, userAnswers) mustBe
              aboutReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode)
          }
        }
      }

      "from the Agent Acting on behalf of Company page" when {

        "answer is set to false" should {

          "go to the Check Your Answers page" in {

            val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, false).get

            navigator.nextPage(AgentActingOnBehalfOfCompanyPage, CheckMode, userAnswers) mustBe
              aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
          }
        }

        "answer is set to true" should {

          "go to the Agent Name page" in {

            val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, true).get

            navigator.nextPage(AgentActingOnBehalfOfCompanyPage, CheckMode, userAnswers) mustBe
              aboutReturnRoutes.AgentNameController.onPageLoad(CheckMode)
          }
        }
      }

      "from the Agent Name page" should {

        "go to the Check Your Answers page" in {
          navigator.nextPage(AgentNamePage, CheckMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
        }
      }

      "from the Reporting Company UTR page" should {

        "go to the Check Your Answers page" in {
          navigator.nextPage(ReportingCompanyCTUTRPage, CheckMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
        }
      }

      "from the Reporting Company Name page" should {

        "go to the Check Your Answers page" in {
          navigator.nextPage(ReportingCompanyNamePage, CheckMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
        }
      }


      "go to CheckYourAnswers from a page that doesn't exist in the edit route map" ignore {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, emptyUserAnswers) mustBe ??? //TODO: Update as part of future Check Answers Story
      }
    }
  }



  "AboutReturnNavigator" when {

    "in Normal mode" must {

      "from the ReportingCompanyNamePage" should {

        "go to the ReportingCompanyCTUTRPage" in {
          navigator.nextPage(ReportingCompanyNamePage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.ReportingCompanyCTUTRController.onPageLoad(NormalMode)
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
            aboutReturnRoutes.ReportingCompanyNameController.onPageLoad(NormalMode)
        }
      }

      "from the ReportingCompanyCTUTRPage" should {

        "go to the AccountingPeriodStartPage" in {
          navigator.nextPage(ReportingCompanyCTUTRPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.AccountingPeriodStartController.onPageLoad(NormalMode)
        }
      }

      "from the AccountingPeriodStartPage" should {

        "go to the AccountingPeriodEndPage" in {
          navigator.nextPage(AccountingPeriodStartPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.AccountingPeriodEndController.onPageLoad(NormalMode)
        }
      }

      "from the AccountingPeriodEndPage" should {

        "go to the CheckAnswersReportingCompany" in {
          navigator.nextPage(AccountingPeriodEndPage, NormalMode, emptyUserAnswers) mustBe
            aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
        }
      }

      "from the CheckAnswersReportingCompanyPage" should {

        "go to the next section" in {
          navigator.nextPage(CheckAnswersReportingCompanyPage, NormalMode, emptyUserAnswers) mustBe
            ultimateParentCompanyRoutes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
        }
      }
    }

    "in Check mode" must {

      "from ReportingCompanyCTUTRPage go to Reporting Company CheckYourAnswers " in {
        navigator.nextPage(ReportingCompanyCTUTRPage, CheckMode, emptyUserAnswers) mustBe
          aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
      }

      "from AccountingPeriodStartPage go to Reporting Company CheckYourAnswers" in {
        navigator.nextPage(AccountingPeriodStartPage, CheckMode, emptyUserAnswers) mustBe
          aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
      }

      "from AccountingPeriodEndPage go to Reporting Company CheckYourAnswers" in {
        navigator.nextPage(AccountingPeriodEndPage, CheckMode, emptyUserAnswers) mustBe
          aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
      }

      "from the Revising Return page" should {

        "go to the Revision Information page when yes selected to revising a return" in {

          val userAnswers = emptyUserAnswers.set(RevisingReturnPage, true).get

          navigator.nextPage(RevisingReturnPage, CheckMode, userAnswers) mustBe controllers.routes.UnderConstructionController.onPageLoad()
        }

        "go to Reporting Company CheckYourAnswers" in {

          val userAnswers = emptyUserAnswers.set(RevisingReturnPage, false).get

          navigator.nextPage(RevisingReturnPage, CheckMode, userAnswers) mustBe
            aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
        }
      }
    }
  }
}
