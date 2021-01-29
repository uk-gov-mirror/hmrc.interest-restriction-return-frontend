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

import base.SpecBase
import models.SectionStatus._
import pages.aboutReturn._
import pages.Page._

class AboutReturnSectionStatusSpec extends SpecBase {

  "aboutReturnSectionStatus" must {
    "return NotStarted" when {
      "no data from the section has been entered" in {
        val userAnswers = emptyUserAnswers
        AboutReturnSectionStatus.aboutReturnSectionStatus(userAnswers) mustEqual NotStarted
      }
    }
    "return InProgress" when {
      "has some data been entered" in {
        val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, true).get
        AboutReturnSectionStatus.aboutReturnSectionStatus(userAnswers) mustEqual InProgress
      }
    }
    
    "return Completed" when {
      "has all data been entered from " in {
        aboutReturnSectionPages
        val userAnswers = emptyUserAnswers
          .set(AgentActingOnBehalfOfCompanyPage, true).get
          .set(AgentNamePage, "true").get
          .set(FullOrAbbreviatedReturnPage, true).get
          .set(ReportingCompanyAppointedPage, true).get
          .set(ReportingCompanyRequiredPage, true).get
          .set(AccountingPeriodPage, true).get
          .set(ReportingCompanyNamePage, true).get
          .set(ReportingCompanyCTUTRPage, true).get
          .set(RevisingReturnPage, true).get
          //.set(IndexPage, true).get
          //.set(CheckAnswersAboutReturnPage, true).get
          .set(TellUsWhatHasChangedPage, true).get
          
        AboutReturnSectionStatus.aboutReturnSectionStatus(userAnswers) mustEqual Completed
      }
    }
  }
}