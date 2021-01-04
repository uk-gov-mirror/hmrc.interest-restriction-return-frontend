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

package utils

import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.BaseMessages
import base.SpecBase
import pages.ukCompanies.UkCompaniesPage
import viewmodels.SummaryListRowHelper


class UkCompaniesReviewAnswersListHelperSpec extends SpecBase with SummaryListRowHelper with CurrencyFormatter {

  "UkCompaniesReviewAnswersListHelper.rows" when {

    "given a list of deemed companies" should {

      "return the correct summary list row models" in {

        val helper = new UkCompaniesReviewAnswersListHelper(
          emptyUserAnswers
            .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
            .set(UkCompaniesPage, ukCompanyModelMax, Some(2)).get
            .set(UkCompaniesPage, ukCompanyModelMax, Some(3)).get
        )

        helper.rows mustBe Seq(
          summaryListRow(
            ukCompanyModelMax.companyDetails.companyName,
            ukCompanyModelMax.companyDetails.ctutr,
            controllers.ukCompanies.routes.CheckAnswersUkCompanyController.onPageLoad(1) -> BaseMessages.review,
            controllers.ukCompanies.routes.UkCompaniesDeletionConfirmationController.onPageLoad(1) -> BaseMessages.delete
          ),
          summaryListRow(
            ukCompanyModelMax.companyDetails.companyName,
            ukCompanyModelMax.companyDetails.ctutr,
            controllers.ukCompanies.routes.CheckAnswersUkCompanyController.onPageLoad(2) -> BaseMessages.review,
            controllers.ukCompanies.routes.UkCompaniesDeletionConfirmationController.onPageLoad(2) -> BaseMessages.delete
          ),
          summaryListRow(
            ukCompanyModelMax.companyDetails.companyName,
            ukCompanyModelMax.companyDetails.ctutr,
            controllers.ukCompanies.routes.CheckAnswersUkCompanyController.onPageLoad(3) -> BaseMessages.review,
            controllers.ukCompanies.routes.UkCompaniesDeletionConfirmationController.onPageLoad(3) -> BaseMessages.delete
          )
        )
      }
    }
  }
}
