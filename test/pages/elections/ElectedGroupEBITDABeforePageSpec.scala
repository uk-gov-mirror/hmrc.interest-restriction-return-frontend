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
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class ElectedGroupEBITDABeforePageSpec extends PageBehaviours {

  "ElectedGroupEBITDABeforePage" must {

    beRetrievable[Boolean](ElectedGroupEBITDABeforePage)

    beSettable[Boolean](ElectedGroupEBITDABeforePage)

    beRemovable[Boolean](ElectedGroupEBITDABeforePage)

    "Cleanup" when {
      "a user has selected yes" when {
        "they revisit the page and change the answer to no" should {
          "not delete any data" in {
            forAll(arbitrary[UserAnswers]) {
              userAnswers =>
                val result = userAnswers
                  .set(ElectedGroupEBITDABeforePage, true).success.value
                  .set(ElectedInterestAllowanceAlternativeCalcBeforePage, true).success.value
                  .set(ElectedGroupEBITDABeforePage, false).success.value

                result.get(ElectedGroupEBITDABeforePage) mustBe defined
                result.get(ElectedInterestAllowanceAlternativeCalcBeforePage) mustBe defined
            }
          }
        }
      }
      "a user has selected no" when {
        "they revisit the page and change the answer to yes" should {
          "deleted only elect group EBITDA data data" in {
            forAll(arbitrary[UserAnswers]) {
              userAnswers =>
                val result = userAnswers
                  .set(ElectedGroupEBITDABeforePage, false).success.value
                  .set(GroupEBITDAChargeableGainsElectionPage, true).success.value
                  .set(ElectedInterestAllowanceAlternativeCalcBeforePage, true).success.value
                  .set(ElectedGroupEBITDABeforePage, true).success.value

                result.get(ElectedGroupEBITDABeforePage) mustBe defined
                result.get(GroupEBITDAChargeableGainsElectionPage) must not be defined
                result.get(ElectedInterestAllowanceAlternativeCalcBeforePage) mustBe defined
            }
          }
        }
      }
    }

  }
}
