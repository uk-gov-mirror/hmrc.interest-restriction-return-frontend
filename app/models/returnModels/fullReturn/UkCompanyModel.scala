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

import models.NetTaxInterestIncomeOrExpense
import models.CompanyDetailsModel
import play.api.libs.json.Json

case class UkCompanyModel(companyDetails: CompanyDetailsModel,
                          consenting: Option[Boolean] = None,
                          netTaxInterestIncomeOrExpense: Option[NetTaxInterestIncomeOrExpense] = None,
                          netTaxInterest: Option[BigDecimal] = None,
                          taxEBITDA: Option[BigDecimal] = None,
                          allocatedRestrictions: Option[AllocatedRestrictionsModel] = None,
                          allocatedReactivations: Option[AllocatedReactivationsModel] = None
                         )
object UkCompanyModel {

  implicit val format = Json.format[UkCompanyModel]

}
