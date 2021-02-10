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
import models.returnModels.{CompanyNameModel, DeemedParentModel}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours

class DeemedParentPageSpec extends PageBehaviours {

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
                .set(ParentCompanyNamePage, "company name").success.value
                .set(PayTaxInUkPage, true).success.value
                .set(LimitedLiabilityPartnershipPage, true).success.value
                .set(ParentCompanyCTUTRPage, "111111111").success.value
                .set(ParentCompanySAUTRPage, "222222222").success.value
                .set(CountryOfIncorporationPage, "Germany").success.value
                .set(HasDeemedParentPage, true).success.value

              result.get(HasDeemedParentPage) mustBe defined
              result.get(PayTaxInUkPage) mustBe defined
              result.get(LimitedLiabilityPartnershipPage) mustBe defined
              result.get(ParentCompanyCTUTRPage) mustBe defined
              result.get(ParentCompanySAUTRPage) mustBe defined
              result.get(CountryOfIncorporationPage) mustBe defined
          }
        }
      }
      "they revisit the page and change the answer to no" should {
        "delete any data in this section after has deemed parent" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers
                .set(HasDeemedParentPage, true).success.value
                .set(ParentCompanyNamePage, "company name").success.value
                .set(PayTaxInUkPage, true).success.value
                .set(LimitedLiabilityPartnershipPage, true).success.value
                .set(ParentCompanyCTUTRPage, "111111111").success.value
                .set(ParentCompanySAUTRPage, "222222222").success.value
                .set(CountryOfIncorporationPage, "Germany").success.value
                .set(HasDeemedParentPage, false).success.value

              result.get(HasDeemedParentPage) mustBe defined
              result.get(PayTaxInUkPage) must not be defined
              result.get(LimitedLiabilityPartnershipPage) must not be defined
              result.get(ParentCompanyCTUTRPage) must not be defined
              result.get(ParentCompanySAUTRPage) must not be defined
              result.get(CountryOfIncorporationPage) must not be defined
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
                .set(ParentCompanyNamePage, "company name").success.value
                .set(PayTaxInUkPage, true).success.value
                .set(LimitedLiabilityPartnershipPage, true).success.value
                .set(ParentCompanyCTUTRPage, "111111111").success.value
                .set(ParentCompanySAUTRPage, "222222222").success.value
                .set(CountryOfIncorporationPage, "Germany").success.value
                .set(HasDeemedParentPage, false).success.value

              result.get(ParentCompanyNamePage) mustBe defined
              result.get(PayTaxInUkPage) mustBe defined
              result.get(LimitedLiabilityPartnershipPage) mustBe defined
              result.get(ParentCompanyCTUTRPage) mustBe defined
              result.get(ParentCompanySAUTRPage) mustBe defined
              result.get(CountryOfIncorporationPage) mustBe defined
          }
        }
      }
      "they revisit the page and change the answer to yes" should {
        "delete any data in this section after has deemed parent" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers
                .set(HasDeemedParentPage, false).success.value
                .set(ParentCompanyNamePage, "company name").success.value
                .set(PayTaxInUkPage, true).success.value
                .set(LimitedLiabilityPartnershipPage, true).success.value
                .set(ParentCompanyCTUTRPage, "111111111").success.value
                .set(ParentCompanySAUTRPage, "222222222").success.value
                .set(CountryOfIncorporationPage, "Germany").success.value
                .set(HasDeemedParentPage, true).success.value

              result.get(HasDeemedParentPage) mustBe defined
              result.get(PayTaxInUkPage) must not be defined
              result.get(LimitedLiabilityPartnershipPage) must not be defined
              result.get(ParentCompanyCTUTRPage) must not be defined
              result.get(ParentCompanySAUTRPage) must not be defined
              result.get(CountryOfIncorporationPage) must not be defined
          }
        }
      }
    }
  }
}
