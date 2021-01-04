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

package views.elections

import assets.messages.{BaseMessages, SectionHeaderMessages}
import assets.constants.PartnershipsConstants._
import assets.messages.elections.PartnershipsReviewAnswersListMessages
import forms.elections.PartnershipsReviewAnswersListFormProvider
import pages.elections.PartnershipsPage
import play.api.data.Form
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import utils.PartnershipsReviewAnswersListHelper
import viewmodels.SummaryListRowHelper
import views.behaviours.YesNoViewBehaviours
import views.html.elections.PartnershipsReviewAnswersListView

class PartnershipsReviewAnswersListViewSpec extends YesNoViewBehaviours with SummaryListRowHelper {

  val view: PartnershipsReviewAnswersListView = viewFor[PartnershipsReviewAnswersListView]()
  val messageKeyPrefix: String => String = addition => s"partnershipsReviewAnswersList.$addition"
  val section: Option[String] = Some(SectionHeaderMessages.elections)
  val form = new PartnershipsReviewAnswersListFormProvider()()

    "PartnershipsReviewAnswersList View" when {

      def applyView(summaryList: Seq[SummaryListRow])(form: Form[_]): HtmlFormat.Appendable =
        view.apply(
          form,
          summaryList,
          onwardRoute
        )(fakeRequest, messages, frontendAppConfig)

      "given a single partnership" must {

        val summaryList = new PartnershipsReviewAnswersListHelper(
          emptyUserAnswers
            .set(PartnershipsPage, partnershipModelUK, Some(1)).get
        ).rows

        val view = applyView(summaryList)(form)

        behave like normalPage(view, messageKeyPrefix("singular"), section = section, headingArgs = Seq(summaryList.length.toString))
      }

      "given multiple partnerships" must {

        val summaryList = new PartnershipsReviewAnswersListHelper(
          emptyUserAnswers
            .set(PartnershipsPage, partnershipModelUK, Some(1)).get
            .set(PartnershipsPage, partnershipModelUK, Some(2)).get
            .set(PartnershipsPage, partnershipModelUK, Some(3)).get
            .set(PartnershipsPage, partnershipModelUK, Some(4)).get
            .set(PartnershipsPage, partnershipModelUK, Some(5)).get
        ).rows

        val view = applyView(summaryList)(form)

        behave like normalPage(view, messageKeyPrefix("plural"), section = section, headingArgs = Seq(summaryList.length.toString))

        behave like pageWithBackLink(view)

        behave like pageWithSubmitButton(view, BaseMessages.continue)

        behave like pageWithSubHeading(view, SectionHeaderMessages.elections)

        behave like pageWithSaveForLater(view)

        "behave like a page with a Yes/No question" when {

          "rendered with no errors" must {

            lazy val doc = asDocument(applyView(summaryList)(form))

            "contain a legend for the question" in {

              val legends = doc.getElementsByTag("legend")
              legends.size mustBe 1
              legends.first.text mustBe PartnershipsReviewAnswersListMessages.addAnother
            }

            "contain an input for the value" in {

              assertRenderedByCssSelector(doc, "input[value='true']")
              assertRenderedByCssSelector(doc, "input[value='false']")
            }

            "have no values checked when rendered with no form" in {

              assert(!doc.select("input[value='true']").hasAttr("checked"))
              assert(!doc.select("input[value='false']").hasAttr("checked"))
            }

            "not render an error summary" in {
              assertNotRenderedById(doc, "error-summary_header")
            }
          }

          "rendered with an error" must {

            lazy val doc = asDocument(applyView(summaryList)(form.withError(error)))

            "show an error summary" in {
              assertRenderedById(doc, "error-summary-title")
            }

            "show an error associated with the value field" in {

              val errorSpan = doc.getElementsByClass("govuk-error-message").first

              errorSpan.text mustBe messages("error.browser.title.prefix") + " " + messages(errorMessage)
              doc.getElementsByTag("fieldset").first.attr("aria-describedby") contains errorSpan.attr("id")
            }

            "show an error prefix in the browser title" in {
              assertEqualsValue(doc, "title", s"""${messages("error.browser.title.prefix")} ${title(PartnershipsReviewAnswersListMessages.title(5), section)}""")
            }
          }
        }
      }




    }
  }
