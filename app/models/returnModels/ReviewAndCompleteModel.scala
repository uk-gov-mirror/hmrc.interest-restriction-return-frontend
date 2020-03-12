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

import models.{Section, SectionStatus}
import models.SectionStatus.NotStarted
import play.api.libs.json.Json

case class ReviewAndCompleteModel(startReturn: SectionStatus = NotStarted,
                                  elections: SectionStatus = NotStarted,
                                  aboutReturn: SectionStatus = NotStarted,
                                  groupStructure: SectionStatus = NotStarted,
                                  ukCompanies: SectionStatus = NotStarted,
                                  checkTotals: SectionStatus = NotStarted){

  def update(section: Section, sectionStatus: SectionStatus): ReviewAndCompleteModel = section match {
    case Section.StartReturn => this.copy(startReturn = sectionStatus)
    case Section.Elections => this.copy(elections = sectionStatus)
    case Section.AboutReturn => this.copy(elections = aboutReturn)
    case Section.GroupStructure => this.copy(elections = groupStructure)
    case Section.UkCompanies => this.copy(elections = ukCompanies)
    case Section.CheckTotals => this.copy(elections = checkTotals)
  }
}

object ReviewAndCompleteModel {
  implicit val format = Json.format[ReviewAndCompleteModel]
}