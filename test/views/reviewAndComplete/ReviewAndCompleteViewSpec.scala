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

package views.reviewAndComplete

import assets.messages.BaseMessages
import models.SectionStatus.{Completed, InProgress, NotStarted}
import models.returnModels.ReviewAndCompleteModel
import utils.ReviewAndCompleteHelper
import viewmodels.TaskListRow
import views.behaviours.ViewBehaviours
import views.html.reviewAndComplete.ReviewAndCompleteView

class ReviewAndCompleteViewSpec extends ViewBehaviours {

  val taskListRows: Seq[TaskListRow] = new ReviewAndCompleteHelper().rows(
    ReviewAndCompleteModel(NotStarted, InProgress, Completed, NotStarted, InProgress, Completed)
  )

  lazy val viewTemplate = viewFor[ReviewAndCompleteView](Some(emptyUserAnswers))
  lazy val view = viewTemplate.apply(taskListRows, onwardRoute)(fakeRequest, frontendAppConfig, messages)

  val messageKeyPrefix = "reviewAndComplete"
  val section = Some(messages("section.reviewAndComplete"))

  object Selectors {
    val section: Int => String = i => s"#main-content > div > div > ul > li:nth-child($i) > span > a"
    val state: Int => String = i => s"#main-content > div > div > ul > li:nth-child($i) > strong"
  }

  "ReviewAndCompleteView" must {

    behave like normalPage(view, messageKeyPrefix, section = section)

    behave like pageWithBackLink(view)

    behave like pageWithSubmitButton(view, BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(view)

    taskListRows.zipWithIndex.foreach { case (row, i) =>

      val document = asDocument(view)
      val number = i + 1

      s"have a row for ${i}" in {
        document.select(Selectors.section(number)).text() mustBe row.name
        document.select(Selectors.section(number)).attr("href") mustBe row.link.url
        document.select(Selectors.state(number)).text() mustBe row.state
      }
    }
  }
}
