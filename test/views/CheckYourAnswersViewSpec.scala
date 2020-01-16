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

package views

import assets.messages.BaseMessages._
import models.Section._
import models.UserAnswers
import nunjucks.CheckYourAnswersTemplate
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import utils.CheckYourAnswersHelper
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersViewSpec extends ViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "reportingCompany.checkYourAnswers"
  val reportingCompanySubheading = s"$ReportingCompany.checkYourAnswers.subheading"
  val reportingCompanyHeading = s"$ReportingCompany.checkYourAnswers.heading"
  val checkYourAnswersHelper = new CheckYourAnswersHelper(UserAnswers("id"))

  val reportingCompanyAnswers = Seq(
    checkYourAnswersHelper.reportingCompanyName,
    checkYourAnswersHelper.reportingCompanyCTUTR,
    checkYourAnswersHelper.reportingCompanyCRN
  ).flatten

  def pageWithSaveButton(view: HtmlFormat.Appendable, msg: String): Unit = {

    "behave like a page with a submit button" must {

      s"have a button with message '$msg'" in {

        val doc = asDocument(view)
        assertEqualsMessage(doc, "#main-content > div > div > button", msg)
      }
    }
  }

  def pageSaveForLater(view: HtmlFormat.Appendable): Unit = {

    "behave like a page with a save for later link" must {

      s"have a link with message ${saveForLater}" in {

        val element = asDocument(view).select("#saveForLater > a")
        element.text mustBe saveForLater
        element.attr("href") mustBe controllers.routes.SavedReturnController.onPageLoad().url
      }
    }
  }

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"CheckYourAnswer ($templatingSystem) view" must {

      def applyView(): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(
            CheckYourAnswersTemplate,
            Json.obj(
              "rows" -> reportingCompanyAnswers,
              "section" -> ReportingCompany
            ))(fakeRequest))
        } else {
          val view = viewFor[CheckYourAnswersView](Some(emptyUserAnswers))
          view.apply(reportingCompanyAnswers, "reportingCompany")(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(), messageKeyPrefix)

      behave like pageWithBackLink(applyView())

      behave like pageWithSubHeading(applyView(), reportingCompanySubheading)

      behave like pageWithHeading(applyView(), reportingCompanyHeading)

      behave like pageWithSaveButton(applyView(), saveAndContinue)

      behave like pageSaveForLater(applyView())
    }
  }
}
