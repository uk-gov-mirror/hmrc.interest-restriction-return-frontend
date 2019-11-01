/*
 * Copyright 2019 HM Revenue & Customs
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
import controllers.actions._
import play.api.test.Helpers._
import views.html.CheckYourAnswersView

class CheckYourAnswersControllerSpec extends SpecBase {

  val view = injector.instanceOf[CheckYourAnswersView]

  def controller(dataRetrieval: DataRetrievalAction = FakeDataRetrievalActionNone) = new CheckYourAnswersController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = dataRetrieval,
    requireData = new DataRequiredActionImpl,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "Check Your Answers Controller" must {

    "return a OK (200) when given empty answers" in {

      val result = controller(FakeDataRetrievalActionEmptyAnswers).onPageLoad()(fakeRequest)

      status(result) mustEqual OK
    }

    "return a SEE_OTHER (303) when given None" in {

        val result = controller().onPageLoad()(fakeRequest)

        status(result) mustEqual SEE_OTHER
    }
  }
}
