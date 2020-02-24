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

import assets.constants.DeemedParentConstants._
import assets.messages.{BaseMessages, SectionHeaderMessages}
import forms.groupStructure.DeletionConfirmationFormProvider
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.groupStructure.DeletionConfirmationView

class DeletionConfirmationViewSpec extends YesNoViewBehaviours  {

  val messageKeyPrefix = "deletionConfirmation"
  val section = Some(messages("section.groupStructure"))
  val form = new DeletionConfirmationFormProvider()()
  val view = viewFor[DeletionConfirmationView]()

  "DeletionConfirmationView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = view.apply(form, onwardRoute, deemedParentModelUkCompany.companyName.name)(fakeRequest, messages, frontendAppConfig)

      behave like normalPage(applyView(form), messageKeyPrefix, section = section, headingArgs = Seq(deemedParentModelUkCompany.companyName.name))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.groupStructure)

      behave like pageWithBackLink(applyView(form))

      behave like yesNoPage(form, applyView, messageKeyPrefix, onwardRoute.url, section = section, headingArgs = Seq(deemedParentModelUkCompany.companyName.name))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
