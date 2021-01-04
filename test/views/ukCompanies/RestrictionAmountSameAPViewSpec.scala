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

package views.ukCompanies

import assets.constants.fullReturn.UkCompanyConstants.companyNameModel
import assets.messages.BaseMessages
import assets.messages.ukCompanies.RestrictionAmountSameAPMessages
import forms.ukCompanies.RestrictionAmountSameAPFormProvider
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.DecimalViewBehaviours
import views.html.ukCompanies.RestrictionAmountSameAPView

class RestrictionAmountSameAPViewSpec extends DecimalViewBehaviours  {

  val messageKeyPrefix = "restrictionAmountSameAP"
  val section = Some(RestrictionAmountSameAPMessages.subheading(companyNameModel.name))
  val form = new RestrictionAmountSameAPFormProvider()()
  val view = viewFor[RestrictionAmountSameAPView]()

  "RestrictionAmountSameAPView" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, companyNameModel.name, onwardRoute)(fakeRequest, messages, frontendAppConfig)

    behave like normalPage(applyView(form), messageKeyPrefix, section = section)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), section.get)

    behave like decimalPage(form, applyView, messageKeyPrefix, onwardRoute.url, section = section)

    behave like currencyPage(applyView)

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(applyView(form))
  }
}
