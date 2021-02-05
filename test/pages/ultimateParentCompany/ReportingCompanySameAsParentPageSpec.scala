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

class ReportingCompanySameAsParentPageSpec extends PageBehaviours {

  "ReportingCompanySameAsParentPage" must {

    beRetrievable[Boolean](ReportingCompanySameAsParentPage)

    beSettable[Boolean](ReportingCompanySameAsParentPage)

    beRemovable[Boolean](ReportingCompanySameAsParentPage)
  }

  "Cleanup" when {
    "We select yes on ReportingCompanySameAsParentPage" should {
      "clear deemedParent answer" in {
        forAll(arbitrary[UserAnswers]) {
          userAnswers =>
            val result = userAnswers
              .set(HasDeemedParentPage,true).success.value
              .set(ReportingCompanySameAsParentPage,true).success.value


            result.get(HasDeemedParentPage) must not be defined
            result.get(ReportingCompanySameAsParentPage) mustBe defined
        }
      }
      "clear all deemed parents" in {
        forAll(arbitrary[UserAnswers]) {
          userAnswers =>
            val deemedParent = DeemedParentModel(CompanyNameModel("Test Company"))
            val result = userAnswers
              .set(DeemedParentPage,deemedParent,Some(1)).success.value
              .set(DeemedParentPage,deemedParent,Some(2)).success.value
              .set(DeemedParentPage,deemedParent,Some(3)).success.value
              .set(ReportingCompanySameAsParentPage,true).success.value

            result.get(DeemedParentPage,Some(1)) must not be defined
            result.get(DeemedParentPage,Some(2)) must not be defined
            result.get(DeemedParentPage,Some(3)) must not be defined
            result.get(ReportingCompanySameAsParentPage) mustBe defined
        }
      }
    }
  }
}
