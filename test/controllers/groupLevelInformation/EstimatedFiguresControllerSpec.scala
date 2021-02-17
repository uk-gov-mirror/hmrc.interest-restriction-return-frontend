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

package controllers.groupLevelInformation

import controllers.errors
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.groupLevelInformation.EstimatedFiguresFormProvider
import models.{EstimatedFigures, NormalMode, UserAnswers}
import pages.groupLevelInformation._
import play.api.test.Helpers._
import views.html.groupLevelInformation.EstimatedFiguresView
import navigation.FakeNavigators.FakeGroupLevelInformationNavigator

class EstimatedFiguresControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[EstimatedFiguresView]
  val formProvider = injector.instanceOf[EstimatedFiguresFormProvider]
  val form = formProvider()

  object Controller extends EstimatedFiguresController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeGroupLevelInformationNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "EstimatedFigures Controller" must {

    "return OK and the correct view for a GET" in {

      val userAnswers = (for {
        a                 <- UserAnswers("id").set(EnterANGIEPage, BigDecimal(123))
        b                 <- a.set(EnterQNGIEPage, BigDecimal(123))
        c                 <- b.set(EnterQNGIEPage, BigDecimal(123))
        d                 <- c.set(GroupEBITDAPage, BigDecimal(123))
        e                 <- d.set(InterestReactivationsCapPage, BigDecimal(123))
        f                 <- e.set(DisallowedAmountPage, BigDecimal(123)) 
        g                 <- f.set(InterestAllowanceBroughtForwardPage, BigDecimal(123))
        h                 <- g.set(GroupInterestAllowancePage, BigDecimal(123))
        finalUserAnswers  <- h.set(GroupInterestCapacityPage, BigDecimal(123))
      } yield finalUserAnswers).get

      val checkboxes = EstimatedFigures.optionsFilteredByUserAnswers(form, userAnswers)

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustEqual OK
      contentAsString(result) mustEqual view(form, NormalMode, checkboxes)(fakeRequest, messages, frontendAppConfig).toString
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers.set(EstimatedFiguresPage, EstimatedFigures.values.toSet).success.value

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value[0]", EstimatedFigures.values.head.toString))

      mockGetAnswers(Some(emptyUserAnswers))
      mockSetAnswers

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value", "invalid value"))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe BAD_REQUEST
    }

    "redirect to Session Expired for a GET if no existing data is found" in {

      mockGetAnswers(None)

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value[0]", EstimatedFigures.values.head.toString))

      mockGetAnswers(None)

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}

