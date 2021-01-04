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
import models.returnModels.fullReturn.UkCompanyModel
import models.{CheckMode, CompanyDetailsModel, UserAnswers}
import pages.QuestionPage
import pages.ukCompanies.{UkCompaniesPage, _}
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow


class CheckYourAnswersUkCompanyHelper(val userAnswers: UserAnswers)
                                     (implicit val messages: Messages) extends CheckYourAnswersHelper {

  private def ukCompanyModel(idx: Int): Option[UkCompanyModel] = userAnswers.get(UkCompaniesPage, Some(idx))

  private def companyDetailsModel(idx: Int): Option[CompanyDetailsModel] = ukCompanyModel(idx).map(_.companyDetails)

  private def incomeOrExpense(idx: Int)(implicit messages: Messages) = {
    val incomeOrExpense = ukCompanyModel(idx).flatMap(_.netTaxInterestIncomeOrExpense).getOrElse("None")
      messages(s"netTaxInterestAmount.$incomeOrExpense")
  }

  def companyName(idx: Int): Option[SummaryListRow] =
  companyDetailsModel(idx).map(_.companyName).map(companyName =>
    summaryListRow(
      label = messages("companyDetails.companyName.checkYourAnswersLabel", Seq()),
      value = companyName,
      (ukCompanyRoutes.CompanyDetailsController.onPageLoad(idx, CheckMode), messages("site.edit"))
    )
  )
  def ctutr(idx: Int): Option[SummaryListRow] =
    companyDetailsModel(idx).map(_.ctutr).map(ctutr =>
      summaryListRow(
        label = messages("companyDetails.ctutr.checkYourAnswersLabel", Seq()),
        value = ctutr,
        (ukCompanyRoutes.CompanyDetailsController.onPageLoad(idx, CheckMode), messages("site.edit"))
      )
    )

  def consentingCompany(idx: Int): Option[SummaryListRow] =
    ukCompanyModel(idx).flatMap(_.consenting.map(consenting =>
      ukCompanyAnswer(
        page = ConsentingCompanyPage,
        value = consenting,
        changeLinkCall = ukCompanyRoutes.ConsentingCompanyController.onPageLoad(idx, CheckMode)
      )
    ))

  def enterCompanyTaxEBITDA(idx: Int): Option[SummaryListRow] =
    ukCompanyModel(idx).flatMap(_.taxEBITDA.map(taxEBITDA =>
      summaryListRow(
        label = messages("enterCompanyTaxEBITDA.checkYourAnswersLabel", Seq()),
        value = currencyFormat(taxEBITDA),
        (ukCompanyRoutes.EnterCompanyTaxEBITDAController.onPageLoad(idx, CheckMode), messages("site.edit"))
      )
    ))

  def netTaxInterestAmount(idx: Int): Option[SummaryListRow] =
    ukCompanyModel(idx).flatMap(_.netTaxInterest.map(netTaxInterest =>
      summaryListRow(
        label = messages("netTaxInterestAmount.checkYourAnswersLabel", Seq()),
        value = s"${currencyFormat(netTaxInterest)} ${incomeOrExpense(idx)}",
        (ukCompanyRoutes.NetTaxInterestAmountController.onPageLoad(idx, CheckMode), messages("site.edit"))
      )
    ))

  def companyReactivationAmount(idx: Int): Option[SummaryListRow] =
    ukCompanyModel(idx).flatMap(_.allocatedReactivations.map(reactivationModel =>
      summaryListRow(
        label = messages("reactivationAmount.checkYourAnswersLabel", Seq()),
        value = currencyFormat(reactivationModel.reactivation),
        (ukCompanyRoutes.ReactivationAmountController.onPageLoad(idx, CheckMode), messages("site.edit"))
      )
    ))


  def rows(idx: Int): Seq[SummaryListRow] = Seq(
    companyName(idx),
    ctutr(idx),
    consentingCompany(idx),
    enterCompanyTaxEBITDA(idx),
    netTaxInterestAmount(idx),
    companyReactivationAmount(idx)
  ).flatten


  def ukCompanyAnswer[A](page: QuestionPage[A],
                         value: A,
                         changeLinkCall: Call,
                         answerIsMsgKey: Boolean = false,
                         headingMessageArgs: Seq[String] = Seq())
                        (implicit messages: Messages, conversion: A => String): SummaryListRow =
    summaryListRow(
      label = messages(s"$page.checkYourAnswersLabel", headingMessageArgs: _*),
      value = if (answerIsMsgKey) messages(s"$page.$value") else value,
      changeLinkCall -> messages("site.edit")
    )
}
