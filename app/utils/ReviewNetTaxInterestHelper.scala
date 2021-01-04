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

import models.{ReviewMode, UserAnswers}
import pages.ukCompanies.UkCompaniesPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class ReviewNetTaxInterestHelper(val userAnswers: UserAnswers)
                                (implicit val messages: Messages) extends CheckYourAnswersHelper {

  def rows: Seq[SummaryListRow] = userAnswers.getList(UkCompaniesPage).zipWithIndex.flatMap {
    case (model, idx) =>
      for {
        amount <- model.netTaxInterest
        incomeOrExpense <- model.netTaxInterestIncomeOrExpense
      } yield {
        summaryListRow(
          label = model.companyDetails.companyName,
          value = s"${currencyFormat(amount)} ${messages(s"reviewNetTaxInterest.checkYourAnswers.$incomeOrExpense")}",
          actions = controllers.ukCompanies.routes.NetTaxInterestAmountController.onPageLoad(idx + 1, ReviewMode) -> messages("site.edit")
        )
      }
  }
}