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

import assets.constants.BaseConstants
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import controllers.errors
import forms.groupStructure.ParentCompanySAUTRFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeGroupStructureNavigator
import pages.groupStructure.ParentCompanySAUTRPage
import play.api.data.Form
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Call
import play.api.test.Helpers._
import views.html.groupStructure.ParentCompanySAUTRView

class ParentCompanySAUTRControllerSpec extends SpecBase with FeatureSwitching with BaseConstants with MockDataRetrievalAction {

  val view = injector.instanceOf[ParentCompanySAUTRView]
  val formProvider = injector.instanceOf[ParentCompanySAUTRFormProvider]
  val form = formProvider()

  object Controller extends ParentCompanySAUTRController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeGroupStructureNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "ParentCompanySAUTR Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(ParentCompanySAUTRPage, sautrModel.ctutr).success.value

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "1111111111"))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER

      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "1"))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result).value mustBe errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "1111111111"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustBe errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
