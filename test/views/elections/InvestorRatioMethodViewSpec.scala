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
import views.behaviours.YesNoViewBehaviours
import views.html.elections.InvestorRatioMethodView
import controllers.elections.routes
import models.NormalMode

class InvestorRatioMethodViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "investorRatioMethod"
  val section = Some(messages("section.elections"))
  val form = new InvestorRatioMethodFormProvider()()
  val view = viewFor[InvestorRatioMethodView]()
  val name = "investor group name"

  "InvestorRatioMethodView" must {

    def applyView(form: Form[Boolean]): HtmlFormat.Appendable = view.apply(form, name, onwardRoute)(fakeRequest, messages, frontendAppConfig)

    behave like normalPage(applyView(form), messageKeyPrefix, section = section)

    behave like pageWithSubHeading(applyView(form), name)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(applyView(form))

    behave like yesNoPage(form, applyView, messageKeyPrefix, routes.InvestorRatioMethodController.onSubmit(1, NormalMode).url, section = section)

  }
}
