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

package views.checkTotals
import assets.messages.{BaseMessages, SectionHeaderMessages}
import views.behaviours.ViewBehaviours
import views.html.checkTotals.DerivedCompanyView

class DerivedCompanyViewSpec extends ViewBehaviours {

  lazy val view = viewFor[DerivedCompanyView](Some(emptyUserAnswers))
  val call = controllers.checkTotals.routes.DerivedCompanyController.onSubmit()
  lazy val applyView = view.apply(postAction = call)(fakeRequest, frontendAppConfig, messages)

  val messageKeyPrefix = "derivedCompany"
  val section = Some(SectionHeaderMessages.checkTotals)

  "DerivedCompanyView" must {

    behave like normalPage(applyView, messageKeyPrefix, section = section)

    behave like pageWithBackLink(applyView)

    behave like pageWithSubHeading(applyView, section.get)

    behave like pageWithSubmitButton(applyView, BaseMessages.confirm)

    behave like pageWithSaveForLater(applyView)
  }
}
