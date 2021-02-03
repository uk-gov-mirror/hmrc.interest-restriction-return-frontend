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

import models.SectionStatus._
import models.{SectionStatus, UserAnswers}
import pages.aboutReturn._
import pages.groupLevelInformation.RevisingReturnPage

object AboutReturnSectionStatus {
  
  def getStatus(userAnswers: UserAnswers): SectionStatus = {

    val requiredPages = List(
      userAnswers.get(ReportingCompanyAppointedPage),
      userAnswers.get(AgentActingOnBehalfOfCompanyPage),
      userAnswers.get(FullOrAbbreviatedReturnPage),
      userAnswers.get(RevisingReturnPage),
      userAnswers.get(ReportingCompanyNamePage),
      userAnswers.get(ReportingCompanyCTUTRPage),
      userAnswers.get(AccountingPeriodPage)
    )
    
    val optionalAgentPages =
      userAnswers.get(AgentActingOnBehalfOfCompanyPage) match {
        case Some(true) => Seq(userAnswers.get(AgentNamePage))
        case _ => Nil
      }

    val optionalRevisionPages =
      userAnswers.get(RevisingReturnPage) match {
        case Some(true) => Seq(userAnswers.get(TellUsWhatHasChangedPage))
        case _ => Nil
      }

    val pages = requiredPages ++ optionalAgentPages ++ optionalRevisionPages

    pages.flatten match {
      case result if result.isEmpty => NotStarted
      case result if result.size == pages.size => Completed
      case _ => InProgress
    }
  }
}