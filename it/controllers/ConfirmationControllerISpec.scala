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

import assets.{BaseITConstants, PageTitles}
import config.SessionKeys
import play.api.http.Status._
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class ConfirmationControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val ackRef = "abc123"

  "GET /confirmation" when {

    "user is authorised" when {

      "there is an reference number stored in session" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/confirmation")(SessionKeys.acknowledgementReference -> ackRef)

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.confirmation)
            )
          }
        }
      }

      "there is NO reference number stored in session" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/confirmation")()

          whenReady(res) { result =>
            result should have(
              httpStatus(INTERNAL_SERVER_ERROR)
            )
          }
        }
      }

    }

    "user not authorised" should {

      "return SEE_OTHER (303)" in {

        AuthStub.unauthorised()

        val res = getRequest("/confirmation")()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
          )
        }
      }
    }
  }
}
