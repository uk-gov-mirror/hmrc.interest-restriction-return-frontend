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

import assets.messages.SectionHeaderMessages
import views.behaviours.ViewBehaviours
import views.html.ReportingCompanyRequiredView
import nunjucks.ReportingCompanyRequiredTemplate
import views.{Nunjucks, Twirl}

class ReportingCompanyRequiredViewSpec extends ViewBehaviours {

  lazy val twirlViewTemplate = viewFor[ReportingCompanyRequiredView](Some(emptyUserAnswers))
  lazy val twirlView = twirlViewTemplate.apply()(fakeRequest, frontendAppConfig, messages)
  lazy val nunjucksView = await(nunjucksRenderer.render(ReportingCompanyRequiredTemplate)(fakeRequest))

  Seq(twirlView -> Twirl, nunjucksView -> Nunjucks).foreach {
    case (html, templatingSystem) =>
      s"ReportingCompanyRequiredView ($templatingSystem)" must {

        behave like normalPage(html, "reportingCompanyRequired")

        behave like pageWithSubHeading(html, SectionHeaderMessages.reportingCompany)

        behave like pageWithBackLink(html)
      }
  }
}
