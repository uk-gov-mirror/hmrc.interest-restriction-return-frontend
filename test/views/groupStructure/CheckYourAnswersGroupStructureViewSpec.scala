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

package views.groupStructure

import assets.constants.GroupStructureCheckYourAnswersConstants
import assets.messages.BaseMessages.saveAndContinue
import assets.messages.{CheckAnswersGroupStructureMessages, SectionHeaderMessages}
import models.Section.GroupStructure
import play.twirl.api.HtmlFormat
import utils.CheckYourAnswersGroupStructureHelper
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView
import assets.constants.DeemedParentConstants._

class CheckYourAnswersGroupStructureViewSpec extends ViewBehaviours with GroupStructureCheckYourAnswersConstants {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"$GroupStructure.checkYourAnswers"
  val groupStructureSubheading = s"$GroupStructure.checkYourAnswers.subheading"
  val groupStructureHeading = s"$GroupStructure.checkYourAnswers.heading"

  val view = injector.instanceOf[CheckYourAnswersView]

  def applyView(checkYourAnswersHelper: CheckYourAnswersGroupStructureHelper)(): HtmlFormat.Appendable = {
    view.apply(checkYourAnswersHelper.rows(1), GroupStructure, onwardRoute)(fakeRequest, messages, frontendAppConfig)
  }

  "CheckYourAnswer view" when {

    "ultimate parent is uk company" must {

        val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(userAnswersUKCompany)

        behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.groupStructure))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), groupStructureSubheading)

        behave like pageWithHeading(applyView(checkYourAnswersHelper)(), groupStructureHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

        implicit lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        checkYourAnswersRowChecks(
          CheckAnswersGroupStructureMessages.reportingCompanySameAsParent -> "No",
          CheckAnswersGroupStructureMessages.deemedParent -> "No",
          CheckAnswersGroupStructureMessages.parentCompanyName -> deemedParentModelUkCompany.companyName.toString,
          CheckAnswersGroupStructureMessages.payTaxInUk -> "Yes",
          CheckAnswersGroupStructureMessages.limitedLiabilityPartnership -> "No",
          CheckAnswersGroupStructureMessages.parentCompanyCTUTR -> deemedParentModelUkCompany.ctutr.toString,
          CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse -> "Yes",
          CheckAnswersGroupStructureMessages.parentCRN -> deemedParentModelUkCompany.crn.toString
        )
      }

    "ultimate parent is UK LLP" must {

        val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(userAnswersUKLLP)

        behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.groupStructure))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), groupStructureSubheading)

        behave like pageWithHeading(applyView(checkYourAnswersHelper)(), groupStructureHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

        implicit lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        checkYourAnswersRowChecks(
          CheckAnswersGroupStructureMessages.reportingCompanySameAsParent -> "No",
          CheckAnswersGroupStructureMessages.deemedParent -> "No",
          CheckAnswersGroupStructureMessages.parentCompanyName -> deemedParentModelUkPartnership.companyName.toString,
          CheckAnswersGroupStructureMessages.payTaxInUk -> "Yes",
          CheckAnswersGroupStructureMessages.limitedLiabilityPartnership -> "Yes",
          CheckAnswersGroupStructureMessages.parentCompanySAUTR -> deemedParentModelUkPartnership.sautr.toString,
          CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse -> "Yes",
          CheckAnswersGroupStructureMessages.parentCRN -> deemedParentModelUkPartnership.crn.toString
        )
      }

    "ultimate parent is foreign company" must {

        val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(userAnswersForeignRegisteredCompany)

        behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.groupStructure))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), groupStructureSubheading)

        behave like pageWithHeading(applyView(checkYourAnswersHelper)(), groupStructureHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

        implicit lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        checkYourAnswersRowChecks(
          CheckAnswersGroupStructureMessages.reportingCompanySameAsParent ->  "No",
          CheckAnswersGroupStructureMessages.deemedParent -> "No",
          CheckAnswersGroupStructureMessages.parentCompanyName -> deemedParentModelNonUkCompany.companyName.toString,
          CheckAnswersGroupStructureMessages.payTaxInUk -> "No",
          CheckAnswersGroupStructureMessages.registeredForTaxInAnotherCountry -> "Yes",
          CheckAnswersGroupStructureMessages.registeredCountry -> deemedParentModelNonUkCompany.countryOfIncorporation.get.country,
          CheckAnswersGroupStructureMessages.localCRN -> deemedParentModelNonUkCompany.nonUkCrn.get
        )
      }
  }
}
