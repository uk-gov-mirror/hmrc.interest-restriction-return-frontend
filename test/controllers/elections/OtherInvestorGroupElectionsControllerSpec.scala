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

import assets.constants.InvestorGroupConstants.investorGroupsGroupRatioModel
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import controllers.errors
import forms.elections.OtherInvestorGroupElectionsFormProvider
import models.InvestorRatioMethod.GroupRatioMethod
import models.{NormalMode, OtherInvestorGroupElections}
import navigation.FakeNavigators.FakeElectionsNavigator
import pages.elections.InvestorGroupsPage
import play.api.test.Helpers._
import views.html.elections.OtherInvestorGroupElectionsView

class OtherInvestorGroupElectionsControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[OtherInvestorGroupElectionsView]
  val formProvider = injector.instanceOf[OtherInvestorGroupElectionsFormProvider]
  val form = formProvider()

  object Controller extends OtherInvestorGroupElectionsController(
    messagesApi = messagesApi,
    sessionRepository = sessionRepository,
    navigator = FakeElectionsNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    updateSectionService = updateSectionService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    formProvider = formProvider,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "OtherInvestorGroupElections Controller" when {

    "calling the onPageLoad(idx: Int, mode: Mode) method" when {

      "answer exists for the model at the requested idx" must {

        "return OK and the correct view for a GET" in {

          val userAnswers = emptyUserAnswers
            .set(InvestorGroupsPage, investorGroupsGroupRatioModel.copy(otherInvestorGroupElections = None), Some(1)).success.value

          mockGetAnswers(Some(userAnswers))

          val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

          status(result) mustEqual OK
          contentAsString(result) mustEqual view(
            form, routes.OtherInvestorGroupElectionsController.onSubmit(1, NormalMode), GroupRatioMethod
          )(fakeRequest, messages, frontendAppConfig).toString
        }
      }

      "answer DOES NOT exist for the model at the requested idx" must {

        "return ISE (500)" in {

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

          status(result) mustEqual INTERNAL_SERVER_ERROR
          contentAsString(result) mustEqual errorHandler.internalServerErrorTemplate(fakeRequest).toString
        }
      }

      "redirect to Session Expired for a GET if no existing data is found" in {

        mockGetAnswers(None)

        val result = Controller.onPageLoad(1, NormalMode)(fakeRequest)

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
      }
    }

    "calling the onSubmit(idx: Int, mode: Mode) method" when {

      "answer exists for the model at the requested idx" when {

        "valid data is submitted" must {

          "redirect to the next page" in {

            val userAnswers = emptyUserAnswers.set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).success.value

            mockGetAnswers(Some(userAnswers))

            val request = fakeRequest.withFormUrlEncodedBody(("value[0]", OtherInvestorGroupElections.values(GroupRatioMethod).head.toString))

            val result = Controller.onSubmit(1, NormalMode)(request)

            status(result) mustEqual SEE_OTHER
            redirectLocation(result) mustBe Some(onwardRoute.url)
          }
        }

        "invalid data is submitted" must {

          "return a Bad Request" in {

            val userAnswers = emptyUserAnswers.set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).success.value

            mockGetAnswers(Some(userAnswers))

            val request = fakeRequest.withFormUrlEncodedBody(("value[0]", "Err"))

            val result = Controller.onSubmit(1, NormalMode)(request)

            status(result) mustEqual BAD_REQUEST
          }
        }
      }

      "answer DOES NOT exist for the model at the requested idx" must {

        "return ISE (500)" in {

          val request = fakeRequest.withFormUrlEncodedBody(("value[0]", OtherInvestorGroupElections.values(GroupRatioMethod).head.toString))

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onSubmit(1, NormalMode)(request)

          status(result) mustEqual INTERNAL_SERVER_ERROR
          contentAsString(result) mustEqual errorHandler.internalServerErrorTemplate(fakeRequest).toString
        }
      }

      "redirect to Session Expired for a POST if no existing data is found" in {

        val request = fakeRequest.withFormUrlEncodedBody(("value[0]", OtherInvestorGroupElections.values(GroupRatioMethod).head.toString))

        mockGetAnswers(None)

        val result = Controller.onSubmit(1, NormalMode)(request)

        status(result) mustEqual SEE_OTHER
        redirectLocation(result).value mustEqual errors.routes.SessionExpiredController.onPageLoad().url
      }
    }
  }
}

