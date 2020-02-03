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
import forms.ParentCompanyNameFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.StringViewBehaviours
import views.html.groupStructure.ParentCompanyNameView

class ParentCompanyNameViewSpec extends StringViewBehaviours  {

  val messageKeyPrefix = "parentCompanyName"

  val form = new ParentCompanyNameFormProvider()()

  s"ParentCompanyName view" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[ParentCompanyNameView](Some(emptyUserAnswers))
      view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.groupStructure)

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like stringPage(form, applyView, messageKeyPrefix, routes.ParentCompanyNameController.onSubmit(NormalMode).url)

    behave like pageWithSaveForLater(applyView(form))
  }
}
