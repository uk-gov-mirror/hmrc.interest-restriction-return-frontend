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

import models.SectionStatus
import models.returnModels.ReviewAndCompleteModel
import play.api.libs.json.{JsObject, Json}

object ReviewAndCompleteConstants {

  val reviewAndCompleteModel: ReviewAndCompleteModel = ReviewAndCompleteModel(
    startReturn = SectionStatus.NotStarted,
    elections = SectionStatus.InProgress,
    aboutReturn = SectionStatus.Completed,
    groupStructure = SectionStatus.NotStarted,
    ukCompanies = SectionStatus.InProgress,
    checkTotals = SectionStatus.Completed
  )

  val reviewAndCompleteJson: JsObject = Json.obj(
    "startReturn" -> SectionStatus.NotStarted.toString,
    "elections" -> SectionStatus.InProgress.toString,
    "aboutReturn" -> SectionStatus.Completed.toString,
    "groupStructure" -> SectionStatus.NotStarted.toString,
    "ukCompanies" -> SectionStatus.InProgress.toString,
    "checkTotals" -> SectionStatus.Completed.toString
  )

}
