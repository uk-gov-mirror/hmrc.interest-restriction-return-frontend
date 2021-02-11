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
import pages.checkTotals.CompanyDetailsPage
import assets.constants.BaseConstants

class CheckTotalsSectionStatusSpec extends SpecBase with BaseConstants {

  "aboutReturnSectionStatus" must {
    "return NotStarted" when {
      "no data from the section has been entered" in {
        val userAnswers = emptyUserAnswers
        CheckTotalsSectionStatus.getStatus(userAnswers) mustEqual NotStarted
      }
    }
    "return InProgress" when {
    }
    
    "return Completed" when {     
    }
  }
}