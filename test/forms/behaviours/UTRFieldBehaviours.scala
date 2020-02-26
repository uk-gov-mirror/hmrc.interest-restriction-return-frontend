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

package forms.behaviours

import play.api.data.{Form, FormError}

trait UTRFieldBehaviours extends FieldBehaviours {

  def validUTR(
                form: Form[_],
                utrFieldName: String = "value",
                validUTR: String = "1111111111",
                invalidUTR: String = "1234567899",
                validRegexp: String = "^[0-9]{10}$",
                invalidRegexp: String = "1234",
                utrChecksumErrorKey: String,
                utrRegexpKey: String
              ): Unit = {
    s"$utrFieldName" must {

      "when binding a value which does match the regexp" when {

        "checksum fails" should {

          s"return the checksum error for $utrFieldName" in {
            val result = form.bind(Map(utrFieldName -> invalidUTR)).apply(utrFieldName)
            result.errors.headOption mustEqual Some(FormError(utrFieldName, utrChecksumErrorKey))
          }
        }

        "checksum is successful" should {

          "return the field value" in {
            val result = form.bind(Map(utrFieldName -> validRegexp)).apply(utrFieldName)
            result.value mustBe Some(validRegexp)
          }
        }

        "when binding a value which does not match the regexp" should {

          "return the regexp error" in {
            val result = form.bind(Map(utrFieldName -> invalidRegexp)).apply(utrFieldName)
            result.errors.headOption mustEqual Some(FormError(utrFieldName, utrRegexpKey, Seq(validRegexp)))
          }
        }
      }
    }
  }
}

