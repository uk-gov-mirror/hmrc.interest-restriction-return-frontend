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

import base.SpecBase
import forms.mappings.Mappings
import models.NormalMode
import play.api.data._
import play.api.libs.json.Json
import uk.gov.hmrc.nunjucks.NunjucksSupport

class DateViewModelSpec extends SpecBase with Mappings with NunjucksSupport {

  val form: Form[_] = Form("value" -> localDate(
    invalidKey     = "invalid",
    allRequiredKey = "all",
    twoRequiredKey = "two",
    requiredKey    = "required"
  ))

  val formWithDate = form.bind(Map("value.day" -> "12", "value.month" -> "09", "value.year" -> "1990"))
  val formWithError = form.bind(Map.empty[String, String])

  "DateViewModel" when {

    "calling the .dateValue(form, mode)" when {

      "A date is bound to the form" must {

        "serialize date to JSON correctly" in {

          DateViewModel(formWithDate, NormalMode).dateValue mustBe Some(Json.obj("date" -> Json.obj(
            "day" -> "12",
            "month" -> "09",
            "year" -> "1990"
          )))
        }
      }

      "A date is NOT bound to the form" must {

        "return None" in {

          DateViewModel(form, NormalMode).dateValue mustBe None
        }
      }
    }

    "calling the .errorMessage" when {

      "An error is bound to the form" must {

        "serialize date to JSON correctly" in {

          DateViewModel(formWithError, NormalMode).errorMsg mustBe Some(Json.obj("errorMessage" -> Json.obj(
            "text" -> "all"
          )))
        }
      }

      "An error is NOT bound to the form" must {

        "return None" in {

          DateViewModel(formWithDate, NormalMode).errorMsg mustBe None
        }
      }
    }

    "serialising the whole case class to a JsObject" must {

      "return the correct Json" when {

        "form has valid date bound" in {
          Json.toJsObject(DateViewModel(formWithDate, NormalMode)) mustBe Json.obj(
            "form" -> Json.toJson(formWithDate),
            "mode" -> NormalMode,
            "date" -> Json.obj(
              "day" -> "12",
              "month" -> "09",
              "year" -> "1990"
            )
          )
        }

        "form is empty (no value bound)" in {
          Json.toJsObject(DateViewModel(form, NormalMode)) mustBe Json.obj(
            "form" -> Json.toJson(form),
            "mode" -> NormalMode
          )
        }

        "form has errors (missing date bound)" in {

          Json.toJsObject(DateViewModel(formWithError, NormalMode)) mustBe Json.obj(
            "form" -> Json.toJson(formWithError),
            "mode" -> NormalMode,
            "errorMessage" -> Json.obj(
              "text" -> "all"
            )
          )
        }
      }
    }
  }
}
