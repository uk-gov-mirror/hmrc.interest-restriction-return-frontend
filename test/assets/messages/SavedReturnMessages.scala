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

object SavedReturnMessages {

  val heading = "Your return has been saved"
  val savedTil: String => String = date => s"Your information is held until $date"
  val p1 = "You will need to log in to the sign-in page to retrieve your return"
  val indent = "For us to consider your return, you will need to submit it"
  val p2 = "You can:"
  val bullet1 = "Continue this return"
  val bullet2 = "Delete this form and start again"

}
