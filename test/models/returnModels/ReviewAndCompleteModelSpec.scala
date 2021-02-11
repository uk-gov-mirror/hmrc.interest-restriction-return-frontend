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

package models.returnModels

import assets.constants.ReviewAndCompleteConstants._
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json.Json

class ReviewAndCompleteModelSpec extends WordSpec with MustMatchers {

  "ReviewAndCompleteModel" must {

    "correctly write to json" in {

      val expectedValue = reviewAndCompleteJson
      val actualValue = Json.toJson(reviewAndCompleteModel)

      actualValue mustBe expectedValue
    }

    "correctly reads from json" in {

      val expectedValue = reviewAndCompleteModel
      val actualValue = reviewAndCompleteJson.as[ReviewAndCompleteModel]

      actualValue mustBe expectedValue
    }

    "determine current statuses when instantiated" in {

      1 mustBe 2

    }
  }
}
