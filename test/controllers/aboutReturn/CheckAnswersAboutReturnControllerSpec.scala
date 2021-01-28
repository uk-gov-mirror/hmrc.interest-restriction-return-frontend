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

package controllers.aboutReturn

import assets.messages.{CheckAnswersAboutReturnMessages, SectionHeaderMessages}
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import models.NormalMode
import navigation.FakeNavigators.FakeAboutReturnNavigator
import pages.aboutReturn.CheckAnswersAboutReturnPage
import play.api.test.Helpers._
import views.html.CheckYourAnswersView

class CheckAnswersAboutReturnControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[CheckYourAnswersView]

  object Controller extends CheckAnswersAboutReturnController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeAboutReturnNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "Check Your Answers Controller" when {

    "calling the onPageLoad() method" must {

      "return a OK (200) when given empty answers" in {

        mockGetAnswers(Some(emptyUserAnswers))

        val result = Controller.onPageLoad()(fakeRequest)

        status(result) mustEqual OK
        titleOf(contentAsString(result)) mustEqual title(CheckAnswersAboutReturnMessages.title, Some(SectionHeaderMessages.aboutReturn))
      }

      "calling the onSubmit() method" must {

        "redirect to the next page in the navigator" in {

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onSubmit()(fakeRequest)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeAboutReturnNavigator.nextPage(
            page = CheckAnswersAboutReturnPage,
            mode = NormalMode,
            userAnswers = emptyUserAnswers,
            id = Some(1)
          ).url)
        }
      }
    }
  }
}
