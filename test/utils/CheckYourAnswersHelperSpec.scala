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
import controllers.aboutReportingCompany.{routes => aboutReportingCompanyRoutes}
import models.{CheckMode, UserAnswers}
import pages._
import pages.aboutReportingCompany._
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersHelperSpec extends SpecBase with BaseConstants {

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

  "Check Your Answers Helper" must {

    "For the ReportingCompanyName" must {

      "get an answer from useranswers for true" in {

        val helper = new CheckYourAnswersHelper(UserAnswers("id").set(ReportingCompanyNamePage, "A Company Name").get)

        helper.reportingCompanyName mustBe Some(summaryListRow(
          messages("reportingCompanyName.checkYourAnswersLabel"),
          "A Company Name",
          aboutReportingCompanyRoutes.ReportingCompanyNameController.onPageLoad(CheckMode)
        ))
      }
    }

    "For the ReportingCompanyCTUTR" must {

      "get an answer from useranswers for true" in {

        val helper = new CheckYourAnswersHelper(UserAnswers("id").set(ReportingCompanyCTUTRPage, ctutr).get)

        helper.reportingCompanyCTUTR mustBe Some(summaryListRow(
          messages("reportingCompanyCTUTR.checkYourAnswersLabel"),
          ctutr,
          aboutReportingCompanyRoutes.ReportingCompanyCTUTRController.onPageLoad(CheckMode)
        ))
      }
    }

    "For the ReportingCompanyCRN" must {

      "get an answer from useranswers for true" in {

        val helper = new CheckYourAnswersHelper(UserAnswers("id").set(ReportingCompanyCRNPage, "12345678").get)

        helper.reportingCompanyCRN mustBe Some(summaryListRow(
          messages("reportingCompanyCRN.checkYourAnswersLabel"),
          "12345678",
          aboutReportingCompanyRoutes.ReportingCompanyCRNController.onPageLoad(CheckMode)
        ))
      }
    }
  }
}
