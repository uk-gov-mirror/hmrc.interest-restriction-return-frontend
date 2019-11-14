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

import models.Mode
import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json.{JsObject, Json, OWrites}
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.nunjucks.NunjucksSupport


case class CheckboxViewModel[T](options: Seq[CheckboxItem], form: Form[Set[T]], mode: Mode)(implicit messages: Messages){

  val error: Option[JsObject] = form.errors.headOption.map(x => Json.obj("errorMessage" -> Json.obj("text" -> messages(x.message))))
}

object CheckboxViewModel extends NunjucksSupport {

  implicit def writes[T](implicit messages: Messages): OWrites[CheckboxViewModel[T]] = OWrites { model =>

    val json: JsObject = Json.obj(
      "form" -> model.form,
      "checkboxes" -> model.options,
      "mode" -> model.mode
    )

    model.error.fold(json){json ++ _}
  }
}