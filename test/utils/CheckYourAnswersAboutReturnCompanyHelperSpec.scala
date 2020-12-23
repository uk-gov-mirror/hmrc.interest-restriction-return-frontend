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

import java.time.LocalDate

import assets.constants.BaseConstants
import assets.messages.{BaseMessages, CheckAnswersAboutReturnMessages}
import base.SpecBase
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import models.FullOrAbbreviatedReturn.Full
import models.{CheckMode, UserAnswers}
import models.returnModels.AccountingPeriodModel
import pages.aboutReturn._
import pages.groupLevelInformation.RevisingReturnPage
import pages.aboutReturn.ReportingCompanyAppointedPage
import viewmodels.SummaryListRowHelper

class CheckYourAnswersAboutReturnCompanyHelperSpec extends SpecBase with BaseConstants with SummaryListRowHelper {

  val date = LocalDate.of(2020,1,1)

  val helper = new CheckYourAnswersAboutReturnCompanyHelper(
    UserAnswers("id")
      .set(ReportingCompanyAppointedPage, true).get
      .set(AgentActingOnBehalfOfCompanyPage, true).get
      .set(AgentNamePage, agentName).get
      .set(FullOrAbbreviatedReturnPage, Full).get
      .set(RevisingReturnPage, true).get
      .set(TellUsWhatHasChangedPage, "Something has changed").get
      .set(ReportingCompanyNamePage, companyNameModel.name).get
      .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
      .set(AccountingPeriodPage, AccountingPeriodModel(date, date.plusMonths(1))).get
  )

  val expectedReportingCompanyAppointed = summaryListRow(
    messages("reportingCompanyAppointed.checkYourAnswersLabel"),
    "Yes",
    aboutReturnRoutes.ReportingCompanyAppointedController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )
  val expectedAgentActingOnBehalfOfCompany = summaryListRow(
    messages("agentActingOnBehalfOfCompany.checkYourAnswersLabel"),
    "Yes",
    aboutReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )
  val expectedAgentName = summaryListRow(
    messages("agentName.checkYourAnswersLabel"),
    agentName,
    aboutReturnRoutes.AgentNameController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )
  val expectedFullOrAbbreviatedReturn = summaryListRow(
    messages("fullOrAbbreviatedReturn.checkYourAnswersLabel"),
    messages("fullOrAbbreviatedReturn.full"),
    aboutReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )
  val expectedRevisingReturn = summaryListRow(
    CheckAnswersAboutReturnMessages.revisingReturn,
    BaseMessages.yes,
    aboutReturnRoutes.RevisingReturnController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )
  val expectedWhatHasChanged = summaryListRow(
    messages("tellUsWhatHasChanged.checkYourAnswersLabel"),
    "Something has changed",
    aboutReturnRoutes.TellUsWhatHasChangedController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )
  val expectedReportingCompanyName = summaryListRow(
    messages("reportingCompanyName.checkYourAnswersLabel"),
    companyNameModel.name,
    aboutReturnRoutes.ReportingCompanyNameController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )
  val expectedReportingCompanyCTUTR = summaryListRow(
    messages("reportingCompanyCTUTR.checkYourAnswersLabel"),
    ctutrModel.utr,
    aboutReturnRoutes.ReportingCompanyCTUTRController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )
  val expectedAccountingPeriodStart = summaryListRow(
    messages("accountingPeriod.start.checkYourAnswersLabel"),
    "1 January 2020",
    aboutReturnRoutes.AccountingPeriodController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )
  val expectedAccountingPeriodEnd = summaryListRow(
    messages("accountingPeriod.end.checkYourAnswersLabel"),
    "1 February 2020",
    aboutReturnRoutes.AccountingPeriodController.onPageLoad(CheckMode) -> BaseMessages.changeLink
  )

  "Check Your Answers Helper" must {

    "For the ReportingCompanyAppointed answer" must {
      "have a correctly formatted summary list row" in {
        helper.reportingCompanyAppointed mustBe Some(expectedReportingCompanyAppointed)
      }
    }

    "For the AgentActingOnBehalfOfCompany answer" must {
      "have a correctly formatted summary list row" in {
        helper.agentActingOnBehalfOfCompany mustBe Some(expectedAgentActingOnBehalfOfCompany)
      }
    }

    "For the AgentName answer" must {
      "have a correctly formatted summary list row" in {
        helper.agentName mustBe Some(expectedAgentName)
      }
    }

    "For the FullOrAbbreviatedReturn answer" must {
      "have a correctly formatted summary list row" in {
        helper.fullOrAbbreviatedReturn mustBe Some(expectedFullOrAbbreviatedReturn)
      }
    }

    "For the RevisingReturn answer" must {
      "have a correctly formatted summary list row" in {
        helper.revisingReturn mustBe Some(expectedRevisingReturn)
      }
    }


    "For the What has changed" must {
      "get an answer from useranswers" in {
        helper.whatHasChanged mustBe Some(expectedWhatHasChanged)
      }
    }


    "For the ReportingCompanyName" must {
      "get an answer from useranswers for true" in {
        helper.reportingCompanyName mustBe Some(expectedReportingCompanyName)
      }
    }

    "For the ReportingCompanyCTUTR" must {
      "get an answer from useranswers for true" in {
        helper.reportingCompanyCTUTR mustBe Some(expectedReportingCompanyCTUTR)
      }
    }

    "For the AP start date" must {
      "get an answer from useranswers for a date" in {
        helper.accountingPeriodStart mustBe Some(expectedAccountingPeriodStart)
      }
    }

    "For the AP end date" must {
      "get an answer from useranswers for a date" in {
        helper.accountingPeriodEnd mustBe Some(expectedAccountingPeriodEnd)
      }
    }

    "return the completed fields from `rows`" in {
      helper.rows mustBe Seq(
        expectedReportingCompanyAppointed,
        expectedAgentActingOnBehalfOfCompany,
        expectedAgentName,
        expectedFullOrAbbreviatedReturn,
        expectedRevisingReturn,
        expectedWhatHasChanged,
        expectedReportingCompanyName,
        expectedReportingCompanyCTUTR,
        expectedAccountingPeriodStart,
        expectedAccountingPeriodEnd
      )
    }

    "not return uncompleted fields from `rows`" in {
      val helper = new CheckYourAnswersAboutReturnCompanyHelper(
        UserAnswers("id")
          .set(ReportingCompanyAppointedPage, true).get
          .set(AgentActingOnBehalfOfCompanyPage, true).get
          .set(AgentNamePage, agentName).get
          .set(FullOrAbbreviatedReturnPage, Full).get
          .set(RevisingReturnPage, false).get
          .set(ReportingCompanyNamePage, companyNameModel.name).get
          .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
          .set(AccountingPeriodPage, AccountingPeriodModel(date, date.plusMonths(1))).get
      )
      helper.rows mustBe Seq(
        expectedReportingCompanyAppointed,
        expectedAgentActingOnBehalfOfCompany,
        expectedAgentName,
        expectedFullOrAbbreviatedReturn,
        summaryListRow(
          CheckAnswersAboutReturnMessages.revisingReturn,
          BaseMessages.no,
          aboutReturnRoutes.RevisingReturnController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ),
        expectedReportingCompanyName,
        expectedReportingCompanyCTUTR,
        expectedAccountingPeriodStart,
        expectedAccountingPeriodEnd
      )
    }

  }
}
