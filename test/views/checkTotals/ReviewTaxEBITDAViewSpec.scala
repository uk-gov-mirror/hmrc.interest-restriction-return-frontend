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

package views.checkTotals

import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.BaseMessages.confirmAndContinue
import assets.messages.SectionHeaderMessages
import models.Section.ReviewTaxEBITDA
import pages.ukCompanies.UkCompaniesPage
import play.twirl.api.HtmlFormat
import utils.ReviewTaxEBITDARowsHelper
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class ReviewTaxEBITDAViewSpec extends ViewBehaviours {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"$ReviewTaxEBITDA.checkYourAnswers"
  val ultimateParentCompanySubheading = s"$ReviewTaxEBITDA.checkYourAnswers.subheading"
  val ultimateParentCompanyHeading = s"$ReviewTaxEBITDA.checkYourAnswers.heading"

  val view = injector.instanceOf[CheckYourAnswersView]

  def applyView(checkYourAnswersHelper: ReviewTaxEBITDARowsHelper)(): HtmlFormat.Appendable =
    view.apply(checkYourAnswersHelper.rows, ReviewTaxEBITDA, onwardRoute)(fakeRequest, messages, frontendAppConfig)

  "Review Tax EBITDA view" must {

    val userAnswers = emptyUserAnswers
      .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
      .set(UkCompaniesPage, ukCompanyModelMax, Some(2)).get
      .set(UkCompaniesPage, ukCompanyModelMax, Some(3)).get
      .set(UkCompaniesPage, ukCompanyModelMax, Some(4)).get

    val checkYourAnswersHelper = new ReviewTaxEBITDARowsHelper(userAnswers)

    behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.checkTotals))

    behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

    behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ultimateParentCompanySubheading)

    behave like pageWithHeading(applyView(checkYourAnswersHelper)(), ultimateParentCompanyHeading)

    behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), confirmAndContinue)

    behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

    implicit lazy val document = asDocument(applyView(checkYourAnswersHelper)())

    checkYourAnswersRowChecks(
      ukCompanyModelMax.companyDetails.companyName -> currency(ukCompanyModelMax.taxEBITDA.get),
      ukCompanyModelMax.companyDetails.companyName -> currency(ukCompanyModelMax.taxEBITDA.get),
      ukCompanyModelMax.companyDetails.companyName -> currency(ukCompanyModelMax.taxEBITDA.get),
      ukCompanyModelMax.companyDetails.companyName -> currency(ukCompanyModelMax.taxEBITDA.get)
    )
  }
}
