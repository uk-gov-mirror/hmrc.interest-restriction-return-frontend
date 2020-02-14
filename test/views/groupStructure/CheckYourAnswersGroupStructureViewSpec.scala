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

        lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        "have an answer row for reportingCompanySameAsParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(1)).text mustBe CheckAnswersGroupStructureMessages.reportingCompanySameAsParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(1)).text mustBe "No"
          }
        }

        "have an answer row for deemedParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(2)).text mustBe CheckAnswersGroupStructureMessages.deemedParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(2)).text mustBe "No"
          }
        }

        "have an answer row for parentCompanyName" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(3)).text mustBe CheckAnswersGroupStructureMessages.parentCompanyName
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(3)).text mustBe ultimateParentCompanyUK.companyName.toString
          }
        }

        "have an answer row for payTaxInUk" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(4)).text mustBe CheckAnswersGroupStructureMessages.payTaxInUk
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(4)).text mustBe "Yes"
          }
        }

        "have an answer row for limitedLiabilityPartnership" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(5)).text mustBe CheckAnswersGroupStructureMessages.limitedLiabilityPartnership
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(5)).text mustBe "No"
          }
        }

        "have an answer row for parentCompanyCTUTR" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(6)).text mustBe CheckAnswersGroupStructureMessages.parentCompanyCTUTR
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(6)).text mustBe ultimateParentCompanyUK.ctutr.toString
          }
        }

        "have an answer row for registeredWithCompaniesHouse" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(7)).text mustBe CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(7)).text mustBe "Yes"
          }
        }

        "have an answer row for parentCRN" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(8)).text mustBe CheckAnswersGroupStructureMessages.parentCRN
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(8)).text mustBe ultimateParentCompanyUK.crn.toString
          }
        }
      }

      "minimum values are provided" must {

        val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(userAnswersUKCompanyMin)

        behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.groupStructure))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), groupStructureSubheading)

        behave like pageWithHeading(applyView(checkYourAnswersHelper)(), groupStructureHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

        lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        "have an answer row for reportingCompanySameAsParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(1)).text mustBe CheckAnswersGroupStructureMessages.reportingCompanySameAsParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(1)).text mustBe "No"
          }
        }

        "have an answer row for deemedParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(2)).text mustBe CheckAnswersGroupStructureMessages.deemedParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(2)).text mustBe "No"
          }
        }

        "have an answer row for parentCompanyName" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(3)).text mustBe CheckAnswersGroupStructureMessages.parentCompanyName
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(3)).text mustBe ultimateParentCompanyUKMin.companyName.toString
          }
        }

        "have an answer row for payTaxInUk" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(4)).text mustBe CheckAnswersGroupStructureMessages.payTaxInUk
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(4)).text mustBe "Yes"
          }
        }

        "have an answer row for limitedLiabilityPartnership" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(5)).text mustBe CheckAnswersGroupStructureMessages.limitedLiabilityPartnership
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(5)).text mustBe "No"
          }
        }

        "have an answer row for parentCompanyCTUTR" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(6)).text mustBe CheckAnswersGroupStructureMessages.parentCompanyCTUTR
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(6)).text mustBe ultimateParentCompanyUKMin.ctutr.toString
          }
        }

        "have an answer row for registeredWithCompaniesHouse" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(7)).text mustBe CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(7)).text mustBe "No"
          }
        }

        "have an answer row for parentCRN" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(8)).text mustBe CheckAnswersGroupStructureMessages.parentCRN
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(8)).text mustBe "None"
          }
        }
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

        lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        "have an answer row for reportingCompanySameAsParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(1)).text mustBe CheckAnswersGroupStructureMessages.reportingCompanySameAsParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(1)).text mustBe "No"
          }
        }

        "have an answer row for deemedParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(2)).text mustBe CheckAnswersGroupStructureMessages.deemedParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(2)).text mustBe "No"
          }
        }

        "have an answer row for parentCompanyName" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(3)).text mustBe CheckAnswersGroupStructureMessages.parentCompanyName
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(3)).text mustBe ultimateParentUKLLP.companyName.toString
          }
        }

        "have an answer row for payTaxInUk" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(4)).text mustBe CheckAnswersGroupStructureMessages.payTaxInUk
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(4)).text mustBe "Yes"
          }
        }

        "have an answer row for limitedLiabilityPartnership" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(5)).text mustBe CheckAnswersGroupStructureMessages.limitedLiabilityPartnership
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(5)).text mustBe "Yes"
          }
        }

        "have an answer row for parentCompanySAUTR" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(6)).text mustBe CheckAnswersGroupStructureMessages.parentCompanySAUTR
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(6)).text mustBe ultimateParentUKLLP.sautr.toString
          }
        }

        "have an answer row for registeredWithCompaniesHouse" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(7)).text mustBe CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(7)).text mustBe "Yes"
          }
        }

        "have an answer row for parentCRN" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(8)).text mustBe CheckAnswersGroupStructureMessages.parentCRN
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(8)).text mustBe ultimateParentUKLLP.crn.toString
          }
        }
      }

      "minimum values are provided" must {

        val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(userAnswersUKLLPMin)

        behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.groupStructure))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), groupStructureSubheading)

        behave like pageWithHeading(applyView(checkYourAnswersHelper)(), groupStructureHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

        lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        "have an answer row for reportingCompanySameAsParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(1)).text mustBe CheckAnswersGroupStructureMessages.reportingCompanySameAsParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(1)).text mustBe "No"
          }
        }

        "have an answer row for deemedParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(2)).text mustBe CheckAnswersGroupStructureMessages.deemedParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(2)).text mustBe "No"
          }
        }

        "have an answer row for parentCompanyName" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(3)).text mustBe CheckAnswersGroupStructureMessages.parentCompanyName
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(3)).text mustBe ultimateParentUKLLPMin.companyName.toString
          }
        }

        "have an answer row for payTaxInUk" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(4)).text mustBe CheckAnswersGroupStructureMessages.payTaxInUk
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(4)).text mustBe "Yes"
          }
        }

        "have an answer row for limitedLiabilityPartnership" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(5)).text mustBe CheckAnswersGroupStructureMessages.limitedLiabilityPartnership
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(5)).text mustBe "Yes"
          }
        }

        "have an answer row for parentCompanySAUTR" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(6)).text mustBe CheckAnswersGroupStructureMessages.parentCompanySAUTR
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(6)).text mustBe ultimateParentUKLLPMin.sautr.toString
          }
        }

        "have an answer row for registeredWithCompaniesHouse" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(7)).text mustBe CheckAnswersGroupStructureMessages.registeredWithCompaniesHouse
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(7)).text mustBe "No"
          }
        }

        "have an answer row for parentCRN" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(8)).text mustBe CheckAnswersGroupStructureMessages.parentCRN
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(8)).text mustBe "None"
          }
        }
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

        lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        "have an answer row for reportingCompanySameAsParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(1)).text mustBe CheckAnswersGroupStructureMessages.reportingCompanySameAsParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(1)).text mustBe "No"
          }
        }

        "have an answer row for deemedParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(2)).text mustBe CheckAnswersGroupStructureMessages.deemedParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(2)).text mustBe "No"
          }
        }

        "have an answer row for parentCompanyName" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(3)).text mustBe CheckAnswersGroupStructureMessages.parentCompanyName
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(3)).text mustBe ultimateParentCompanyUK.companyName.toString
          }
        }

        "have an answer row for payTaxInUk" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(4)).text mustBe CheckAnswersGroupStructureMessages.payTaxInUk
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(4)).text mustBe "No"
          }
        }

        "have an answer row for registered for tax in another country" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(5)).text mustBe CheckAnswersGroupStructureMessages.registeredForTaxInAnotherCountry
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(5)).text mustBe "Yes"
          }
        }

        "have an answer row for country of incorporation" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(6)).text mustBe CheckAnswersGroupStructureMessages.registeredCountry
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(6)).text mustBe ultimateParentCompanyForeign.countryOfIncorporation.get.country
          }
        }

        "have an answer row for local CRN" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(7)).text mustBe CheckAnswersGroupStructureMessages.localCRN
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(7)).text mustBe ultimateParentCompanyForeign.nonUkCrn.get
          }
        }
      }

      "minimum values are provided" must {

        val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(userAnswersForeignNotRegisteredCompany)

        behave like normalPage(applyView(checkYourAnswersHelper)(), messageKeyPrefix, section = Some(SectionHeaderMessages.groupStructure))

        behave like pageWithBackLink(applyView(checkYourAnswersHelper)())

        behave like pageWithSubHeading(applyView(checkYourAnswersHelper)(), groupStructureSubheading)

        behave like pageWithHeading(applyView(checkYourAnswersHelper)(), groupStructureHeading)

        behave like pageWithSubmitButton(applyView(checkYourAnswersHelper)(), saveAndContinue)

        behave like pageWithSaveForLater(applyView(checkYourAnswersHelper)())

        lazy val document = asDocument(applyView(checkYourAnswersHelper)())

        "have an answer row for reportingCompanySameAsParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(1)).text mustBe CheckAnswersGroupStructureMessages.reportingCompanySameAsParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(1)).text mustBe "No"
          }
        }

        "have an answer row for deemedParent" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(2)).text mustBe CheckAnswersGroupStructureMessages.deemedParent
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(2)).text mustBe "No"
          }
        }

        "have an answer row for parentCompanyName" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(3)).text mustBe CheckAnswersGroupStructureMessages.parentCompanyName
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(3)).text mustBe ultimateParentCompanyUKMin.companyName.toString
          }
        }

        "have an answer row for payTaxInUk" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(4)).text mustBe CheckAnswersGroupStructureMessages.payTaxInUk
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(4)).text mustBe "No"
          }
        }

        "have an answer row for registered for tax in another country" which {

          "should have the correct heading" in {
            document.select(Selectors.checkAnswersHeading(5)).text mustBe CheckAnswersGroupStructureMessages.registeredForTaxInAnotherCountry
          }

          "should have the correct value" in {
            document.select(Selectors.checkAnswersAnswerValue(5)).text mustBe "No"
          }
        }
      }
    }

  }
}
