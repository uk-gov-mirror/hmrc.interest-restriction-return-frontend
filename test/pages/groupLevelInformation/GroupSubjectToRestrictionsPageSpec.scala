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

import models.{FullOrAbbreviatedReturn, UserAnswers}
import pages.behaviours.PageBehaviours
import org.scalacheck.Arbitrary.arbitrary
import pages.aboutReturn.FullOrAbbreviatedReturnPage

class GroupSubjectToRestrictionsPageSpec extends PageBehaviours {

  "GroupSubjectToRestrictionsPage" must {

    beRetrievable[Boolean](GroupSubjectToRestrictionsPage)

    beSettable[Boolean](GroupSubjectToRestrictionsPage)

    beRemovable[Boolean](GroupSubjectToRestrictionsPage)
  }

  "Cleanup" must {
    "the answer is set to 'Yes'" when {
      "the user doesn't change the answer" should {
        "no data should be cleared" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val updatedUserAnswers = for {
                foa <- userAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.values.head)
                gsr <- foa.set(GroupSubjectToRestrictionsPage, true)
                gsa <- gsr.set(GroupSubjectToReactivationsPage, true)
                irc <- gsa.set(InterestReactivationsCapPage, BigDecimal(1.23))
                da <- irc.set(DisallowedAmountPage, BigDecimal(1.23))
                uua <- da.set(GroupSubjectToRestrictionsPage, true)
              } yield uua

              val result = updatedUserAnswers.success.value

              result.get(FullOrAbbreviatedReturnPage) mustBe defined
              result.get(GroupSubjectToRestrictionsPage) mustBe defined
              result.get(DisallowedAmountPage) mustBe defined
              result.get(InterestReactivationsCapPage) mustBe defined
              result.get(GroupSubjectToReactivationsPage) mustBe defined
          }
        }
      }
      "the user changes the answer to 'No'" should {
        "clear all data after Subject To Restrictions page" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val updatedUserAnswers = for {
                foa <- userAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.values.head)
                gsr <- foa.set(GroupSubjectToRestrictionsPage, true)
                gsa <- gsr.set(GroupSubjectToReactivationsPage, true)
                irc <- gsa.set(InterestReactivationsCapPage, BigDecimal(1.23))
                da <- irc.set(DisallowedAmountPage, BigDecimal(1.23))
                uua <- da.set(GroupSubjectToRestrictionsPage, false)
              } yield uua

              val result = updatedUserAnswers.success.value

              result.get(FullOrAbbreviatedReturnPage) mustBe defined
              result.get(GroupSubjectToRestrictionsPage) mustBe defined
              result.get(DisallowedAmountPage) must not be defined
              result.get(InterestReactivationsCapPage) must not be defined
              result.get(GroupSubjectToReactivationsPage) must not be defined
          }
        }
      }
    }

    "the answer is set to 'No'" when {
      "the user doesn't change the answer" should {
        "no data should be cleared" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val updatedUserAnswers = for {
                foa <- userAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.values.head)
                gsr <- foa.set(GroupSubjectToRestrictionsPage, false)
                gsa <- gsr.set(GroupSubjectToReactivationsPage, true)
                irc <- gsa.set(InterestReactivationsCapPage, BigDecimal(1.23))
                da <- irc.set(DisallowedAmountPage, BigDecimal(1.23))
                uua <- da.set(GroupSubjectToRestrictionsPage, false)
              } yield uua

              val result = updatedUserAnswers.success.value

              result.get(FullOrAbbreviatedReturnPage) mustBe defined
              result.get(GroupSubjectToRestrictionsPage) mustBe defined
              result.get(DisallowedAmountPage) mustBe defined
              result.get(InterestReactivationsCapPage) mustBe defined
              result.get(GroupSubjectToReactivationsPage) mustBe defined
          }
        }
      }
      "the user changes the answer to 'Yes'" should {
        "clear all data after Subject To Restrictions page" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val updatedUserAnswers = for {
                foa <- userAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.values.head)
                gsr <- foa.set(GroupSubjectToRestrictionsPage, false)
                gsa <- gsr.set(GroupSubjectToReactivationsPage, true)
                irc <- gsa.set(InterestReactivationsCapPage, BigDecimal(1.23))
                da <- irc.set(DisallowedAmountPage, BigDecimal(1.23))
                uua <- da.set(GroupSubjectToRestrictionsPage, true)
              } yield uua

              val result = updatedUserAnswers.success.value

              result.get(FullOrAbbreviatedReturnPage) mustBe defined
              result.get(GroupSubjectToRestrictionsPage) mustBe defined
              result.get(DisallowedAmountPage) must not be defined
              result.get(InterestReactivationsCapPage) must not be defined
              result.get(GroupSubjectToReactivationsPage) must not be defined
          }
        }
      }
    }
  }
}
