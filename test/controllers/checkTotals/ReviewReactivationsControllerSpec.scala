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

package controllers.checkTotals

import assets.constants.fullReturn.GroupLevelAmountConstants.interestReactivationCap
import assets.messages.SectionHeaderMessages
import assets.messages.checkTotals.ReviewReactivationsMessages
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import models.NormalMode
import navigation.FakeNavigators.FakeCheckTotalsNavigator
import pages.groupLevelInformation.InterestReactivationsCapPage
import pages.checkTotals.ReviewReactivationsPage
import play.api.test.Helpers._
import views.html.checkTotals.ReviewReactivationsView

class ReviewReactivationsControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[ReviewReactivationsView]

  object Controller extends ReviewReactivationsController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view,
    navigator = FakeCheckTotalsNavigator
  )

  "Review Reactivations Controller" when {

    "calling the onPageLoad() method" when {

      "the Interest Reactivation Cap is successfully retrieved" must {

        "return a OK (200)" in {

          val userAnswers = emptyUserAnswers.set(InterestReactivationsCapPage, interestReactivationCap).get

          mockGetAnswers(Some(userAnswers))

          val result = Controller.onPageLoad(fakeRequest)

          status(result) mustEqual OK
          titleOf(contentAsString(result)) mustEqual title(ReviewReactivationsMessages.title, Some(SectionHeaderMessages.checkTotals))
        }
      }

      "the Interest Reactivation Cap is NOT retrieved" must {

        "return a ISE (500)" in {

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onPageLoad(fakeRequest)

          status(result) mustEqual INTERNAL_SERVER_ERROR
          contentAsString(result) mustEqual errorHandler.internalServerErrorTemplate(fakeRequest).toString
        }
      }
    }

    "calling the onSubmit() method" when {

      "redirect to the next page in the navigator" in {

        mockGetAnswers(Some(emptyUserAnswers))

        val result = Controller.onSubmit(fakeRequest)

        status(result) mustBe SEE_OTHER
        redirectLocation(result) mustBe Some(FakeCheckTotalsNavigator.nextPage(ReviewReactivationsPage, NormalMode, emptyUserAnswers).url)
      }
    }
  }
}
