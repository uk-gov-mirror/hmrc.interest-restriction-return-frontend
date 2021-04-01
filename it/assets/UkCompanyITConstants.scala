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

package assets

import AllocatedReactivationsITConstants._
import AllocatedRestrictionsITConstants._
import models.CompanyDetailsModel
import models.NetTaxInterestIncomeOrExpense.{NetTaxInterestExpense, NetTaxInterestIncome}
import models.returnModels.fullReturn.UkCompanyModel
import play.api.libs.json.Json

object UkCompanyITConstants extends BaseITConstants {

  val netTaxInterestExpense: BigDecimal = 1.11
  val netTaxInterestIncome: BigDecimal = 1.11
  val taxEBITDA: BigDecimal = 3.33
  val companyDetailsModel = CompanyDetailsModel(
    companyName = companyNameModel.name,
    ctutr = ctutrModel.utr
  )
  val companyDetailsJson = Json.toJson(companyDetailsModel)

  val ukCompanyModelMax = UkCompanyModel(
    companyDetails = companyDetailsModel,
    consenting = Some(true),
    netTaxInterest = Some(netTaxInterestExpense),
    taxEBITDA = Some(taxEBITDA),
    allocatedRestrictions = Some(allocatedRestrictionsModel),
    allocatedReactivations = Some(allocatedReactivationsModel),
    qicElection = Some(true)
  )

  val ukCompanyJsonMax = Json.obj(
    "companyDetails" -> companyDetailsJson,
    "consenting" -> true,
    "netTaxInterest" -> netTaxInterestExpense,
    "taxEBITDA" -> taxEBITDA,
    "allocatedRestrictions" -> allocatedRestrictionsJson,
    "allocatedReactivations" -> allocatedReactivationsJson,
    "addRestriction" -> true,
    "qicElection" -> true
  )

  val ukCompanyModelReactivationMaxIncome = UkCompanyModel(
    companyDetails = companyDetailsModel,
    consenting = Some(true),
    netTaxInterestIncomeOrExpense = Some(NetTaxInterestIncome),
    netTaxInterest = Some(netTaxInterestIncome),
    taxEBITDA = Some(taxEBITDA),
    allocatedRestrictions = None,
    allocatedReactivations = Some(allocatedReactivationsModel),
    qicElection = Some(true)
  )

  val ukCompanyReactivationJsonMaxIncome = Json.obj(
    "companyDetails" -> companyDetailsJson,
    "consenting" -> true,
    "netTaxInterestIncomeOrExpense" -> NetTaxInterestIncome.toString,
    "netTaxInterest" -> netTaxInterestIncome,
    "taxEBITDA" -> taxEBITDA,
    "allocatedReactivations" -> allocatedReactivationsJson,
    "addRestriction" -> true,
    "qicElection" -> true
  )

  val ukCompanyModelRestrictionMaxIncome = UkCompanyModel(
    companyDetails = companyDetailsModel,
    consenting = Some(true),
    netTaxInterestIncomeOrExpense = Some(NetTaxInterestIncome),
    netTaxInterest = Some(netTaxInterestIncome),
    taxEBITDA = Some(taxEBITDA),
    allocatedRestrictions = Some(allocatedRestrictionsModel),
    allocatedReactivations = None,
    qicElection = Some(true)
  )

  val ukCompanyRestrictionJsonMaxIncome = Json.obj(
    "companyDetails" -> companyDetailsJson,
    "consenting" -> true,
    "netTaxInterestIncomeOrExpense" -> NetTaxInterestIncome.toString,
    "netTaxInterest" -> netTaxInterestIncome,
    "taxEBITDA" -> taxEBITDA,
    "allocatedRestrictions" -> allocatedRestrictionsJson,
    "addRestriction" -> true,
    "qicElection" -> true
  )

  val ukCompanyModelReactivationMaxExpense = UkCompanyModel(
    companyDetails = companyDetailsModel,
    consenting = Some(true),
    netTaxInterestIncomeOrExpense = Some(NetTaxInterestExpense),
    netTaxInterest = Some(netTaxInterestExpense),
    taxEBITDA = Some(taxEBITDA),
    allocatedRestrictions = None,
    allocatedReactivations = Some(allocatedReactivationsModel),
    qicElection = Some(true)
  )

  val ukCompanyReactivationJsonMaxExpense = Json.obj(
    "companyDetails" -> companyDetailsJson,
    "consenting" -> true,
    "netTaxInterestIncomeOrExpense" -> NetTaxInterestExpense.toString,
    "netTaxInterest" -> netTaxInterestExpense,
    "taxEBITDA" -> taxEBITDA,
    "allocatedReactivations" -> allocatedReactivationsJson,
    "addRestriction" -> true,
    "qicElection" -> true
  )

  val ukCompanyModelRestrictionMaxExpense = UkCompanyModel(
    companyDetails = companyDetailsModel,
    consenting = Some(true),
    netTaxInterestIncomeOrExpense = Some(NetTaxInterestExpense),
    netTaxInterest = Some(netTaxInterestExpense),
    taxEBITDA = Some(taxEBITDA),
    allocatedRestrictions = Some(allocatedRestrictionsModel),
    allocatedReactivations = None,
    qicElection = Some(true)
  )

  val ukCompanyRestrictionJsonMaxExpense = Json.obj(
    "companyDetails" -> companyDetailsJson,
    "consenting" -> true,
    "netTaxInterestIncomeOrExpense" -> NetTaxInterestExpense.toString,
    "netTaxInterest" -> netTaxInterestExpense,
    "taxEBITDA" -> taxEBITDA,
    "allocatedRestrictions" -> allocatedRestrictionsJson,
    "addRestriction" -> true,
    "qicElection" -> true
  )


  val ukCompanyModelMin = UkCompanyModel(
    companyDetails = companyDetailsModel,
    consenting = Some(true),
    netTaxInterest = Some(netTaxInterestIncome),
    taxEBITDA = Some(taxEBITDA),
    allocatedRestrictions = None,
    allocatedReactivations = None
  )

  val ukCompanyJsonMin = Json.obj(
    "companyDetails" -> companyDetailsJson,
    "consenting" -> true,
    "netTaxInterest" -> netTaxInterestIncome,
    "taxEBITDA" -> taxEBITDA
  )
}
