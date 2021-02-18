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

package sectionstatus

import pages.{Page, QuestionPage}
import models.{UserAnswers, SectionStatus}
import models.SectionStatus._

trait CurrentSectionStatus[A] {

  def pages: Seq[Page]

  def currentSection(userAnswers: UserAnswers): Option[A]

  def isComplete(section: A): Boolean

  def sectionQuestionPages = pages.collect{case page: QuestionPage[_] => page}
  def isNotStarted(userAnswers: UserAnswers) = sectionQuestionPages.flatMap(userAnswers.get(_)).isEmpty
  
  def getStatus(userAnswers: UserAnswers): SectionStatus = 
    (isNotStarted(userAnswers), currentSection(userAnswers)) match {
      case (true, _)                                  => NotStarted
      case (_, Some(section)) if isComplete(section)  => Completed
      case (_, _)                                     => InProgress
    }
}
