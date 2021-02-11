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

import models.SectionStatus
import models.SectionStatus.NotStarted
import play.api.libs.json.Json
import sectionstatus._
import models.UserAnswers

case class ReviewAndCompleteModel(aboutReturnStatus: SectionStatus = NotStarted,
                                  electionsStatus: SectionStatus = NotStarted,
                                  groupLevelInformationStatus: SectionStatus = NotStarted,
                                  ultimateParentCompanyStatus: SectionStatus = NotStarted,
                                  ukCompaniesStatus: SectionStatus = NotStarted,
                                  checkTotalsStatus: SectionStatus = NotStarted) {
}

object ReviewAndCompleteModel {
  implicit val format = Json.format[ReviewAndCompleteModel]

  def apply(userAnswers: UserAnswers): ReviewAndCompleteModel = {

    val aboutReturnStatus = AboutReturnSectionStatus.getStatus(userAnswers)
    val electionsStatus = ElectionsSectionStatus.getStatus(userAnswers)
    val groupLevelInformationStatus = GroupLevelInformationSectionStatus.getStatus(userAnswers)
    val ultimateParentCompanyStatus = UltimateParentCompanySectionStatus.getStatus(userAnswers)
    val ukCompaniesStatus = UkCompaniesSectionStatus.getStatus(userAnswers)
    val checkTotalsStatus = CheckTotalsSectionStatus.getStatus(userAnswers)

    ReviewAndCompleteModel(
      aboutReturnStatus = aboutReturnStatus,
      electionsStatus = electionsStatus,
      groupLevelInformationStatus = groupLevelInformationStatus,
      ultimateParentCompanyStatus = ultimateParentCompanyStatus,
      ukCompaniesStatus = ukCompaniesStatus,
      checkTotalsStatus = checkTotalsStatus
    )
  }
}