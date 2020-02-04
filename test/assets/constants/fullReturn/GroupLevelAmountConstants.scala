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

package assets.constants.fullReturn

import models.returnModels.fullReturn.GroupLevelAmountModel
import play.api.libs.json.Json

object GroupLevelAmountConstants {

  val interestReactivationCap: BigDecimal = 2.22
  val interestAllowanceForward: BigDecimal = 3.33
  val interestAllowanceForPeriod: BigDecimal = 4.44
  val interestCapacityForPeriod: BigDecimal = 5.55

  val groupLevelAmountModel = GroupLevelAmountModel(
    interestReactivationCap = Some(interestReactivationCap),
    interestAllowanceForward = interestAllowanceForward,
    interestAllowanceForPeriod = interestAllowanceForPeriod,
    interestCapacityForPeriod = interestCapacityForPeriod
  )

  val groupLevelAmountJson = Json.obj(
    "interestReactivationCap" -> interestReactivationCap,
    "interestAllowanceForward" -> interestAllowanceForward,
    "interestAllowanceForPeriod" -> interestAllowanceForPeriod,
    "interestCapacityForPeriod" -> interestCapacityForPeriod
  )
}