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

import models.Mode
import play.api.data.Form
import play.api.i18n.Messages
import play.api.libs.json.{JsObject, Json, OWrites}
import uk.gov.hmrc.nunjucks.NunjucksSupport

case class DateViewModel[T](form: Form[T], mode: Mode)(implicit messages: Messages) {

  val dateValue: Option[JsObject] =
    for {
      day <- form("value.day").value
      month <- form("value.month").value
      year <- form("value.year").value
    } yield Json.obj("date" -> Json.obj(
      "day" -> day,
      "month" -> month,
      "year" -> year
    ))

  val errorMsg: Option[JsObject] =
    form.errors.headOption.map(err => Json.obj("errorMessage" -> Json.obj("text" -> messages(err.message, err.args:_*))))

}

object DateViewModel extends NunjucksSupport {

  private implicit val handleOption: Option[JsObject] => JsObject = _.getOrElse(Json.obj())

  implicit def writes[T](implicit messages: Messages): OWrites[DateViewModel[T]] = OWrites { model =>
    Json.obj(
      "form" -> model.form,
      "mode" -> model.mode
    ) ++ model.dateValue ++ model.errorMsg
  }
}
