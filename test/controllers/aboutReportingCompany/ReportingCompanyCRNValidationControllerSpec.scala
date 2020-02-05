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

import assets.constants.BaseConstants
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import connectors.httpParsers.{InvalidCRN, UnexpectedFailure, ValidCRN}
import controllers.actions._
import controllers.errors
import forms.aboutReportingCompany.ReportingCompanyCRNFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeAboutReportingCompanyNavigator
import pages.aboutReportingCompany.ReportingCompanyCRNPage
import play.api.http.Status
import play.api.test.Helpers._
import services.mocks.MockCRNValidationService
import views.html.aboutReportingCompany.ReportingCompanyCRNView

class ReportingCompanyCRNValidationControllerSpec extends SpecBase with FeatureSwitching
  with MockCRNValidationService with BaseConstants with MockDataRetrievalAction {

  val view = injector.instanceOf[ReportingCompanyCRNView]
  val formProvider = injector.instanceOf[ReportingCompanyCRNFormProvider]
  val form = formProvider()

  object Controller extends ReportingCompanyCRNController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeAboutReportingCompanyNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    crnValidationService = mockCRNValidationService,
    errorHandler = errorHandler
  )

  "ReportingCompanyCRN Controller" must {

    "If rendering using the Twirl templating engine" must {

      "return OK and the correct view for a GET" in {

        mockGetAnswers(Some(emptyUserAnswers))

        val result = Controller.onPageLoad(NormalMode)(fakeRequest)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
      }
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(ReportingCompanyCRNPage, "answer").success.value

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
    }

    "redirect to the next page when valid data is submitted" in {

      mockValidateCRN(crnModel.crn)(Right(ValidCRN))

      val request = fakeRequest.withFormUrlEncodedBody(("value", crnModel.crn))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "return a Bad Request and errors when invalid crn is submitted" in {

      mockValidateCRN(crnModel.crn)(Left(InvalidCRN))

      val request = fakeRequest.withFormUrlEncodedBody(("value", crnModel.crn))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "return a Internal Server Error when Unexpected Failure is returned" in {

      mockValidateCRN(crnModel.crn)(Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "Error")))

      val request = fakeRequest.withFormUrlEncodedBody(("value", crnModel.crn))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe INTERNAL_SERVER_ERROR
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "answer"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}
