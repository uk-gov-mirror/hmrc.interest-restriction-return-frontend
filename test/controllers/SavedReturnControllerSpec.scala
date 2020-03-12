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

package controllers

import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions.{FakeIdentifierAction, MockDataRetrievalAction}
import mocks.MockSessionRepository
import models.NormalMode
import navigation.FakeNavigators.{FakeAboutReportingCompanyNavigator, FakeAboutReturnNavigator, FakeElectionsNavigator, FakeGroupStructureNavigator, FakeStartReturnNavigator}
import play.api.test.Helpers._
import views.html.SavedReturnView

class SavedReturnControllerSpec extends SpecBase with FeatureSwitching with MockSessionRepository with MockDataRetrievalAction {

  val view = injector.instanceOf[SavedReturnView]

  object Controller extends SavedReturnController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view,
    sessionRepository = mockSessionRepository,
    startReturnNavigator = FakeStartReturnNavigator,
    aboutReportingCompanyNavigator = FakeAboutReportingCompanyNavigator,
    aboutReturnNavigator = FakeAboutReturnNavigator,
    electionsNavigator = FakeElectionsNavigator,
    groupStructureNavigator = FakeGroupStructureNavigator
  )

  "SavedReturn Controller" must {

    "for the onPageLoad() method" must {

      "return OK and the correct view for a GET" in {

        mockGetAnswers(Some(emptyUserAnswers))

        val result = Controller.onPageLoad(fakeRequest)

        status(result) mustBe OK
      }
    }
  }

  "for the deleteAndStartAgain() method" must {

    "redirect to the IndexRoute and clear the user answers held" in {

      mockGetAnswers(Some(emptyUserAnswers))
      mockDeleteAnswers(result = true)

      val result = Controller.deleteAndStartAgain(fakeRequest)

      status(result) mustBe SEE_OTHER
      redirectLocation(result) mustBe Some(controllers.routes.IndexController.onPageLoad().url)
    }
  }
}
