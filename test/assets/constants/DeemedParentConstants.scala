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

package assets.constants

import models.returnModels.DeemedParentModel
import play.api.libs.json.Json

object DeemedParentConstants extends BaseConstants {

  val deemedParentModelMax = DeemedParentModel(
    isUk = true,
    companyName = companyNameModel,
    knownAs = Some(knownAs),
    ctutr = Some(ctutrModel),
    sautr = Some(sautrModel),
    crn = Some(crnModel),
    nonUkCrn = Some(nonUkCrn),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val deemedParentModelMin = DeemedParentModel(
    isUk = true,
    companyName = companyNameModel,
    knownAs = None,
    ctutr = None,
    crn = None,
    sautr = None,
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val deemedParentModelUkCompany = DeemedParentModel(
    isUk = true,
    companyName = companyNameModel,
    knownAs = Some(knownAs),
    ctutr = Some(ctutrModel),
    sautr = None,
    crn = Some(crnModel),
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val deemedParentModelNonUkCompany = DeemedParentModel(
    isUk = false,
    companyName = companyNameModel,
    knownAs = Some(knownAs),
    ctutr = None,
    sautr = None,
    crn = None,
    nonUkCrn = Some(nonUkCrn),
    countryOfIncorporation = Some(nonUkCountryCode)
  )

  val deemedParentModelUkPartnership = DeemedParentModel(
    isUk = true,
    companyName = companyNameModel,
    knownAs = Some(knownAs),
    ctutr = None,
    sautr = Some(sautrModel),
    crn = Some(crnModel),
    nonUkCrn = None,
    countryOfIncorporation = None
  )

  val deemedParentJsonMax = Json.obj(
    "isUk" -> true,
    "companyName" -> companyNameModel,
    "knownAs" -> knownAs,
    "ctutr" -> ctutrModel,
    "sautr" -> sautrModel,
    "crn" -> crnModel,
    "nonUkCrn" -> nonUkCrn,
    "countryOfIncorporation" -> Some(nonUkCountryCode)
  )

  val deemedParentJsonMin = Json.obj(
    "isUk" -> true,
    "companyName" -> companyNameModel
  )

  val deemedParentJsonUkCompany = Json.obj(
    "isUk" -> true,
    "companyName" -> companyNameModel,
    "knownAs" -> knownAs,
    "ctutr" -> ctutrModel,
    "crn" -> crnModel
  )

  val deemedParentJsonNonUkCompany= Json.obj(
    "isUk" -> false,
    "companyName" -> companyNameModel,
    "knownAs" -> knownAs,
    "nonUkCrn" -> crnModel,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val deemedParentJsonUkPartnership = Json.obj(
    "isUk" -> false,
    "companyName" -> companyNameModel,
    "knownAs" -> knownAs,
    "sautr" -> sautrModel,
    "crn" -> crnModel
  )
}
