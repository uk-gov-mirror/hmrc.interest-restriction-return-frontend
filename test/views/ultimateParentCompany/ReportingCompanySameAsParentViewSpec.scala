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

import assets.constants.BaseConstants
import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.ultimateParentCompany.routes
import forms.ultimateParentCompany.ReportingCompanySameAsParentFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.BaseSelectors
import views.behaviours.YesNoViewBehaviours
import views.html.ultimateParentCompany.ReportingCompanySameAsParentView

class ReportingCompanySameAsParentViewSpec extends YesNoViewBehaviours with BaseConstants {

  object Selectors extends BaseSelectors

  val messageKeyPrefix = "reportingCompanySameAsParent"

  val section = Some(messages("section.ultimateParentCompany"))

  val form = new ReportingCompanySameAsParentFormProvider()()

  "ReportingCompanySameAsParent view" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[ReportingCompanySameAsParentView](Some(emptyUserAnswers))
      view.apply(form, NormalMode, companyNameModel.name, onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(
      view = applyView(form),
      messageKeyPrefix = messageKeyPrefix,
      headingArgs = Seq(companyNameModel.name),
      section = section)

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(
      form = form,
      createView = applyView,
      messageKeyPrefix = messageKeyPrefix,
      expectedFormAction = onwardRoute.url,
      headingArgs = Seq(companyNameModel.name),
      section = section
    )

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.ultimateParentCompany)

    behave like pageWithSaveForLater(applyView(form))

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)
  }
}
