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

package services

import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.startReturn.{AgentActingOnBehalfOfCompanyPage, AgentNamePage, ReportingCompanyAppointedPage}
import pages.Page._

@Singleton
class QuestionDeletionLookupService @Inject()() {

  private val startReturnLogic: Map[QuestionPage[_], UserAnswers => List[QuestionPage[_]]] = Map(
    ReportingCompanyAppointedPage -> (_.get(ReportingCompanyAppointedPage) match {
      case Some(false) => allQuestionPages.filterNot(_ == ReportingCompanyAppointedPage)
      case _ => List()
    }),
    AgentActingOnBehalfOfCompanyPage -> (_.get(AgentActingOnBehalfOfCompanyPage) match {
      case Some(false) => List(AgentNamePage)
      case _ => List()
    })
  )

  private val pagesToRemove: Map[QuestionPage[_], UserAnswers => List[QuestionPage[_]]] =
    startReturnLogic

  def getPagesToRemove(currentPage: QuestionPage[_]): UserAnswers => List[QuestionPage[_]] = {
    pagesToRemove.getOrElse(currentPage,_ => List.empty)
  }
}
