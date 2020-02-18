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

import assets.constants.{BaseConstants, GroupStructureCheckYourAnswersConstants}
import assets.messages.BaseMessages.saveAndContinue
import assets.messages.{CheckAnswersGroupStructureMessages, SectionHeaderMessages}
import models.Section.GroupStructure
import play.twirl.api.HtmlFormat
import utils.CheckYourAnswersGroupStructureHelper
import views.BaseSelectors
import views.behaviours.ViewBehaviours
import views.html.CheckYourAnswersView

class CheckYourAnswersGroupStructureViewSpec extends ViewBehaviours with BaseConstants with GroupStructureCheckYourAnswersConstants {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = s"$GroupStructure.checkYourAnswers"
  val groupStructureSubheading = s"$GroupStructure.checkYourAnswers.subheading"
  val groupStructureHeading = s"$GroupStructure.checkYourAnswers.heading"

  val view = injector.instanceOf[CheckYourAnswersView]

  def applyView(checkYourAnswersHelper: CheckYourAnswersGroupStructureHelper)(): HtmlFormat.Appendable = {
    view.apply(checkYourAnswersHelper, GroupStructure, onwardRoute)(fakeRequest, messages, frontendAppConfig)
  }

  "CheckYourAnswer view" when {

    "ultimate parent is uk company" must {

      "maximum values are provided" must {

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
          CheckAnswersGroupStructureMessages.parentCompanyName -> ultimateParentCompanyUK.companyName.toString,
          CheckAnswersGroupStructureMessages.payTaxInUk -> "Yes",
          CheckAnswersGroupStructureMessages.limitedLiabilityPartnership -> "No",
          CheckAnswersGroupStructureMessages.parentCompanyCTUTR -> ultimateParentCompanyUK.ctutr.toString,
          CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse -> "Yes",
          CheckAnswersGroupStructureMessages.parentCRN -> ultimateParentCompanyUK.crn.toString
        )
      }

      "minimum values are provided" must {

        val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(userAnswersUKCompanyMin)

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
          CheckAnswersGroupStructureMessages.parentCompanyName -> ultimateParentCompanyUKMin.companyName.toString,
          CheckAnswersGroupStructureMessages.payTaxInUk -> "Yes",
          CheckAnswersGroupStructureMessages.limitedLiabilityPartnership -> "No",
          CheckAnswersGroupStructureMessages.parentCompanyCTUTR -> ultimateParentCompanyUKMin.ctutr.toString,
          CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse -> "No",
          CheckAnswersGroupStructureMessages.parentCRN -> "None"
        )
      }
    }

    "ultimate parent is UK LLP" must {

      "maximum values are provided" must {

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
          CheckAnswersGroupStructureMessages.parentCompanyName -> ultimateParentUKLLP.companyName.toString,
          CheckAnswersGroupStructureMessages.payTaxInUk -> "Yes",
          CheckAnswersGroupStructureMessages.limitedLiabilityPartnership -> "Yes",
          CheckAnswersGroupStructureMessages.parentCompanySAUTR -> ultimateParentUKLLP.sautr.toString,
          CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse -> "Yes",
          CheckAnswersGroupStructureMessages.parentCRN -> ultimateParentUKLLP.crn.toString
        )
      }

      "minimum values are provided" must {

        val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(userAnswersUKLLPMin)

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
          CheckAnswersGroupStructureMessages.parentCompanyName -> ultimateParentUKLLPMin.companyName.toString,
          CheckAnswersGroupStructureMessages.payTaxInUk -> "Yes",
          CheckAnswersGroupStructureMessages.limitedLiabilityPartnership -> "Yes",
          CheckAnswersGroupStructureMessages.parentCompanySAUTR -> ultimateParentUKLLPMin.sautr.toString,
          CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse -> "No",
          CheckAnswersGroupStructureMessages.parentCRN -> "None"
        )
      }
    }

    "ultimate parent is foreign company" must {

      "maximum values are provided" must {

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
          CheckAnswersGroupStructureMessages.parentCompanyName -> ultimateParentCompanyUK.companyName.toString,
          CheckAnswersGroupStructureMessages.payTaxInUk -> "No",
          CheckAnswersGroupStructureMessages.registeredForTaxInAnotherCountry -> "Yes",
          CheckAnswersGroupStructureMessages.registeredCountry -> ultimateParentCompanyForeign.countryOfIncorporation.get.country,
          CheckAnswersGroupStructureMessages.localCRN -> ultimateParentCompanyForeign.nonUkCrn.get
        )
      }

      "minimum values are provided" must {

        val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(userAnswersForeignNotRegisteredCompany)

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
          CheckAnswersGroupStructureMessages.parentCompanyName -> ultimateParentCompanyUKMin.companyName.toString,
          CheckAnswersGroupStructureMessages.payTaxInUk -> "No",
          CheckAnswersGroupStructureMessages.registeredForTaxInAnotherCountry -> "No"
        )
      }
    }
  }
}
