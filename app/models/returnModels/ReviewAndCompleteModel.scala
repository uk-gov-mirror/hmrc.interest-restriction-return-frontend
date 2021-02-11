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

package models.returnModels

import models.SectionStatus.NotStarted
import models.SectionStatus
import play.api.libs.json.{Format, Json}
import sectionstatus._
import models.UserAnswers

case class SectionState(status: SectionStatus = NotStarted)

object SectionState {
  implicit val fmt: Format[SectionState] = Json.format[SectionState]
}

case class ReviewAndCompleteModel(aboutReturn: SectionState = SectionState(),
                                  elections: SectionState = SectionState(),
                                  groupLevelInformation: SectionState = SectionState(),
                                  ultimateParentCompany: SectionState = SectionState(),
                                  ukCompanies: SectionState = SectionState(),
                                  checkTotals: SectionState = SectionState()) {
}

object ReviewAndCompleteModel {
  implicit val format = Json.format[ReviewAndCompleteModel]

  def apply(userAnswers: UserAnswers): ReviewAndCompleteModel = {

    val aboutReturnStatus = AboutReturnSectionStatus.getStatus(userAnswers)
    val electionsStatus = ElectionsSectionStatus.getStatus(userAnswers)
    val groupLevelInformationStatus = GroupLevelInformationSectionStatus.getStatus(userAnswers)
    val ultimateParentCompanyStatus = UltimateParentCompanySectionStatus.getStatus(userAnswers)
    val ukCompaniesStatus = UltimateParentCompanySectionStatus.getStatus(userAnswers)

    ReviewAndCompleteModel(
      aboutReturn = SectionState(aboutReturnStatus),
      elections = SectionState(electionsStatus),
      groupLevelInformation = SectionState(groupLevelInformationStatus),
      ultimateParentCompany = SectionState(ultimateParentCompanyStatus),
      ukCompanies = SectionState(ukCompaniesStatus),
      checkTotals = SectionState()
    )
  }
}