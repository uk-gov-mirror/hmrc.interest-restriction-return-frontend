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

package controllers.elections

import assets.constants.InvestorGroupConstants.investorGroupsGroupRatioModel
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import controllers.errors
import forms.elections.InvestorGroupsDeletionConfirmationFormProvider
import navigation.FakeNavigators.FakeElectionsNavigator
import pages.elections.InvestorGroupsPage
import play.api.test.Helpers._
import views.html.elections.InvestorGroupsDeletionConfirmationView

class InvestorGroupsDeletionConfirmationControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[InvestorGroupsDeletionConfirmationView]
  val formProvider = injector.instanceOf[InvestorGroupsDeletionConfirmationFormProvider]
  val form = formProvider()

  object Controller extends InvestorGroupsDeletionConfirmationController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeElectionsNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "InvestorGroupsDeletionConfirmation Controller" when {

    "calling .onPageLoad(idx: Int)" when {

      "there is an investor group the user answers" should {

        "return OK and the correct view" in {

          mockGetAnswers(Some(emptyUserAnswers.set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).get))

          val result = Controller.onPageLoad(idx = 1)(fakeRequest)

          status(result) mustEqual OK
          contentAsString(result) mustEqual view(
            form, routes.InvestorGroupsDeletionConfirmationController.onSubmit(idx = 1), investorGroupsGroupRatioModel.investorName
          )(fakeRequest, messages, frontendAppConfig).toString
        }
      }

      "there are no investments in the user answers" should {

        "return ISE (500)" in {

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onPageLoad(idx = 1)(fakeRequest)

          status(result) mustEqual INTERNAL_SERVER_ERROR
        }
      }

      "redirect to Session Expired for a GET if no existing data is found" in {

        mockGetAnswers(None)

        val result = Controller.onPageLoad(idx = 1)(fakeRequest)

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
      }
    }

    "calling .onSubmit(idx: Int)" when {

      "user confirms deletion by choosing Yes" should {

        "delete the selected investment and redirect to the next page" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

          val userAnswers = emptyUserAnswers
            .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).get
            .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2)).get

          mockGetAnswers(Some(userAnswers))
          mockSetAnswers()

          val result = Controller.onSubmit(idx = 2)(request)

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(onwardRoute.url)
        }
      }

      "user declines deletion by choosing No" should {

        "redirect to the next page when valid data is submitted" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value", "false"))

          val userAnswers = emptyUserAnswers
            .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).get
            .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2)).get

          mockGetAnswers(Some(userAnswers))

          val result = Controller.onSubmit(idx = 2)(request)

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(onwardRoute.url)
        }
      }

      "return a Bad Request and errors when invalid data is submitted" in {

        mockGetAnswers(Some(emptyUserAnswers.set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).get))

        val request = fakeRequest.withFormUrlEncodedBody(("value", ""))

        val result = Controller.onSubmit(idx = 1)(request)

        status(result) mustEqual BAD_REQUEST
      }

      "redirect to Session Expired for a POST if no existing data is found" in {

        val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

        mockGetAnswers(None)

        val result = Controller.onSubmit(idx = 1)(request)

        status(result) mustEqual SEE_OTHER

        redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
      }
    }
  }
}
