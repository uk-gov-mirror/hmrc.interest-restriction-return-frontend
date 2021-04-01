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

package views.reviewAndComplete

import assets.messages.{BaseMessages, SectionHeaderMessages}
import views.behaviours.ViewBehaviours
import views.html.reviewAndComplete.DeclarationView
import views.{Twirl}

class DeclarationViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[DeclarationView](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply(controllers.reviewAndComplete.routes.DeclarationController.onSubmit())(fakeRequest, frontendAppConfig, messages)

  val messageKeyPrefix = "declaration"
  val section = Some(messages("section.reviewAndComplete"))

      "DeclarationView" must {

        behave like normalPage(twirlView, messageKeyPrefix, section = section)
        behave like pageWithBackLink(twirlView)
        behave like pageWithSubmitButton(twirlView, BaseMessages.acceptAndSubmit)
      }
  }
