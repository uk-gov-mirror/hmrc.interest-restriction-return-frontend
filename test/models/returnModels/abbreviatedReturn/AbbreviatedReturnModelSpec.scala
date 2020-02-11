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

package models.returnModels.abbreviatedReturn

import assets.constants.BaseConstants
import assets.constants.abbreviatedReturn.AbbreviatedReturnConstants._
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json.Json

class AbbreviatedReturnModelSpec extends WordSpec with MustMatchers with BaseConstants {

  "AbbreviatedReturnModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = abbreviatedReturnUltimateParentJson
        val actualValue = Json.toJson(abbreviatedReturnUltimateParentModel)

        actualValue mustBe expectedValue
      }

      "min values given" in {

        val expectedValue = abbreviatedReturnJsonMin
        val actualValue = Json.toJson(abbreviatedReturnModelMin)

        actualValue mustBe expectedValue
      }
    }
  }
}
