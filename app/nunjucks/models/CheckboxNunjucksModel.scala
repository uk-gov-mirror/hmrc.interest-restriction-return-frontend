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

package nunjucks.models

import models.Mode
import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json.{JsObject, Json, Writes}
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.nunjucks.NunjucksSupport


case class CheckboxNunjucksModel(options: Seq[CheckboxItem], form: Form[Set[_]], mode: Mode){

  val error: Option[JsObject] = form.errors.headOption.map(x => Json.obj("errorMessage" -> Json.obj("text" -> x.message)))
}

object CheckboxNunjucksModel extends NunjucksSupport {

  implicit def writes(implicit messages: Messages): Writes[CheckboxNunjucksModel] = Writes { model =>

    val json: JsObject = Json.obj(
      "form" -> model.form,
      "checkboxes" -> model.options,
      "mode" -> model.mode
    )

    model.error.fold(json){error => json ++ error}
  }
}