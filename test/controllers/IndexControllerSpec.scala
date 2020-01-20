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
import controllers.actions._
import mocks.MockSessionRepository
import navigation.FakeNavigators.FakeStartReturnNavigator
import pages.startReturn.ReportingCompanyAppointedPage
import play.api.test.Helpers._

class IndexControllerSpec extends SpecBase with MockSessionRepository {

  def controller(dataRetrievalAction: DataRetrievalAction = FakeDataRetrievalActionEmptyAnswers) = new IndexController(
    identify = FakeIdentifierAction,
    getData = dataRetrievalAction,
    sessionRepository = mockSessionRepository,
    navigator = FakeStartReturnNavigator,
    controllerComponents = messagesControllerComponents
  )

  "Index Controller" must {

    "return OK and the correct view for a GET with UserAnswers already supplied" in {

      val userAnswers = emptyUserAnswers.set(ReportingCompanyAppointedPage, true).success.value

      val result = controller(FakeDataRetrievalActionGeneral(Some(userAnswers))).onPageLoad()(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some("/interest-restriction-return/continue-saved-return")
    }

    "return OK and the correct view for a GET with UserAnswers NOT already supplied" in {
      mockSet(true)

      val result = controller(FakeDataRetrievalActionNone).onPageLoad()(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")
    }

    "return OK and get the correct view for a GET with empty userAnswers supplied due to 'start a new return' being selected" in {
      mockSet(true)

      val result = controller(FakeDataRetrievalActionGeneral(Some(emptyUserAnswers))).onPageLoad()(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some("/foo")

    }
  }
}
