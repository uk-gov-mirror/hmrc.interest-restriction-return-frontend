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

package models

import assets.constants.DeemedParentConstants._
import base.SpecBase
import pages.ultimateParentCompany.DeemedParentPage
import play.api.libs.json.Json
import scala.util.Success
import pages.ukCompanies.RestrictionQueryHelper
import pages.ukCompanies.AddRestrictionAmountPage

class UserAnswersSpec extends SpecBase {

  "UserAnswers" when {

    "calling getList" when {

      "key exists with a list of data" must {

        "return Seq of DeemedParents" in {

          val userAnswers = emptyUserAnswers
            .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).get
            .set(DeemedParentPage, deemedParentModelUkPartnership, Some(2)).get
            .set(DeemedParentPage, deemedParentModelNonUkCompany, Some(3)).get

          val result = userAnswers.getList(DeemedParentPage)

          result mustBe Seq(
            deemedParentModelUkCompany,
            deemedParentModelUkPartnership,
            deemedParentModelNonUkCompany
          )
        }
      }

      "key exists with no data" must {

        "return an empty sequence" in {

          val userAnswers = emptyUserAnswers.copy(data = Json.obj(
            DeemedParentPage.toString -> Json.arr()
          ))

          val result = userAnswers.getList(DeemedParentPage)

          result mustBe Seq.empty
        }
      }

      "key doesn't exist" must {

        "return an empty sequence" in {

          val result = emptyUserAnswers.getList(DeemedParentPage)

          result mustBe Seq.empty
        }
      }
    }

    "remove(path)" must {

      "remove all objects within the defined path" in {
        val userAnswers = 
          (for {
            ua        <- emptyUserAnswers.set(AddRestrictionAmountPage(1,1), true)
            ua2       <- ua.set(AddRestrictionAmountPage(1,2), false)
            ua3       <- ua2.set(AddRestrictionAmountPage(1,3), true)
            ua4       <- ua3.set(AddRestrictionAmountPage(2,1), false)
            finalUa   <- ua4.set(AddRestrictionAmountPage(3,1), true)
          } yield finalUa).get

          userAnswers.remove(RestrictionQueryHelper.restrictionCompanyPath) mustEqual Success(emptyUserAnswers)
      }

    }
  }

}