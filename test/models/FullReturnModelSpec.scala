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

import assets.constants.BaseConstants
import base.SpecBase

class FullReturnModelSpec extends SpecBase with BaseConstants {
  "A full return model" when {
    "Populating it" should {
      "Return nothing if any of the sections is not complete" in {
        val model = FullReturnModel.load(emptyUserAnswers)

        model mustBe None
      }

      "Return a populated model if all relevant sections are complete" in {
        val model = FullReturnModel.load(completeUserAnswers)

        model mustBe defined
      }
    }
  }
}
