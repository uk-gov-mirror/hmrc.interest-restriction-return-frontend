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

package models.returnModels.fullReturn

import java.time.LocalDate

import play.api.libs.json.{Json, Reads, Writes}

case class AllocatedRestrictionsModel(ap1End: Option[LocalDate] = None,
                                      ap2End: Option[LocalDate] = None,
                                      ap3End: Option[LocalDate] = None,
                                      disallowanceAp1: Option[BigDecimal] = None,
                                      disallowanceAp2: Option[BigDecimal] = None,
                                      disallowanceAp3: Option[BigDecimal] = None
                                     ) {
  val totalDisallowances = List(disallowanceAp1, disallowanceAp2, disallowanceAp3).flatten.sum

  def setRestriction(period: Int, endDate: LocalDate, restriction: BigDecimal): AllocatedRestrictionsModel = period match {
    case 1 => copy(ap1End = Some(endDate), disallowanceAp1 = Some(restriction))
    case 2 => copy(ap2End = Some(endDate), disallowanceAp2 = Some(restriction))
    case 3 => copy(ap3End = Some(endDate), disallowanceAp3 = Some(restriction))
    case _ => throw new IndexOutOfBoundsException(s"'$period' is not a valid Account Period which can be set")
  }
}

object AllocatedRestrictionsModel {

  implicit val reads: Reads[AllocatedRestrictionsModel] = Json.reads[AllocatedRestrictionsModel]

  implicit val writes: Writes[AllocatedRestrictionsModel] = Writes { model =>
    Json.obj(
      "ap1End" -> model.ap1End,
      "ap2End" -> model.ap2End,
      "ap3End" -> model.ap3End,
      "disallowanceAp1" -> model.disallowanceAp1,
      "disallowanceAp2" -> model.disallowanceAp2,
      "disallowanceAp3" -> model.disallowanceAp3,
      "totalDisallowances" -> model.totalDisallowances
    )
  }
}
