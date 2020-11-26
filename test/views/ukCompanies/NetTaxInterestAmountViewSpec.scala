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

import assets.constants.fullReturn.UkCompanyConstants.companyNameModel
import assets.messages.BaseMessages
import assets.messages.ukCompanies.NetTaxInterestAmountMessages
import forms.ukCompanies.NetTaxInterestAmountFormProvider
import models.NetTaxInterestIncomeOrExpense.{NetTaxInterestExpense, NetTaxInterestIncome}
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.DecimalViewBehaviours
import views.html.ukCompanies.NetTaxInterestAmountView
import views.ViewUtils.addPossessive

class NetTaxInterestAmountViewSpec extends DecimalViewBehaviours  {

  val section = Some(NetTaxInterestAmountMessages.subheading(companyNameModel.name))
  val view = viewFor[NetTaxInterestAmountView]()
  val form = new NetTaxInterestAmountFormProvider()(NetTaxInterestIncome)

  "NetTaxInterestAmountView for an income" must {

    val messageKeyPrefix = "netTaxInterestAmount.income"

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode, companyNameModel.name, NetTaxInterestIncome, onwardRoute)(fakeRequest, messages, frontendAppConfig)

    behave like normalPage(applyView(form), messageKeyPrefix, section = section, headingArgs = Seq(addPossessive(companyNameModel.name)))

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), section.get)

    behave like decimalPage(
      form = form,
      createView = applyView,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = onwardRoute.url,
      section = section,
      headingArgs = Seq(addPossessive(companyNameModel.name)))

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(applyView(form))
  }

  "NetTaxInterestAmountView for an expense" must {

    val form = new NetTaxInterestAmountFormProvider()(NetTaxInterestExpense)

    val messageKeyPrefix = "netTaxInterestAmount.expense"

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      view.apply(form, NormalMode, companyNameModel.name, NetTaxInterestExpense, onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(form), messageKeyPrefix, section = section, headingArgs = Seq(addPossessive(companyNameModel.name)))

    behave like decimalPage(
      form = form,
      createView = applyView,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = onwardRoute.url,
      section = section,
      headingArgs = Seq(addPossessive(companyNameModel.name)))
  }
}
