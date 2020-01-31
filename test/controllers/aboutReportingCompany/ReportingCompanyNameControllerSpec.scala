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

package controllers.aboutReportingCompany

import base.SpecBase
import config.featureSwitch.{FeatureSwitching}
import controllers.actions._
import controllers.errors
import forms.aboutReportingCompany.ReportingCompanyNameFormProvider
import models.{NormalMode, UserAnswers}
import navigation.FakeNavigators.FakeAboutReportingCompanyNavigator
import pages.aboutReportingCompany.ReportingCompanyNamePage
import play.api.data.Form
import play.api.libs.json.{JsObject, Json}
import play.api.mvc.Call
import play.api.test.Helpers._
import play.twirl.api.Html

import views.html.aboutReportingCompany.ReportingCompanyNameView

class ReportingCompanyNameControllerSpec extends SpecBase   with FeatureSwitching {

  def onwardRoute = Call("GET", "/foo")

  val view = injector.instanceOf[ReportingCompanyNameView]
  val formProvider = new ReportingCompanyNameFormProvider()
  val form = formProvider()

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new ReportingCompanyNameController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeAboutReportingCompanyNavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = new DataRequiredActionImpl,
    formProvider = new ReportingCompanyNameFormProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "ReportingCompanyName Controller" must {

    "If rendering using the Twirl templating engine" must {

      "return OK and the correct view for a GET" in {

        val result = controller(FakeDataRetrievalActionEmptyAnswers).onPageLoad(NormalMode)(fakeRequest)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
      }
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(ReportingCompanyNamePage, "answer").success.value

      val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "answer"))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val result = controller(FakeDataRetrievalActionNone).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "answer"))

      val result = controller(FakeDataRetrievalActionNone).onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "return a bad request given an invalid name length (161)" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "a"*1611))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }

    "return a bad request given an invalid name length (0)" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }
  }
}
