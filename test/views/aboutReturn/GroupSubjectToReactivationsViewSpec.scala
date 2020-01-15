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

package views.aboutReturn

import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.aboutReturn.routes
import forms.aboutReturn.GroupSubjectToReactivationsFormProvider
import models.NormalMode
import nunjucks.GroupSubjectToReactivationsTemplate
import nunjucks.viewmodels.YesNoRadioViewModel
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.behaviours.YesNoViewBehaviours
import views.html.aboutReturn.GroupSubjectToReactivationsView
import views.{Nunjucks, Twirl}

class GroupSubjectToReactivationsViewSpec extends YesNoViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "groupSubjectToReactivations"

  val form = new GroupSubjectToReactivationsFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"GroupSubjectToReactivations ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(GroupSubjectToReactivationsTemplate, Json.toJsObject(YesNoRadioViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[GroupSubjectToReactivationsView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.aboutReturn)

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like yesNoPage(form, applyView, messageKeyPrefix, routes.GroupSubjectToReactivationsController.onSubmit(NormalMode).url)
    }
  }
}
