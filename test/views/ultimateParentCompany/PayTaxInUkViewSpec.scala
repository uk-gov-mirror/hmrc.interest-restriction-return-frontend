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

package views.ultimateParentCompany

import assets.messages.{BaseMessages, SectionHeaderMessages}
import assets.messages.ultimateParentCompany.PayTaxInUkMessages
import controllers.ultimateParentCompany.routes
import forms.ultimateParentCompany.PayTaxInUkFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.BaseSelectors
import views.behaviours.YesNoViewBehaviours
import views.html.ultimateParentCompany.PayTaxInUkView

class PayTaxInUkViewSpec extends YesNoViewBehaviours  {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = "payTaxInUk"

  val companyName = "My Company Ltd."
  val section = Some(messages("section.ultimateParentCompany"))

  val form = new PayTaxInUkFormProvider()()

  "PayTaxInUk view" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[PayTaxInUkView](Some(emptyUserAnswers))
      view.apply(form, NormalMode, companyName, onwardRoute)(fakeRequest, messages, frontendAppConfig)
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
      expectedFormAction = onwardRoute.url,
      headingArgs = Seq(companyName),
      section = section
    )

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.ultimateParentCompany)

    behave like pageWithSaveForLater(applyView(form))

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    lazy val document = asDocument(applyView(form))

    "have a hint" in {
      document.select(Selectors.hint).text mustBe PayTaxInUkMessages.hint
    }
  }
}
