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

package controllers.ultimateParentCompany

import assets.constants.BaseConstants
import assets.constants.DeemedParentConstants.deemedParentModelMin
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import controllers.errors
import forms.ultimateParentCompany.CountryOfIncorporationFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeUltimateParentCompanyNavigator
import pages.ultimateParentCompany.DeemedParentPage
import play.api.test.Helpers._
import views.html.ultimateParentCompany.CountryOfIncorporationView

class CountryOfIncorporationControllerSpec extends SpecBase with FeatureSwitching with BaseConstants with MockDataRetrievalAction {

  val view = injector.instanceOf[CountryOfIncorporationView]
  val formProvider = injector.instanceOf[CountryOfIncorporationFormProvider]
  val form = formProvider()

  object Controller extends CountryOfIncorporationController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeUltimateParentCompanyNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    updateSectionService = updateSectionService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "CountryOfIncorporation Controller" must {

    "return OK and the correct view for a GET" in {

      val userAnswers = emptyUserAnswers
        .set(DeemedParentPage, deemedParentModelMin, Some(1)).success.value

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(
        form = form,
        mode = NormalMode,
        companyName = companyNameModel.name,
        postAction = routes.CountryOfIncorporationController.onSubmit(1, NormalMode)
      )(fakeRequest, messages, frontendAppConfig).toString
    }

    "redirect to the next page when valid data is submitted" in {

      val userAnswers = emptyUserAnswers
        .set(DeemedParentPage, deemedParentModelMin, Some(1)).success.value

      val request = fakeRequest.withFormUrlEncodedBody(("value", frontendAppConfig.countryCodeMap("US")))

      mockGetAnswers(Some(userAnswers))
      mockSetAnswers

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when No data is submitted" in {

      val userAnswers = emptyUserAnswers
        .set(DeemedParentPage, deemedParentModelMin, Some(1)).success.value

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val userAnswers = emptyUserAnswers
        .set(DeemedParentPage, deemedParentModelMin, Some(1)).success.value

      mockGetAnswers(Some(userAnswers))

      val request = fakeRequest.withFormUrlEncodedBody(("value", "Invalid Country Name"))

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
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
