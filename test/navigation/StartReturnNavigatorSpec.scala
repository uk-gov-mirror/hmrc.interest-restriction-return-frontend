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
import controllers.startReturn.{routes => startReturnRoutes}
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import models._
import pages._
import pages.aboutReportingCompany.{ReportingCompanyCTUTRPage, ReportingCompanyNamePage}
import pages.startReturn.{AgentActingOnBehalfOfCompanyPage, AgentNamePage, FullOrAbbreviatedReturnPage, ReportingCompanyAppointedPage}

class StartReturnNavigatorSpec extends SpecBase {

  val navigator = new StartReturnNavigator

  "StartReturnNavigator" when {

    "in Normal mode" must {

      "from the IndexPage" should {

        "go to the ReportingCompanyAppointedPage" in {
          navigator.nextPage(ReportingCompanyAppointedPage, NormalMode, emptyUserAnswers) mustBe
            startReturnRoutes.ReportingCompanyAppointedController.onPageLoad(NormalMode)
        }
      }

      "from the HasReportingBeenAppointedPage" should {

        "go to the AgentActingOnBehalfOfCompanyPage when answer is true" in {

          val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, true).get
          navigator.nextPage(ReportingCompanyAppointedPage, NormalMode, userAnswers) mustBe
            startReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode)
        }

        "go to the ReportingCompanyRequiredPage when answer is false" in {

          val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, false).get
          navigator.nextPage(ReportingCompanyAppointedPage, NormalMode, userAnswers) mustBe
            startReturnRoutes.ReportingCompanyRequiredController.onPageLoad
        }

        "go to the HasReportingBeenAppointedPage when answer is not set" in {
          navigator.nextPage(ReportingCompanyAppointedPage, NormalMode, emptyUserAnswers) mustBe
            startReturnRoutes.ReportingCompanyAppointedController.onPageLoad(NormalMode)
        }
      }

      "from the AgentActingOnBehalfOfCompanyPage" should {

        "go to the AgentNamePage when answer is true" in {

          val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, true).get
          navigator.nextPage(AgentActingOnBehalfOfCompanyPage, NormalMode, userAnswers) mustBe
            startReturnRoutes.AgentNameController.onPageLoad(NormalMode)
        }

        "go to the FullOrAbbreviatedReturnPage when answer is false" in {

          val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, false).get
          navigator.nextPage(AgentActingOnBehalfOfCompanyPage, NormalMode, userAnswers) mustBe
            startReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(NormalMode)
        }

        "go to the AgentActingOnBehalfOfCompanyPage when answer is not set" in {
          navigator.nextPage(AgentActingOnBehalfOfCompanyPage, NormalMode, emptyUserAnswers) mustBe
            startReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode)
        }
      }

      "from the AgentNamePage" should {

        "go to the FullOrAbbreviatedReturnPage when answer is true" in {
          navigator.nextPage(AgentNamePage, NormalMode, emptyUserAnswers) mustBe
            startReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(NormalMode)
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
              startReturnRoutes.ReportingCompanyRequiredController.onPageLoad()
          }
        }

        "answer is set to true" should {

          "go to Agent Acting on behalf of Company (in Normal Mode)" in {

            val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, true).get

            navigator.nextPage(ReportingCompanyAppointedPage, CheckMode, userAnswers) mustBe
              startReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode)
          }
        }
      }

      "from the Agent Acting on behalf of Company page" when {

        "answer is set to false" should {

          "go to the Check Your Answers page" in {

            val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, false).get

            navigator.nextPage(AgentActingOnBehalfOfCompanyPage, CheckMode, userAnswers) mustBe
              aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
          }
        }

        "answer is set to true" should {

          "go to the Agent Name page" in {

            val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, true).get

            navigator.nextPage(AgentActingOnBehalfOfCompanyPage, CheckMode, userAnswers) mustBe
              startReturnRoutes.AgentNameController.onPageLoad(CheckMode)
          }
        }
      }

      "from the Agent Name page" should {

        "go to the Check Your Answers page" in {
          navigator.nextPage(AgentNamePage, CheckMode, emptyUserAnswers) mustBe
            aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
        }
      }

      "from the Reporting Company UTR page" should {

        "go to the Check Your Answers page" in {
          navigator.nextPage(ReportingCompanyCTUTRPage, CheckMode, emptyUserAnswers) mustBe
            aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
        }
      }

      "from the Reporting Company Name page" should {

        "go to the Check Your Answers page" in {
          navigator.nextPage(ReportingCompanyNamePage, CheckMode, emptyUserAnswers) mustBe
            aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
        }
      }


      "go to CheckYourAnswers from a page that doesn't exist in the edit route map" ignore {

        case object UnknownPage extends Page
        navigator.nextPage(UnknownPage, CheckMode, emptyUserAnswers) mustBe ??? //TODO: Update as part of future Check Answers Story
      }
    }
  }
}
