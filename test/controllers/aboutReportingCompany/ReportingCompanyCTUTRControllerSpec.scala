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
import controllers.actions._
import controllers.errors
import forms.aboutReportingCompany.ReportingCompanyCTUTRFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeAboutReportingCompanyNavigator
import pages.aboutReportingCompany.ReportingCompanyCTUTRPage
import play.api.test.Helpers._
import views.html.aboutReportingCompany.ReportingCompanyCTUTRView

class ReportingCompanyCTUTRControllerSpec extends SpecBase with FeatureSwitching with BaseConstants with MockDataRetrievalAction {

  val view = injector.instanceOf[ReportingCompanyCTUTRView]
  val formProvider = injector.instanceOf[ReportingCompanyCTUTRFormProvider]
  val form = formProvider()

  object Controller extends ReportingCompanyCTUTRController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeAboutReportingCompanyNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    updateSectionService = updateSectionService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "ReportingCompanyCTUTR Controller" must {

    "If rendering using the Twirl templating engine" must {

      "return OK and the correct view for a GET" in {

        mockGetAnswers(Some(emptyUserAnswers))

        val result = Controller.onPageLoad(NormalMode)(fakeRequest)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString
      }
    }


    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(ReportingCompanyCTUTRPage, "answer").success.value

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ctutrModel.utr))

      mockGetAnswers(Some(emptyUserAnswers))
      mockSetAnswers(true)

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
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

    "return a bad request given an invalid UTR (big)" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "a"*1611))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }

    "return a Bad Request given an invalid UTR (0)" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "return a Bad Request given an invalid UTR (10 letters)" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "aaaaaaaaaa"))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "return a Bad Request given an invalid UTR (10 numbers bad checksum)" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "1212121212"))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }
  }
}
