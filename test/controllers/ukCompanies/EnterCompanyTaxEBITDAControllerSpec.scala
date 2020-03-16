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

package controllers.ukCompanies

import controllers.errors
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.ukCompanies.EnterCompanyTaxEBITDAFormProvider
import models.NormalMode
import pages.ukCompanies.{EnterCompanyTaxEBITDAPage, UkCompaniesPage}
import play.api.test.Helpers._
import views.html.ukCompanies.EnterCompanyTaxEBITDAView
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import assets.constants.fullReturn.UkCompanyConstants._

class EnterCompanyTaxEBITDAControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[EnterCompanyTaxEBITDAView]
  val formProvider = injector.instanceOf[EnterCompanyTaxEBITDAFormProvider]
  val form = formProvider()

  val validAnswer = 0

  object Controller extends EnterCompanyTaxEBITDAController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeUkCompaniesNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    updateSectionService = updateSectionService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "EnterCompanyTaxEBITDA Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax.copy(taxEBITDA = None), Some(1)).get))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(
        form = form,
        mode = NormalMode,
        companyName = ukCompanyModelMax.companyDetails.companyName,
        postAction = routes.EnterCompanyTaxEBITDAController.onSubmit(1, NormalMode)
      )(fakeRequest, messages, frontendAppConfig).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "01"))

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get))
      mockSetAnswers(true)

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe SEE_OTHER

      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "a"))

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get))

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result).value mustBe errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "2"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustBe errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
