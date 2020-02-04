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

import models.returnModels.ReportingCompanyModel
import play.api.libs.json.Json

object ReportingCompanyConstants extends BaseConstants {

  val reportingCompanyName = "some reporting company"

  val reportingCompanyJson = Json.obj(
    "companyName" -> companyNameModel,
    "ctutr" -> ctutrModel,
    "crn" -> crnModel,
    "sameAsUltimateParent" -> false
  )

  val reportingCompanyModel = ReportingCompanyModel(
    companyName = companyNameModel,
    ctutr = ctutrModel,
    crn = crnModel,
    sameAsUltimateParent = false
  )
}