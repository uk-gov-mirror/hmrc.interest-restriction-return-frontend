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

import nunjucks.UnderConstructionTemplate
import views.behaviours.ViewBehaviours
import views.html.UnderConstructionView

class UnderConstructionViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[UnderConstructionView](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)

  Seq(twirlView -> Twirl).foreach {
    case (html, templatingSystem) =>
      s"UnderConstructionView ($templatingSystem)" must {

        behave like normalPage(html, "underConstruction")

        behave like pageWithBackLink(html)
      }
  }
}
