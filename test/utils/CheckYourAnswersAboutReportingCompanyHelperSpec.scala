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

package utils

import assets.constants.BaseConstants
import assets.messages.BaseMessages
import base.SpecBase
import controllers.aboutReportingCompany.{routes => aboutReportingCompanyRoutes}
import controllers.startReturn.{routes => startReturnRoutes}
import models.FullOrAbbreviatedReturn.Full
import models.{CheckMode, UserAnswers}
import pages.aboutReportingCompany._
import pages.startReturn.{AgentActingOnBehalfOfCompanyPage, AgentNamePage, FullOrAbbreviatedReturnPage, ReportingCompanyAppointedPage}
import viewmodels.SummaryListRowHelper

class CheckYourAnswersAboutReportingCompanyHelperSpec extends SpecBase with BaseConstants with SummaryListRowHelper {

  val helper = new CheckYourAnswersAboutReportingCompanyHelper(
    UserAnswers("id")
      .set(ReportingCompanyAppointedPage, true).get
      .set(AgentActingOnBehalfOfCompanyPage, true).get
      .set(AgentNamePage, agentName).get
      .set(FullOrAbbreviatedReturnPage, Full).get
      .set(ReportingCompanyNamePage, companyNameModel.name).get
      .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
      .set(ReportingCompanyCRNPage, crnModel.crn).get
  )

  "Check Your Answers Helper" must {

    "For the ReportingCompanyAppointed answer" must {

      "have a correctly formatted summary list row" in {

        helper.reportingCompanyAppointed mustBe Some(summaryListRow(
          messages("reportingCompanyAppointed.checkYourAnswersLabel"),
          "Yes",
          startReturnRoutes.ReportingCompanyAppointedController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the AgentActingOnBehalfOfCompany answer" must {

      "have a correctly formatted summary list row" in {

        helper.agentActingOnBehalfOfCompany mustBe Some(summaryListRow(
          messages("agentActingOnBehalfOfCompany.checkYourAnswersLabel"),
          "Yes",
          startReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the AgentName answer" must {

      "have a correctly formatted summary list row" in {

        helper.agentName mustBe Some(summaryListRow(
          messages("agentName.checkYourAnswersLabel"),
          agentName,
          startReturnRoutes.AgentNameController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the FullOrAbbreviatedReturn answer" must {

      "have a correctly formatted summary list row" in {

        helper.fullOrAbbreviatedReturn mustBe Some(summaryListRow(
          messages("fullOrAbbreviatedReturn.checkYourAnswersLabel"),
          messages("fullOrAbbreviatedReturn.full"),
          startReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ReportingCompanyName" must {

      "get an answer from useranswers for true" in {

        helper.reportingCompanyName mustBe Some(summaryListRow(
          messages("reportingCompanyName.checkYourAnswersLabel"),
          companyNameModel.name,
          aboutReportingCompanyRoutes.ReportingCompanyNameController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ReportingCompanyCTUTR" must {

      "get an answer from useranswers for true" in {

        helper.reportingCompanyCTUTR mustBe Some(summaryListRow(
          messages("reportingCompanyCTUTR.checkYourAnswersLabel"),
          ctutrModel.utr,
          aboutReportingCompanyRoutes.ReportingCompanyCTUTRController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ReportingCompanyCRN" must {

      "get an answer from useranswers for true" in {

        helper.reportingCompanyCRN mustBe Some(summaryListRow(
          messages("reportingCompanyCRN.checkYourAnswersLabel"),
          crnModel.crn,
          aboutReportingCompanyRoutes.ReportingCompanyCRNController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }
  }
}
