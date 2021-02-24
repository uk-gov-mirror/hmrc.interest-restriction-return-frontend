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

import assets.constants.PartnershipsConstants._
import pages.behaviours.PageBehaviours
import models.UserAnswers
import models.returnModels.PartnershipModel

class InterestAllowanceConsolidatedPshipElectionPageSpec extends PageBehaviours {

  "InterestAllowanceConsolidatedPshipElectionPage" must {

    beRetrievable[Boolean](InterestAllowanceConsolidatedPshipElectionPage)

    beSettable[Boolean](InterestAllowanceConsolidatedPshipElectionPage)

    beRemovable[Boolean](InterestAllowanceConsolidatedPshipElectionPage)
  }

  "Cleanup" when {
    "InterestAllowanceConsolidatedPshipElectionPage" when {
      "Remove ConsolidatedPartnerships when there is a change of the answer to 'No'" in {
        val userAnswers = for {
          rootPage <- UserAnswers(id = "id").set(InterestAllowanceConsolidatedPshipElectionPage, true)
          finalPartnershipPage <- rootPage.set(PartnershipsPage, partnershipModelUK, Some(1))
          setRootPageFalse <- finalPartnershipPage.set(InterestAllowanceConsolidatedPshipElectionPage, false)
        } yield setRootPageFalse

        val result = userAnswers.map(_.getList(PartnershipsPage)).get
        result mustBe Seq()
      }

      "Remove all ConsolidatedPartnerships when there is a change of the answer to 'No'" in {
        val userAnswers = for {
          rootPage <- UserAnswers(id = "id").set(InterestAllowanceConsolidatedPshipElectionPage, true)
          part1 <- rootPage.set(PartnershipsPage, partnershipModelUK, Some(1))
          part2 <- part1.set(PartnershipsPage, partnershipModelUK, Some(2))
          part3 <- part2.set(PartnershipsPage, partnershipModelUK, Some(3))
          setRootPageFalse <- part3.set(InterestAllowanceConsolidatedPshipElectionPage, false)
        } yield setRootPageFalse

        val result = userAnswers.map(_.getList(PartnershipsPage)).get
        result mustBe Seq()
      }

      "Do not Remove AlternativeCalcElect when the answer to 'Yes'" in {
        val userAnswers = for {
          rootPage <- UserAnswers(id = "id").set(InterestAllowanceConsolidatedPshipElectionPage, true)
          finalPartnershipPage <- rootPage.set(PartnershipsPage, partnershipModelUK, Some(1))
          setRootPageFalse <- finalPartnershipPage.set(InterestAllowanceConsolidatedPshipElectionPage, true)
        } yield setRootPageFalse

        val result = userAnswers.map(_.getList(PartnershipsPage)).get
        result mustBe Seq(partnershipModelUK)
      }


      "Do not Remove Any AlternativeCalcElect when the answer to 'Yes'" in {
        val userAnswers = for {
          rootPage <- UserAnswers(id = "id").set(InterestAllowanceConsolidatedPshipElectionPage, true)
          part1 <- rootPage.set(PartnershipsPage, partnershipModelUK, Some(1))
          part2 <- part1.set(PartnershipsPage, partnershipModelUK, Some(2))
          part3 <- part2.set(PartnershipsPage, partnershipModelUK, Some(3))
          setRootPageFalse <- part3.set(InterestAllowanceConsolidatedPshipElectionPage, true)
        } yield setRootPageFalse

        val result = userAnswers.map(_.getList(PartnershipsPage)).get
        result mustBe Seq(partnershipModelUK, partnershipModelUK, partnershipModelUK)
      }
    }
  }
}
