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

import assets.constants.BaseConstants
import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.groupStructure.routes.LimitedLiabilityPartnershipController
import forms.LimitedLiabilityPartnershipFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.groupStructure.LimitedLiabilityPartnershipView

class LimitedLiabilityPartnershipViewSpec extends YesNoViewBehaviours with BaseConstants {

  val messageKeyPrefix = "limitedLiabilityPartnership"
  val section = Some(messages("section.groupStructure"))
  val form = new LimitedLiabilityPartnershipFormProvider()()

  "LimitedLiabilityPartnershipView" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[LimitedLiabilityPartnershipView](Some(emptyUserAnswers))
      view.apply(form, NormalMode, companyNameModel.name)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(
      view = applyView(form),
      messageKeyPrefix = messageKeyPrefix,
      section = section,
      headingArgs = Seq(companyNameModel.name)
    )

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.groupStructure)

    behave like yesNoPage(
      form = form,
      createView = applyView,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = LimitedLiabilityPartnershipController.onSubmit(NormalMode).url,
      section = section,
      headingArgs = Seq(companyNameModel.name)
    )

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(applyView(form))
  }
}
