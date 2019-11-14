/*
 * Copyright 2019 HM Revenue & Customs
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

package nunjucks.viewmodels

import akka.http.scaladsl.model
import base.SpecBase
import forms.mappings.Mappings
import models.NormalMode
import play.api.data._
import play.api.libs.json.Json
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios

class YesNoRadioViewModelSpec extends SpecBase with Mappings with NunjucksSupport {

  val form: Form[_] = Form("value" -> boolean("required"))

  val formWithAnswer = form.bind(Map("value" -> "true"))
  val formWithError = form.bind(Map.empty[String, String])

  "YesNoRadioViewModel" when {

    "serialising to a JsObject" must {

      "return the correct Json" when {

        "form has valid boolean bound" in {
          Json.toJsObject(YesNoRadioViewModel(formWithAnswer, NormalMode)) mustBe Json.obj(
            "form" -> Json.toJson(formWithAnswer),
            "mode" -> NormalMode,
            "radios" -> Radios.yesNo(formWithAnswer("value"))
          )
        }

        "form is empty" in {
          Json.toJsObject(YesNoRadioViewModel(form, NormalMode)) mustBe Json.obj(
            "form" -> Json.toJson(form),
            "mode" -> NormalMode,
            "radios" -> Radios.yesNo(form("value"))
          )
        }

        "form has errors (no boolean bound)" in {

          Json.toJsObject(YesNoRadioViewModel(formWithError, NormalMode)) mustBe Json.obj(
            "form" -> Json.toJson(formWithError),
            "mode" -> NormalMode,
            "radios" -> Radios.yesNo(formWithError("value"))
          )
        }
      }
    }
  }
}
