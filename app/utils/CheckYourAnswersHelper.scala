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

import java.time.format.DateTimeFormatter

import controllers.routes
import models.{CheckMode, UserAnswers}
import pages._
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.{HtmlContent, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) {

  def helloWorldYesNo: Option[SummaryListRow] = yesOrNo(HelloWorldYesNoPage, routes.HelloWorldYesNoController.onPageLoad(CheckMode))
  def helloWorldYesNoNunjucks: Option[SummaryListRow] = yesOrNo(HelloWorldYesNoPageNunjucks, routes.HelloWorldYesNoNunjucksController.onPageLoad(CheckMode))

  private val dateFormatter = DateTimeFormatter.ofPattern("d MMMM yyyy")

  private def yesOrNo(page: QuestionPage[Boolean], changeLinkCall: Call)(implicit messages: Messages): Option[SummaryListRow] =
    userAnswers.get(page) map { bool =>
      SummaryListRow(
        key = Key(content = HtmlContent(messages(s"$page.checkYourAnswersLabel"))),
        value = Value(content = HtmlContent(yesNoValue(bool))),
        actions = Some(Actions(
          items = Seq(ActionItem(
            href = changeLinkCall.url,
            content = Text(messages("site.edit"))
          ))
        ))
      )
    }

  private val yesNoValue: Boolean => String = {
    case true => messages("site.yes")
    case _ => messages("site.no")
  }

  private def inputText(page: QuestionPage[String], changeLinkCall: Call)(implicit messages: Messages): Option[SummaryListRow] =
    userAnswers.get(page) map { text =>
      SummaryListRow(
        key = Key(content = HtmlContent(messages(s"$page.checkYourAnswersLabel"))),
        value = Value(content = Text(text)),
        actions = Some(Actions(
          items = Seq(ActionItem(
            href = changeLinkCall.url,
            content = Text(messages("site.edit"))
          ))
        ))
      )
    }
}
