/*
 * Copyright 2020 HM Revenue & Customs
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

import models.{CheckMode, CompanyDetailsModel, UserAnswers}
import models.returnModels.fullReturn.UkCompanyModel
import pages.ukCompanies.UkCompaniesPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import pages.ukCompanies._
import controllers.ukCompanies.{routes => ukCompanyRoutes}
import pages.QuestionPage
import play.api.libs.json.Reads
import play.api.mvc.Call


class CheckYourAnswersUkCompanyHelper(val userAnswers: UserAnswers)
                                     (implicit val messages: Messages) extends CheckYourAnswersHelper {

  private def ukCompanyModel(idx: Int): Option[UkCompanyModel] = userAnswers.get(UkCompaniesPage, Some(idx))

  private def companyDetailsModel(idx: Int): Option[CompanyDetailsModel] = ukCompanyModel(idx).map(_.companyDetails)

  def companyDetails(idx: Int): Option[SummaryListRow] =
    companyDetailsModel(idx).map(companyDetails =>
      ukCompanyAnswer(
        page = CompanyDetailsPage,
        value = companyDetails,
        changeLinkCall = ukCompanyRoutes.CompanyDetailsController.onPageLoad(idx, CheckMode)
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
      ukCompanyAnswer(
        page = EnterCompanyTaxEBITDAPage,
        value = taxEBITDA,
        changeLinkCall = ukCompanyRoutes.EnterCompanyTaxEBITDAController.onPageLoad(idx, CheckMode)
      )
    ))

  def netTaxInterestIncomeOrExpense(idx: Int): Option[SummaryListRow] =
    ukCompanyModel(idx).flatMap(_.netTaxInterestIncomeOrExpense.map(netTaxInterestIncomeOrExpense =>
      ukCompanyAnswer(
        page = NetTaxInterestIncomeOrExpensePage,
        value = netTaxInterestIncomeOrExpense,
        changeLinkCall = ukCompanyRoutes.NetTaxInterestIncomeOrExpenseController.onPageLoad(idx, CheckMode)
      )
    ))

  def netTaxInterestAmount(idx: Int): Option[SummaryListRow] =
    ukCompanyModel(idx).flatMap(_.netTaxInterest.map(netTaxInterest =>
      ukCompanyAnswer(
        page = NetTaxInterestAmountPage,
        value = netTaxInterest,
        changeLinkCall = ukCompanyRoutes.NetTaxInterestAmountController.onPageLoad(idx, CheckMode)
      )
    ))

  def rows(idx: Int): Seq[SummaryListRow] = Seq(
    companyDetails(idx),
    consentingCompany(idx),
    enterCompanyTaxEBITDA(idx),
    netTaxInterestIncomeOrExpense(idx),
    netTaxInterestAmount(idx)
  ).flatten


  def ukCompanyAnswer[A](page: QuestionPage[A],
                         value: A,
                         changeLinkCall: Call,
                         answerIsMsgKey: Boolean = false,
                         headingMessageArgs: Seq[String] = Seq())
                        (implicit messages: Messages, reads: Reads[A], conversion: A => String): SummaryListRow =
    summaryListRow(
      label = messages(s"$page.checkYourAnswersLabel", headingMessageArgs: _*),
      value = if (answerIsMsgKey) messages(s"$page.$value") else value,
      changeLinkCall -> messages("site.edit")
    )
}
