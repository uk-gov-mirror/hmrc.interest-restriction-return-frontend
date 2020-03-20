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

package views.checkTotals

import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.BaseMessages.confirm
import assets.messages.SectionHeaderMessages
import assets.messages.checkTotals.ReviewReactivationsMessages
import pages.groupLevelInformation.InterestReactivationsCapPage
import pages.checkTotals.ReviewReactivationsPage
import pages.ukCompanies.UkCompaniesPage
import play.twirl.api.HtmlFormat
import utils.ReviewReactivationsHelper
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.checkTotals.ReviewReactivationsView

class ReviewReactivationsViewSpec extends ViewBehaviours {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"$ReviewReactivationsPage"
  val subheading = s"$ReviewReactivationsPage.subheading"
  val heading = s"$ReviewReactivationsPage.heading"

  val view = injector.instanceOf[ReviewReactivationsView]

  def applyView(reviewReactivationsHelper: ReviewReactivationsHelper)(): HtmlFormat.Appendable =
    view.apply(
      reviewReactivationsHelper.rows,
      onwardRoute,
      interestReactivationCap,
      reviewReactivationsHelper.totalReactivations
    )(fakeRequest, frontendAppConfig, messages)

  "Review Reactivations view" must {

    val userAnswers = emptyUserAnswers
      .set(InterestReactivationsCapPage, interestReactivationCap).get
      .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
      .set(UkCompaniesPage, ukCompanyModelMax, Some(2)).get
      .set(UkCompaniesPage, ukCompanyModelMax, Some(3)).get
      .set(UkCompaniesPage, ukCompanyModelMax, Some(4)).get

    val reviewReactivationsHelper = new ReviewReactivationsHelper(userAnswers)

    behave like normalPage(applyView(reviewReactivationsHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.checkTotals))

    behave like pageWithBackLink(applyView(reviewReactivationsHelper)())

    behave like pageWithSubHeading(applyView(reviewReactivationsHelper)(), subheading)

    behave like pageWithHeading(applyView(reviewReactivationsHelper)(), heading)

    behave like pageWithSubmitButton(applyView(reviewReactivationsHelper)(), confirm)

    behave like pageWithSaveForLater(applyView(reviewReactivationsHelper)())

    implicit lazy val document = asDocument(applyView(reviewReactivationsHelper)())

    checkYourAnswersRowChecks(
      ukCompanyModelMax.companyDetails.companyName -> currency(ukCompanyModelMax.allocatedReactivations.get.reactivation),
      ukCompanyModelMax.companyDetails.companyName -> currency(ukCompanyModelMax.allocatedReactivations.get.reactivation),
      ukCompanyModelMax.companyDetails.companyName -> currency(ukCompanyModelMax.allocatedReactivations.get.reactivation),
      ukCompanyModelMax.companyDetails.companyName -> currency(ukCompanyModelMax.allocatedReactivations.get.reactivation)
    )

    "include a panel indent" which {

      lazy val panelIndent = document.select(indent)

      "contains the first paragraph for the Reactivation Cap" in {
        panelIndent.select("p:nth-of-type(1)").text mustBe ReviewReactivationsMessages.reactivationCap(interestReactivationCap)
      }

      "contains the second paragraph for the Total Allocated Reactivations" in {
        val totalReactivations = ukCompanyModelMax.allocatedReactivations.get.reactivation * 4
        panelIndent.select("p:nth-of-type(2)").text mustBe ReviewReactivationsMessages.totalReactivations(totalReactivations)
      }
    }
  }
}
