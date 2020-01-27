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
import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import models.NormalMode
import pages.startReturn.FullOrAbbreviatedReturnPage
import play.api.libs.json.Json
import play.api.test.Helpers._
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}


class InfrastructureCompanyElectionControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /infrastructure-company-election" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/infrastructure-company-election")

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf("Has the group made the Infrastructure company election?")
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/infrastructure-company-election")

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /infrastructure-company-election" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "on a Full journey redirect to ReturnContainEstimates page" in {

            AuthStub.authorised()
            setAnswers(FullOrAbbreviatedReturnPage, Full)

            val res = postRequest("/infrastructure-company-election", Json.obj("value" -> "true"))

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.aboutReturn.routes.ReturnContainEstimatesController.onPageLoad(NormalMode).url)
              )
            }
          }

          "on a Abbreviated journey redirect to UnderConstruction page" in {

            AuthStub.authorised()
            setAnswers(FullOrAbbreviatedReturnPage, Abbreviated)

            val res = postRequest("/infrastructure-company-election", Json.obj("value" -> "true"))

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.routes.UnderConstructionController.onPageLoad().url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/infrastructure-company-election", Json.obj("value" -> "true"))

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

    "GET /infrastructure-company-election" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/infrastructure-company-election/change")

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf("Has the group made the Infrastructure company election?")
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/infrastructure-company-election/change")

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
