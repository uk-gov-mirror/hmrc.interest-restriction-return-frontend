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

package controllers.groupStructure

import assets.{BaseITConstants, PageTitles}
import controllers.groupStructure.{routes => groupStructureRoutes}
import models.NormalMode
import pages.groupStructure.ParentCompanyNamePage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class LocalRegistrationNumberControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /group-structure/local-registration-number" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(ParentCompanyNamePage, companyName)

          val res = getRequest("/group-structure/local-registration-number")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.localRegistrationNumber(companyName))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/group-structure/local-registration-number")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /group-structure/local-registration-number" when {

      "user is authorised" when {

        "a value is entered" should {

          "redirect to Check Your Answers page" in {

            AuthStub.authorised()

            val res = postRequest("/group-structure/local-registration-number", Json.obj("value" -> "CRN"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CheckAnswersGroupStructureController.onPageLoad().url)
              )
            }
          }
        }

        "a value is NOT entered" should {

          "redirect to Check Your Answers page" in {
            AuthStub.authorised()

            val res = postRequest("/group-structure/local-registration-number", Json.obj("value" -> ""))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CheckAnswersGroupStructureController.onPageLoad().url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/group-structure/local-registration-number", Json.obj("value" -> true))()

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

    "GET /group-structure/local-registration-number" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(ParentCompanyNamePage, companyName)

          val res = getRequest("/group-structure/local-registration-number/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.localRegistrationNumber(companyName))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/group-structure/local-registration-number/change")()

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
