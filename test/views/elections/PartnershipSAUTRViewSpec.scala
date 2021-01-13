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

package views.elections

import assets.constants.BaseConstants
import assets.messages.{BaseMessages, SectionHeaderMessages}
import controllers.elections.routes
import forms.elections.PartnershipSAUTRFormProvider
import models.NormalMode
import play.api.data.Form
import play.twirl.api.HtmlFormat
import views.ViewUtils.addPossessive
import views.behaviours.OptionalStringViewBehaviours
import views.html.elections.PartnershipSAUTRView

class PartnershipSAUTRViewSpec extends OptionalStringViewBehaviours with BaseConstants {

  val messageKeyPrefix = "partnershipSAUTR"
  val section = Some(messages("section.elections"))
  val form = new PartnershipSAUTRFormProvider()()
  val companyName: String = "Company Name ltd"

    "PartnershipSAUTRView" must {

      def applyView(form: Form[_]): HtmlFormat.Appendable = {
          val view = viewFor[PartnershipSAUTRView](Some(emptyUserAnswers))
          view.apply(form, companyNameModel.name, routes.PartnershipSAUTRController.onSubmit(1, NormalMode))(fakeRequest, messages, frontendAppConfig)
        }

      behave like normalPage(view = applyView(form),
        messageKeyPrefix = messageKeyPrefix,
        section = section,
        headingArgs = Seq(addPossessive(companyNameModel.name))
      )

      behave like pageWithBackLink(applyView(form))

      behave like pageWithSubHeading(applyView(form), companyName)

      behave like optionalStringPage(form = form,
        createView = applyView,
        messageKeyPrefix = messageKeyPrefix,
        expectedFormAction = routes.PartnershipSAUTRController.onSubmit(1, NormalMode).url,
        section = section,
        headingArgs = Seq(addPossessive(companyNameModel.name))
      )

      behave like pageWithSubmitButton(applyView(form), BaseMessages.saveAndContinue)

      behave like pageWithSaveForLater(applyView(form))
    }
  }
