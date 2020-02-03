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

package views

import assets.messages.ConfirmationMessages
import views.behaviours.ViewBehaviours
import views.html.ConfirmationView

class ConfirmationViewSpec extends ViewBehaviours {

  object Selectors extends BaseSelectors

  val ref = "abc123"

  lazy val twirlViewTemplate = viewFor[ConfirmationView](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply(ref)(fakeRequest, frontendAppConfig, messages)

  Seq(twirlView -> Twirl).foreach {

    case (html, templatingSystem) =>

      s"ConfirmationView ($templatingSystem)" must {

        behave like normalPage(html, "confirmation")

        behave like pageWithBackLink(html)

        lazy val doc = asDocument(html)

        "have a panel notification" which {

          "has the correct heading" in {
            doc.select(Selectors.panelHeading).text mustBe ConfirmationMessages.heading
          }

          "has the correct additional content for acknowledgement reference" in {
            doc.select(Selectors.panelBody).text mustBe ConfirmationMessages.reference(ref)
          }
        }

        "have the correct 1st para" in {
          doc.select(Selectors.p(1)).text mustBe ConfirmationMessages.p1
        }

        "have the correct exitSurvey paragraph" which {

          lazy val exitSurvey = doc.select(Selectors.p(2))

          "has the correct text" in {
            exitSurvey.text mustBe ConfirmationMessages.feedbackLink + " " + ConfirmationMessages.feedbackTime
          }

          "has the correct link" which {

            lazy val exitSurveyLink = exitSurvey.select("a")

            "has the correct URL to the exit survey frontend" in {
              exitSurveyLink.attr("href") mustBe frontendAppConfig.exitSurveyUrl
            }

            "has the correct link text" in {
              exitSurveyLink.text mustBe ConfirmationMessages.feedbackLink
            }
          }
        }
      }
  }
}
