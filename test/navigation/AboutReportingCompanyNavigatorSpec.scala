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
import controllers.groupStructure.{routes => groupStructureRoutes}
import models._
import pages.aboutReportingCompany.{CheckAnswersReportingCompanyPage, ReportingCompanyCTUTRPage, ReportingCompanyNamePage}

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

      "from the ReportingCompanyCTUTRPage" should {

        "go to the ReportingCompanyCRNPage" in {
          navigator.nextPage(ReportingCompanyCTUTRPage, NormalMode, emptyUserAnswers) mustBe
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

      "go to Reporting Company CheckYourAnswers" in {
        navigator.nextPage(ReportingCompanyCTUTRPage, CheckMode, emptyUserAnswers) mustBe
          aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
      }
    }
  }
}
