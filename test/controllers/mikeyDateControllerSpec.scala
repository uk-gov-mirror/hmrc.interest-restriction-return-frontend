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

import java.time.{LocalDate, ZoneOffset}

import base.SpecBase
import controllers.actions._
import forms.mikeyDateFormProvider
import models.{NormalMode, UserAnswers}
import navigation.FakeNavigator
import pages.mikeyDatePage
import play.api.test.Helpers._
import views.html.mikeyDateView

class mikeyDateControllerSpec extends SpecBase {

  val formProvider = new mikeyDateFormProvider()
  val validAnswer = LocalDate.now(ZoneOffset.UTC)

  val view = injector.instanceOf[mikeyDateView]

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new mikeyDateController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeNavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = new DataRequiredActionImpl,
    formProvider = new mikeyDateFormProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "mikeyDate Controller" must {

    "return OK and the correct view for a GET" in {

      val result = controller(FakeDataRetrievalActionEmptyAnswers).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = UserAnswers(userAnswersId).set(mikeyDatePage, validAnswer).success.value

      val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(
        "value.day"   -> validAnswer.getDayOfMonth.toString,
        "value.month" -> validAnswer.getMonthValue.toString,
        "value.year"  -> validAnswer.getYear.toString
      )

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))

      val result = controller().onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      val result = controller(FakeDataRetrievalActionNone).onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(
        "value.day"   -> validAnswer.getDayOfMonth.toString,
        "value.month" -> validAnswer.getMonthValue.toString,
        "value.year"  -> validAnswer.getYear.toString
      )

      val result = controller(FakeDataRetrievalActionNone).onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER

      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}

