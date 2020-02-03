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
import config.SessionKeys
import config.featureSwitch.FeatureSwitching
import controllers.actions.{DataRequiredActionImpl, FakeDataRetrievalActionEmptyAnswers, FakeIdentifierAction}
import play.api.test.Helpers._
import views.html.ConfirmationView

class ConfirmationControllerSpec extends SpecBase with FeatureSwitching {

  val view = injector.instanceOf[ConfirmationView]

  val reference = "abc123"

  def controller = new ConfirmationController(
    messagesApi = messagesApi,
    identify = FakeIdentifierAction,
    getData = FakeDataRetrievalActionEmptyAnswers,
    requireData = new DataRequiredActionImpl,
    controllerComponents = messagesControllerComponents,
    view = view,
    errorHandler = errorHandler
  )

  lazy val requestWithRef = fakeRequest.withSession(SessionKeys.acknowledgementReference -> reference)

  "Confirmation Controller" must {

    "when the acknowledgement reference is held in session" should {

      "When using the Twirl Template" must {

        "return OK and the correct view for a GET" in {

          val result = controller.onPageLoad(requestWithRef)

          status(result) mustBe OK
          contentAsString(result) mustEqual view(reference)(requestWithRef, frontendAppConfig, messages).toString
        }
      }
    }

    "when the acknowledgement reference is NOT held in session" should {

      "return the ISE page" in {

        val result = controller.onPageLoad(fakeRequest)

        status(result) mustBe INTERNAL_SERVER_ERROR
        contentAsString(result) mustEqual errorHandler.internalServerErrorTemplate(fakeRequest).toString
      }
    }
  }
}
