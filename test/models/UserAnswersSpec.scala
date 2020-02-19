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

package models

import assets.constants.DeemedParentConstants._
import base.SpecBase
import play.api.libs.json.{Json, __}

import scala.util.Success

class UserAnswersSpec extends SpecBase {

  "UserAnswers" when {

    "calling addToList" when {

      "key exists with a list of data" must {

        "return the updated user answers" in {
          val userAnswers = emptyUserAnswers.copy(data = Json.obj("deemedParents" -> Json.arr(
            Json.toJson(deemedParentModelUkPartnership)
          )))

          val result = userAnswers.addToList(__ \ "deemedParents", deemedParentModelUkCompany)

          result mustBe Success(userAnswers.copy(data = Json.obj("deemedParents" -> Json.arr(
            Json.toJson(deemedParentModelUkPartnership),
            Json.toJson(deemedParentModelUkCompany)
          ))))
        }
      }

      "key exists with empty array" must {

        "return the updated user answers with first entry" in {

          val userAnswers = emptyUserAnswers.copy(data = Json.obj("deemedParents" -> Json.arr()))

          val result = userAnswers.addToList(__ \ "deemedParents", deemedParentModelUkCompany)

          result mustBe Success(userAnswers.copy(data = Json.obj("deemedParents" -> Json.arr(
            Json.toJson(deemedParentModelUkCompany)
          ))))
        }
      }

      "key does not exist" must {

        "return the updated user answers with first entry" in {
          val result = emptyUserAnswers.addToList(__ \ "deemedParents", deemedParentModelUkCompany)

          result mustBe Success(emptyUserAnswers .copy(data = Json.obj("deemedParents" -> Json.arr(
            Json.toJson(deemedParentModelUkCompany)
          ))))
        }
      }

      "key exists but with invalid data" must {

        "return the updated user answers with first entry" in {

          val userAnswers = emptyUserAnswers.copy(data = Json.obj("deemedParents" -> Json.arr(
            Json.obj("foo" -> "bar")
          )))

          val result = userAnswers.addToList(__ \ "deemedParents", deemedParentModelUkCompany)

          result.isFailure mustBe true
        }
      }
    }
  }
}