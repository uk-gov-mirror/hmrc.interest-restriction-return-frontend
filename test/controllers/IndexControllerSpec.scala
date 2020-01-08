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
import controllers.actions.{DataRetrievalAction, FakeDataRetrievalActionEmptyAnswers, FakeDataRetrievalActionNone, FakeIdentifierAction}
import mocks.MockSessionRepository
import navigation.FakeNavigator
import play.api.mvc.Call
import play.api.test.Helpers._

class IndexControllerSpec extends SpecBase with MockSessionRepository {

  def controller(dataRetrievalAction: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new IndexController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    sessionRepository = mockSessionRepository,
    navigator = FakeNavigator,
    controllerComponents = messagesControllerComponents
  )

  "Index Controller" must {

    "return OK and the correct view for a GET with UserAnswers already supplied" in {
      mockSet(true)

      val result = controller().onPageLoad()(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }

    "return OK and the correct view for a GET with UserAnswers NOT already supplied" in {
      mockSet(true)

      val result = controller(FakeDataRetrievalActionNone).onPageLoad()(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }
  }
}
