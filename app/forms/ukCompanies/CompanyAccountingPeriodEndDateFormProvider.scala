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

package forms.ukCompanies

import java.time.LocalDate
import forms.mappings.Mappings
import javax.inject.Inject
import play.api.data.Form
import play.api.data.Mapping
import play.api.data.FieldMapping
import models.UserAnswers
import pages.aboutReturn.AccountingPeriodPage
import pages.ukCompanies.CompanyAccountingPeriodEndDatePage
import play.api.i18n.Messages
import utils.ImplicitLocalDateFormatter._

class CompanyAccountingPeriodEndDateFormProvider @Inject() extends Mappings {

  def apply(idx: Int, restrictionidx: Int, userAnswers: UserAnswers)(implicit messages: Messages): Form[LocalDate] = {

    val page = CompanyAccountingPeriodEndDatePage(idx, restrictionidx)

    val fieldMapping = localDate(
      invalidKey     = "companyAccountingPeriodEndDate.error.invalid",
      allRequiredKey = "companyAccountingPeriodEndDate.error.required.all",
      twoRequiredKey = "companyAccountingPeriodEndDate.error.required.two",
      requiredKey    = "companyAccountingPeriodEndDate.error.required"
    )
 
    val accountingPeriodEndDateMapping = restrictionidx match {
      case 1 => formWithFirstAPValidation(fieldMapping, userAnswers)
      case 2 => formWithSecondAPValidation(fieldMapping, userAnswers, page)
      case _ => formWithThirdAPValidation(fieldMapping, userAnswers, page)
    }

    Form("value" -> accountingPeriodEndDateMapping)

  }

  def formWithFirstAPValidation(form: FieldMapping[LocalDate], userAnswers: UserAnswers)(implicit messages: Messages): Mapping[LocalDate] = {
    val periodOfAccountStartDate = userAnswers.get(AccountingPeriodPage).map(_.startDate)
    form
      .verifying(periodOfAccountError(periodOfAccountStartDate), endDate => afterDate(endDate, periodOfAccountStartDate))
  }

  def formWithSecondAPValidation(form: FieldMapping[LocalDate], userAnswers: UserAnswers, page: CompanyAccountingPeriodEndDatePage): Mapping[LocalDate] = {
    val companysFirstAPEndDate = userAnswers.get(page.copy(restrictionIdx = 1))
    form
      .verifying("companyAccountingPeriodEndDate.error.second.notBefore", endDate => afterDate(endDate, companysFirstAPEndDate))
      .verifying("companyAccountingPeriodEndDate.error.second.afterOneYear", endDate => notMoreThanOneYearAfterDate(endDate, companysFirstAPEndDate))
  }

  def formWithThirdAPValidation(form: FieldMapping[LocalDate], userAnswers: UserAnswers, page: CompanyAccountingPeriodEndDatePage): Mapping[LocalDate] = {
    val companysSecondAPEndDate = userAnswers.get(page.copy(restrictionIdx = 2))
    form
      .verifying("companyAccountingPeriodEndDate.error.third.notBefore", endDate => afterDate(endDate, companysSecondAPEndDate))
      .verifying("companyAccountingPeriodEndDate.error.third.afterOneYear", endDate => notMoreThanOneYearAfterDate(endDate, companysSecondAPEndDate))
  }

  def afterDate(endDate: LocalDate, thatDate: Option[LocalDate]): Boolean = thatDate match {
    case Some(firstEndDate) if endDate.isAfter(firstEndDate) => true
    case _ => false
  }

  def notMoreThanOneYearAfterDate(endDate: LocalDate, thatDate: Option[LocalDate]): Boolean = thatDate match {
    case Some(firstEndDate) if !endDate.isAfter(firstEndDate.plusMonths(12)) => true
    case _ => false
  }

  def periodOfAccountError(periodOfAccountStartDate: Option[LocalDate])(implicit messages: Messages) = periodOfAccountStartDate match {
    case Some(startDate) => 
      val startDateString = startDate.toFormattedString
      messages("companyAccountingPeriodEndDate.error.first.afterStart", startDateString)
    case None => messages("companyAccountingPeriodEndDate.error.first.periodOfAccount")
  }


}
