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
import controllers.elections.routes
import forms.elections.GroupRatioPercentageFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.DecimalViewBehaviours
import views.html.elections.GroupRatioPercentageView

class GroupRatioPercentageViewSpec extends DecimalViewBehaviours  {

  val messageKeyPrefix = "groupRatioPercentage"

  val form = new GroupRatioPercentageFormProvider()()

    "GroupRatioPercentageView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
          val view = viewFor[GroupRatioPercentageView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix, section = Some(SectionHeaderMessages.elections))

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.elections)

      behave like decimalPage(
        form,
        applyView,
        messageKeyPrefix,
        routes.GroupRatioPercentageController.onSubmit(NormalMode).url,
        section = Some(SectionHeaderMessages.elections)
      )

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }