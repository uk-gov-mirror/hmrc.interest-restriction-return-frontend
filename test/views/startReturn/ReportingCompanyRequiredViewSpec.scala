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

package views.startReturn

import assets.messages.SectionHeaderMessages
import views.behaviours.ViewBehaviours
import views.html.startReturn.ReportingCompanyRequiredView

class ReportingCompanyRequiredViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[ReportingCompanyRequiredView](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)

  "ReportingCompanyRequiredView" must {

    behave like normalPage(twirlView, "reportingCompanyRequired")

    behave like pageWithSubHeading(twirlView, SectionHeaderMessages.reportingCompany)

    behave like pageWithBackLink(twirlView)
  }
}
