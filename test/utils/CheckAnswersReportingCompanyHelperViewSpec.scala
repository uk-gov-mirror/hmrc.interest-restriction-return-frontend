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

import base.SpecBase
import controllers.routes
import models.{CheckMode, UserAnswers}
import pages._
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckAnswersReportingCompanyHelperViewSpec extends SpecBase {

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

  "Check Answers Reporting Company" must {

    "For the reportingCompanyAppointed" must {

      "get an answer from user answers for true" in {

        val helper = new CheckAnswersReportingCompanyHelper(UserAnswers("id").set(ReportingCompanyAppointedPage, true).get)

        helper.reportingCompanyAppointed mustBe Some(summaryListRow(
          messages("reportingCompanyAppointed.checkYourAnswersLabel"),
          messages("site.yes"),
          routes.ReportingCompanyAppointedController.onPageLoad(CheckMode)
        ))

      }
    }

    "get an answer from user answers for false" in {

      val helper = new CheckAnswersReportingCompanyHelper(UserAnswers("id").set(ReportingCompanyAppointedPage, false).get)

      helper.reportingCompanyAppointed mustBe Some(summaryListRow(
        messages("reportingCompanyAppointed.checkYourAnswersLabel"),
        messages("site.no"),
        routes.ReportingCompanyAppointedController.onPageLoad(CheckMode)
      ))

    }
  }
}
