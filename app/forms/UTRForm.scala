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

package forms

import forms.mappings.Mappings
import pages.Page
import play.api.data.Form
import play.api.data.Forms
import utils.RemoveWhitespace
import play.api.data.Forms.optional

trait UTRForm extends Mappings with UTRFormValidation with RemoveWhitespace {

  def utrForm(page: Page, value: String = "value"): Form[String] =
    Form(
      s"$value" -> text(s"${page.toString}.error.required")
        .transform(
          (value: String) => removeWhitespace(value): String,
          (x: String) => x
        )
        .verifying(regexp("^[0-9]{10}$", s"${page.toString}.error.regexp"))
        .verifying(checksum(s"${page.toString}.error.checksum"))
    )

  def optionalUtrForm(page: Page, value: String = "value"): Form[Option[String]] =
    Form(
      s"$value" -> optional(Forms.text)
        .transform(
          (value: Option[String]) => value.map(removeWhitespace),
          (x: Option[String]) => x
        )
        .verifying(optionalRegExp("^[0-9]{10}$", s"${page.toString}.error.regexp"))
        .verifying(optionalChecksum(s"${page.toString}.error.checksum"))
    )
}
