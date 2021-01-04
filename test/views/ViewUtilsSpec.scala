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

package views

import play.api.data.FormError
import org.scalatest.WordSpec
import org.scalatest.Matchers

class ViewUtilsSpec extends WordSpec with Matchers {
	
  "getErrorFieldFromPrefixes" should {
    "Return Some when the error message matches one of the mapped prefixes" in {
      val map = Map("error.message.prefix1" -> "error.field1", "error.message.prefix2" -> "error.field2")
      val error = FormError("", Seq("error.message.prefix1.message1"))
      val result = ViewUtils.getErrorFieldFromPrefixes(error, map)
      result shouldBe Some("error.field1")
    }

    "Return None when the error message doesn't match one of the mapped prefixes" in {
      val map = Map("error.message.prefix1" -> "error.field1", "error.message.prefix2" -> "error.field2")
      val error = FormError("", Seq("error.message.prefix3.message3"))
      val result = ViewUtils.getErrorFieldFromPrefixes(error, map)
      result shouldBe None
    }

    "Return None when the map is empty" in {
      val map = Map[String, String]()
      val error = FormError("", Seq("error.message.prefix1.message1"))
      val result = ViewUtils.getErrorFieldFromPrefixes(error, map)
      result shouldBe None
    }
  }

  "getErrorField" should {
    "Return the map value where the message matches a mapped prefix and a field is passed in" in {
      val map = Map("error.message.prefix1" -> "error.field1", "error.message.prefix2" -> "error.field2")
      val error = FormError("error.key", Seq("error.message.prefix1.message1"))
      val result = ViewUtils.getErrorField(error, Some("specified.field"), map)
      result shouldBe "error.field1"
    }

    "Return the map value where the message matches a mapped prefix and a field is not passed in" in {
      val map = Map("error.message.prefix1" -> "error.field1", "error.message.prefix2" -> "error.field2")
      val error = FormError("error.key", Seq("error.message.prefix1.message1"))
      val result = ViewUtils.getErrorField(error, None, map)
      result shouldBe "error.field1"
    }

    "Return the map value where the message does not match a mapped prefix and a field is passed in" in {
      val map = Map("error.message.prefix1" -> "error.field1", "error.message.prefix2" -> "error.field2")
      val error = FormError("error.key", Seq("error.message.prefix3.message3"))
      val result = ViewUtils.getErrorField(error, Some("specified.field"), map)
      result shouldBe "specified.field"
    }

    "Return the map value where the message does not match a mapped prefix and a field is not passed in" in {
      val map = Map("error.message.prefix1" -> "error.field1", "error.message.prefix2" -> "error.field2")
      val error = FormError("error.key", Seq("error.message.prefix3.message3"))
      val result = ViewUtils.getErrorField(error, None, map)
      result shouldBe "error.key"
    }
  }
}
