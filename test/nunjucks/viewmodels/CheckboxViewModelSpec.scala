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
import play.api.data.Form
import play.api.data.Forms.set
import play.api.libs.json.{JsObject, Json}
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.nunjucks.NunjucksSupport

class CheckboxViewModelSpec extends SpecBase with Mappings with NunjucksSupport {

  "MyNewPage" must {

    val form: Form[Set[String]] =
      Form(
        "value" -> set(text("myNewPage.error.required"))
          .verifying(nonEmptySet("myNewPage.error.required"))
      )

    def expectedResult(form: Form[Set[String]]) = Json.obj(
      "form" ->
        Json.toJson(form),
      "checkboxes" -> Seq(
        Json.obj(
          "text" -> "option1",
          "id" -> "option1",
          "value" -> "",
          "checked" -> false,
          "disabled" -> false,
          "attributes" -> Json.obj()
        ),
        Json.obj(
          "text" -> "option2",
          "id" -> "option2",
          "value" -> "",
          "checked" -> false,
          "disabled" -> false,
          "attributes" -> Json.obj()
        ),
        Json.obj(
          "text" -> "option3",
          "id" -> "option3",
          "value" -> "",
          "checked" -> false,
          "disabled" -> false,
          "attributes" -> Json.obj()
        )
      ),
      "mode" -> NormalMode
    )

    def actualResult(form: Form[Set[String]]) = Json.toJsObject(CheckboxViewModel(
      Seq(
        CheckboxItem(Text("option1"), Some("option1")),
        CheckboxItem(Text("option2"), Some("option2")),
        CheckboxItem(Text("option3"), Some("option3"))
      ),
      form,
      NormalMode
    ))


    "write correctly to Json" when {

      "valid value is supplied" in {

        val bindedForm: Form[Set[String]] = form.bind(Map("value[0]" -> "option1"))

        expectedResult(bindedForm) mustBe actualResult(bindedForm)
      }

      "invalid value is supplied" in {

        val bindedForm: Form[Set[String]] = form.bind(Map("value[0]" -> "invalid"))

        expectedResult(bindedForm) mustBe actualResult(bindedForm)
      }

      "no value is supplied" in {
        expectedResult(form) mustBe actualResult(form)
      }
    }
  }
}
