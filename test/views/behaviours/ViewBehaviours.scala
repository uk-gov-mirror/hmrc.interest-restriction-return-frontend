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

package views.behaviours

import assets.messages.BaseMessages
import play.twirl.api.HtmlFormat
import views.ViewSpecBase

trait ViewBehaviours extends ViewSpecBase {

  def normalPage(view: HtmlFormat.Appendable,
                 messageKeyPrefix: String,
                 headingArgs: Seq[String] = Seq(),
                 section: Option[String] = None): Unit = {

    "behave like a normal page" when {

      "rendered" must {

        "display the correct browser title" in {

          val doc = asDocument(view)
          assertEqualsMessage(doc, "title", title(messages(s"$messageKeyPrefix.title", headingArgs:_*), section))
        }

        "display the correct page heading" in {

          val doc = asDocument(view)
          assertPageTitleEqualsMessage(doc, s"$messageKeyPrefix.heading", headingArgs:_*)
        }

        if(frontendAppConfig.languageTranslationEnabled) {
          "display language toggles" in {
            val doc = asDocument(view)
            assertRenderedById(doc, "cymraeg-switch")
          }
        }
      }
    }
  }

  def pageWithBackLink(view: HtmlFormat.Appendable): Unit = {

    "behave like a page with a back link" must {

      "have a back link" in {

        val doc = asDocument(view)
        assertRenderedById(doc, "back-link")
      }
    }
  }

  def pageWithSubHeading(view: HtmlFormat.Appendable, subheading: String): Unit = {

    "behave like a page with a Subheading" must {

      "display the correct subheading" in {
        assertEqualsMessage(asDocument(view), "span.govuk-caption-xl", subheading)
      }
    }
  }

  def pageWithHeading(view: HtmlFormat.Appendable, heading: String) = {

    "behave like a page with a Heading" must {

      "display the correct Heading" in {
        assertEqualsMessage(asDocument(view), "#main-content > div > div > h1", heading)
      }
    }
  }

  def pageWithSignOutLink(view: HtmlFormat.Appendable): Unit = {

    "behave like a page with a Sign Out link" must {

      "have a Sign Out link" in {

        val doc = asDocument(view)
        assertRenderedByCssSelector(doc, "ul.govuk-header__navigation li:nth-of-type(1) a")
      }
    }
  }

  def pageWithSubmitButton(view: HtmlFormat.Appendable, msg: String): Unit = {

    "behave like a page with a submit button" must {

      s"have a button with message '$msg'" in {

        val doc = asDocument(view)
        assertEqualsMessage(doc, "#main-content > div > div > form > button", msg)
      }
    }
  }

  def pageWithSaveForLater(view: HtmlFormat.Appendable): Unit = {

    "behave like a page with a save for later link" must {

      s"have a link with message ${BaseMessages.saveForLater}" in {

        val element = asDocument(view).getElementById("saveForLater")
        element.text mustBe BaseMessages.saveForLater
        element.attr("href") mustBe controllers.routes.SavedReturnController.onPageLoad().url
      }
    }
  }
}
