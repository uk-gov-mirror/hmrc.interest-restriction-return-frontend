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

package views.elections

import assets.messages.{BaseMessages, SectionHeaderMessages}
import forms.elections.InvestorRatioMethodFormProvider
import models.InvestorRatioMethod
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.ViewBehaviours
import views.html.elections.InvestorRatioMethodView

class InvestorRatioMethodViewSpec extends ViewBehaviours {

  val messageKeyPrefix = "investorRatioMethod"
  val section = Some(messages("section.elections"))
  val form = new InvestorRatioMethodFormProvider()()
  val view = viewFor[InvestorRatioMethodView]()

  "InvestorRatioMethodView" must {

    def applyView(form: Form[InvestorRatioMethod]): HtmlFormat.Appendable = view.apply(form, onwardRoute)(fakeRequest, messages, frontendAppConfig)

    behave like normalPage(applyView(form), messageKeyPrefix, section = section)

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.elections)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(applyView(form))

    InvestorRatioMethod.options(form).zipWithIndex.foreach { case (option, i) =>

      val id = if (i == 0) "value" else s"value-${i + 1}"

      s"contain radio buttons for the value '${option.value.get}'" in {

        val doc = asDocument(applyView(form))
        assertContainsRadioButton(doc, id, "value", option.value.get, false)
      }

      s"rendered with a value of '${option.value.get}'" must {

        s"have the '${option.value.get}' radio button selected" in {

          val formWithData = form.bind(Map("value" -> s"${option.value.get}"))
          val doc = asDocument(applyView(formWithData))

          assertContainsRadioButton(doc, id, "value", option.value.get, true)
        }
      }
    }
  }
}
