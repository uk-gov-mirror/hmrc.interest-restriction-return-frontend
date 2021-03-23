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

import controllers.ukCompanies.{routes => ukCompanyRoutes}
import models.{CheckMode, UserAnswers}
import pages.QuestionPage
import pages.ukCompanies._
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import utils.ImplicitLocalDateFormatter.DateFormatter


class CheckYourAnswersRestrictionHelper(val userAnswers: UserAnswers)
                                       (implicit val messages: Messages) extends CheckYourAnswersHelper {

  def accountingPeriodEndDate(companyIdx: Int, restrictionIdx: Int): Option[SummaryListRow] = {
    val page = CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx)
    userAnswers.get(page).map { endDate =>
      summaryListRow(
        label = messages(s"$page.checkYourAnswersLabel"),
        value = endDate.toFormattedString,
        visuallyHiddenText = None,
        actions = ukCompanyRoutes.CompanyAccountingPeriodEndDateController.onPageLoad(companyIdx, restrictionIdx, CheckMode) -> messages("site.edit")
      )
    }
  }

  def accountingPeriodRestrictionAmount(companyIdx: Int, restrictionIdx: Int): Option[SummaryListRow] = {
    val page = RestrictionAmountForAccountingPeriodPage(companyIdx, restrictionIdx)
    userAnswers.get(page).map { amount =>
      summaryListRow(
        label = messages(s"$page.checkYourAnswersLabel"),
        value = currencyFormat(amount),
        visuallyHiddenText = None,
        actions = ukCompanyRoutes.RestrictionAmountForAccountingPeriodController.onPageLoad(companyIdx, restrictionIdx, CheckMode) -> messages("site.edit")
      )
    }
  }

  def accountingPeriodRestriction(companyIdx: Int, restrictionIdx: Int): Option[SummaryListRow] = {
    val page = AddRestrictionAmountPage(companyIdx, restrictionIdx)
    userAnswers.get(page).map { addRestriction =>
      summaryListRow(
        label = messages(s"$page.checkYourAnswersLabel"),
        value = addRestriction,
        visuallyHiddenText = None,
        actions = ukCompanyRoutes.AddRestrictionAmountController.onPageLoad(companyIdx, restrictionIdx, CheckMode) -> messages("site.edit")
      )
    }
  }

  def rows(companyIdx: Int, restrictionIdx: Int): Seq[SummaryListRow] = Seq(
    accountingPeriodEndDate(companyIdx, restrictionIdx),
    accountingPeriodRestrictionAmount(companyIdx, restrictionIdx),
    accountingPeriodRestriction(companyIdx, restrictionIdx)
  ).flatten

}
