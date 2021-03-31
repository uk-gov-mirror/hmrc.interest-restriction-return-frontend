/*
 * Copyright 2021 HM Revenue & Customs
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

import assets.constants.fullReturn.UkCompanyConstants.{companyNameModel, ukCompanyModelMax}
import controllers.errors
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.ukCompanies.AddNetTaxInterestFormProvider
import models.{CompanyDetailsModel, NormalMode, UserAnswers}
import models.returnModels.fullReturn.UkCompanyModel
import pages.ukCompanies.UkCompaniesPage
import play.api.test.Helpers._
import views.html.ukCompanies.AddNetTaxInterestView
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import scala.concurrent.Future

class AddNetTaxInterestControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[AddNetTaxInterestView]
  val formProvider = injector.instanceOf[AddNetTaxInterestFormProvider]
  val form = formProvider()
  val idx = 1

  object Controller extends AddNetTaxInterestController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeUkCompaniesNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "AddNetTaxInterest Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(idx)).get))

      val result = Controller.onPageLoad(idx, NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(
        form = form,
        companyName = companyNameModel.name,
        postAction = routes.AddNetTaxInterestController.onSubmit(idx, NormalMode)
      )(fakeRequest, messages, frontendAppConfig).toString
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(idx)).get))
      mockSetAnswers

      val result = Controller.onSubmit(idx, NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "clear the netTaxInterest page when 'false' is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val company = UkCompanyModel(
        companyDetails = CompanyDetailsModel("123", "1123456789"),
        addNetTaxInterest = Some(true)
      )
      val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).get

      val expectedCompany = UkCompanyModel(
        companyDetails = CompanyDetailsModel("123", "1123456789"),
        addNetTaxInterest = Some(false)
      )

      mockGetAnswers(Some(userAnswers))

      (mockSessionRepository.set(_: UserAnswers))
        .expects(emptyUserAnswers
          .set(UkCompaniesPage, expectedCompany, Some(1)).success.value)
        .returns(Future.successful(true))

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

      mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(idx)).get))

      val result = Controller.onSubmit(idx, NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(idx, NormalMode)(fakeRequest)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(idx, NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
