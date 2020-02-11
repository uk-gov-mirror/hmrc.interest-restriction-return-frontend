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

package models.returnModels

import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json.Json
import assets.constants.ConsolidatedPartnershipConstants._

class ConsolidatedPartnershipModelSpec extends WordSpec with MustMatchers {

  "ConsolidatedPartnershipModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = consolidatedPartnershipsJsonMax
        val actualValue = Json.toJson(consolidatedPartnershipsModelMax)

        actualValue mustBe expectedValue
      }

      "min values given" in {

        val expectedValue = consolidatedPartnershipsJsonMin
        val actualValue =  Json.toJson(consolidatedPartnershipsModelMin)

        actualValue mustBe expectedValue
      }

    }
  }
}

