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

package forms.behaviours

import play.api.data.{Form, FormError}
import utils.RemoveWhitespace

trait TellUsWhatHasChangedBehaviours extends FieldBehaviours with RemoveWhitespace {
  def validTellUsWhatHasChanged(
                                 form: Form[_],
                                 fieldName: String = "value",
                                 fieldRegexpKey: String
                               ) : Unit = {
    s"$fieldName" must {
      val validRegexp: String = "^[ -~¢-¥©®±×÷‐₠-₿−-∝≈≠≣-≥]{1,5000}$"
      val invalidRegexp: String = "This¬is invalid¬¬"
      val validText: String = "This is valid"

      "when binding a value which does match the regexp" when {
        "returns the field value" in {
          val result = form.bind(Map(fieldName -> validText))
          result.value mustBe Some(validText)
        }
      }

      "when binding a value which does not match the regexp" should {
        "return the regexp error" in {
          val result = form.bind(Map(fieldName -> invalidRegexp))
          result.errors.headOption mustEqual Some(FormError(fieldName, fieldRegexpKey, Seq(validRegexp)))
        }
      }
    }
    }
}
