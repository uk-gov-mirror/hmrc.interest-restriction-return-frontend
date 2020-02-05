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

package controllers.startReturn

import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import controllers.errors
import forms.startReturn.FullOrAbbreviatedReturnFormProvider
import models.{FullOrAbbreviatedReturn, NormalMode}
import navigation.FakeNavigators.FakeStartReturnNavigator
import pages.startReturn.FullOrAbbreviatedReturnPage
import play.api.data.Form
import play.api.libs.json.{JsObject, Json}
import play.api.test.Helpers._
import play.twirl.api.Html
import views.html.startReturn.FullOrAbbreviatedReturnView

class FullOrAbbreviatedReturnControllerSpec extends SpecBase with FeatureSwitching {

  val formProvider = new FullOrAbbreviatedReturnFormProvider()
  val view = injector.instanceOf[FullOrAbbreviatedReturnView]
  val form = formProvider()

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new FullOrAbbreviatedReturnController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeStartReturnNavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = new DataRequiredActionImpl,
    formProvider = new FullOrAbbreviatedReturnFormProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "FullOrAbbreviatedReturn Controller" must {

    "return OK and the correct view for a GET" in {

      val result = controller(FakeDataRetrievalActionEmptyAnswers).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.values.head).success.value

      val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", FullOrAbbreviatedReturn.values.head.toString))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val result = controller(FakeDataRetrievalActionNone).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", FullOrAbbreviatedReturn.values.head.toString))

      val result = controller(FakeDataRetrievalActionNone).onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
