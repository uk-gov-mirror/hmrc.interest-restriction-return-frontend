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

package assets

import models.InvestorRatioMethod.{FixedRatioMethod, GroupRatioMethod}
import models.OtherInvestorGroupElections
import models.returnModels.InvestorGroupModel

object InvestorGroupITConstants {

  val investorName = "some Investor"

  val investorGroupsGroupRatioModel = InvestorGroupModel(
    investorName = investorName,
    ratioMethod = Some(GroupRatioMethod),
    otherInvestorGroupElections = Some(OtherInvestorGroupElections.allValues.toSet)
  )

  val investorGroupsFixedRatioModel = InvestorGroupModel(
    investorName = investorName,
    ratioMethod = Some(FixedRatioMethod),
    otherInvestorGroupElections = Some(OtherInvestorGroupElections.fixedRatioValues.toSet)
  )
}
