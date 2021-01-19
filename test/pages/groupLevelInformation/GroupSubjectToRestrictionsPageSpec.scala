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

package pages.groupLevelInformation

import models.UserAnswers
import pages.behaviours.PageBehaviours
import org.scalacheck.Arbitrary.arbitrary

class GroupSubjectToRestrictionsPageSpec extends PageBehaviours {

  "GroupSubjectToRestrictionsPage" must {

    beRetrievable[Boolean](GroupSubjectToRestrictionsPage)

    beSettable[Boolean](GroupSubjectToRestrictionsPage)

    beRemovable[Boolean](GroupSubjectToRestrictionsPage)
  }

  "Cleanup" must {
    "remove subject to restrictions and reactivation cap when there is a change of the answer to 'Yes'" in {
      forAll(arbitrary[UserAnswers]) {
        userAnswers =>
          val result = userAnswers
            .set(GroupSubjectToReactivationsPage, true).success.value
            .set(InterestReactivationsCapPage, BigDecimal(1.23)).success.value
            .set(DisallowedAmountPage, BigDecimal(1.23)).success.value
            .set(GroupSubjectToRestrictionsPage, true).success.value

          result.get(DisallowedAmountPage) must be ('defined)
          result.get(InterestReactivationsCapPage) must not be defined
          result.get(GroupSubjectToReactivationsPage) must not be defined
      }
    }

    "remove disallowed amount when there is a change of the answer to 'No'" in {
      forAll(arbitrary[UserAnswers]) {
        userAnswers =>
          val result = userAnswers
            .set(GroupSubjectToReactivationsPage, true).success.value
            .set(InterestReactivationsCapPage, BigDecimal(1.23)).success.value
            .set(DisallowedAmountPage, BigDecimal(1.23)).success.value
            .set(GroupSubjectToRestrictionsPage, false).success.value

          result.get(DisallowedAmountPage) must not be defined
          result.get(GroupSubjectToReactivationsPage) must be ('defined)
          result.get(InterestReactivationsCapPage) must be ('defined)
      }
    }
  }
}
