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
import models.returnModels.{ReviewAndCompleteModel, SectionState}
import pages.elections.GroupRatioBlendedElectionPage
import pages.groupLevelInformation.GroupInterestCapacityPage
import pages.ukCompanies.{DerivedCompanyPage, UkCompaniesPage}
import play.api.libs.json.{JsObject, Json}

object ReviewAndCompleteConstants {

  val reviewAndCompleteModel: ReviewAndCompleteModel = ReviewAndCompleteModel(
    aboutReturn = SectionState(NotStarted, None),
    elections = SectionState(InProgress, Some(GroupRatioBlendedElectionPage)),
    groupLevelInformation = SectionState(Completed, Some(GroupInterestCapacityPage)),
    ultimateParentCompany = SectionState(NotStarted, None),
    ukCompanies = SectionState(InProgress, Some(UkCompaniesPage)),
    checkTotals = SectionState(Completed, Some(DerivedCompanyPage))
  )

  val reviewAndCompleteJson: JsObject = Json.obj(
    "aboutReturn" -> Json.obj(
      "status" -> SectionStatus.NotStarted.toString
    ),
    "elections" -> Json.obj(
      "status" -> SectionStatus.InProgress.toString,
      "lastPageSaved" -> GroupRatioBlendedElectionPage.toString
    ),
    "groupLevelInformation" -> Json.obj(
      "status" -> SectionStatus.Completed.toString,
      "lastPageSaved" -> GroupInterestCapacityPage.toString
    ),
    "ultimateParentCompany" -> Json.obj(
      "status" -> SectionStatus.NotStarted.toString
    ),
    "ukCompanies" -> Json.obj(
      "status" -> SectionStatus.InProgress.toString,
      "lastPageSaved" -> UkCompaniesPage.toString
    ),
    "checkTotals" -> Json.obj(
      "status" -> SectionStatus.Completed.toString,
      "lastPageSaved" -> DerivedCompanyPage.toString
    )
  )

}
