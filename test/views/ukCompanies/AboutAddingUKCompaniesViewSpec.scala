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

import assets.messages.{BaseMessages, SectionHeaderMessages}
import views.behaviours.ViewBehaviours
import views.html.ukCompanies.AboutAddingUKCompaniesView

class AboutAddingUKCompaniesViewSpec extends ViewBehaviours {

  lazy val viewTemplate = viewFor[AboutAddingUKCompaniesView](Some(emptyUserAnswers))
  lazy val view = viewTemplate.apply(onwardRoute)(fakeRequest, frontendAppConfig, messages)

  val messageKeyPrefix = "aboutAddingUKCompanies"
  val section = Some(messages("section.ukCompanies"))

  "AboutAddingUKCompaniesView" must {

    behave like normalPage(view, messageKeyPrefix, section = section)

    behave like pageWithBackLink(view)

    behave like pageWithSubHeading(view, SectionHeaderMessages.ukCompanies)

    behave like pageWithSubmitButton(view, BaseMessages.saveAndContinue)

    behave like pageWithSaveForLater(view)
  }
}
