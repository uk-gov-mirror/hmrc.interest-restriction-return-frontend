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

package models

import play.api.libs.json.{JsValue, Json, Writes}

sealed trait Section

object Section {

  object ReportingCompany extends Section {
    override val toString = "reportingCompany"
  }

  object GroupStructure extends Section {
    override val toString = "groupStructure"
  }

  object Elections extends Section {
    override val toString = "elections"
  }

  object UkCompanies extends Section {
    override val toString = "ukCompanies"
  }

  object ReviewTaxEBITDA extends Section {
    override val toString = "reviewTaxEBITDA"
  }

  implicit object SectionWrites extends Writes[Section]{
    def writes(section: Section): JsValue = Json.toJson(section.toString)
  }
}