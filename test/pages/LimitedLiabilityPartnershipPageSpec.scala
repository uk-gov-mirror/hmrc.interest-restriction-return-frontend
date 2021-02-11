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

package pages

import models.UserAnswers
import models.returnModels.{CompanyNameModel, DeemedParentModel, UTRModel}
import org.scalacheck.Arbitrary.arbitrary
import pages.behaviours.PageBehaviours
import pages.ultimateParentCompany.{DeemedParentPage, LimitedLiabilityPartnershipPage}

class LimitedLiabilityPartnershipPageSpec extends PageBehaviours {

  "LimitedLiabilityPartnershipPage" must {

    beRetrievable[Boolean](LimitedLiabilityPartnershipPage)

    beSettable[Boolean](LimitedLiabilityPartnershipPage)

    beRemovable[Boolean](LimitedLiabilityPartnershipPage)
  }

  "Cleanup" when {
    "A user changes LLP to 'Yes'" should {
      "Clear the CTUTR" in {
        forAll(arbitrary[UserAnswers]) {
          userAnswers =>
            val deemedParent = DeemedParentModel(CompanyNameModel("Test Company"), Some(UTRModel("1123456789")))

            val result = userAnswers
              .set(DeemedParentPage, deemedParent, Some(1)).success.value
              .set(LimitedLiabilityPartnershipPage, true, Some(1)).success.value

            val parentResult: Option[DeemedParentModel] = result.get(DeemedParentPage, Some(1))
            parentResult.get.ctutr must not be defined
        }
      }
    }
    "A user changes LLP to 'No'" should {
      "Clear the STUTR" in {
        forAll(arbitrary[UserAnswers]) {
          userAnswers =>
            val deemedParent = DeemedParentModel(CompanyNameModel("Test Company"), None, Some(UTRModel("1123456789")))

            val result = userAnswers
              .set(DeemedParentPage, deemedParent, Some(1)).success.value
              .set(LimitedLiabilityPartnershipPage, false, Some(1)).success.value

            val parentResult: Option[DeemedParentModel] = result.get(DeemedParentPage, Some(1))
            parentResult.get.sautr must not be defined
        }
      }
    }
  }
}
