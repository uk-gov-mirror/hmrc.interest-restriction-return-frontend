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

package controllers.aboutReturn

import assets.{BaseITConstants, PageTitles}
import models.NormalMode
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class TellUsWhatHasChangedControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /aboutReturn/tell-us-what-has-changed" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          val res = getRequest("/aboutReturn/tell-us-what-has-changed")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.tellUsWhatHasChanged)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/aboutReturn/tell-us-what-has-changed")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /aboutReturn/tell-us-what-has-changed" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to TellUsWhatHasChanged page" in {

            AuthStub.authorised()

            val res = postRequest("/aboutReturn/tell-us-what-has-changed", Json.obj("value" -> 1))()
//TODO: Implement
//            whenReady(res) { result =>
//              result should have(
//                httpStatus(SEE_OTHER),
//                redirectLocation(controllers.aboutReturn.routes.TellUsWhatHasChangedController.onPageLoad(NormalMode).url)
//              )
//            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/aboutReturn/tell-us-what-has-changed", Json.obj("value" -> 1))()

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

  "in Change mode" when {

    "GET /aboutReturn/tell-us-what-has-changed" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/aboutReturn/tell-us-what-has-changed/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.tellUsWhatHasChanged)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/aboutReturn/tell-us-what-has-changed/change")()

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
}
