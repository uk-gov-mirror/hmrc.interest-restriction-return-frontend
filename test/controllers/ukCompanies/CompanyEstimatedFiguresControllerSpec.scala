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
import forms.ukCompanies.CompanyEstimatedFiguresFormProvider
import models.{CompanyEstimatedFigures, NormalMode}
import pages.ukCompanies._
import play.api.test.Helpers._
import views.html.ukCompanies.CompanyEstimatedFiguresView
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import models.{UserAnswers, CompanyDetailsModel}
import models.returnModels.fullReturn._

class CompanyEstimatedFiguresControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[CompanyEstimatedFiguresView]
  val formProvider = injector.instanceOf[CompanyEstimatedFiguresFormProvider]
  val form = formProvider()

  object Controller extends CompanyEstimatedFiguresController(
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

  "CompanyEstimatedFigures Controller" must {

    "return OK and the correct view for a GET" in {

      val company = UkCompanyModel(CompanyDetailsModel("Company 1", "1123456789"))
          .copy(taxEBITDA = Some(BigDecimal(123)), netTaxInterest = Some(BigDecimal(123)))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)
      val checkboxes = CompanyEstimatedFigures.options(form, 1, userAnswers)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, NormalMode, "Company 1", checkboxes, routes.CompanyEstimatedFiguresController.onSubmit(1, NormalMode))(fakeRequest, messages, frontendAppConfig).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val company = UkCompanyModel(CompanyDetailsModel("Company 1", "1123456789"))
          .copy(taxEBITDA = Some(BigDecimal(123)), netTaxInterest = Some(BigDecimal(123)))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value[0]", CompanyEstimatedFigures.values.head.toString))
      val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
          .copy(taxEBITDA = Some(BigDecimal(123)), netTaxInterest = Some(BigDecimal(123)))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
      mockGetAnswers(Some(userAnswers))
      mockSetAnswers

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))

      val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
          .copy(taxEBITDA = Some(BigDecimal(123)), netTaxInterest = Some(BigDecimal(123)))
      val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
      mockGetAnswers(Some(userAnswers))

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value[0]", CompanyEstimatedFigures.values.head.toString))

      mockGetAnswers(None)

      val result = Controller.onSubmit(1, NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}

