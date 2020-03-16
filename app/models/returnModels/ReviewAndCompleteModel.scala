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

package models.returnModels

import models.SectionStatus.NotStarted
import models.{Section, SectionStatus}
import pages.Page
import play.api.libs.json.{Format, Json}


case class SectionState(status: SectionStatus = NotStarted, lastPageSaved: Option[Page] = None)

object SectionState {
  implicit val fmt: Format[SectionState] = Json.format[SectionState]
}

case class ReviewAndCompleteModel(startReturn: SectionState = SectionState(),
                                  elections: SectionState = SectionState(),
                                  aboutReturn: SectionState = SectionState(),
                                  groupStructure: SectionState = SectionState(),
                                  ukCompanies: SectionState = SectionState(),
                                  checkTotals: SectionState = SectionState()) {

  def update(section: Section, sectionStatus: SectionStatus, page: Page): ReviewAndCompleteModel = section match {
    case Section.StartReturn => this.copy(startReturn = SectionState(sectionStatus, Some(page)))
    case Section.Elections => this.copy(elections = SectionState(sectionStatus, Some(page)))
    case Section.AboutReturn => this.copy(aboutReturn = SectionState(sectionStatus, Some(page)))
    case Section.GroupStructure => this.copy(groupStructure = SectionState(sectionStatus, Some(page)))
    case Section.UkCompanies => this.copy(ukCompanies = SectionState(sectionStatus, Some(page)))
    case Section.CheckTotals => this.copy(checkTotals = SectionState(sectionStatus, Some(page)))
  }
}

object ReviewAndCompleteModel {
  implicit val format = Json.format[ReviewAndCompleteModel]
}