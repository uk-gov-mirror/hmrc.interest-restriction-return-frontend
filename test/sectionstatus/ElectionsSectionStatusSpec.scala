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
import assets.constants.BaseConstants
import models.UserAnswers
import models.returnModels._
import models.{InvestorRatioMethod, OtherInvestorGroupElections}
import models.IsUKPartnershipOrPreferNotToAnswer._

class ElectionsSectionStatusSpec extends SpecBase with BaseConstants {

  "getStatus" must {
    "return NotStarted" when {
      "no data from the section has been entered" in {
        val userAnswers = UserAnswers("id")
        ElectionsSectionStatus.getStatus(userAnswers) mustEqual NotStarted
      }
    }
  }

  "isInvestorGroupComplete" must {
    "return true where all data is populated" in {
      val investorGroup = 
        InvestorGroupModel(
          investorName = "name", 
          ratioMethod = Some(InvestorRatioMethod.GroupRatioMethod), 
          otherInvestorGroupElections = Some(Set(OtherInvestorGroupElections.GroupRatioBlended))
        )
      ElectionsSectionStatus.isInvestorGroupComplete(investorGroup) mustEqual true
    }

    "return false where the ratioMethod and otherInvestorGroupElections is missing" in {
      val investorGroup = 
        InvestorGroupModel(
          investorName = "name", 
          ratioMethod = None, 
          otherInvestorGroupElections = None
        )
      ElectionsSectionStatus.isInvestorGroupComplete(investorGroup) mustEqual false
    }
  }

  "isPartnershipComplete" must {
    "return true where isUkPartnership is PreferNotToAnswer and sautr is not populated" in {
      val partnership = 
        PartnershipModel(
          name = "name",
          isUkPartnership = Some(PreferNotToAnswer),
          sautr = None
        )
      ElectionsSectionStatus.isPartnershipComplete(partnership) mustEqual true
    }

    "return true where isUkPartnership is answered and sautr is populated" in {
      val partnership = 
        PartnershipModel(
          name = "name",
          isUkPartnership = Some(IsUkPartnership),
          sautr = Some(UTRModel("1111111111"))
        )
      ElectionsSectionStatus.isPartnershipComplete(partnership) mustEqual true
    }

    "return false where isUkPartnership is answered and sautr is not populated" in {
      val partnership = 
        PartnershipModel(
          name = "name",
          isUkPartnership = Some(IsUkPartnership),
          sautr = None
        )
      ElectionsSectionStatus.isPartnershipComplete(partnership) mustEqual false
    }

  }
}