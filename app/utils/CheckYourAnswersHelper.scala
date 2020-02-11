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
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import controllers.groupStructure.{routes => groupStructureRoutes}
import controllers.startReturn.{routes => startReturnRoutes}
import controllers.elections.{routes => electionsRoutes}
import models.{CheckMode, UserAnswers}
import pages._
import pages.aboutReportingCompany.{ReportingCompanyCRNPage, ReportingCompanyCTUTRPage, ReportingCompanyNamePage}
import pages.aboutReturn._
import pages.elections.{EnterANGIEPage, GroupRatioElectionPage}
import pages.groupStructure._
import pages.startReturn._
import play.api.i18n.Messages
import play.api.libs.json.Reads
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersHelper(userAnswers: UserAnswers)(implicit messages: Messages) extends ImplicitDateFormatter {

  def enterANGIE: Option[SummaryListRow] = answer(EnterANGIEPage, electionsRoutes.EnterANGIEController.onPageLoad(CheckMode))

  def groupRatioElection: Option[SummaryListRow] = answer(GroupRatioElectionPage, electionsRoutes.GroupRatioElectionController.onPageLoad(CheckMode))

  def registeredForTaxInAnotherCountry: Option[SummaryListRow] =
    answer(RegisteredForTaxInAnotherCountryPage, groupStructureRoutes.RegisteredForTaxInAnotherCountryController.onPageLoad(CheckMode))

  def parentCRN: Option[SummaryListRow] =
    answer(ParentCRNPage, groupStructureRoutes.ParentCRNController.onPageLoad(CheckMode))

  def parentCompanyCTUTR: Option[SummaryListRow] =
    answer(ParentCompanyCTUTRPage, groupStructureRoutes.ParentCompanyCTUTRController.onPageLoad(CheckMode))

  def parentCompanySAUTR: Option[SummaryListRow] =
    answer(ParentCompanySAUTRPage, groupStructureRoutes.ParentCompanySAUTRController.onPageLoad(CheckMode))

  def payTaxInUk: Option[SummaryListRow] =
    answer(PayTaxInUkPage, groupStructureRoutes.PayTaxInUkController.onPageLoad(CheckMode))

  def reportingCompanySameAsParent: Option[SummaryListRow] =
    answer(ReportingCompanySameAsParentPage, groupStructureRoutes.ReportingCompanySameAsParentController.onPageLoad(CheckMode))

  def limitedLiabilityPartnership: Option[SummaryListRow] =
    answer(LimitedLiabilityPartnershipPage, groupStructureRoutes.LimitedLiabilityPartnershipController.onPageLoad(CheckMode))

  def registeredCompaniesHouse: Option[SummaryListRow] =
    answer(RegisteredCompaniesHousePage, groupStructureRoutes.RegisteredCompaniesHouseController.onPageLoad(CheckMode))

  def parentCompanyName: Option[SummaryListRow] =
    answer(ParentCompanyNamePage, groupStructureRoutes.ParentCompanyNameController.onPageLoad(CheckMode))

  def deemedParent: Option[SummaryListRow] =
    answer(DeemedParentPage, groupStructureRoutes.DeemedParentController.onPageLoad(CheckMode))

  def returnContainEstimates: Option[SummaryListRow] =
    answer(ReturnContainEstimatesPage, aboutReturnRoutes.ReturnContainEstimatesController.onPageLoad(CheckMode))

  def groupInterestAllowance: Option[SummaryListRow] =
    answer(GroupInterestAllowancePage, aboutReturnRoutes.GroupInterestAllowanceController.onPageLoad(CheckMode))

  def groupInterestCapacity: Option[SummaryListRow] =
    answer(GroupInterestCapacityPage, aboutReturnRoutes.GroupInterestCapacityController.onPageLoad(CheckMode))

  def groupSubjectToRestrictions: Option[SummaryListRow] =
    answer(GroupSubjectToRestrictionsPage, aboutReturnRoutes.GroupSubjectToRestrictionsController.onPageLoad(CheckMode))

  def interestReactivationsCap: Option[SummaryListRow] =
    answer(InterestReactivationsCapPage, aboutReturnRoutes.InterestReactivationsCapController.onPageLoad(CheckMode))

  def fullOrAbbreviatedReturn: Option[SummaryListRow] =
    answer(FullOrAbbreviatedReturnPage, startReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(CheckMode), answerIsMsgKey = true)

  def reportingCompanyAppointed: Option[SummaryListRow] =
    answer(ReportingCompanyAppointedPage, startReturnRoutes.ReportingCompanyAppointedController.onPageLoad(CheckMode))

  def reportingCompanyName: Option[SummaryListRow] =
    answer(ReportingCompanyNamePage, aboutReportingCompanyRoutes.ReportingCompanyNameController.onPageLoad(CheckMode))

  def reportingCompanyRequired: Option[SummaryListRow] =
    answer(ReportingCompanyRequiredPage, startReturnRoutes.ReportingCompanyRequiredController.onPageLoad())

  def reportingCompanyCTUTR: Option[SummaryListRow] =
    answer(ReportingCompanyCTUTRPage, aboutReportingCompanyRoutes.ReportingCompanyCTUTRController.onPageLoad(CheckMode))

  def reportingCompanyCRN: Option[SummaryListRow] =
    answer(ReportingCompanyCRNPage, aboutReportingCompanyRoutes.ReportingCompanyCRNController.onPageLoad(CheckMode))

  def revisingReturn: Option[SummaryListRow] =
    answer(RevisingReturnPage, aboutReturnRoutes.RevisingReturnController.onPageLoad(CheckMode))

  def groupSubjectToReactivations: Option[SummaryListRow] =
    answer(GroupSubjectToReactivationsPage, aboutReturnRoutes.GroupSubjectToReactivationsController.onPageLoad(CheckMode))

  def interestAllowanceBroughtForward: Option[SummaryListRow] =
    answer(InterestAllowanceBroughtForwardPage, aboutReturnRoutes.InterestAllowanceBroughtForwardController.onPageLoad(CheckMode))

  def agentName: Option[SummaryListRow] =
    answer(AgentNamePage, startReturnRoutes.AgentNameController.onPageLoad(CheckMode))

  def agentActingOnBehalfOfCompany: Option[SummaryListRow] =
    answer(AgentActingOnBehalfOfCompanyPage, startReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(CheckMode))

  def infrastructureCompanyElection: Option[SummaryListRow] =
    answer(InfrastructureCompanyElectionPage, aboutReturnRoutes.InfrastructureCompanyElectionController.onPageLoad(CheckMode))

  private def answer[A](page: QuestionPage[A], changeLinkCall: Call, answerIsMsgKey: Boolean = false, headingMessageArgs: Seq[String] = Seq())
                       (implicit messages: Messages, reads: Reads[A], conversion: A => String): Option[SummaryListRow] =
    userAnswers.get(page) map { ans =>
      SummaryListRow(
        key = Key(content = Text(messages(s"$page.checkYourAnswersLabel", headingMessageArgs:_*))),
        value = Value(content = Text(if(answerIsMsgKey) messages(s"$page.$ans") else ans)),
        actions = Some(Actions(
          items = Seq(ActionItem(
            href = changeLinkCall.url,
            content = Text(messages("site.edit"))
          ))
        ))
      )
    }

  implicit private val yesNoValue: Boolean => String = {
    case true => messages("site.yes")
    case _ => messages("site.no")
  }

  implicit private val intToString: Int => String = _.toString

  implicit val bigDecimalConversion: BigDecimal => String = _.toString
}
