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

package models.returnModels.fullReturn

import models.{NetTaxInterestIncomeOrExpense, CompanyEstimatedFigures, CompanyDetailsModel}
import play.api.libs.json.Json

case class UkCompanyModel(companyDetails: CompanyDetailsModel,
                          consenting: Option[Boolean] = None,
                          netTaxInterestIncomeOrExpense: Option[NetTaxInterestIncomeOrExpense] = None,
                          netTaxInterest: Option[BigDecimal] = None,
                          taxEBITDA: Option[BigDecimal] = None,
                          restriction: Option[Boolean] = None,
                          allocatedRestrictions: Option[AllocatedRestrictionsModel] = None,
                          reactivation: Option[Boolean] = None,
                          allocatedReactivations: Option[AllocatedReactivationsModel] = None,
                          accountPeriodSameAsGroup: Option[Boolean] = None,
                          containsEstimates: Option[Boolean] = None,
                          estimatedFigures: Option[Set[CompanyEstimatedFigures]] = None,
                          addRestrictionAmount: Option[Boolean] = None
                         )
object UkCompanyModel {

  implicit val format = Json.format[UkCompanyModel]

}


