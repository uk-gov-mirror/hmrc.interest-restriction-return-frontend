/*
 * Copyright 2019 HM Revenue & Customs
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

package views

import controllers.routes
import forms.HelloWorldYesNoFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.behaviours.YesNoViewBehaviours
import views.html.HelloWorldYesNoView

class HelloWorldYesNoViewSpec extends YesNoViewBehaviours {

  val messageKeyPrefix = "helloWorldYesNo"

  val form = new HelloWorldYesNoFormProvider()()

  "HelloWorldYesNo view" must {

    val view = viewFor[HelloWorldYesNoView](Some(emptyUserAnswers))

    def applyView(form: Form[_]): HtmlFormat.Appendable =
      view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)

    behave like normalPage(applyView(form), messageKeyPrefix)

    behave like pageWithBackLink(applyView(form))

    behave like yesNoPage(form = form,
                          createView = applyView,
                          messageKeyPrefix = messageKeyPrefix,
                          expectedFormAction = routes.HelloWorldYesNoController.onSubmit(NormalMode).url)

    behave like pageWithSignOutLink(applyView(form))
  }
}
