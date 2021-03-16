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

import java.time.{LocalDate, ZoneOffset}

import forms.behaviours.DateBehaviours
import models.returnModels.AccountingPeriodModel
import pages.aboutReturn.AccountingPeriodPage
import pages.ukCompanies.{UkCompaniesPage, CompanyAccountingPeriodEndDatePage}
import assets.constants.fullReturn.UkCompanyConstants.ukCompanyModelMax

class CompanyAccountingPeriodEndDateFormProviderSpec extends DateBehaviours {

  val periodOfAccountStartDate = LocalDate.of(2017, 1, 1)
  val periodOfAccountEndDate = LocalDate.of(2018, 1, 1)
  val periodOfAccount = AccountingPeriodModel(periodOfAccountStartDate, periodOfAccountEndDate)

  val ap1EndDate = LocalDate.of(2017, 3, 1)
  val ap2EndDate = LocalDate.of(2017, 9, 1)

  val userAnswers = (for {
    ua  <- emptyUserAnswers.set(AccountingPeriodPage, periodOfAccount)
    ua2 <- ua.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
    ua3 <- ua2.set(CompanyAccountingPeriodEndDatePage(1,1), ap1EndDate)
    ua4 <- ua3.set(CompanyAccountingPeriodEndDatePage(1,2), ap2EndDate)
  } yield ua4).get

  val firstAPEndDateForm = new CompanyAccountingPeriodEndDateFormProvider()(1, 1, userAnswers)
  val secondAPEndDateForm = new CompanyAccountingPeriodEndDateFormProvider()(1, 2, userAnswers)
  val thirdAPEndDateForm = new CompanyAccountingPeriodEndDateFormProvider()(1, 3, userAnswers)

  ".value" should {

    val validData = datesBetween(
      min = periodOfAccountStartDate,
      max = LocalDate.now(ZoneOffset.UTC)
    )

    behave like dateField(firstAPEndDateForm, "value", validData)

    behave like mandatoryDateField(firstAPEndDateForm, "value", "companyAccountingPeriodEndDate.error.required.all")

  }
  
  "Form with restriction index 1" should {
    behave like dateFieldWithMin(firstAPEndDateForm, "value", periodOfAccountStartDate.plusDays(1), "End date must be after 01 01 2017")
  }

  "Form with restriction index 2" should {
    behave like dateFieldWithMin(secondAPEndDateForm, "value", ap1EndDate.plusDays(1), "companyAccountingPeriodEndDate.error.second.notBefore")
    behave like dateFieldWithMax(firstAPEndDateForm, "value", periodOfAccountStartDate.plusMonths(12), "companyAccountingPeriodEndDate.error.second.afterOneYear")
  }

  "Form with restriction index 3" should {
    behave like dateFieldWithMin(thirdAPEndDateForm, "value", ap2EndDate.plusDays(1), "companyAccountingPeriodEndDate.error.third.notBefore")
    behave like dateFieldWithMax(firstAPEndDateForm, "value", periodOfAccountStartDate.plusMonths(12), "companyAccountingPeriodEndDate.error.third.afterOneYear")
  }
}
