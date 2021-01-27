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
import assets.constants.fullReturn.AllocatedReactivationsConstants._
import assets.messages.BaseMessages
import forms.ukCompanies.ReactivationAmountFormProvider
import models.NormalMode
import models.returnModels.fullReturn.AllocatedReactivationsModel
import play.api.data.{Form, FormError}
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.ukCompanies.ReactivationAmountView

class ReactivationAmountViewSpec extends QuestionViewBehaviours[AllocatedReactivationsModel] {

  val messageKeyPrefix = "reactivationAmount"
  val section = Some(messages("section.ukCompanies"))
  val form = new ReactivationAmountFormProvider()()
  val headingArgs: Seq[String] = Seq()
  val number = reactivation

    "ReactivationAmountView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
          val view = viewFor[ReactivationAmountView](Some(emptyUserAnswers))
          view.apply(form, NormalMode, companyNameModel.name, onwardRoute)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix, section = section)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), companyNameModel.name)

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))

      "behave like a page with a decimal value field" when {

        "rendered" must {

          "contain a label for the value" in {

            val doc = asDocument(applyView(form))
            assertContainsLabel(doc, "value", messages(s"$messageKeyPrefix.label", headingArgs:_*))
          }

          "contain an input for the value" in {

            val doc = asDocument(applyView(form))
            assertRenderedById(doc, "value")
          }
        }

        "rendered with a valid form" must {

          "include the form's value in the value input" in {

            val doc = asDocument(applyView(form.fill(allocatedReactivationsModel)))
            println(doc.getElementById("value"))
            doc.getElementById("value").attr("value") mustBe number.toString
          }
        }

        "rendered with an error" must {
          val errorKey = value
          val errorMessage = "error.number"
          val error = FormError(errorKey, errorMessage)

          "show an error summary" in {

            val doc = asDocument(applyView(form.withError(error)))
            assertRenderedById(doc, "error-summary-title")
          }

          "show an error associated with the value field" in {

            val doc = asDocument(applyView(form.withError(error)))
            val errorSpan = doc.getElementsByClass("govuk-error-message").first
            errorSpan.text mustBe (messages("error.browser.title.prefix") + " " + messages(errorMessage))
          }

          "show an error prefix in the browser title" in {

            val doc = asDocument(applyView(form.withError(error)))
            assertEqualsValue(doc, "title", s"""${messages("error.browser.title.prefix")} ${title(messages(s"$messageKeyPrefix.title", headingArgs:_*), section)}""")
          }
        }
      }

      "behave like a currency page" which {

        val doc = asDocument(applyView(form))

        "has the currency input class on the input field" in {
          doc.select("input").hasClass("govuk-currency-input__inner__input") mustBe true
        }

        "has a div for the currency unit with a pound sign" in {
          doc.select("div.govuk-input__prefix").text mustBe "£"
        }
      }

    }
  }
