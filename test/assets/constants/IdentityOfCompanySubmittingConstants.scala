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

import models.returnModels.IdentityOfCompanySubmittingModel
import play.api.libs.json.Json

object IdentityOfCompanySubmittingConstants extends BaseConstants {

  val countryCode = "US"

  val identityOfCompanySubmittingJsonMax = Json.obj(
    "companyName" -> companyNameModel,
    "ctutr" -> ctutrModel,
    "crn" -> crnModel
  )

  val identityOfCompanySubmittingModelMax = IdentityOfCompanySubmittingModel(
    companyName = companyNameModel,
    ctutr = Some(ctutrModel),
    crn = Some(crnModel),
    countryOfIncorporation = None,
    nonUkCrn = None
  )

  val identityOfCompanySubmittingJsonMin = Json.obj(
    "companyName" -> companyNameModel
  )

  val identityOfCompanySubmittingModelMin = IdentityOfCompanySubmittingModel(
    companyName = companyNameModel,
    ctutr = None,
    crn = None,
    countryOfIncorporation = None,
    nonUkCrn = None
  )
}
