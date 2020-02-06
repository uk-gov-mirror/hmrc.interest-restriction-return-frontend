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
import utils.CheckYourAnswersHelper
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersViewSpec extends ViewBehaviours with BaseConstants {

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
    .set(ReportingCompanyCTUTRPage, ctutrModel.ctutr).get
    .set(ReportingCompanyCRNPage, crnModel.crn).get

  val checkYourAnswersHelper = new CheckYourAnswersHelper(userAnswers)

  val reportingCompanyAnswers = Seq(
    checkYourAnswersHelper.reportingCompanyAppointed,
    checkYourAnswersHelper.agentActingOnBehalfOfCompany,
    checkYourAnswersHelper.agentName,
    checkYourAnswersHelper.fullOrAbbreviatedReturn,
    checkYourAnswersHelper.reportingCompanyName,
    checkYourAnswersHelper.reportingCompanyCTUTR,
    checkYourAnswersHelper.reportingCompanyCRN
  ).flatten

  s"CheckYourAnswer view" must {

    def applyView(): HtmlFormat.Appendable = {
      val view = viewFor[CheckYourAnswersView](Some(userAnswers))
      view.apply(reportingCompanyAnswers, ReportingCompany, onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(), messageKeyPrefix, section = Some(SectionHeaderMessages.reportingCompany))

    behave like pageWithBackLink(applyView())

    behave like pageWithSubHeading(applyView(), reportingCompanySubheading)

    behave like pageWithHeading(applyView(), reportingCompanyHeading)

    behave like pageWithSubmitButton(applyView(), saveAndContinue)

    behave like pageWithSaveForLater(applyView())

    lazy val document = asDocument(applyView())

    "have an answer row for Reporting Company appointed" which {

      "should have the correct heading" in {
        document.select(Selectors.checkAnswersHeading(1)).text mustBe CheckAnswersReportingCompanyMessages.reportingCompanyAppointed
      }

      "should have the correct value" in {
        document.select(Selectors.checkAnswersAnswerValue(1)).text mustBe "Yes"
      }
    }

    "have an answer row for Agent Appointed" which {

      "should have the correct heading" in {
        document.select(Selectors.checkAnswersHeading(2)).text mustBe CheckAnswersReportingCompanyMessages.agentAppointed
      }

      "should have the correct value" in {
        document.select(Selectors.checkAnswersAnswerValue(2)).text mustBe "Yes"
      }
    }

    "have an answer row for Agent Name" which {

      "should have the correct heading" in {
        document.select(Selectors.checkAnswersHeading(3)).text mustBe CheckAnswersReportingCompanyMessages.agentName
      }

      "should have the correct value" in {
        document.select(Selectors.checkAnswersAnswerValue(3)).text mustBe agentName
      }
    }

    "have an answer row for Type of Return" which {

      "should have the correct heading" in {
        document.select(Selectors.checkAnswersHeading(4)).text mustBe CheckAnswersReportingCompanyMessages.fullOrAbbreviatedReturn
      }

      "should have the correct value" in {
        document.select(Selectors.checkAnswersAnswerValue(4)).text mustBe "Full"
      }
    }

    "have an answer row for Reporting Company name" which {

      "should have the correct heading" in {
        document.select(Selectors.checkAnswersHeading(5)).text mustBe CheckAnswersReportingCompanyMessages.name
      }

      "should have the correct value" in {
        document.select(Selectors.checkAnswersAnswerValue(5)).text mustBe companyNameModel.name
      }
    }

    "have an answer row for CTUTR" which {

      "should have the correct heading" in {
        document.select(Selectors.checkAnswersHeading(6)).text mustBe CheckAnswersReportingCompanyMessages.ctutr
      }

      "should have the correct value" in {
        document.select(Selectors.checkAnswersAnswerValue(6)).text mustBe ctutrModel.ctutr
      }
    }

    "have an answer row for CRN" which {

      "should have the correct heading" in {
        document.select(Selectors.checkAnswersHeading(7)).text mustBe CheckAnswersReportingCompanyMessages.crn
      }

      "should have the correct value" in {
        document.select(Selectors.checkAnswersAnswerValue(7)).text mustBe crnModel.crn
      }
    }
  }
}
