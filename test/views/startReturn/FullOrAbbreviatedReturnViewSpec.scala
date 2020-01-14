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

package views.startReturn

import assets.messages.{BaseMessages, SectionHeaderMessages}
import forms.startReturn.FullOrAbbreviatedReturnFormProvider
import models.{FullOrAbbreviatedReturn, NormalMode}
import nunjucks.FullOrAbbreviatedReturnTemplate
import nunjucks.viewmodels.RadioOptionsViewModel
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.behaviours.ViewBehaviours
import views.html.startReturn.FullOrAbbreviatedReturnView
import views.{Nunjucks, Twirl}

class FullOrAbbreviatedReturnViewSpec extends ViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "fullOrAbbreviatedReturn"

  val form = new FullOrAbbreviatedReturnFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"FullOrAbbreviatedReturn ($templatingSystem) view" must {

      def applyView(form: Form[FullOrAbbreviatedReturn]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(FullOrAbbreviatedReturnTemplate, Json.toJsObject(RadioOptionsViewModel(
            FullOrAbbreviatedReturn.options(form),
            form,
            NormalMode
          )))(fakeRequest))
        } else {
          val view = viewFor[FullOrAbbreviatedReturnView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.aboutReturn)


      "rendered" must {

        "contain radio buttons for the value" in {

          val doc = asDocument(applyView(form))

          for (option <- FullOrAbbreviatedReturn.options(form)) {
            assertContainsRadioButton(doc, option.id.get, "value", option.value.get, false)
          }
        }
      }

      for (option <- FullOrAbbreviatedReturn.options(form)) {

        s"rendered with a value of '${option.value.get}'" must {

          s"have the '${option.value.get}' radio button selected" in {

            val formWithData = form.bind(Map("value" -> s"${option.value.get}"))
            val doc = asDocument(applyView(formWithData))

            assertContainsRadioButton(doc, option.id.get, "value", option.value.get, true)
          }
        }
      }
    }
  }
}
