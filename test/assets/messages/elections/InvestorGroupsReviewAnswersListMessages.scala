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

package assets.messages.elections

object InvestorGroupsReviewAnswersListMessages {
  val title: Int => String = amount => s"$amount Investor group${if(amount > 1) "s" else ""} added for Group ratio (blended) election"
  val addParent = "Do you need to add another investor group?"
  val groupRatio = "Group Ratio"
  val fixedRatio = "Fixed Ratio"
}
