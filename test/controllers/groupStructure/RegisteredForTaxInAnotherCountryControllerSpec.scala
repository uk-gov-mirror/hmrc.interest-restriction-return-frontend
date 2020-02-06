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
import forms.groupStructure.RegisteredForTaxInAnotherCountryFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeGroupStructureNavigator
import pages.groupStructure.{ParentCompanyNamePage, RegisteredForTaxInAnotherCountryPage}
import play.api.test.Helpers._
import views.html.groupStructure.RegisteredForTaxInAnotherCountryView

class RegisteredForTaxInAnotherCountryControllerSpec extends SpecBase with FeatureSwitching with BaseConstants with MockDataRetrievalAction {

  val view = injector.instanceOf[RegisteredForTaxInAnotherCountryView]
  val formProvider = injector.instanceOf[RegisteredForTaxInAnotherCountryFormProvider]
  val form = formProvider()

  lazy val companyNameAnswer = emptyUserAnswers.set(ParentCompanyNamePage, companyNameModel.name).success.value

  object Controller extends RegisteredForTaxInAnotherCountryController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeGroupStructureNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    errorHandler = errorHandler
  )

  "RegisteredForTaxInAnotherCountry Controller" must {

    "For the onPageLoad() method" when {

      "a parentCompanyName is held in the user answers" should {

        "return OK and the correct view for a GET" in {

          mockGetAnswers(Some(companyNameAnswer))

          val result = Controller.onPageLoad(NormalMode)(fakeRequest)

          status(result) mustEqual OK
          contentAsString(result) mustEqual view(form, NormalMode, companyNameModel.name)(fakeRequest, messages, frontendAppConfig).toString
        }

        "populate the view correctly on a GET when the question has previously been answered" in {

          val userAnswers = companyNameAnswer.set(RegisteredForTaxInAnotherCountryPage, true).success.value

          mockGetAnswers(Some(userAnswers))

          val result = Controller.onPageLoad(NormalMode)(fakeRequest)

          status(result) mustEqual OK
        }
      }

      "a parentCompanyName is NOT held in the user answers" should {

        "return ISE and Error Page" in {

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onPageLoad(NormalMode)(fakeRequest)

          status(result) mustEqual INTERNAL_SERVER_ERROR
          contentAsString(result) mustEqual errorHandler.internalServerErrorTemplate(fakeRequest).toString
        }
      }

      "no existing data is found" should {

        "redirect to Session Expired page" in {

          mockGetAnswers(None)

          val result = Controller.onPageLoad(NormalMode)(fakeRequest)

          status(result) mustEqual SEE_OTHER

          redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
        }
      }
    }

    "For the onSubmit() method" when {

      "a parentCompanyName is held in the user answers" should {

        "redirect to the next page when valid data is submitted" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

          mockGetAnswers(Some(companyNameAnswer))

          val result = Controller.onSubmit(NormalMode)(request)

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(onwardRoute.url)
        }

        "return a Bad Request and errors when invalid data is submitted" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

          mockGetAnswers(Some(companyNameAnswer))

          val result = Controller.onSubmit(NormalMode)(request)

          status(result) mustEqual BAD_REQUEST
        }
      }

      "a parentCompanyName is NOT held in the user answers and is a bad request with invalid data" should {

        "return ISE and Error Page" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onSubmit(NormalMode)(request)

          status(result) mustEqual INTERNAL_SERVER_ERROR
          contentAsString(result) mustEqual errorHandler.internalServerErrorTemplate(fakeRequest).toString
        }
      }

      "no existing data is found" should {

        "redirect to Session Expired page" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

          mockGetAnswers(None)

          val result = Controller.onSubmit(NormalMode)(request)

          status(result) mustEqual SEE_OTHER

          redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
        }
      }
    }
  }
}
