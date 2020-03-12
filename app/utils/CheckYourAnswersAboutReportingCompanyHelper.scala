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

import controllers.aboutReportingCompany.{routes => aboutReportingCompanyRoutes}
import controllers.startReturn.{routes => startReturnRoutes}
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import models.{CheckMode, UserAnswers}
import pages.aboutReportingCompany._
import pages.aboutReturn.RevisingReturnPage
import pages.startReturn._
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersAboutReportingCompanyHelper(val userAnswers: UserAnswers)
                                                 (implicit val messages: Messages) extends CheckYourAnswersHelper {

  def reportingCompanyAppointed: Option[SummaryListRow] =
    answer(ReportingCompanyAppointedPage, startReturnRoutes.ReportingCompanyAppointedController.onPageLoad(CheckMode))

  def agentActingOnBehalfOfCompany: Option[SummaryListRow] =
    answer(AgentActingOnBehalfOfCompanyPage, startReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(CheckMode))

  def agentName: Option[SummaryListRow] =
    answer(AgentNamePage, startReturnRoutes.AgentNameController.onPageLoad(CheckMode))

  def fullOrAbbreviatedReturn: Option[SummaryListRow] =
    answer(FullOrAbbreviatedReturnPage, startReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(CheckMode), answerIsMsgKey = true)

  def reportingCompanyName: Option[SummaryListRow] =
    answer(ReportingCompanyNamePage, aboutReportingCompanyRoutes.ReportingCompanyNameController.onPageLoad(CheckMode))

  def reportingCompanyCTUTR: Option[SummaryListRow] =
    answer(ReportingCompanyCTUTRPage, aboutReportingCompanyRoutes.ReportingCompanyCTUTRController.onPageLoad(CheckMode))

  def accountingPeriodStart: Option[SummaryListRow] =
    answer(AccountingPeriodStartPage, aboutReportingCompanyRoutes.AccountingPeriodStartController.onPageLoad(CheckMode))

  def accountingPeriodEnd: Option[SummaryListRow] =
    answer(AccountingPeriodEndPage, aboutReportingCompanyRoutes.AccountingPeriodEndController.onPageLoad(CheckMode))

  def revisingReturn: Option[SummaryListRow] =
    answer(RevisingReturnPage, aboutReportingCompanyRoutes.RevisingReturnController.onPageLoad(CheckMode))

  val rows: Seq[SummaryListRow] = Seq(
    reportingCompanyAppointed,
    agentActingOnBehalfOfCompany,
    agentName,
    fullOrAbbreviatedReturn,
    revisingReturn,
    reportingCompanyName,
    reportingCompanyCTUTR,
    accountingPeriodStart,
    accountingPeriodEnd
  ).flatten

}
