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

import assets.messages.SectionHeaderMessages
import assets.messages.checkTotals.ReviewTaxEBITDAMessages
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import models.NormalMode
import navigation.FakeNavigators.FakeCheckTotalsNavigator
import pages.checkTotals.ReviewTaxEBITDAPage
import play.api.test.Helpers._
import views.html.CheckYourAnswersView

class ReviewTaxEBITDAControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[CheckYourAnswersView]

  object Controller extends ReviewTaxEBITDAController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view,
    navigator = FakeCheckTotalsNavigator
  )

  "Review Tax EBITDA Controller" when {

    "calling the onPageLoad() method" must {

      "return a OK (200)" in {

        mockGetAnswers(Some(emptyUserAnswers))

        val result = Controller.onPageLoad(fakeRequest)

        status(result) mustEqual OK
        titleOf(contentAsString(result)) mustEqual title(ReviewTaxEBITDAMessages.title, Some(SectionHeaderMessages.checkTotals))
      }
    }

    "calling the onSubmit() method" when {

        "redirect to the next page in the navigator" in {

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onSubmit(fakeRequest)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeCheckTotalsNavigator.nextPage(ReviewTaxEBITDAPage, NormalMode, emptyUserAnswers).url)
        }
      }
    }
}
