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

import assets.constants.fullReturn.UkCompanyConstants.companyDetailsModel
import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.ukCompanies.routes
import forms.ukCompanies.CompanyDetailsFormProvider
import models.{CompanyDetailsModel, NormalMode}
import play.api.data.{Form, FormError}
import play.twirl.api.HtmlFormat
import views.behaviours.QuestionViewBehaviours
import views.html.ukCompanies.CompanyDetailsView

class CompanyDetailsViewSpec extends QuestionViewBehaviours[CompanyDetailsModel] {

  val companyDetailsMessageKeyPrefix = "companyDetails"
  val companyNameMessageKeyPrefix = "companyDetails.companyName"
  val ctutrMessageKeyPrefix = "companyDetails.ctutr"
  val section: Option[String] = Some(messages("section.ukCompanies"))
  val form: Form[CompanyDetailsModel] = new CompanyDetailsFormProvider()()
  val companyNameField = "companyNameValue"
  val ctutrField = "ctutrValue"

  "CompanyDetailsView" must {

    def applyView(form: Form[_]): HtmlFormat.Appendable = {
      val view = viewFor[CompanyDetailsView](Some(emptyUserAnswers))
      view.apply(form, NormalMode, onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    behave like normalPage(applyView(form), companyDetailsMessageKeyPrefix, section = section)

    behave like pageWithBackLink(applyView(form))

    behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.ukCompanies)

    behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(applyView(form))

    "company name" when {

      behave like companyDetailsPage(
        form, applyView, value = companyNameField, companyNameMessageKeyPrefix, routes.CompanyDetailsController.onSubmit(1, NormalMode).url, section = section, answer = companyDetailsModel.companyName
      )

      behave like companyDetailsPageWithTextFields(
        form, applyView, companyNameMessageKeyPrefix, routes.CompanyDetailsController.onSubmit(1, NormalMode).url, fields = companyNameField
      )
    }

    "ctutr" when {

      behave like companyDetailsPage(
        form, applyView, value = ctutrField, ctutrMessageKeyPrefix, routes.CompanyDetailsController.onSubmit(1, NormalMode).url, section = section, answer = companyDetailsModel.ctutr
      )

      behave like companyDetailsPageWithTextFields(
        form, applyView, companyNameMessageKeyPrefix, routes.CompanyDetailsController.onSubmit(1, NormalMode).url, fields = ctutrField
      )
    }
  }

  def companyDetailsPage(form: Form[CompanyDetailsModel],
                         createView: Form[CompanyDetailsModel] => HtmlFormat.Appendable,
                         value: String,
                         messageKeyPrefix: String,
                         expectedFormAction: String,
                         expectedHintKey: Option[String] = None,
                         section: Option[String] = None,
                         headingArgs: Seq[String] = Seq(),
                         answer: String = companyDetailsModel
                        ): Unit = {

    "behave like a page with a string value field" when {

      "rendered" must {

        s"contain a label for the $value" in {

          val doc = asDocument(createView(form))
          val expectedHintText = expectedHintKey map (k => messages(k))
          assertContainsLabel(doc, forElement = value, expectedText = messages(s"$messageKeyPrefix.label", headingArgs: _*), expectedHintText = expectedHintText)
        }

        s"contain an input for the $value" in {

          val doc = asDocument(createView(form))
          assertRenderedById(doc, value)
        }
      }

      "rendered with a valid form" must {

        s"include the form's $value in the value input" in {

          val doc = asDocument(createView(form.fill(companyDetailsModel)))
          doc.getElementById(value).attr("value") mustBe answer
        }
      }

      "rendered with an error" must {
        val errorKey = value
        val errorMessage = "error.number"
        val error = FormError(errorKey, errorMessage)

        "show an error summary" in {

          val doc = asDocument(createView(form.withError(error)))
          assertRenderedById(doc, "error-summary-title")
        }

        s"show an error associated to the $value field" in {

          val doc = asDocument(createView(form.withError(error)))
          val errorSpan = doc.getElementsByClass("govuk-error-message").first
          errorSpan.text mustBe (messages("error.browser.title.prefix") + " " + messages(errorMessage))
        }

        "show an error prefix in the browser title" in {

          val doc = asDocument(createView(form.withError(error)))
          assertEqualsValue(doc, "title", s"""${messages("error.browser.title.prefix")} ${title(messages(s"$companyDetailsMessageKeyPrefix.title", headingArgs:_*), section)}""")
        }
      }
    }
  }

  def companyDetailsPageWithTextFields(form: Form[CompanyDetailsModel],
                                       createView: Form[CompanyDetailsModel] => HtmlFormat.Appendable,
                                       messageKeyPrefix: String,
                                       expectedFormAction: String,
                                       fields: String*): Unit = {

    "behave like a question page" when {

      "rendered" must {

        for (field <- fields) {

          s"contain an input for $field" in {
            val doc = asDocument(createView(form))
            assertRenderedById(doc, field)
          }
        }

        s"not render an error summary for $fields" in {

          val doc = asDocument(createView(form))
          assertNotRenderedById(doc, "error-summary-heading")
        }
      }

      "rendered with any error" must {

        s"show an error prefix in the browser title for $fields" in {

          val doc = asDocument(createView(form.withError(error)))
          assertEqualsValue(doc, "title", s"""${messages("error.browser.title.prefix")} ${title(messages(s"$companyDetailsMessageKeyPrefix.title"), section)}""")
        }
      }

      for (field <- fields) {

        s"rendered with an error with field '$field'" must {

          "show an error summary" in {

            val doc = asDocument(createView(form.withError(FormError(field, "error"))))
            assertRenderedById(doc, "error-summary-title")
          }

          s"show an error associated with the field '$field'" in {

            val doc = asDocument(createView(form.withError(FormError(field, "error"))))

            assertRenderedById(doc, s"$field-error")
          }
        }
      }
    }
  }
}