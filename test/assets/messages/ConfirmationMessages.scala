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

package assets.messages

object ConfirmationMessages {

  val heading = "Return submitted"
  val reference: String => String = ref => s"Your reference number $ref"
  val p1 = "HMRC has successfully received your return"
  val feedbackLink = "What did you think of this service?"
  val feedbackTime = "(takes 30 seconds)"

}
