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

package controllers.elections

import assets.{BaseITConstants, PageTitles}
import models.NormalMode
import pages.elections.GroupRatioElectionPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class EnterANGIEControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /elections/enter-angie" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          val res = getRequest("/elections/enter-angie")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.enterANGIE)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/enter-angie")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/enter-angie" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "Group Ratio is elected" should {

            "redirect to QNGIE page" in {

              AuthStub.authorised()

              setAnswers(GroupRatioElectionPage, true)

              val res = postRequest("/elections/enter-angie", Json.obj("value" -> 1))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(routes.EnterQNGIEController.onPageLoad(NormalMode).url)
                )
              }
            }
          }

          "Group Ratio is NOT elected" should {

            "redirect to Elected Interest Allowance Alternative Calculation Before page" in {

              AuthStub.authorised()

              setAnswers(GroupRatioElectionPage, false)

              val res = postRequest("/elections/enter-angie", Json.obj("value" -> 1))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode).url)
                )
              }
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/elections/enter-angie", Json.obj("value" -> 1))()

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

    "GET /elections/enter-angie" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/elections/enter-angie/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.enterANGIE)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/enter-angie/change")()

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
