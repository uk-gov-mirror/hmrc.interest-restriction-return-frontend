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

package views.components

import base.SpecBase
import org.jsoup.Jsoup
import play.api.libs.json.Json
import play.twirl.api.Html
import views.html.components.button
import views.{Nunjucks, Twirl}

class ButtonSpec extends SpecBase {

  lazy val buttonComponent: button = app.injector.instanceOf[button]
  lazy val twirlPhaseBanner: Html = buttonComponent("site.continue")
  lazy val nunjucksPhaseBanner: Html =
    await(nunjucksRenderer.render("components/button/template.njk", Json.obj("msgKey" -> "site.continue"))(fakeRequest))

  object Selectors {
    val button = "button"
  }

  Seq(twirlPhaseBanner -> Twirl, nunjucksPhaseBanner -> Nunjucks).foreach {
    case (html, templatingSystem) =>
      s"button ($templatingSystem) component" must {

        lazy val document = Jsoup.parse(html.toString)

        "Have the correct class" in {
          document.select(Selectors.button).hasClass("govuk-button") mustBe true
        }

        "Have the correct button text" in {
          document.select(Selectors.button).text mustBe messages("site.continue")
        }
      }
  }
}
