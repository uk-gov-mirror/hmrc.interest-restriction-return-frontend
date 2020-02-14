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

package views.elections

import assets.messages.{BaseMessages, SectionHeaderMessages}
import forms.elections.OtherInvestorGroupElectionsFormProvider
import models.InvestorRatioMethod.FixedRatioMethod
import models.{NormalMode, OtherInvestorGroupElections}
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.CheckboxViewBehaviours
import views.html.elections.OtherInvestorGroupElectionsView

class OtherInvestorGroupElectionsViewSpec extends CheckboxViewBehaviours[OtherInvestorGroupElections] {

  val messageKeyPrefix = "otherInvestorGroupElections"
  val section = Some(messages("section.elections"))
  val form = new OtherInvestorGroupElectionsFormProvider()()

    "OtherInvestorGroupElectionsView" must {

      val view = viewFor[OtherInvestorGroupElectionsView](Some(emptyUserAnswers))

      def applyView(form: Form[Set[OtherInvestorGroupElections]]): HtmlFormat.Appendable = {
        val view = viewFor[OtherInvestorGroupElectionsView](Some(emptyUserAnswers))
        view.apply(form, NormalMode, FixedRatioMethod)(fakeRequest, messages, frontendAppConfig)
      }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.elections)

      behave like checkboxPage(form, applyView, messageKeyPrefix, OtherInvestorGroupElections.options(form, FixedRatioMethod), messages("section.elections"))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
  }
}
