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

package pages.elections

import pages.behaviours.PageBehaviours
import models.UserAnswers

class InterestAllowanceNonConsolidatedInvestmentsElectionPageSpec extends PageBehaviours {

  "InterestAllowanceNonConsolidatedInvestmentsElectionPage" must {

    beRetrievable[Boolean](InterestAllowanceNonConsolidatedInvestmentsElectionPage)

    beSettable[Boolean](InterestAllowanceNonConsolidatedInvestmentsElectionPage)

    beRemovable[Boolean](InterestAllowanceNonConsolidatedInvestmentsElectionPage)
  }

  "cleanup" must {
    "clear down investments when set to false and only one investment exists" in {
      val userAnswers = (for {
        ua <- UserAnswers("id").set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, true)
        uaWithInvestments <- ua.set(InvestmentNamePage, "Investment 1", Some(1))
        finalUa <- uaWithInvestments.set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, false)
      } yield finalUa).get

      userAnswers.getList(InvestmentNamePage) mustEqual Nil
    }

    "not clear down investments when set to true and only one investment exists" in {
      val userAnswers = (for {
        ua <- UserAnswers("id").set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, true)
        uaWithInvestments <- ua.set(InvestmentNamePage, "Investment 1", Some(1))
        finalUa <- uaWithInvestments.set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, true)
      } yield finalUa).get

      userAnswers.getList(InvestmentNamePage) mustEqual Seq("Investment 1")
    }

    "clear down investments when set to false and multiple investments exist" in {
      val userAnswers = (for {
        ua <- UserAnswers("id").set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, true)
        ua1 <- ua.set(InvestmentNamePage, "Investment 1", Some(1))
        ua2 <- ua1.set(InvestmentNamePage, "Investment 2", Some(2))
        ua3 <- ua2.set(InvestmentNamePage, "Investment 3", Some(3))
        finalUa <- ua3.set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, false)
      } yield finalUa).get

      userAnswers.getList(InvestmentNamePage) mustEqual Nil
    }

    "not clear down investments when set to true and multiple investments exist" in {
      val userAnswers = (for {
        ua <- UserAnswers("id").set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, true)
        ua1 <- ua.set(InvestmentNamePage, "Investment 1", Some(1))
        ua2 <- ua1.set(InvestmentNamePage, "Investment 2", Some(2))
        ua3 <- ua2.set(InvestmentNamePage, "Investment 3", Some(3))
        finalUa <- ua3.set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, true)
      } yield finalUa).get

      userAnswers.getList(InvestmentNamePage) mustEqual Seq("Investment 1", "Investment 2", "Investment 3")
    }

  }
}
