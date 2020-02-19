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

package services

import assets.constants.BaseConstants
import base.SpecBase
import models.ErrorModel
import pages.groupStructure.ParentCompanyNamePage
import play.api.libs.json.{JsPath, Json}


class DeemedParentServiceSpec extends SpecBase with BaseConstants {

  object TestDeemedParentService extends DeemedParentService(sessionRepository)

  "DeemedParentService" when {

    "calling the method to add a deemed parent" when {

      "no data is held for the parent company flow in user answers" should {

        "return an error" in {

          await(TestDeemedParentService.addDeemedParent(emptyUserAnswers)) mustBe
            Left(ErrorModel("Cannot Construct Deemed Parent Model from User Answers as Company Name is missing"))
        }
      }

      "valid data is held in the parent company flow but an error exists in the stored json" should {

        "return an error" in {

          val userAnswers = emptyUserAnswers.set(ParentCompanyNamePage, companyNameModel.name).success.value
            .addToList(JsPath \ "deemedParents", Json.obj()).success.value

          await(TestDeemedParentService.addDeemedParent(userAnswers)) mustBe
            Left(ErrorModel("Failed to add to list of deemed parents"))
        }
      }

      "valid data is held in the parent company and no existing data exists for deemed" should {

        "return a success response" in {

          val userAnswers = emptyUserAnswers.set(ParentCompanyNamePage, companyNameModel.name).success.value

          await(TestDeemedParentService.addDeemedParent(userAnswers)) mustBe Right(AddedDeemedParentSuccess)
        }
      }
    }
  }
}
