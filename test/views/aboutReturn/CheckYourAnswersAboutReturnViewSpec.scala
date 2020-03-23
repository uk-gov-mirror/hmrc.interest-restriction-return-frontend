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

package views.aboutReturn

import java.time.format.{DateTimeFormatter, FormatStyle}

import assets.constants.BaseConstants
import assets.constants.AccountingPeriodConstants._
import assets.messages.BaseMessages._
import assets.messages.{CheckAnswersAboutReturnMessages, SectionHeaderMessages}
import models.FullOrAbbreviatedReturn.Full
import models.Section._
import pages.aboutReturn._
import play.twirl.api.HtmlFormat
import utils.{CheckYourAnswersAboutReturnCompanyHelper, ImplicitDateFormatter}
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersAboutReturnViewSpec extends ViewBehaviours with BaseConstants with ImplicitDateFormatter {

  object Selectors extends BaseSelectors
  val section = "aboutReturn"
  val messageKeyPrefix = s"$section.checkYourAnswers"
  val reportingCompanySubheading = s"$section.checkYourAnswers.subheading"
  val reportingCompanyHeading = s"$section.checkYourAnswers.heading"

  val userAnswers = emptyUserAnswers
    .set(ReportingCompanyAppointedPage, true).get
    .set(AgentActingOnBehalfOfCompanyPage, true).get
    .set(AgentNamePage, agentName).get
    .set(FullOrAbbreviatedReturnPage, Full).get
    .set(ReportingCompanyNamePage, companyNameModel.name).get
    .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
    .set(AccountingPeriodStartPage, accountingPeriodModel.startDate).get
    .set(AccountingPeriodEndPage, accountingPeriodModel.endDate).get

  val checkYourAnswersHelper = new CheckYourAnswersAboutReturnCompanyHelper(userAnswers)

  s"CheckYourAnswer view" must {

    def applyView(): HtmlFormat.Appendable = {
      val view = viewFor[CheckYourAnswersView](Some(userAnswers))
      view.apply(checkYourAnswersHelper.rows, AboutReturn, onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(), messageKeyPrefix, section = Some(SectionHeaderMessages.aboutReturn))

    behave like pageWithBackLink(applyView())

    behave like pageWithSubHeading(applyView(), reportingCompanySubheading)

    behave like pageWithHeading(applyView(), reportingCompanyHeading)

    behave like pageWithSubmitButton(applyView(), saveAndContinue)

    behave like pageWithSaveForLater(applyView())

    implicit lazy val document = asDocument(applyView())

    checkYourAnswersRowChecks(

      CheckAnswersAboutReturnMessages.reportingCompanyAppointed -> "Yes",
      CheckAnswersAboutReturnMessages.agentAppointed -> "Yes",
      CheckAnswersAboutReturnMessages.agentName -> agentName,
      CheckAnswersAboutReturnMessages.fullOrAbbreviatedReturn -> "Full",
      CheckAnswersAboutReturnMessages.name -> companyNameModel.name,
      CheckAnswersAboutReturnMessages.ctutr -> ctutrModel.utr,
      CheckAnswersAboutReturnMessages.accountingPeriodStart -> accountingPeriodModel.startDate,
      CheckAnswersAboutReturnMessages.accountingPeriodEnd -> accountingPeriodModel.endDate
    )
  }
}
