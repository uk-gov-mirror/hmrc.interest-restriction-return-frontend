/*
 * Copyright 2021 HM Revenue & Customs
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

package pages.aboutReturn

import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import models.{FullOrAbbreviatedReturn, UserAnswers}
import pages.Page.allPagesWithoutAboutSection
import pages.QuestionPage
import play.api.libs.json.JsPath

import scala.util.Try

case object FullOrAbbreviatedReturnPage extends QuestionPage[FullOrAbbreviatedReturn] {

  override def path: JsPath = JsPath \ toString

  override def toString: String = "fullOrAbbreviatedReturn"

  override def cleanup(value: Option[FullOrAbbreviatedReturn], userAnswers: UserAnswers): Try[UserAnswers] = {
    val answerTest = userAnswers.get(FullOrAbbreviatedReturnPage)

    value match {
      case Some(Full) => {
        answerTest match {
          case Some(Abbreviated) =>userAnswers.remove(allPagesWithoutAboutSection)
          case _ => super.cleanup(value, userAnswers)
        }
      }
      case _ => {
        answerTest match {
          case Some(Full) => userAnswers.remove(allPagesWithoutAboutSection)
          case _ => super.cleanup(value, userAnswers)
        }
      }
    }
  }
}
