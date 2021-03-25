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
import models.UserAnswers
import pages.ukCompanies.{AddRestrictionAmountPage, CompanyAccountingPeriodEndDatePage, RestrictionAmountForAccountingPeriodPage, UkCompaniesPage}
import views.behaviours.ViewBehaviours

import java.time.LocalDate

trait CheckRestrictionConstants extends ViewBehaviours with BaseConstants {

  def userAnswersUKCompanyAddRestriction(endDate: LocalDate): UserAnswers = (for {
    comp <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelRestrictionMaxIncome, idx = Some(1))
    cap <- comp.set(CompanyAccountingPeriodEndDatePage(1, 1), endDate)
    ara <- cap.set(AddRestrictionAmountPage(1, 1), true)
    ua <- ara.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(1234.56))
  } yield ua).get

  def userAnswersUKCompanyDontAddRestriction(endDate: LocalDate): UserAnswers = (for {
    comp <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelRestrictionMaxIncome, idx = Some(1))
    cap <- comp.set(CompanyAccountingPeriodEndDatePage(1, 1), endDate)
    ua <- cap.set(AddRestrictionAmountPage(1, 1), false)
  } yield ua).get

}
