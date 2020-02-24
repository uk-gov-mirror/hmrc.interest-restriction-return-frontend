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
    companyName = companyNameModel,
    knownAs = Some(knownAs),
    ctutr = Some(ctutrModel),
    sautr = Some(sautrModel),
    crn = Some(crnModel),
    nonUkCrn = Some(nonUkCrn),
    countryOfIncorporation = Some(nonUkCountryCode),
    limitedLiabilityPartnership = Some(true),
    payTaxInUk = Some(true),
    registeredCompaniesHouse = Some(true),
    registeredForTaxInAnotherCountry = Some(true),
    reportingCompanySameAsParent = Some(true)
  )

  val deemedParentModelMin = DeemedParentModel(
    companyName = companyNameModel
  )

  val deemedParentModelUkCompany = DeemedParentModel(
    knownAs = Some(knownAs),
    companyName = companyNameModel,
    payTaxInUk = Some(true),
    limitedLiabilityPartnership = Some(false),
    ctutr = Some(ctutrModel),
    registeredCompaniesHouse = Some(true),
    crn = Some(crnModel),
    registeredForTaxInAnotherCountry = Some(false)
  )

  val deemedParentModelUkPartnership = DeemedParentModel(
    companyName = companyNameModel,
    knownAs = Some(knownAs),
    payTaxInUk = Some(true),
    limitedLiabilityPartnership = Some(true),
    sautr = Some(sautrModel),
    crn = Some(crnModel)
  )

  val deemedParentModelNonUkCompany = DeemedParentModel(
    companyName = companyNameModel,
    knownAs = Some(knownAs),
    payTaxInUk = Some(false),
    countryOfIncorporation = Some(nonUkCountryCode),
    nonUkCrn = Some(nonUkCrn),
    registeredForTaxInAnotherCountry = Some(true)
  )

  val deemedParentJsonMax = Json.obj(
    "companyName" -> companyNameModel,
    "knownAs" -> knownAs,
    "ctutr" -> ctutrModel,
    "sautr" -> sautrModel,
    "crn" -> crnModel,
    "nonUkCrn" -> nonUkCrn,
    "countryOfIncorporation" -> nonUkCountryCode,
    "limitedLiabilityPartnership" -> true,
    "payTaxInUk" -> true,
    "registeredCompaniesHouse" -> true,
    "registeredForTaxInAnotherCountry" -> true,
    "reportingCompanySameAsParent" -> true
  )

  val deemedParentJsonMin = Json.obj(
    "companyName" -> companyNameModel
  )

  val deemedParentJsonUkCompany = Json.obj(
    "companyName" -> companyNameModel,
    "knownAs" -> knownAs,
    "ctutr" -> ctutrModel,
    "crn" -> crnModel
  )

  val deemedParentJsonNonUkCompany= Json.obj(
    "companyName" -> companyNameModel,
    "knownAs" -> knownAs,
    "nonUkCrn" -> crnModel,
    "countryOfIncorporation" -> nonUkCountryCode
  )

  val deemedParentJsonUkPartnership = Json.obj(
    "companyName" -> companyNameModel,
    "knownAs" -> knownAs,
    "sautr" -> sautrModel,
    "crn" -> crnModel
  )
}
