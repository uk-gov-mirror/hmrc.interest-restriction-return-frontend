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

import assets.constants.BaseConstants
import base.SpecBase
import connectors.mocks.MockCRNValidationConnector
import pages.Page
import pages.startReturn.{AgentActingOnBehalfOfCompanyPage, AgentNamePage, ReportingCompanyAppointedPage}


class QuestionDeletionLookupServiceSpec extends SpecBase with MockCRNValidationConnector with BaseConstants {

  object TestQuestionDeletionLookupService extends QuestionDeletionLookupService

  "QuestionDeletionLookupService.getPagesToRemove()" must {

    "for the StartReturnSection" when {

      "called with the Reporting Appointed Page" when {

        "the answer for the page is true" must {

          "return no pages to delete" in {

            val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, true).get

            val result = TestQuestionDeletionLookupService.getPagesToRemove(ReportingCompanyAppointedPage)(userAnswers)
            result mustBe List()
          }
        }

        "the answer for the page is false" must {

          "return all question pages to be deleted" in {

            val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, false).get

            val result = TestQuestionDeletionLookupService.getPagesToRemove(ReportingCompanyAppointedPage)(userAnswers)
            result mustBe Page.allQuestionPages
          }
        }
      }

      "called with the Agent Acting on Behalf of Client Page" when {

        "the answer for the page is true" must {

          "return no pages to delete" in {

            val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, true).get

            val result = TestQuestionDeletionLookupService.getPagesToRemove(AgentActingOnBehalfOfCompanyPage)(userAnswers)
            result mustBe List()
          }
        }

        "the answer for the page is false" must {

          "return the Agent Name page to delete" in {

            val userAnswers = emptyUserAnswers.set(AgentActingOnBehalfOfCompanyPage, false).get

            val result = TestQuestionDeletionLookupService.getPagesToRemove(AgentActingOnBehalfOfCompanyPage)(userAnswers)
            result mustBe List(AgentNamePage)
          }
        }
      }
    }
  }
}
