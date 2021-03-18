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

import assets.messages.{BaseMessages, SectionHeaderMessages}
import forms.ultimateParentCompany.ParentCompanyNameFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.StringViewBehaviours
import views.html.ultimateParentCompany.ParentCompanyNameView

class ParentCompanyNameViewSpec extends StringViewBehaviours  {

  val messageKeyPrefix = "parentCompanyName"
  val ultimatelabel : String = "Enter the name of the ultimate parent"
  val ultimateRequired = "parentCompanyName.ultimate.error.required"

  val form = new ParentCompanyNameFormProvider()(ultimateRequired)

  s"ParentCompanyName view" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[ParentCompanyNameView](Some(emptyUserAnswers))
      view.apply(form, NormalMode, ultimatelabel, onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.ultimateParentCompany)

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like stringPage(form, applyView, messageKeyPrefix, onwardRoute.url)

    behave like pageWithSaveForLater(applyView(form))
  }
}
