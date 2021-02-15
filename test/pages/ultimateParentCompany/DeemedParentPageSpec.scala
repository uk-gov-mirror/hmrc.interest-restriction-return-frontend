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

package pages.ultimateParentCompany

import models.UserAnswers
import models.returnModels.{CompanyNameModel, CountryCodeModel, DeemedParentModel, UTRModel}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class DeemedParentPageSpec extends PageBehaviours {

  private val deemedParentModel = new DeemedParentModel(
    companyName = CompanyNameModel("company name"),
    ctutr = Some(UTRModel("111111111")),
    sautr = Some(UTRModel("222222222")),
    countryOfIncorporation = Some(CountryCodeModel("US", "United States of America")),
    limitedLiabilityPartnership = Some(true),
    payTaxInUk = Some(true),
    reportingCompanySameAsParent = Some(true)
  )

  "DeemedParentPage" must {

    beRetrievable[Boolean](HasDeemedParentPage)

    beSettable[Boolean](HasDeemedParentPage)

    beRemovable[Boolean](HasDeemedParentPage)
  }

  "Cleanup" when {
    "a user has selected yes" when {
      "they revisit the page but do not change the answer" should {
        "not delete any data" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers
                .set(HasDeemedParentPage, true).success.value
                .set(DeemedParentPage, deemedParentModel).success.value
                .set(HasDeemedParentPage, true).success.value

              result.get(HasDeemedParentPage) mustBe defined
              val deemedParentPage = result.get(DeemedParentPage)
              deemedParentPage mustBe defined
              deemedParentPage.get.payTaxInUk mustBe defined
              deemedParentPage.get.limitedLiabilityPartnership mustBe defined
              deemedParentPage.get.ctutr mustBe defined
              deemedParentPage.get.sautr mustBe defined
              deemedParentPage.get.countryOfIncorporation mustBe defined
          }
        }
      }
      "they revisit the page and change the answer to no" should {
        "delete any data in this section after has deemed parent" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers
                .set(HasDeemedParentPage, true).success.value
                .set(DeemedParentPage, deemedParentModel).success.value
                .set(HasDeemedParentPage, false).success.value

              result.get(HasDeemedParentPage) mustBe defined
              val deemedParentPage = result.get(DeemedParentPage)
              deemedParentPage must not be defined
          }
        }
      }
    }
    "a user has selected no" when {
      "they revisit the page but do not change the answer" should {
        "not delete any data" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers
                .set(HasDeemedParentPage, false).success.value
                .set(DeemedParentPage, deemedParentModel).success.value
                .set(HasDeemedParentPage, false).success.value

              result.get(HasDeemedParentPage) mustBe defined
              val deemedParentPage = result.get(DeemedParentPage)
              deemedParentPage mustBe defined
              deemedParentPage.get.payTaxInUk mustBe defined
              deemedParentPage.get.limitedLiabilityPartnership mustBe defined
              deemedParentPage.get.ctutr mustBe defined
              deemedParentPage.get.sautr mustBe defined
              deemedParentPage.get.countryOfIncorporation mustBe defined
          }
        }
      }
      "they revisit the page and change the answer to yes" should {
        "delete any data in this section after has deemed parent" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers
                .set(HasDeemedParentPage, false).success.value
                .set(DeemedParentPage, deemedParentModel).success.value
                .set(HasDeemedParentPage, true).success.value

              result.get(HasDeemedParentPage) mustBe defined
              val deemedParentPage = result.get(DeemedParentPage)
              deemedParentPage must not be defined
          }
        }
      }
    }
  }
}
