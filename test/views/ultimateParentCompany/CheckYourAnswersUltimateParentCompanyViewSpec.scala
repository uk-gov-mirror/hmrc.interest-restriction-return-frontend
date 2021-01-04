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

package views.ultimateParentCompany

import assets.constants.UltimateParentCompanyCheckYourAnswersConstants
import assets.messages.BaseMessages.saveAndContinue
import assets.messages.{CheckAnswersUltimateParentCompanyMessages, SectionHeaderMessages}
import models.Section.UltimateParentCompany
import play.twirl.api.HtmlFormat
import utils.CheckYourAnswersUltimateParentCompanyHelper
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView
import assets.constants.DeemedParentConstants._

class CheckYourAnswersUltimateParentCompanyViewSpec extends ViewBehaviours with UltimateParentCompanyCheckYourAnswersConstants {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"$UltimateParentCompany.checkYourAnswers"
  val ultimateParentCompanySubheading = s"$UltimateParentCompany.checkYourAnswers.subheading"
  val ultimateParentCompanyHeading = s"$UltimateParentCompany.checkYourAnswers.heading"

  val view = injector.instanceOf[CheckYourAnswersView]

  def applyView(checkYourAnswersHelper: CheckYourAnswersUltimateParentCompanyHelper, subheading: Option[String] = None)(): HtmlFormat.Appendable = {
    view.apply(
      answers = checkYourAnswersHelper.rows(1), 
      section = UltimateParentCompany, 
      postAction = onwardRoute,
      subheader = subheading
    )(fakeRequest, messages, frontendAppConfig)
  }

  "CheckYourAnswer view" when {

    "ultimate parent is uk company" must {

        val checkYourAnswersHelper = new CheckYourAnswersUltimateParentCompanyHelper(userAnswersUKCompany)

        behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.ultimateParentCompany))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ultimateParentCompanySubheading)

        behave like pageWithHeading(applyView(checkYourAnswersHelper)(), ultimateParentCompanyHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

        implicit lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        checkYourAnswersRowChecks(
          CheckAnswersUltimateParentCompanyMessages.reportingCompanySameAsParent -> "No",
          CheckAnswersUltimateParentCompanyMessages.deemedParent -> "No",
          CheckAnswersUltimateParentCompanyMessages.parentCompanyName -> deemedParentModelUkCompany.companyName.name,
          CheckAnswersUltimateParentCompanyMessages.payTaxInUk -> "Yes",
          CheckAnswersUltimateParentCompanyMessages.limitedLiabilityPartnership -> "No",
          CheckAnswersUltimateParentCompanyMessages.parentCompanyCTUTR -> deemedParentModelUkCompany.ctutr.get.utr
        )
      }

    "ultimate parent is UK LLP" must {

        val checkYourAnswersHelper = new CheckYourAnswersUltimateParentCompanyHelper(userAnswersUKLLP)

        behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.ultimateParentCompany))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ultimateParentCompanySubheading)

        behave like pageWithHeading(applyView(checkYourAnswersHelper)(), ultimateParentCompanyHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

        implicit lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        checkYourAnswersRowChecks(
          CheckAnswersUltimateParentCompanyMessages.reportingCompanySameAsParent -> "No",
          CheckAnswersUltimateParentCompanyMessages.deemedParent -> "No",
          CheckAnswersUltimateParentCompanyMessages.parentCompanyName -> deemedParentModelUkPartnership.companyName.name,
          CheckAnswersUltimateParentCompanyMessages.payTaxInUk -> "Yes",
          CheckAnswersUltimateParentCompanyMessages.limitedLiabilityPartnership -> "Yes",
          CheckAnswersUltimateParentCompanyMessages.parentCompanySAUTR -> deemedParentModelUkPartnership.sautr.get.utr
        )
      }

    "ultimate parent is foreign company" must {

        val checkYourAnswersHelper = new CheckYourAnswersUltimateParentCompanyHelper(userAnswersForeignRegisteredCompany)

        behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.ultimateParentCompany))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), ultimateParentCompanySubheading)

        behave like pageWithHeading(applyView(checkYourAnswersHelper)(), ultimateParentCompanyHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

        implicit lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        checkYourAnswersRowChecks(
          CheckAnswersUltimateParentCompanyMessages.reportingCompanySameAsParent ->  "No",
          CheckAnswersUltimateParentCompanyMessages.deemedParent -> "No",
          CheckAnswersUltimateParentCompanyMessages.parentCompanyName -> deemedParentModelNonUkCompany.companyName.name,
          CheckAnswersUltimateParentCompanyMessages.payTaxInUk -> "No" ,
          CheckAnswersUltimateParentCompanyMessages.registeredCountry -> deemedParentModelNonUkCompany.countryOfIncorporation.get.country
        )
      }


    "a subheading is passed in" must {

        val checkYourAnswersHelper = new CheckYourAnswersUltimateParentCompanyHelper(userAnswersUKCompany)

        behave like normalPage(applyView(checkYourAnswersHelper, Some("Manual Subheading"))(), messageKeyPrefix, section = Some(SectionHeaderMessages.ultimateParentCompany))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper, Some("Manual Subheading"))())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper, Some("Manual Subheading"))(), "Manual Subheading")

        behave like pageWithHeading(applyView(checkYourAnswersHelper, Some("Manual Subheading"))(), ultimateParentCompanyHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper, Some("Manual Subheading"))(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper, Some("Manual Subheading"))())

        implicit lazy val document = asDocument(applyView(checkYourAnswersHelper, Some("Manual Subheading"))())

        checkYourAnswersRowChecks(
          CheckAnswersUltimateParentCompanyMessages.reportingCompanySameAsParent -> "No",
          CheckAnswersUltimateParentCompanyMessages.deemedParent -> "No",
          CheckAnswersUltimateParentCompanyMessages.parentCompanyName -> deemedParentModelUkCompany.companyName.name,
          CheckAnswersUltimateParentCompanyMessages.payTaxInUk -> "Yes",
          CheckAnswersUltimateParentCompanyMessages.limitedLiabilityPartnership -> "No",
          CheckAnswersUltimateParentCompanyMessages.parentCompanyCTUTR -> deemedParentModelUkCompany.ctutr.get.utr
        )
      }
  }
}
