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
import base.SpecBase
import controllers.routes
import controllers.startReturn.{routes => startReturnRoutes}
import controllers.aboutReportingCompany.{routes => aboutReportingCompanyRoutes}
import models.FullOrAbbreviatedReturn.Full
import models.{CheckMode, UserAnswers}
import pages._
import pages.aboutReportingCompany._
import pages.startReturn.{AgentActingOnBehalfOfCompanyPage, AgentNamePage, FullOrAbbreviatedReturnPage, ReportingCompanyAppointedPage}
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersAboutReportingCompanyHelperSpec extends SpecBase with BaseConstants {


  private def summaryListRow(label: String, answer: String, changeLink: Call) =
    SummaryListRow(
      key = Key(Text(label)),
      value = Value(Text(answer)),
      actions = Some(Actions(
        items = Seq(ActionItem(
          href = changeLink.url,
          content = Text(messages("site.edit"))
        ))
      ))
    )

  val helper = new CheckYourAnswersAboutReportingCompanyHelper(
    UserAnswers("id")
      .set(ReportingCompanyAppointedPage, true).get
      .set(AgentActingOnBehalfOfCompanyPage, true).get
      .set(AgentNamePage, agentName).get
      .set(FullOrAbbreviatedReturnPage, Full).get
      .set(ReportingCompanyNamePage, companyNameModel.name).get
      .set(ReportingCompanyCTUTRPage, ctutrModel.ctutr).get
      .set(ReportingCompanyCRNPage, crnModel.crn).get
  )

  "Check Your Answers Helper" must {

    "For the ReportingCompanyAppointed answer" must {

      "have a correctly formatted summary list row" in {

        helper.reportingCompanyAppointed mustBe Some(summaryListRow(
          messages("reportingCompanyAppointed.checkYourAnswersLabel"),
          "Yes",
          startReturnRoutes.ReportingCompanyAppointedController.onPageLoad(CheckMode)
        ))
      }
    }

    "For the AgentActingOnBehalfOfCompany answer" must {

      "have a correctly formatted summary list row" in {

        helper.agentActingOnBehalfOfCompany mustBe Some(summaryListRow(
          messages("agentActingOnBehalfOfCompany.checkYourAnswersLabel"),
          "Yes",
          startReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(CheckMode)
        ))
      }
    }

    "For the AgentName answer" must {

      "have a correctly formatted summary list row" in {

        helper.agentName mustBe Some(summaryListRow(
          messages("agentName.checkYourAnswersLabel"),
          agentName,
          startReturnRoutes.AgentNameController.onPageLoad(CheckMode)
        ))
      }
    }

    "For the FullOrAbbreviatedReturn answer" must {

      "have a correctly formatted summary list row" in {

        helper.fullOrAbbreviatedReturn mustBe Some(summaryListRow(
          messages("fullOrAbbreviatedReturn.checkYourAnswersLabel"),
          messages("fullOrAbbreviatedReturn.full"),
          startReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(CheckMode)
        ))
      }
    }

    "For the ReportingCompanyName" must {

      "get an answer from useranswers for true" in {

        helper.reportingCompanyName mustBe Some(summaryListRow(
          messages("reportingCompanyName.checkYourAnswersLabel"),
          companyNameModel.name,
          aboutReportingCompanyRoutes.ReportingCompanyNameController.onPageLoad(CheckMode)
        ))
      }
    }

    "For the ReportingCompanyCTUTR" must {

      "get an answer from useranswers for true" in {

        helper.reportingCompanyCTUTR mustBe Some(summaryListRow(
          messages("reportingCompanyCTUTR.checkYourAnswersLabel"),
          ctutrModel.ctutr,
          aboutReportingCompanyRoutes.ReportingCompanyCTUTRController.onPageLoad(CheckMode)
        ))
      }
    }

    "For the ReportingCompanyCRN" must {

      "get an answer from useranswers for true" in {

        helper.reportingCompanyCRN mustBe Some(summaryListRow(
          messages("reportingCompanyCRN.checkYourAnswersLabel"),
          crnModel.crn,
          aboutReportingCompanyRoutes.ReportingCompanyCRNController.onPageLoad(CheckMode)
        ))
      }
    }
  }
}
