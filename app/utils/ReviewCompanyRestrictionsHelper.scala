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

  def getAmount(restrictionIdx: Int): Option[BigDecimal] = {
    val accountingPeriod: Option[LocalDate] = userAnswers.get(CompanyAccountingPeriodEndDatePage(idx, restrictionIdx))
    accountingPeriod.map(
      _ => userAnswers.get(RestrictionAmountForAccountingPeriodPage(idx, restrictionIdx)).getOrElse(BigDecimal(0))
    )
  }

  val firstAmount: Option[BigDecimal] = getAmount(1)
  val secondAmount: Option[BigDecimal] = getAmount(2)
  val thirdAmount: Option[BigDecimal] = getAmount(3)
  val totalAmount: BigDecimal = Seq(firstAmount, secondAmount, thirdAmount).flatten.sum

  val firstAmountRow: Option[SummaryListRow] =
    firstAmount.map(amount =>
      summaryListRow(
        label = messages("reviewCompanyRestrictionsPage.first.checkYourAnswersLabel", Seq()),
        value = currencyFormat(amount),
        visuallyHiddenText = None,
        controllers.ukCompanies.routes.RestrictionAmountForAccountingPeriodController.onPageLoad(idx, 1, NormalMode) -> messages("site.review"),
        controllers.ukCompanies.routes.RestrictionDeletionConfirmationController.onPageLoad(idx, 1, NormalMode) -> messages("site.delete")
      )
    )

  val secondAmountRow: Option[SummaryListRow] =
    secondAmount.map(amount =>
      summaryListRow(
        label = messages("reviewCompanyRestrictionsPage.second.checkYourAnswersLabel", Seq()),
        value = currencyFormat(amount),
        visuallyHiddenText = None,
        controllers.ukCompanies.routes.RestrictionAmountForAccountingPeriodController.onPageLoad(idx, 2, NormalMode) -> messages("site.review"),
        controllers.ukCompanies.routes.RestrictionDeletionConfirmationController.onPageLoad(idx, 2, NormalMode) -> messages("site.delete")
      )
    )

  val thirdAmountRow: Option[SummaryListRow] =
    thirdAmount.map(amount =>
      summaryListRow(
        label = messages("reviewCompanyRestrictionsPage.third.checkYourAnswersLabel", Seq()),
        value = currencyFormat(amount),
        visuallyHiddenText = None,
        controllers.ukCompanies.routes.RestrictionAmountForAccountingPeriodController.onPageLoad(idx, 3, NormalMode) -> messages("site.review"),
        controllers.ukCompanies.routes.RestrictionDeletionConfirmationController.onPageLoad(idx, 3, NormalMode) -> messages("site.delete")
      )
    )

  val totalAmountRow: Option[SummaryListRow] =
    Some(summaryListRow(
      label = messages("reviewCompanyRestrictionsPage.total.checkYourAnswersLabel", Seq()),
      value = currencyFormat(totalAmount),
      visuallyHiddenText = None
    ))
    

  val rows: Seq[SummaryListRow] = Seq(
    firstAmountRow,
    secondAmountRow,
    thirdAmountRow,
    totalAmountRow
  ).flatten

}
