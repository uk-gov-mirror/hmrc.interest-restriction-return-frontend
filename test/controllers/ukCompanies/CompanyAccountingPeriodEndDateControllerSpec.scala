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

import java.time.{LocalDate, ZoneOffset}
import controllers.errors
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.ukCompanies.CompanyAccountingPeriodEndDateFormProvider
import models.NormalMode
import pages.ukCompanies.CompanyAccountingPeriodEndDatePage
import pages.aboutReturn.AccountingPeriodPage
import play.api.test.Helpers._
import views.html.ukCompanies.CompanyAccountingPeriodEndDateView
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import pages.ukCompanies.UkCompaniesPage
import assets.constants.fullReturn.UkCompanyConstants.ukCompanyModelMax
import models.returnModels.AccountingPeriodModel

class CompanyAccountingPeriodEndDateControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {
  
  val periodOfAccount = AccountingPeriodModel(LocalDate.of(2017,1,1), LocalDate.of(2018,1,1))
  val userAnswers = (for {
    ua  <- emptyUserAnswers.set(AccountingPeriodPage, periodOfAccount)
    ua2 <- ua.set(UkCompaniesPage, ukCompanyModelMax, Some(1))
  } yield ua2).get
  val view = injector.instanceOf[CompanyAccountingPeriodEndDateView]
  val formProvider = injector.instanceOf[CompanyAccountingPeriodEndDateFormProvider]
  val form = formProvider(1, 1, userAnswers)

  val validAnswer = LocalDate.now(ZoneOffset.UTC)

  object Controller extends CompanyAccountingPeriodEndDateController(
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

  val companyIdx = 1
  val restrictionIdx = 1
  val postAction = routes.CompanyAccountingPeriodEndDateController.onSubmit(companyIdx, restrictionIdx, NormalMode)

  "CompanyAccountingPeriodEndDate Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(companyIdx, restrictionIdx, NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, "Company Name ltd", "first", postAction)(fakeRequest, messages, frontendAppConfig).toString

    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val updatedUserAnswers = userAnswers.set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), validAnswer).get

      mockGetAnswers(Some(updatedUserAnswers))

      val result = Controller.onPageLoad(companyIdx, restrictionIdx, NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(
        "value.day" -> validAnswer.getDayOfMonth.toString,
        "value.month" -> validAnswer.getMonthValue.toString,
        "value.year" -> validAnswer.getYear.toString
      )

      mockGetAnswers(Some(userAnswers))

      mockSetAnswers

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(companyIdx, restrictionIdx, NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(
        "value.day" -> validAnswer.getDayOfMonth.toString,
        "value.month" -> validAnswer.getMonthValue.toString,
        "value.year" -> validAnswer.getYear.toString
      )

      mockGetAnswers(None)

      val result = Controller.onSubmit(companyIdx, restrictionIdx, NormalMode)(request)

      status(result) mustBe SEE_OTHER

      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }

  "headingPrefix" must {
    "return first where the restriction index is 1" in {
      Controller.headingPrefix(1) mustBe "first"
    }
    "return second where the restriction index is 2" in {
      Controller.headingPrefix(2) mustBe "second"
    }
    "return third where the restriction index is 3" in {
      Controller.headingPrefix(3) mustBe "third"
    }
  }
}

