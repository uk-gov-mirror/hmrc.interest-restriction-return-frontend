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

import assets.messages.PhaseBannerMessages
import base.SpecBase
import nunjucks.Renderer
import org.jsoup.Jsoup
import play.api.Logger
import play.api.libs.json.Json
import play.twirl.api.{Html, HtmlFormat}
import views.html.govukComponents.phaseBanner

class PhaseBannerSpec extends SpecBase {

  lazy val twirlTemplate = app.injector.instanceOf[phaseBanner]
  lazy val nunjucksRenderer = app.injector.instanceOf[Renderer]

  lazy val twirlPhaseBanner: Html = twirlTemplate("alpha")(fakeRequest, messages, frontendAppConfig)
  lazy val nunjucksPhaseBanner: Html =
    await(nunjucksRenderer.render("components/phaseBanner/template.njk", Json.obj("phase" -> "alpha"))(fakeRequest))

  object Selectors {
    val link = "a"
    val content = "span.govuk-phase-banner__text"
    val phase = "strong.govuk-tag"
  }


  Seq(twirlPhaseBanner -> "twirl", nunjucksPhaseBanner -> "nunjucks").foreach {
    case (phaseBanner, templatingSystem) =>
      s"phaseBanner ($templatingSystem) component" must {

        "render correctly given the appropriate phase" in {

          Logger.debug(phaseBanner.toString)

          val document = Jsoup.parse(phaseBanner.toString)

          document.select(Selectors.link).attr("href") mustBe frontendAppConfig.feedbackUrl(fakeRequest)
          document.select(Selectors.link).text mustBe PhaseBannerMessages.link
          document.select(Selectors.content).text mustBe PhaseBannerMessages.content
          document.select(Selectors.phase).text mustBe PhaseBannerMessages.tag
        }
      }
  }
}
