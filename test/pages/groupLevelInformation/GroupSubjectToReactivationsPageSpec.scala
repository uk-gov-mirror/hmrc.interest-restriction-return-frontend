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

import models.{CompanyDetailsModel, UserAnswers}
import pages.behaviours.PageBehaviours
import org.scalacheck.Arbitrary.arbitrary
import pages.ukCompanies.CompanyDetailsPage

class GroupSubjectToReactivationsPageSpec extends PageBehaviours {

  "GroupSubjectToReactivationsPage" must {

    beRetrievable[Boolean](GroupSubjectToReactivationsPage)

    beSettable[Boolean](GroupSubjectToReactivationsPage)

    beRemovable[Boolean](GroupSubjectToReactivationsPage)
  }

  "Cleanup" must {
      "Remove all answers when there is a change of the answer to 'No'" in {
          forAll(arbitrary[UserAnswers]) {
            userAnswers =>
              val result = userAnswers              
                .set(GroupSubjectToReactivationsPage,true).success.value
                .set(InterestReactivationsCapPage,BigDecimal(1)).success.value
                .set(CompanyDetailsPage,CompanyDetailsModel("test","test")).success.value
                .set(GroupSubjectToReactivationsPage,false).success.value


              result.get(GroupSubjectToReactivationsPage) mustBe defined
              result.get(CompanyDetailsPage) must not be defined
          }
        }

      "Remove all answers when there is a change of the answer to 'Yes'" in {
        forAll(arbitrary[UserAnswers]) {
          userAnswers =>
            val result = userAnswers              
              .set(GroupSubjectToReactivationsPage,false).success.value
              .set(InterestReactivationsCapPage,BigDecimal(1)).success.value
              .set(CompanyDetailsPage,CompanyDetailsModel("test","test")).success.value
              .set(GroupSubjectToReactivationsPage,true).success.value


            result.get(GroupSubjectToReactivationsPage) mustBe defined
            result.get(CompanyDetailsPage) must not be defined
        }
      }

      "Not do anything if we select 'No' and our previous answer was 'No'" in {
        forAll(arbitrary[UserAnswers]) {
          userAnswers =>
            val result = userAnswers
              .set(GroupSubjectToReactivationsPage,false).success.value
              .set(InterestReactivationsCapPage,BigDecimal(1)).success.value
              .set(CompanyDetailsPage,CompanyDetailsModel("test","test")).success.value
              .set(GroupSubjectToReactivationsPage,false).success.value


            result.get(GroupSubjectToReactivationsPage) mustBe defined
            result.get(CompanyDetailsPage) mustBe defined
        }
      }

    "Not do anything if we select 'Yes' and our previous answer was 'Yes'" in {
      forAll(arbitrary[UserAnswers]) {
        userAnswers =>
          val result = userAnswers
            .set(GroupSubjectToReactivationsPage,true).success.value
            .set(InterestReactivationsCapPage,BigDecimal(1)).success.value
            .set(CompanyDetailsPage,CompanyDetailsModel("test","test")).success.value
            .set(GroupSubjectToReactivationsPage,true).success.value


          result.get(GroupSubjectToReactivationsPage) mustBe defined
          result.get(CompanyDetailsPage) mustBe defined
      }
    }
    }
}

