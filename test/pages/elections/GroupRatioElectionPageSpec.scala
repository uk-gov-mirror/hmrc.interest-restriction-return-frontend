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

import models.{FullOrAbbreviatedReturn, UserAnswers}
import org.scalacheck.Arbitrary.arbitrary
import pages.aboutReturn.FullOrAbbreviatedReturnPage
import pages.behaviours.PageBehaviours

class GroupRatioElectionPageSpec extends PageBehaviours {

  "GroupRatioElectionPage" must {

    beRetrievable[Boolean](GroupRatioElectionPage)

    beSettable[Boolean](GroupRatioElectionPage)

    beRemovable[Boolean](GroupRatioElectionPage)
  }

  "Cleanup" when {
    "a user has selected yes" when {
      "they revisit the page and don't change the answer" should {
        "no data should be cleared" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val newUserAnswers = for {
                foa <- userAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.values.head)
                gre <- foa.set(GroupRatioElectionPage, true)
                egeb <- gre.set(ElectedGroupEBITDABeforePage, true)
                newUa <- egeb.set(GroupRatioElectionPage, true)
              } yield newUa

              val result = newUserAnswers.get

              result.get(FullOrAbbreviatedReturnPage) mustBe defined
              result.get(GroupRatioElectionPage) mustBe defined
              result.get(ElectedGroupEBITDABeforePage) mustBe defined
          }
        }
      }
      "they revisit the page and change the answer to no" should {
        "all data should be cleared after group ratio election page" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val newUserAnswers = for {
                foa <- userAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.values.head)
                gre <- foa.set(GroupRatioElectionPage, true)
                egeb <- gre.set(ElectedGroupEBITDABeforePage, true)
                newUa <- egeb.set(GroupRatioElectionPage, false)
              } yield newUa

              val result = newUserAnswers.get

              result.get(FullOrAbbreviatedReturnPage) mustBe defined
              result.get(GroupRatioElectionPage) mustBe defined
              result.get(ElectedGroupEBITDABeforePage) must not be defined
          }
        }
      }
    }
    "a user has selected no" when {
      "they revisit the page and don't change the answer" should {
        "no data should be cleared" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val newUserAnswers = for {
                foa <- userAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.values.head)
                gre <- foa.set(GroupRatioElectionPage, false)
                egeb <- gre.set(ElectedGroupEBITDABeforePage, true)
                newUa <- egeb.set(GroupRatioElectionPage, false)
              } yield newUa

              val result = newUserAnswers.get

              result.get(FullOrAbbreviatedReturnPage) mustBe defined
              result.get(GroupRatioElectionPage) mustBe defined
              result.get(ElectedGroupEBITDABeforePage) mustBe defined
          }
        }
      }
      "they revisit the page and change the answer to yes" should {
        "all data should be cleared after group ratio election page" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val newUserAnswers = for {
                foa <- userAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.values.head)
                gre <- foa.set(GroupRatioElectionPage, false)
                egeb <- gre.set(ElectedGroupEBITDABeforePage, true)
                newUa <- egeb.set(GroupRatioElectionPage, true)
              } yield newUa

              val result = newUserAnswers.get

              result.get(FullOrAbbreviatedReturnPage) mustBe defined
              result.get(GroupRatioElectionPage) mustBe defined
              result.get(ElectedGroupEBITDABeforePage) must not be defined
          }
        }
      }
    }
  }

}
