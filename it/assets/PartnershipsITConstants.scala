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

package assets

import models.returnModels.PartnershipModel
import play.api.libs.json.Json

object PartnershipsITConstants extends  BaseITConstants {

  val partnerName = "some partner"

  val partnershipModelUK = PartnershipModel(
    name = partnerName,
    isUkPartnership = Some(true),
    sautr = Some(sautrModel)
  )
  val partnershipModelNonUk = PartnershipModel(
    name = partnerName,
    isUkPartnership = Some(false)
  )

  val partnershipJsonUk = Json.obj(
    "name" -> partnerName,
    "isUkPartnership" -> true,
    "sautr" -> sautrModel
  )

  val partnershipJsonNonUk = Json.obj(
    "name" -> partnerName,
    "isUkPartnership" -> false
  )
}
