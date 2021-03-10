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

import java.time.LocalDate

import base.SpecBase
import models.SectionStatus._
import pages.aboutReturn._
import pages.groupLevelInformation.RevisingReturnPage
import assets.constants.BaseConstants
import models.UserAnswers
import models.returnModels.AccountingPeriodModel
import models.FullOrAbbreviatedReturn._

class AboutReturnSectionStatusSpec extends SpecBase with BaseConstants {

  "aboutReturnSectionStatus" must {
    "return NotStarted" when {
      "no data from the section has been entered" in {
        val userAnswers = emptyUserAnswers
        AboutReturnSectionStatus.getStatus(userAnswers) mustEqual NotStarted
      }
    }
    "return InProgress" when {
      "has some data been entered" in {
        val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, true).get
        AboutReturnSectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "agent bool is set to true and the agent name isn't populated (and all other data is)" in {
        val date = LocalDate.of(2020,1,1)
        
        val userAnswers = UserAnswers("id")
          .set(ReportingCompanyAppointedPage, true).get
          .set(AgentActingOnBehalfOfCompanyPage, true).get
          .set(FullOrAbbreviatedReturnPage, Full).get
          .set(RevisingReturnPage, true).get
          .set(TellUsWhatHasChangedPage, "Something has changed").get
          .set(ReportingCompanyNamePage, companyNameModel.name).get
          .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
          .set(AccountingPeriodPage, AccountingPeriodModel(date, date.plusMonths(1))).get

        AboutReturnSectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "Revising return is set to true and what has changed is not set" in {
        val date = LocalDate.of(2020,1,1)
        
        val userAnswers = UserAnswers("id")
          .set(ReportingCompanyAppointedPage, true).get
          .set(AgentActingOnBehalfOfCompanyPage, true).get
          .set(AgentNamePage, agentName).get
          .set(FullOrAbbreviatedReturnPage, Full).get
          .set(RevisingReturnPage, true).get
          .set(ReportingCompanyNamePage, companyNameModel.name).get
          .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
          .set(AccountingPeriodPage, AccountingPeriodModel(date, date.plusMonths(1))).get

        AboutReturnSectionStatus.getStatus(userAnswers) mustEqual InProgress
      }
    }
    
    "return Completed" when {
      "has all data been entered for a normal journey" in {
        val date = LocalDate.of(2020,1,1)
        
        val userAnswers = UserAnswers("id")
          .set(ReportingCompanyAppointedPage, true).get
          .set(AgentActingOnBehalfOfCompanyPage, true).get
          .set(AgentNamePage, agentName).get
          .set(FullOrAbbreviatedReturnPage, Full).get
          .set(RevisingReturnPage, true).get
          .set(TellUsWhatHasChangedPage, "Something has changed").get
          .set(ReportingCompanyNamePage, companyNameModel.name).get
          .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
          .set(AccountingPeriodPage, AccountingPeriodModel(date, date.plusMonths(1))).get

        AboutReturnSectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "agent bool is set to false but the agent name isn't populated (and all other data is)" in {
        val date = LocalDate.of(2020,1,1)
        
        val userAnswers = UserAnswers("id")
          .set(ReportingCompanyAppointedPage, true).get
          .set(AgentActingOnBehalfOfCompanyPage, false).get
          .set(FullOrAbbreviatedReturnPage, Full).get
          .set(RevisingReturnPage, true).get
          .set(TellUsWhatHasChangedPage, "Something has changed").get
          .set(ReportingCompanyNamePage, companyNameModel.name).get
          .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
          .set(AccountingPeriodPage, AccountingPeriodModel(date, date.plusMonths(1))).get

        AboutReturnSectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "Revising return is set to false and what has changed is not set" in {
        val date = LocalDate.of(2020,1,1)
        
        val userAnswers = UserAnswers("id")
          .set(ReportingCompanyAppointedPage, true).get
          .set(AgentActingOnBehalfOfCompanyPage, true).get
          .set(AgentNamePage, agentName).get
          .set(FullOrAbbreviatedReturnPage, Full).get
          .set(RevisingReturnPage, false).get
          .set(ReportingCompanyNamePage, companyNameModel.name).get
          .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
          .set(AccountingPeriodPage, AccountingPeriodModel(date, date.plusMonths(1))).get

        AboutReturnSectionStatus.getStatus(userAnswers) mustEqual Completed
      }
      
    }
  }
}