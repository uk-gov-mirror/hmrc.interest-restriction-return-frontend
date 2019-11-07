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
import controllers.actions.{DataRetrievalAction, FakeDataRetrievalActionEmptyAnswers, FakeIdentifierAction}
import mocks.MockSessionRepository
import navigation.FakeNavigator
import play.api.test.Helpers._
import uk.gov.hmrc.play.binders.ContinueUrl

class SignOutControllerSpec extends SpecBase with MockSessionRepository {

  object TestSignOutController extends SignOutController(
    controllerComponents = messagesControllerComponents,
    appConfig = frontendAppConfig
  )

  "SignOut Controller" must {

    "redirect to /gg/sign-out with continue to the feedback survey" in {

      val result = TestSignOutController.signOut(fakeRequest)

      status(result) mustEqual SEE_OTHER
      redirectLocation(result) mustBe Some(frontendAppConfig.signOutUrl + s"?continue=${ContinueUrl(frontendAppConfig.exitSurveyUrl).encodedUrl}")
    }
  }
}
