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

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.groupStructure.routes
import forms.groupStructure.RegisteredForTaxInAnotherCountryFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.BaseSelectors
import views.behaviours.YesNoViewBehaviours
import views.html.groupStructure.RegisteredForTaxInAnotherCountryView

class RegisteredForTaxInAnotherCountryViewSpec extends YesNoViewBehaviours  {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = "registeredForTaxInAnotherCountry"

  val companyName = "My Company Ltd."
  val section = Some(messages("section.groupStructure"))

  val form = new RegisteredForTaxInAnotherCountryFormProvider()()

  "RegisteredForTaxInAnotherCountry view" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[RegisteredForTaxInAnotherCountryView](Some(emptyUserAnswers))
      view.apply(form, NormalMode, companyName)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(
      applyView(form),
      messageKeyPrefix,
      headingArgs = Seq(companyName),
      section = section)

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(
      form,
      applyView,
      messageKeyPrefix,
      expectedFormAction = routes.RegisteredForTaxInAnotherCountryController.onSubmit(NormalMode).url,
      headingArgs = Seq(companyName),
      section = section
    )

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.groupStructure)

    behave like pageWithSaveForLater(applyView(form))

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)
  }
}