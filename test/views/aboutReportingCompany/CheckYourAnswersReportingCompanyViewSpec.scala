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

package views.aboutReportingCompany

import assets.constants.BaseConstants
import assets.messages.BaseMessages._
import assets.messages.{CheckAnswersReportingCompanyMessages, SectionHeaderMessages}
import models.FullOrAbbreviatedReturn.Full
import models.Section._
import pages.aboutReportingCompany.{ReportingCompanyCRNPage, ReportingCompanyCTUTRPage, ReportingCompanyNamePage}
import pages.startReturn.{AgentActingOnBehalfOfCompanyPage, AgentNamePage, FullOrAbbreviatedReturnPage, ReportingCompanyAppointedPage}
import play.twirl.api.HtmlFormat
import utils.{CheckYourAnswersAboutReportingCompanyHelper, CheckYourAnswersHelper}
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersReportingCompanyViewSpec extends ViewBehaviours with BaseConstants {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"$ReportingCompany.checkYourAnswers"
  val reportingCompanySubheading = s"$ReportingCompany.checkYourAnswers.subheading"
  val reportingCompanyHeading = s"$ReportingCompany.checkYourAnswers.heading"

  val userAnswers = emptyUserAnswers
    .set(ReportingCompanyAppointedPage, true).get
    .set(AgentActingOnBehalfOfCompanyPage, true).get
    .set(AgentNamePage, agentName).get
    .set(FullOrAbbreviatedReturnPage, Full).get
    .set(ReportingCompanyNamePage, companyNameModel.name).get
    .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
    .set(ReportingCompanyCRNPage, crnModel.crn).get

  val checkYourAnswersHelper = new CheckYourAnswersAboutReportingCompanyHelper(userAnswers)

  s"CheckYourAnswer view" must {

    def applyView(): HtmlFormat.Appendable = {
      val view = viewFor[CheckYourAnswersView](Some(userAnswers))
      view.apply(checkYourAnswersHelper, ReportingCompany, onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(), messageKeyPrefix, section = Some(SectionHeaderMessages.reportingCompany))

    behave like pageWithBackLink(applyView())

    behave like pageWithSubHeading(applyView(), reportingCompanySubheading)

    behave like pageWithHeading(applyView(), reportingCompanyHeading)

    behave like pageWithSubmitButton(applyView(), saveAndContinue)

    behave like pageWithSaveForLater(applyView())

    implicit lazy val document = asDocument(applyView())

    checkYourAnswersRowChecks(
      CheckAnswersReportingCompanyMessages.reportingCompanyAppointed -> "Yes",
      CheckAnswersReportingCompanyMessages.agentAppointed -> "Yes",
      CheckAnswersReportingCompanyMessages.agentName -> agentName,
      CheckAnswersReportingCompanyMessages.fullOrAbbreviatedReturn -> "Full",
      CheckAnswersReportingCompanyMessages.name -> companyNameModel.name,
      CheckAnswersReportingCompanyMessages.ctutr -> ctutrModel.utr,
      CheckAnswersReportingCompanyMessages.crn -> crnModel.crn
    )
  }
}
