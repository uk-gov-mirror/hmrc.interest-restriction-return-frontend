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
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import models.FullOrAbbreviatedReturn.Full
import models.{CheckMode, UserAnswers}
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
      .set(ReportingCompanyNamePage, companyNameModel.name).get
      .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
      .set(AccountingPeriodStartPage, date).get
      .set(AccountingPeriodEndPage, date.plusMonths(1)).get
  )

  "Check Your Answers Helper" must {

    "For the ReportingCompanyAppointed answer" must {

      "have a correctly formatted summary list row" in {

        helper.reportingCompanyAppointed mustBe Some(summaryListRow(
          messages("reportingCompanyAppointed.checkYourAnswersLabel"),
          "Yes",
          aboutReturnRoutes.ReportingCompanyAppointedController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the AgentActingOnBehalfOfCompany answer" must {

      "have a correctly formatted summary list row" in {

        helper.agentActingOnBehalfOfCompany mustBe Some(summaryListRow(
          messages("agentActingOnBehalfOfCompany.checkYourAnswersLabel"),
          "Yes",
          aboutReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the AgentName answer" must {

      "have a correctly formatted summary list row" in {

        helper.agentName mustBe Some(summaryListRow(
          messages("agentName.checkYourAnswersLabel"),
          agentName,
          aboutReturnRoutes.AgentNameController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the FullOrAbbreviatedReturn answer" must {

      "have a correctly formatted summary list row" in {

        helper.fullOrAbbreviatedReturn mustBe Some(summaryListRow(
          messages("fullOrAbbreviatedReturn.checkYourAnswersLabel"),
          messages("fullOrAbbreviatedReturn.full"),
          aboutReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the RevisingReturn answer" must {

      "have a correctly formatted summary list row" in {

        helper.revisingReturn mustBe Some(summaryListRow(
          CheckAnswersAboutReturnMessages.revisedReturn,
          BaseMessages.yes,
          aboutReturnRoutes.RevisingReturnController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ReportingCompanyName" must {

      "get an answer from useranswers for true" in {

        helper.reportingCompanyName mustBe Some(summaryListRow(
          messages("reportingCompanyName.checkYourAnswersLabel"),
          companyNameModel.name,
          aboutReturnRoutes.ReportingCompanyNameController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the ReportingCompanyCTUTR" must {

      "get an answer from useranswers for true" in {

        helper.reportingCompanyCTUTR mustBe Some(summaryListRow(
          messages("reportingCompanyCTUTR.checkYourAnswersLabel"),
          ctutrModel.utr,
          aboutReturnRoutes.ReportingCompanyCTUTRController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the AP start date" must {

      "get an answer from useranswers for a date" in {

        helper.accountingPeriodStart mustBe Some(summaryListRow(
          messages("accountingPeriodStart.checkYourAnswersLabel"),
          "1 January 2020",
          aboutReturnRoutes.AccountingPeriodStartController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }

    "For the AP end date" must {

      "get an answer from useranswers for a date" in {

        helper.accountingPeriodEnd mustBe Some(summaryListRow(
          messages("accountingPeriodEnd.checkYourAnswersLabel"),
          "1 February 2020",
          aboutReturnRoutes.AccountingPeriodEndController.onPageLoad(CheckMode) -> BaseMessages.changeLink
        ))
      }
    }
  }
}
