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

import models.returnModels._
import models.FullOrAbbreviatedReturn
import play.api.libs.json.{Format, JsObject, JsPath, Json, Writes}
import play.api.libs.functional.syntax._

case class AboutReturnSectionModel(
  appointedReportingCompany: Boolean,
  agentDetails: AgentDetailsModel,
  fullOrAbbreviatedReturn: FullOrAbbreviatedReturn,
  isRevisingReturn: Boolean,
  revisedReturnDetails: Option[String],
  companyName: CompanyNameModel,
  ctutr: UTRModel,
  periodOfAccount: AccountingPeriodModel
)

object AboutReturnSectionModel {
  val writes : Writes[AboutReturnSectionModel] =(
    (JsPath \ "appointedReportingCompany").write[Boolean] and
      (JsPath \ "agentDetails").write[AgentDetailsModel] and
        (JsPath \ "submissionType").write[String] and
      (JsPath \ "revisedReturnDetails").writeNullable[String] and
      (JsPath \ "reportingCompany").write[JsObject]
  )(ar=>(ar.appointedReportingCompany,
    ar.agentDetails,
    if (ar.isRevisingReturn) "revised" else "original",
    ar.revisedReturnDetails,
    Json.obj("companyName" -> ar.companyName,
                    "ctutr" -> ar.ctutr,
                    "sameAsUltimateParent" -> false //TODO:No visiblity of this yet, to be done in next section
    )))
}