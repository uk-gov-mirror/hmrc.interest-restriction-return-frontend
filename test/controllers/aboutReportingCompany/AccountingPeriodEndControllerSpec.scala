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

import java.time.{Instant, LocalDate, ZoneOffset}

import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import controllers.errors
import forms.aboutReportingCompany.AccountingPeriodEndFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeAboutReportingCompanyNavigator
import pages.aboutReportingCompany.{AccountingPeriodEndPage, AccountingPeriodStartPage}
import play.api.test.Helpers._
import views.html.aboutReportingCompany.AccountingPeriodEndView

class AccountingPeriodEndControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[AccountingPeriodEndView]
  val formProvider = injector.instanceOf[AccountingPeriodEndFormProvider]

  val startDate = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate
  val endDate = LocalDate.now(ZoneOffset.UTC).plusDays(1)
  val form = formProvider(endDate)
  val userAnswers = emptyUserAnswers.set[LocalDate](AccountingPeriodStartPage, startDate).success.value

  object Controller extends AccountingPeriodEndController(
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

  "AccountingPeriodEnd Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, NormalMode)(fakeRequest, messages, frontendAppConfig).toString

    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswersEdit = userAnswers.set(AccountingPeriodEndPage, endDate).success.value

      mockGetAnswers(Some(userAnswersEdit))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(
        "value.day" -> endDate.getDayOfMonth.toString,
        "value.month" -> endDate.getMonthValue.toString,
        "value.year" -> endDate.getYear.toString
      )

      mockGetAnswers(Some(userAnswers))
      mockSetAnswers

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Under Construction for a GET if no start date is found" in {

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.UnderConstructionController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(
        "value.day" -> endDate.getDayOfMonth.toString,
        "value.month" -> endDate.getMonthValue.toString,
        "value.year" -> endDate.getYear.toString
      )

      mockGetAnswers(None)

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER

      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}

