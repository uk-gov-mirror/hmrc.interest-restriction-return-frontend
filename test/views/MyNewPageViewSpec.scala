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

import forms.MyNewPageFormProvider
import models.{MyNewPage, NormalMode}
import nunjucks.MyNewPageTemplate
import play.api.Application
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.behaviours.CheckboxViewBehaviours
import views.html.MyNewPageView

class MyNewPageViewSpec extends CheckboxViewBehaviours[MyNewPage] with NunjucksSupport {

  val messageKeyPrefix = "myNewPage"

  val form = new MyNewPageFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"MyNewPage ($templatingSystem) view" must {

      val view = viewFor[MyNewPageView](Some(emptyUserAnswers))

      def applyView(form: Form[Set[MyNewPage]]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(MyNewPageTemplate, Json.obj(
            "form" -> form,
            "checkboxes" -> MyNewPage.options(form),
            "mode" -> NormalMode,
            "errorMessage" -> Json.obj("text" -> messages("error.invalid"))
          ))(fakeRequest))
        } else {
          val view = viewFor[MyNewPageView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like checkboxPage(form, applyView, messageKeyPrefix, MyNewPage.options(form))
    }
  }
}

