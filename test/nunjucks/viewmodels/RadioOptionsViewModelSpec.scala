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

package nunjucks.viewmodels

import base.SpecBase
import forms.mappings.Mappings
import models.{Enumerable, NormalMode, WithName}
import play.api.data._
import play.api.i18n.Messages
import play.api.libs.json.Json
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.radios.RadioItem
import uk.gov.hmrc.nunjucks.NunjucksSupport

class RadioOptionsViewModelSpec extends SpecBase with Mappings with NunjucksSupport {

  sealed trait TestEnum
  object TestEnum extends Enumerable.Implicits {
    case object Option1 extends WithName("Option1") with TestEnum
    case object Option2 extends WithName("Option2") with TestEnum

    val values: Seq[TestEnum] = Seq(Option1, Option2)

    def options(form: Form[_])(implicit messages: Messages): Seq[RadioItem] = values.map {
      value =>
        RadioItem(
          id = Some(value.toString),
          value = Some(value.toString),
          content = Text(messages(s"${value.toString}")),
          checked = form("value").value.contains(value.toString)
      )
    }

    implicit val enumerable: Enumerable[TestEnum] =
      Enumerable(values.map(v => v.toString -> v): _*)
  }

  val form: Form[_] = Form("value" -> enumerable[TestEnum]("required"))

  val formWithAnswer = form.bind(Map("value" -> "Option1"))
  val formWithError = form.bind(Map.empty[String, String])

  "RadioOptionsViewModel" when {

    "serialising to a JsObject" must {

      "return the correct Json" when {

        "form has valid boolean bound" in {
          Json.toJsObject(RadioOptionsViewModel(TestEnum.options(formWithAnswer), formWithAnswer, NormalMode)) mustBe Json.obj(
            "form" -> Json.toJson(formWithAnswer),
            "mode" -> NormalMode,
            "radios" -> TestEnum.options(formWithAnswer)
          )
        }

        "form is empty" in {
          Json.toJsObject(RadioOptionsViewModel(TestEnum.options(form), form, NormalMode)) mustBe Json.obj(
            "form" -> Json.toJson(form),
            "mode" -> NormalMode,
            "radios" -> TestEnum.options(form)
          )
        }

        "form has errors (no boolean bound)" in {

          Json.toJsObject(RadioOptionsViewModel(TestEnum.options(form), formWithError, NormalMode)) mustBe Json.obj(
            "form" -> Json.toJson(formWithError),
            "mode" -> NormalMode,
            "radios" -> TestEnum.options(form)
          )
        }
      }
    }
  }
}
