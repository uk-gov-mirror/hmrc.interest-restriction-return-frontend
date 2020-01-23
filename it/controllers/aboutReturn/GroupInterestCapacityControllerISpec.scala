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

import assets.BaseITConstants
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class GroupInterestCapacityControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /group-interest-capacity" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/group-interest-capacity")

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf("What is the group interest capacity for the period?")
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/group-interest-capacity")

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /group-interest-capacity" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to CheckYourAnswers page" in {

            AuthStub.authorised()

            val res = postRequest("/group-interest-capacity", Json.obj("value" -> 1))

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.routes.UnderConstructionController.onPageLoad().url)
              )
            }
          }

          "user not authorised" should {

            "return SEE_OTHER (303)" in {

              AuthStub.unauthorised()

              val res = postRequest("/group-interest-capacity", Json.obj("value" -> 1))

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
  }

  "in Change mode" when {

    "GET /group-interest-capacity" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/group-interest-capacity/change")

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf("What is the group interest capacity for the period?")
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/group-interest-capacity/change")

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    //TODO: Add Check Your Answers tests
  }
}
