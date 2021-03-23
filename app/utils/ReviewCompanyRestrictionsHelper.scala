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

import models.{NormalMode, UserAnswers}
import pages.ukCompanies.{CompanyAccountingPeriodEndDatePage, RestrictionAmountForAccountingPeriodPage}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._
import java.time.LocalDate

class ReviewCompanyRestrictionsHelper(val idx: Int, val userAnswers: UserAnswers)(implicit val messages: Messages) extends CheckYourAnswersHelper {

  val firstAmount: Option[BigDecimal] = getAmount(1)
  val secondAmount: Option[BigDecimal] = getAmount(2)
  val thirdAmount: Option[BigDecimal] = getAmount(3)
  val totalAmount: BigDecimal = Seq(firstAmount, secondAmount, thirdAmount).flatten.sum
  val numberOfAccountingPeriods: Int = Seq(firstAmount, secondAmount, thirdAmount).flatten.size

  val firstAmountRow: Option[SummaryListRow] = firstAmount.map(amount => restrictionRow(1, amount))

  val secondAmountRow: Option[SummaryListRow] = secondAmount.map(amount => restrictionRow(2, amount))

  val thirdAmountRow: Option[SummaryListRow] = thirdAmount.map(amount => restrictionRow(3, amount))

  val totalAmountRow: Option[SummaryListRow] =
    Some(summaryListRow(
      label = messages("reviewCompanyRestrictionsPage.total.checkYourAnswersLabel"),
      value = currencyFormat(totalAmount),
      visuallyHiddenText = None
    ))
    
  val rows: Seq[SummaryListRow] = Seq(
    firstAmountRow,
    secondAmountRow,
    thirdAmountRow,
    totalAmountRow
  ).flatten

  def getAmount(restrictionIdx: Int): Option[BigDecimal] = {
    val accountingPeriod: Option[LocalDate] = userAnswers.get(CompanyAccountingPeriodEndDatePage(idx, restrictionIdx))
    accountingPeriod.map(
      _ => userAnswers.get(RestrictionAmountForAccountingPeriodPage(idx, restrictionIdx)).getOrElse(BigDecimal(0))
    )
  }

  def restrictionRow(restrictionIdx: Int, amount: BigDecimal): SummaryListRow = {
    val reviewButton = controllers.ukCompanies.routes.RestrictionAmountForAccountingPeriodController.onPageLoad(idx, restrictionIdx, NormalMode) -> messages("site.review")
    val deleteButton = controllers.ukCompanies.routes.RestrictionDeletionConfirmationController.onPageLoad(idx, restrictionIdx, NormalMode) -> messages("site.delete")

    val actions = restrictionIdx match {
      case 1 => Seq(reviewButton)
      case `numberOfAccountingPeriods` => Seq(reviewButton, deleteButton)
      case _ => Seq(reviewButton)
    }

    val labelKey = restrictionIdx match {
      case 1 => "reviewCompanyRestrictionsPage.first.checkYourAnswersLabel"
      case 2 => "reviewCompanyRestrictionsPage.second.checkYourAnswersLabel"
      case _ => "reviewCompanyRestrictionsPage.third.checkYourAnswersLabel"
    }

    summaryListRow(
      label = messages(labelKey),
      value = currencyFormat(amount),
      visuallyHiddenText = None,
      actions = actions: _*
    )
  }

}
