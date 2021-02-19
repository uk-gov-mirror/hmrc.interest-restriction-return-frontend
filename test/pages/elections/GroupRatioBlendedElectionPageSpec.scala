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

import models.UserAnswers
import assets.constants.InvestorGroupConstants._
import pages.behaviours.PageBehaviours

class GroupRatioBlendedElectionPageSpec extends PageBehaviours {

  "GroupRatioBlendedElectionPage" must {

    beRetrievable[Boolean](GroupRatioBlendedElectionPage)

    beSettable[Boolean](GroupRatioBlendedElectionPage)

    beRemovable[Boolean](GroupRatioBlendedElectionPage)
  }

  "Cleanup" when {
    "GroupRatioBlendedElectionPage" when {
      "Remove InvestorGroupsPage & AddInvestorGroupPage when there is a change of the answer to 'No'" in {
        val userAnswers = for {
          ebaUa <- UserAnswers(id = "id").set(GroupRatioBlendedElectionPage, true)
          invUa <- ebaUa.set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(1))
          invGr <- invUa.set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2))
          finalUa <- invGr.set(GroupRatioBlendedElectionPage, false)
        } yield finalUa

        val result = userAnswers.map(_.getList(InvestorGroupsPage)).get
        result mustBe empty
      }

      "Do not Remove InvestorGroupsPage & AddInvestorGroupPage when the answer to 'Yes'" in {
        val userAnswers = for {
          ebaUa <- UserAnswers(id = "id").set(GroupRatioBlendedElectionPage, true)
          invUa <- ebaUa.set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(1))
          invGr <- invUa.set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2))
          finalUa <- invGr.set(GroupRatioBlendedElectionPage, true)
        } yield finalUa

        val result = userAnswers.map(_.getList(InvestorGroupsPage)).get
        result mustBe Seq(investorGroupsFixedRatioModel, investorGroupsGroupRatioModel)
      }
    }
  }
}
