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

package controllers.groupStructure

import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import controllers.errors
import forms.groupStructure.ParentCompanyNameFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeGroupStructureNavigator
import pages.groupStructure.ParentCompanyNamePage
import play.api.mvc.Call
import play.api.test.Helpers._
import views.html.groupStructure.ParentCompanyNameView

class ParentCompanyNameControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[ParentCompanyNameView]
  val formProvider = injector.instanceOf[ParentCompanyNameFormProvider]
  val form = formProvider()

  object Controller extends ParentCompanyNameController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeGroupStructureNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    updateSectionService = updateSectionService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = new DataRequiredActionImpl,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "ParentCompanyName Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, NormalMode, routes.ParentCompanyNameController.onSubmit(1, NormalMode))(fakeRequest, messages, frontendAppConfig).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(ParentCompanyNamePage, "answer").success.value

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustEqual OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "answer"))

      mockGetAnswers(Some(emptyUserAnswers))
      mockSetAnswers(true)

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "answer"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
