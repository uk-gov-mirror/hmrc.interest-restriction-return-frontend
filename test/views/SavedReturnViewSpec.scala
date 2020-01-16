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

import assets.messages.SavedReturnMessages
import nunjucks.SavedReturnTemplate
import nunjucks.viewmodels.SavedReturnViewModel
import play.api.libs.json.Json
import views.behaviours.ViewBehaviours
import views.html.SavedReturnView

class SavedReturnViewSpec extends ViewBehaviours {

  object Selectors extends BaseSelectors

  lazy val twirlViewTemplate = viewFor[SavedReturnView](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply(savedTilDate)(fakeRequest, frontendAppConfig, messages)
  lazy val nunjucksView = await(nunjucksRenderer.render(SavedReturnTemplate, Json.toJsObject(SavedReturnViewModel(savedTilDate)))(fakeRequest))

  Seq(twirlView -> Twirl, nunjucksView -> Nunjucks).foreach {
    case (html, templatingSystem) =>
      s"SavedReturnView ($templatingSystem)" must {

        behave like normalPage(html, "savedReturn")

        behave like pageWithBackLink(html)

        lazy val doc = asDocument(html)

        "have a panel notification" which {

          "has the correct heading" in {
            doc.select(Selectors.panelHeading).text mustBe SavedReturnMessages.heading
          }

          "has the correct additional content for the saved until date" in {
            doc.select(Selectors.panelBody).text mustBe SavedReturnMessages.savedTil(savedTilDate)
          }
        }

        "have the correct 1st para" in {
          doc.select(Selectors.p(1)).text mustBe SavedReturnMessages.p1
        }

        "have the correct panel indent" in {
          doc.select(Selectors.indent).text mustBe SavedReturnMessages.indent
        }

        "have the correct 2nd para" in {
          doc.select(Selectors.p(2)).text mustBe SavedReturnMessages.p2
        }

        "have the correct bullet1" which {

          lazy val bullet1 = doc.select(Selectors.bullet(1))

          "has the correct message" in {
            bullet1.text mustBe SavedReturnMessages.bullet1
          }

          "has the correct link" in {
            bullet1.select("a").attr("href") mustBe controllers.routes.SavedReturnController.nextUnansweredPage().url
          }
        }

        "have the correct bullet2" which {

          lazy val bullet1 = doc.select(Selectors.bullet(2))

          "has the correct message" in {
            bullet1.text mustBe SavedReturnMessages.bullet2
          }

          "has the correct link" in {
            bullet1.select("a").attr("href") mustBe controllers.routes.SavedReturnController.deleteAndStartAgain().url
          }
        }
      }
  }
}
