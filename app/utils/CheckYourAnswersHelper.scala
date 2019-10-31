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

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages._
import play.api.i18n.Messages
import play.api.mvc.Call
import play.twirl.api.HtmlFormat
import viewmodels.AnswerRow

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  def helloWorldYesNo: Option[AnswerRow] = yesOrNo(HelloWorldYesNoPage, routes.HelloWorldYesNoController.onPageLoad(CheckMode))
  def helloWorldYesNoNunjucks: Option[AnswerRow] = yesOrNo(HelloWorldYesNoPageNunjucks, routes.HelloWorldYesNoNunjucksController.onPageLoad(CheckMode))

  private def yesOrNo(page: QuestionPage[Boolean], changeLinkCall: Call)(implicit messages: Messages): Option[AnswerRow] =
    userAnswers.get(page) map { bool =>
      AnswerRow(
        HtmlFormat.escape(messages(s"$page.checkYourAnswersLabel")),
        if (bool) {
          HtmlFormat.escape(messages("site.yes"))
        } else {
          HtmlFormat.escape(messages("site.no"))
        },
        changeLinkCall.url
      )
    }
}
