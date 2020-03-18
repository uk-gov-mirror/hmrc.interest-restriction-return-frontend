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

package controllers.groupLevelInformation

import assets.{BaseITConstants, PageTitles}
import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import models.NormalMode
import pages.aboutReturn.FullOrAbbreviatedReturnPage
import play.api.libs.json.Json
import play.api.test.Helpers._
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}


class InfrastructureCompanyElectionControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /group-level-information/infrastructure-company-election" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/group-level-information/infrastructure-company-election")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.infrastructureCompanyElection)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/group-level-information/infrastructure-company-election")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /group-level-information/infrastructure-company-election" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "on a Full journey redirect to ReturnContainEstimates page" in {

            AuthStub.authorised()
            setAnswers(FullOrAbbreviatedReturnPage, Full)

            val res = postRequest("/group-level-information/infrastructure-company-election", Json.obj("value" -> "true"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.groupLevelInformation.routes.ReturnContainEstimatesController.onPageLoad(NormalMode).url)
              )
            }
          }

          "on a Abbreviated journey redirect to UnderConstruction page" in {

            AuthStub.authorised()
            setAnswers(FullOrAbbreviatedReturnPage, Abbreviated)

            val res = postRequest("/group-level-information/infrastructure-company-election", Json.obj("value" -> "true"))()

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

          val res = postRequest("/group-level-information/infrastructure-company-election", Json.obj("value" -> "true"))()

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

    "GET /group-level-information/infrastructure-company-election" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/group-level-information/infrastructure-company-election/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.infrastructureCompanyElection)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/group-level-information/infrastructure-company-election/change")()

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
