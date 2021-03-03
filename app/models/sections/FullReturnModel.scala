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

package models.sections

import models.returnModels.AgentDetailsModel
import play.api.libs.json.{JsObject, JsPath, Json, Writes}
import play.api.libs.functional.syntax._

case class FullReturnModel(aboutReturn: AboutReturnSectionModel,
                           ultimateParentCompany: UltimateParentCompanySectionModel,
                           elections: Option[ElectionsSectionModel] = None,
                           groupLevelInformation: Option[GroupLevelInformationSectionModel] = None,
                           ukCompanies: Option[UkCompaniesSectionModel] = None)

object FullReturnModel {
  val writes: Writes[FullReturnModel] = (
    (JsPath \ "appointedReportingCompany").write[Boolean] and
      (JsPath \ "agentDetails").write[AgentDetailsModel] and
      (JsPath \ "submissionType").write[String] and
      (JsPath \ "revisedReturnDetails").writeNullable[String] and
      (JsPath \ "reportingCompany").write[JsObject] and
      (JsPath \ "groupCompanyDetails" \ "accountingPeriod").write[JsObject]
    ) (fullReturn => (fullReturn.aboutReturn.appointedReportingCompany,
    fullReturn.aboutReturn.agentDetails,
    if (fullReturn.aboutReturn.isRevisingReturn) "revised" else "original",
    fullReturn.aboutReturn.revisedReturnDetails,
    Json.obj("companyName" -> fullReturn.aboutReturn.companyName,
      "ctutr" -> fullReturn.aboutReturn.ctutr,
      "sameAsUltimateParent" -> fullReturn.ultimateParentCompany.reportingCompanySameAsParent
    ),
    Json.obj("startDate" -> fullReturn.aboutReturn.periodOfAccount.startDate,
      "endDate" -> fullReturn.aboutReturn.periodOfAccount.endDate)))
}