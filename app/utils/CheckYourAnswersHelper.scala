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
import play.api.libs.json.Reads
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) extends ImplicitDateFormatter {

  def helloWorldYesNo: Option[SummaryListRow] = answer(HelloWorldYesNoPage, routes.HelloWorldYesNoController.onPageLoad(CheckMode))
  def helloWorldYesNoNunjucks: Option[SummaryListRow] = answer(HelloWorldYesNoPageNunjucks, routes.HelloWorldYesNoNunjucksController.onPageLoad(CheckMode))

  private def answer[A](page: QuestionPage[A], changeLinkCall: Call)
                       (implicit messages: Messages, reads: Reads[A], conversion: A => String): Option[SummaryListRow] =
    userAnswers.get(page) map { ans =>
      SummaryListRow(
        key = Key(content = Text(messages(s"$page.checkYourAnswersLabel"))),
        value = Value(content = Text(ans)),
        actions = Some(Actions(
          items = Seq(ActionItem(
            href = changeLinkCall.url,
            content = Text(messages("site.edit"))
          ))
        ))
      )
    }

  implicit private val yesNoValue: Boolean => String = {
    case true => messages("site.yes")
    case _ => messages("site.no")
  }

  implicit private val intToString: Int => String = _.toString
}
