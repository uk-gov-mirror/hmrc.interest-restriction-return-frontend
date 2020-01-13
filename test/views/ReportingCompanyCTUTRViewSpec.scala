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
import controllers.routes
import forms.ReportingCompanyCTUTRFormProvider
import models.NormalMode
import play.api.data.Form
import play.api.libs.json.Json
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios
import views.behaviours.StringViewBehaviours
import views.html.ReportingCompanyCTUTRView
import nunjucks.ReportingCompanyCTUTRTemplate
import nunjucks.viewmodels.BasicFormViewModel

class ReportingCompanyCTUTRViewSpec extends StringViewBehaviours with NunjucksSupport {

  val messageKeyPrefix = "reportingCompanyCTUTR"

  val form = new ReportingCompanyCTUTRFormProvider()()

  Seq(Nunjucks, Twirl).foreach { templatingSystem =>

    s"ReportingCompanyCTUTR ($templatingSystem) view" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable =
        if (templatingSystem == Nunjucks) {
          await(nunjucksRenderer.render(ReportingCompanyCTUTRTemplate, Json.toJsObject(BasicFormViewModel(form, NormalMode)))(fakeRequest))
        } else {
          val view = viewFor[ReportingCompanyCTUTRView](Some(emptyUserAnswers))
          view.apply(form, NormalMode)(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(applyView(form), messageKeyPrefix)

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), SectionHeaderMessages.reportingCompany)

      behave like stringPage(form, applyView, messageKeyPrefix, routes.ReportingCompanyCTUTRController.onSubmit(NormalMode).url)
    }
  }
}
