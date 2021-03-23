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

package views.ukCompanies

import assets.constants.{BaseConstants, CheckRestrictionConstants}
import assets.constants.fullReturn.UkCompanyConstants.ukCompanyModelReactivationMaxIncome
import assets.messages.{BaseMessages, CheckRestrictionMessages, SectionHeaderMessages}
import models.Section.UkCompanies
import org.jsoup.nodes.Document
import play.twirl.api.HtmlFormat
import utils.CheckYourAnswersRestrictionHelper
import utils.ImplicitLocalDateFormatter.DateFormatter
import views.BaseSelectors
import views.ViewUtils.addPossessive
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

import java.time.{LocalDate, ZoneOffset}

class CheckRestrictionViewSpec extends ViewBehaviours with BaseConstants with CheckRestrictionConstants {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"restriction.checkYourAnswers"
  val ukCompaniesSubheading = s"$messageKeyPrefix.subheading"
  val ukCompaniesHeading = s"$messageKeyPrefix.heading"

  val view = injector.instanceOf[CheckYourAnswersView]

  val endDateNow = LocalDate.now(ZoneOffset.UTC)

  def applyView(checkYourAnswersHelper: CheckYourAnswersRestrictionHelper)(): HtmlFormat.Appendable = {
    view.apply(
      answers = checkYourAnswersHelper.rows(1, 1),
      section = "restriction",
      postAction = onwardRoute,
      headingMsgArgs = Seq(addPossessive(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)),
      sectionMsgArgs = Seq(ukCompanyModelReactivationMaxIncome.companyDetails.companyName),
      subheader = Some(ukCompanyModelReactivationMaxIncome.companyDetails.companyName),
      buttonMsg = "site.continue"
    )(fakeRequest, messages, frontendAppConfig
    )
  }

  "CheckRestrictionViewSpec" when {

    "add restriction is yes" must {

      val checkYourAnswersHelper = new CheckYourAnswersRestrictionHelper(userAnswersUKCompanyAddRestriction(endDateNow))

      behave like normalPage(
        view = applyView(checkYourAnswersHelper)(),
        messageKeyPrefix = messageKeyPrefix,
        section = Some(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)
      )

      behave like pageWithBackLink (applyView(checkYourAnswersHelper)())

      behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ukCompanyModelReactivationMaxIncome.companyDetails.companyName)

      behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), BaseMessages.continue)

      behave like pageWithSaveForLater (applyView(checkYourAnswersHelper)())

      implicit lazy val document: Document = asDocument(applyView(checkYourAnswersHelper)())

      checkYourAnswersRowChecks(
        CheckRestrictionMessages.endDate -> endDateNow.toFormattedString,
        CheckRestrictionMessages.restrictionAmount -> "£1,234.56",
        CheckRestrictionMessages.addRestrictionAmount -> "Yes"
      )
    }

    "add restriction is no" must {

      val checkYourAnswersHelper = new CheckYourAnswersRestrictionHelper(userAnswersUKCompanyDontAddRestriction(endDateNow))

      behave like normalPage(
        view = applyView(checkYourAnswersHelper)(),
        messageKeyPrefix = messageKeyPrefix,
        section = Some(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)
      )

      behave like pageWithBackLink (applyView(checkYourAnswersHelper)())

      behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ukCompanyModelReactivationMaxIncome.companyDetails.companyName)

      behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), BaseMessages.continue)

      behave like pageWithSaveForLater (applyView(checkYourAnswersHelper)())

      implicit lazy val document: Document = asDocument(applyView(checkYourAnswersHelper)())

      checkYourAnswersRowChecks(
        CheckRestrictionMessages.endDate -> endDateNow.toFormattedString,
        CheckRestrictionMessages.addRestrictionAmount -> "No"
      )
    }
  }
}
