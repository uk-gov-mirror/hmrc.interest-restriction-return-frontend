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
import pages.HelloWorldYesNoPageNunjucks
import play.twirl.api.HtmlFormat
import viewmodels.AnswerRow

class CheckYourAnswersHelperSpec extends SpecBase {

  "Check Your Answers Helper" must {

    "get an answer from useranswers for true" in {

      val helper = new CheckYourAnswersHelper(UserAnswers("id").set(HelloWorldYesNoPageNunjucks,true).get)

      helper.helloWorldYesNo mustBe Some(AnswerRow(
        HtmlFormat.escape(messages("helloWorldYesNo.checkYourAnswersLabel")),
        HtmlFormat.escape(messages("site.yes")),
        routes.HelloWorldYesNoController.onPageLoad(CheckMode).url
      ))

    }

    "get an answer from useranswers for false" in {

      val helper = new CheckYourAnswersHelper(UserAnswers("id").set(HelloWorldYesNoPageNunjucks,false).get)

      helper.helloWorldYesNo mustBe Some(AnswerRow(
        HtmlFormat.escape(messages("helloWorldYesNo.checkYourAnswersLabel")),
        HtmlFormat.escape(messages("site.no")),
        routes.HelloWorldYesNoController.onPageLoad(CheckMode).url
      ))

    }
  }

}
