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

package controllers.reviewAndComplete

import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import models.returnModels.ReviewAndCompleteModel
import navigation.FakeNavigators.FakeReviewAndCompleteNavigator
import pages.reviewAndComplete.ReviewAndCompletePage
import play.api.test.Helpers._
import views.html.reviewAndComplete.ReviewAndCompleteView

class ReviewAndCompleteControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[ReviewAndCompleteView]

  object Controller extends ReviewAndCompleteController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view,
    navigator = FakeReviewAndCompleteNavigator
  )

  "ReviewAndComplete Controller" must {

    "return OK and the correct view for a GET" in {

      mockGetAnswers(Some(emptyUserAnswers
        .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
      ))

      val result = Controller.onPageLoad()(fakeRequest)

      status(result) mustEqual OK
    }

    "redirect to the next page when submitted" in {

      mockGetAnswers(Some(emptyUserAnswers))

      val result = Controller.onSubmit()(fakeDataRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(onwardRoute.url)
    }
  }
}
