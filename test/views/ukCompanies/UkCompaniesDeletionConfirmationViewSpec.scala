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

package views.ukCompanies

import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.{BaseMessages, SectionHeaderMessages}
import forms.ukCompanies.UkCompaniesDeletionConfirmationFormProvider
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.ukCompanies.UkCompaniesDeletionConfirmationView

class UkCompaniesDeletionConfirmationViewSpec extends YesNoViewBehaviours  {

  val messageKeyPrefix = "ukCompaniesDeletionConfirmation"
  val section = Some(messages("section.ukCompanies"))
  val form = new UkCompaniesDeletionConfirmationFormProvider()()
  val view = viewFor[UkCompaniesDeletionConfirmationView]()

  "UkCompaniesDeletionConfirmationView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        view.apply(form, onwardRoute, ukCompanyModelMax.companyDetails.companyName)(fakeRequest, messages, frontendAppConfig)

      behave like normalPage(applyView(form), messageKeyPrefix, section = section, headingArgs = Seq(ukCompanyModelMax.companyDetails.companyName))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.ukCompanies)

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(
        form = form,
        createView = applyView,
        messageKeyPrefix = messageKeyPrefix,
        expectedFormAction = onwardRoute.url,
        section = section,
        headingArgs = Seq(ukCompanyModelMax.companyDetails.companyName)
      )

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
