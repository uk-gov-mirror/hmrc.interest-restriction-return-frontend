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
import controllers.actions._
import controllers.errors
import forms.PayTaxInUkFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeGroupStructureNavigator
import pages.groupStructure.{ParentCompanyNamePage, PayTaxInUkPage}
import play.api.test.Helpers._
import views.html.groupStructure.PayTaxInUkView

class PayTaxInUkControllerSpec extends SpecBase with FeatureSwitching {

  val view = injector.instanceOf[PayTaxInUkView]
  val formProvider = new PayTaxInUkFormProvider
  val form = formProvider()

  lazy val name = "My Company Ltd."
  lazy val companyNameAnswer = emptyUserAnswers.set(ParentCompanyNamePage, name).success.value

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new PayTaxInUkController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeGroupStructureNavigator,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = new DataRequiredActionImpl,
    formProvider = new PayTaxInUkFormProvider,
    controllerComponents = messagesControllerComponents,
    view = view,
    errorHandler = errorHandler
  )

  "PayTaxInUk Controller" must {

    "For the onPageLoad() method" when {

      "a parentCompanyName is held in the user answers" should {

        "return OK and the correct view for a GET" in {

          val result = controller(FakeDataRetrievalActionGeneral(Some(companyNameAnswer))).onPageLoad(NormalMode)(fakeRequest)

          status(result) mustEqual OK
          contentAsString(result) mustEqual view(form, NormalMode, name)(fakeRequest, messages, frontendAppConfig).toString
        }

        "populate the view correctly on a GET when the question has previously been answered" in {

          val userAnswers = companyNameAnswer.set(PayTaxInUkPage, true).success.value

          val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad(NormalMode)(fakeRequest)

          status(result) mustEqual OK
        }
      }

      "a parentCompanyName is NOT held in the user answers" should {

        "return ISE and Error Page" in {

          val result = controller().onPageLoad(NormalMode)(fakeRequest)

          status(result) mustEqual INTERNAL_SERVER_ERROR
          contentAsString(result) mustEqual errorHandler.internalServerErrorTemplate(fakeRequest).toString
        }
      }

      "no existing data is found" should {

        "redirect to Session Expired page" in {

          val result = controller(FakeDataRetrievalActionNone).onPageLoad(NormalMode)(fakeRequest)

          status(result) mustEqual SEE_OTHER

          redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
        }
      }
    }

    "For the onSubmit() method" when {

      "a parentCompanyName is held in the user answers" should {

        "redirect to the next page when valid data is submitted" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

          val result = controller(FakeDataRetrievalActionGeneral(Some(companyNameAnswer))).onSubmit(NormalMode)(request)

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some("/foo")
        }

        "return a Bad Request and errors when invalid data is submitted" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

          val result = controller(FakeDataRetrievalActionGeneral(Some(companyNameAnswer))).onSubmit(NormalMode)(request)

          status(result) mustEqual BAD_REQUEST
        }
      }

      "a parentCompanyName is NOT held in the user answers and is a bad request with invalid data" should {

        "return ISE and Error Page" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

          val result = controller().onSubmit(NormalMode)(request)

          status(result) mustEqual INTERNAL_SERVER_ERROR
          contentAsString(result) mustEqual errorHandler.internalServerErrorTemplate(fakeRequest).toString
        }
      }

      "no existing data is found" should {

        "redirect to Session Expired page" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

          val result = controller(FakeDataRetrievalActionNone).onSubmit(NormalMode)(request)

          status(result) mustEqual SEE_OTHER

          redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
        }
      }
    }
  }
}
