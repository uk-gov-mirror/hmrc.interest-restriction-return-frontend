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

import assets.constants.fullReturn.UkCompanyConstants.ukCompanyModelMax
import controllers.errors
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.ukCompanies.RestrictionAmountForAccountingPeriodFormProvider
import models.NormalMode
import pages.ukCompanies.{CompanyAccountingPeriodEndDatePage, UkCompaniesPage}
import play.api.test.Helpers._
import views.html.ukCompanies.RestrictionAmountForAccountingPeriodView
import navigation.FakeNavigators.FakeUkCompaniesNavigator

import java.time.LocalDate

class RestrictionAmountForAccountingPeriodControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[RestrictionAmountForAccountingPeriodView]
  val formProvider = injector.instanceOf[RestrictionAmountForAccountingPeriodFormProvider]
  val form = formProvider()

  val companyIdx = 1
  val restrictionIdx = 1
  val postAction = routes.RestrictionAmountForAccountingPeriodController.onSubmit(companyIdx, restrictionIdx, NormalMode)

  val endDate = LocalDate.of(2021, 1, 1)

  val validAnswer = 0

  object Controller extends RestrictionAmountForAccountingPeriodController(
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

  "RestrictionAmountForAccountingPeriod Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(
        emptyUserAnswers
          .set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get
          .set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), endDate).get
      ))

      val result = Controller.onPageLoad(companyIdx, restrictionIdx, NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, ukCompanyModelMax.companyDetails.companyName, endDate, postAction)(fakeRequest, messages, frontendAppConfig).toString
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "01"))

      mockGetAnswers(Some(
        emptyUserAnswers
          .set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get
          .set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), endDate).get
      ))
      mockSetAnswers

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustBe SEE_OTHER

      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "a"))

      mockGetAnswers(Some(
        emptyUserAnswers
          .set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get
          .set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), endDate).get
      ))

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(companyIdx, restrictionIdx, NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result).value mustBe errors.routes.SessionExpiredController.onPageLoad().url
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "2"))

      mockGetAnswers(None)

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustEqual SEE_OTHER

      redirectLocation(result).value mustBe errors.routes.SessionExpiredController.onPageLoad().url
    }
    
  }
}
