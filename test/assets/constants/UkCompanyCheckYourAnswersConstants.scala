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

package assets.constants

import assets.constants.fullReturn.UkCompanyConstants._
import pages.ukCompanies.UkCompaniesPage
import views.behaviours.ViewBehaviours

trait UkCompanyCheckYourAnswersConstants extends ViewBehaviours with BaseConstants {

  val userAnswersUKCompanyNetTaxReactivationIncome = emptyUserAnswers
    .set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, idx = Some(1)).get

  val userAnswersUKCompanyNetTaxReactivationExpense = emptyUserAnswers
    .set(UkCompaniesPage, ukCompanyModelReactivationMaxExpense, idx = Some(1)).get

  val userAnswersUKCompanyNetTaxRestrictionIncome = emptyUserAnswers
    .set(UkCompaniesPage, ukCompanyModelRestrictionMaxIncome, idx = Some(1)).get

  val userAnswersUKCompanyNetTaxRestrictionExpense = emptyUserAnswers
    .set(UkCompaniesPage, ukCompanyModelRestrictionMaxExpense, idx = Some(1)).get

  val confirmCompany = "Confirm company"

}
