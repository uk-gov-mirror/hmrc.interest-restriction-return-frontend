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

import models.UserAnswers
import pages._
import play.api.i18n.Messages
import play.api.libs.json.Reads
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._
import viewmodels.SummaryListRowHelper

trait CheckYourAnswersHelper extends ImplicitDateFormatter with SummaryListRowHelper with CurrencyFormatter {

  val userAnswers: UserAnswers
  implicit val messages: Messages

  def answer[A](page: QuestionPage[A],
                changeLinkCall: Call,
                answerIsMsgKey: Boolean = false,
                headingMessageArgs: Seq[String] = Seq(),
                answerIsMonetary: Boolean = false)
               (implicit messages: Messages, reads: Reads[A], conversion: A => String): Option[SummaryListRow] =
    userAnswers.get(page) map { ans =>
      summaryListRow(
        label = messages(s"$page.checkYourAnswersLabel", headingMessageArgs: _*),
        value = if (answerIsMsgKey) messages(s"$page.$ans") else ans,
        changeLinkCall -> messages("site.edit")
      )
    }

  def monetaryAnswer(page: QuestionPage[BigDecimal],
                     changeLinkCall: Call,
                     headingMessageArgs: Seq[String] = Seq())
                    (implicit messages: Messages): Option[SummaryListRow] =
    userAnswers.get(page) map { ans =>
      summaryListRow(
        label = messages(s"$page.checkYourAnswersLabel", headingMessageArgs: _*),
        value = currencyFormat(ans),
        changeLinkCall -> messages("site.edit")
      )
    }

  def percentageAnswer(page: QuestionPage[BigDecimal],
                       changeLinkCall: Call,
                       headingMessageArgs: Seq[String] = Seq())
                      (implicit messages: Messages): Option[SummaryListRow] =
    userAnswers.get(page) map { ans =>
      summaryListRow(
        label = messages(s"$page.checkYourAnswersLabel", headingMessageArgs: _*),
        value = s"$ans%",
        changeLinkCall -> messages("site.edit")
      )
    }

  implicit val yesNoValue: Boolean => String = {
    case true => messages("site.yes")
    case _ => messages("site.no")
  }

  implicit val intToString: Int => String = _.toString

  implicit val bigDecimalConversion: BigDecimal => String = _.toString
}
