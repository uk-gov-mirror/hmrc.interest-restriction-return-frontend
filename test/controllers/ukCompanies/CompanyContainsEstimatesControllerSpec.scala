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

import controllers.errors
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.ukCompanies.CompanyContainsEstimatesFormProvider
import models.NormalMode
import play.api.test.Helpers._
import views.html.ukCompanies.CompanyContainsEstimatesView
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import models.{UserAnswers, CompanyDetailsModel, CompanyEstimatedFigures}
import models.returnModels.fullReturn._
import pages.ukCompanies.UkCompaniesPage
import controllers.ukCompanies.routes.CompanyContainsEstimatesController
import scala.concurrent.Future

class CompanyContainsEstimatesControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[CompanyContainsEstimatesView]
  val formProvider = injector.instanceOf[CompanyContainsEstimatesFormProvider]
  val form = formProvider()

  object Controller extends CompanyContainsEstimatesController(
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

  "CompanyContainsEstimates Controller" must {

    "return OK and the correct view for a GET" in {

      val company = UkCompanyModel(CompanyDetailsModel("Company 1", "1123456789"))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, NormalMode, "Company 1", CompanyContainsEstimatesController.onSubmit(1, NormalMode))(fakeRequest, messages, frontendAppConfig).toString
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
      mockGetAnswers(Some(userAnswers))
      mockSetAnswers

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "clear the Company Estimated Figures page when 'false' is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "false"))

      val company = UkCompanyModel(
        companyDetails = CompanyDetailsModel("123", "1123456789"),
        estimatedFigures = Some(CompanyEstimatedFigures.values.toSet)
      )
      val userAnswers = emptyUserAnswers.set(UkCompaniesPage, company, Some(1)).get

      val expectedCompany = UkCompanyModel(
        companyDetails = CompanyDetailsModel("123", "1123456789"),
        containsEstimates = Some(false)
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

      val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
      mockGetAnswers(Some(userAnswers))

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustEqual BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
    }
  }
}
