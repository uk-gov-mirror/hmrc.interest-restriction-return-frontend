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

package controllers

import base.SpecBase
import controllers.actions.{DataRequiredActionImpl, FakeDataRetrievalActionEmpty, FakeIdentifierAction}
import play.api.test.Helpers._
import views.html.CheckYourAnswersView

class CheckYourAnswersControllerSpec extends SpecBase {

  object TestCheckYourAnswersController extends CheckYourAnswersController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = FakeDataRetrievalActionEmpty,
    requireData = new DataRequiredActionImpl,
    controllerComponents = messagesControllerComponents,
    view = injector.instanceOf[CheckYourAnswersView]
  )

  "Check Your Answers Controller" must {

    "work" in {

      val result = TestCheckYourAnswersController.onPageLoad()(fakeRequest)
      status(result) mustEqual OK

      contentAsString(result) mustEqual
        view(Seq(AnswerSection(None, Seq())))(request, messages).toString

      application.stop()
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val application = applicationBuilder(userAnswers = None).build()

      val request = FakeRequest(GET, routes.CheckYourAnswersController.onPageLoad().url)

      val result = route(application, request).value

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url

      application.stop()
    }
  }
}
