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

import models.sections.AboutReturnSectionModel
import models._
import models.returnModels._
import pages.aboutReturn._
import pages.groupLevelInformation.RevisingReturnPage
import pages.Page._

object AboutReturnSectionStatus extends CurrentSectionStatus[AboutReturnSectionModel] {

  val pages = aboutReturnSectionPages

  def isComplete(section: AboutReturnSectionModel) = 
    agentDetailsComplete(section.agentDetails) && revisedReturnDetailsComplete(section.isRevisingReturn, section.revisedReturnDetails)
  
  def agentDetailsComplete(agentDetails: AgentDetailsModel): Boolean = 
    agentDetails match {
      case AgentDetailsModel(true, Some(_)) => true
      case AgentDetailsModel(false, _) => true
      case _ => false
    }

  def revisedReturnDetailsComplete(isRevisingReturn: Boolean, revisedReturnDetails: Option[String]): Boolean = 
    (isRevisingReturn, revisedReturnDetails) match {
      case (true, Some(_)) => true
      case (false, _) => true
      case (_, _) => false
    }
  
  def currentSection(userAnswers: UserAnswers): Option[AboutReturnSectionModel] = 
    for {
      appointedReportingCompany     <- userAnswers.get(ReportingCompanyAppointedPage)
      agentActingOnBehalfOfCompany  <- userAnswers.get(AgentActingOnBehalfOfCompanyPage)
      fullOrAbbreviatedReturn       <- userAnswers.get(FullOrAbbreviatedReturnPage)
      isRevisingReturn              <- userAnswers.get(RevisingReturnPage)
      companyName                   <- userAnswers.get(ReportingCompanyNamePage)
      ctutr                         <- userAnswers.get(ReportingCompanyCTUTRPage)
      periodOfAccount               <- userAnswers.get(AccountingPeriodPage)
    } yield {

      val agentDetailsModel = AgentDetailsModel(
        agentActingOnBehalfOfCompany  = agentActingOnBehalfOfCompany,
        agentName                     = userAnswers.get(AgentNamePage)
      )

      AboutReturnSectionModel(
        appointedReportingCompany     = appointedReportingCompany,
        agentDetails                  = agentDetailsModel,
        fullOrAbbreviatedReturn       = fullOrAbbreviatedReturn,
        isRevisingReturn              = isRevisingReturn,
        revisedReturnDetails          = userAnswers.get(TellUsWhatHasChangedPage),
        companyName                   = CompanyNameModel(companyName),
        ctutr                         = UTRModel(ctutr),
        periodOfAccount               = periodOfAccount
      )
    }
}
