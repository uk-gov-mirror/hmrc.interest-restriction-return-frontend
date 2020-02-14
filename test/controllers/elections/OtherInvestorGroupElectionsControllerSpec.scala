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

package controllers.elections

import controllers.errors
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.elections.OtherInvestorGroupElectionsFormProvider
import models.InvestorRatioMethod.FixedRatioMethod
import models.OtherInvestorGroupElections.GroupRatioBlended
import models.{NormalMode, OtherInvestorGroupElections}
import pages.elections.{InvestorRatioMethodPage, OtherInvestorGroupElectionsPage}
import play.api.test.Helpers._
import views.html.elections.OtherInvestorGroupElectionsView
import navigation.FakeNavigators.FakeElectionsNavigator

class OtherInvestorGroupElectionsControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[OtherInvestorGroupElectionsView]
  val formProvider = injector.instanceOf[OtherInvestorGroupElectionsFormProvider]
  val form = formProvider()

  object Controller extends OtherInvestorGroupElectionsController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeElectionsNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "OtherInvestorGroupElections Controller" when {

    "an answer exists for the Investor Ratio Method page" should {

      "return OK and the correct view for a GET" in {

        val userAnswers = emptyUserAnswers.set(InvestorRatioMethodPage, FixedRatioMethod).success.value

        mockGetAnswers(Some(userAnswers))

        val result = Controller.onPageLoad(NormalMode)(fakeRequest)

        status(result) mustEqual OK
        contentAsString(result) mustEqual view(form, NormalMode, FixedRatioMethod)(fakeRequest, messages, frontendAppConfig).toString
      }
    }

    "an answer DOES NOT exist for the Investor Ratio Method page" should {

      "return ISE" in {

        mockGetAnswers(Some(emptyUserAnswers))

        val result = Controller.onPageLoad(NormalMode)(fakeRequest)

        status(result) mustEqual INTERNAL_SERVER_ERROR
        contentAsString(result) mustEqual errorHandler.internalServerErrorTemplate(fakeRequest).toString
      }
    }

    "populate the view correctly on a GET when the question has previously been answered" in {

      val userAnswers = emptyUserAnswers
        .set(InvestorRatioMethodPage, FixedRatioMethod).success.value
        .set[Set[OtherInvestorGroupElections]](OtherInvestorGroupElectionsPage, OtherInvestorGroupElections.allValues.toSet).success.value

      mockGetAnswers(Some(userAnswers))

      val result = Controller.onPageLoad(NormalMode)(fakeRequest)

      status(result) mustBe OK
    }

    "redirect to the next page when valid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value[0]", OtherInvestorGroupElections.values(FixedRatioMethod).head.toString))

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit(NormalMode)(request)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }

    "return a Bad Request and errors when invalid data is submitted" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value[0]", "invalid value"))

      val userAnswers = emptyUserAnswers.set(InvestorRatioMethodPage, FixedRatioMethod).success.value

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

    "redirect to Session Expired for a POST if no existing data is found" in {

      val request = fakeRequest.withFormUrlEncodedBody(("value[0]", OtherInvestorGroupElections.values(FixedRatioMethod).head.toString))

      mockGetAnswers(None)

      val result = Controller.onSubmit(NormalMode)(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(errors.routes.SessionExpiredController.onPageLoad().url)
    }
  }
}

