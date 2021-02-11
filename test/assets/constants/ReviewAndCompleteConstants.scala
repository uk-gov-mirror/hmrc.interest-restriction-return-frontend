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

package assets.constants

import models.SectionStatus
import models.SectionStatus.{Completed, InProgress, NotStarted}
import models.returnModels.ReviewAndCompleteModel
import play.api.libs.json.{JsObject, Json}

object ReviewAndCompleteConstants {

  val reviewAndCompleteModel: ReviewAndCompleteModel = ReviewAndCompleteModel(
    aboutReturnStatus = NotStarted,
    electionsStatus = InProgress,
    groupLevelInformationStatus = Completed,
    ultimateParentCompanyStatus = NotStarted,
    ukCompaniesStatus = InProgress,
    checkTotalsStatus = Completed
  )

  val reviewAndCompleteJson: JsObject = Json.obj(
    "aboutReturnStatus" -> SectionStatus.NotStarted.toString,
    "electionsStatus" -> SectionStatus.InProgress.toString,
    "groupLevelInformationStatus" -> SectionStatus.Completed.toString,
    "ultimateParentCompanyStatus" -> SectionStatus.NotStarted.toString,
    "ukCompaniesStatus" -> SectionStatus.InProgress.toString,
    "checkTotalsStatus" -> SectionStatus.Completed.toString
  )

}
