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
import models.ContinueSavedReturn.{ContinueReturn, NewReturn}
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class ContinueSavedReturnControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "GET /continue-saved-return" when {

    "user is authorised" when {

      "return OK (200)" in {

        AuthStub.authorised()

        val res = getRequest("/continue-saved-return")()

        whenReady(res) { result =>
          result should have(
            httpStatus(OK),
            titleOf(PageTitles.continueSavedReturn)
          )
        }
      }
    }

    "user not authorised" should {

      "return SEE_OTHER (303)" in {

        AuthStub.unauthorised()

        val res = getRequest("/continue-saved-return")()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
          )
        }
      }
    }
  }

  "POST /continue-saved-return" when {

    "user is authorised" when {

      "enters NewReturn" should {

        "redirect to under Limited Liability Partnership page" in {

          AuthStub.authorised()

          val res = postRequest("/continue-saved-return", Json.obj("value" -> NewReturn.toString))()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.routes.SavedReturnController.deleteAndStartAgain().url)
            )
          }
        }
      }

      "enters ContinueReturn" should {

        "redirect to under construction page" in {

          AuthStub.authorised()

          val res = postRequest("/continue-saved-return", Json.obj("value" -> ContinueReturn.toString))()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.routes.SavedReturnController.nextUnansweredPage().url)
            )
          }
        }
      }

      "enters an invalid answer" when {

        "return a BAD_REQUEST (400)" in {

          AuthStub.authorised()

          val res = postRequest("/continue-saved-return", Json.obj("value" -> ""))()

          whenReady(res) { result =>
            result should have(
              httpStatus(BAD_REQUEST)
            )
          }
        }
      }
    }

    "user not authorised" should {

      "return SEE_OTHER (303)" in {

        AuthStub.unauthorised()

        val res = postRequest("/continue-saved-return", Json.obj("value" -> companyName))()

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
