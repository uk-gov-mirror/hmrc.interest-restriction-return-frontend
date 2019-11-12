/*
 * Copyright 2019 HM Revenue & Customs
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
import pages.{HelloWorldYesNoPage, HelloWorldYesNoPageNunjucks}
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersHelperSpec extends SpecBase {

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

    "For the HellowWorldYesNoPage" must {

      "get an answer from useranswers for true" in {

        val helper = new CheckYourAnswersHelper(UserAnswers("id").set(HelloWorldYesNoPage, true).get)

        helper.helloWorldYesNo mustBe Some(summaryListRow(
          messages("helloWorldYesNo.checkYourAnswersLabel"),
          messages("site.yes"),
          routes.HelloWorldYesNoController.onPageLoad(CheckMode)
        ))

      }

      "get an answer from useranswers for false" in {

        val helper = new CheckYourAnswersHelper(UserAnswers("id").set(HelloWorldYesNoPage, false).get)

        helper.helloWorldYesNo mustBe Some(summaryListRow(
          messages("helloWorldYesNo.checkYourAnswersLabel"),
          messages("site.no"),
          routes.HelloWorldYesNoController.onPageLoad(CheckMode)
        ))

      }
    }

    "For the HellowWorldYesNoPageNunjucks" must {

      "get an answer from useranswers for true" in {

        val helper = new CheckYourAnswersHelper(UserAnswers("id").set(HelloWorldYesNoPageNunjucks, true).get)

        helper.helloWorldYesNoNunjucks mustBe Some(summaryListRow(
          messages("helloWorldYesNoNunjucks.checkYourAnswersLabel"),
          messages("site.yes"),
          routes.HelloWorldYesNoNunjucksController.onPageLoad(CheckMode)
        ))

      }

      "get an answer from useranswers for false" in {

        val helper = new CheckYourAnswersHelper(UserAnswers("id").set(HelloWorldYesNoPageNunjucks, false).get)

        helper.helloWorldYesNoNunjucks mustBe Some(summaryListRow(
          messages("helloWorldYesNoNunjucks.checkYourAnswersLabel"),
          messages("site.no"),
          routes.HelloWorldYesNoNunjucksController.onPageLoad(CheckMode)
        ))

      }
    }
  }

}
