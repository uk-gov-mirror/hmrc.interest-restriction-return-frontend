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
import controllers.actions.{FakeIdentifierAction, MockDataRetrievalAction}
import controllers.errors
import forms.groupStructure.DeemedParentFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeGroupStructureNavigator
import pages.groupStructure.HasDeemedParentPage
import play.api.test.Helpers._
import views.html.groupStructure.DeemedParentView

class DeemedParentControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[DeemedParentView]
  val formProvider = injector.instanceOf[DeemedParentFormProvider]
  val form = formProvider()

  object Controller extends HasDeemedParentController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeGroupStructureNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "DeemedParent Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(
        form = form,
        mode = NormalMode,
        postAction = routes.HasDeemedParentController.onSubmit(NormalMode)
      )(fakeRequest, messages, frontendAppConfig).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, true).success.value

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
