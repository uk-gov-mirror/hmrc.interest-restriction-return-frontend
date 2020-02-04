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

import assets.constants.abbreviatedReturn.UkCompanyConstants._
import models.returnModels.abbreviatedReturnModel.UkCompanyModel
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json

class UkCompanyModelSpec extends WordSpec with Matchers {

  "UkCompanyModel" must {

    "correctly write to json" in {

      val expectedValue = ukCompanyJson
      val actualValue = Json.toJson(ukCompanyModel)

      actualValue shouldBe expectedValue
    }
  }
}
