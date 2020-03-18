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

package controllers.ultimateParentCompany

import assets.DeemedParentITConstants.deemedParentModelNonUkCompany
import assets.{BaseITConstants, PageTitles}
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import controllers.ultimateParentCompany.{routes => ultimateParentCompanyRoutes}
import models.NormalMode
import pages.ultimateParentCompany.DeemedParentPage

class ParentCompanyCTUTRControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /ultimate-parent-company/1/ctutr" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(
            emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
          )

          val res = getRequest("/ultimate-parent-company/1/ctutr")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.parentCompanyCTUTR)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/ultimate-parent-company/1/ctutr")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /ultimate-parent-company/1/ctutr" when {

      "user is authorised" when {

        "enters a valid answer" should {

          "redirect to check answers page" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/ultimate-parent-company/1/ctutr", Json.obj("value" -> "1111111111"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(ultimateParentCompanyRoutes.CheckAnswersGroupStructureController.onPageLoad(1).url)
              )
            }
          }
        }

        "enters an invalid answer" when {

          "return a BAD_REQUEST (400)" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/ultimate-parent-company/1/ctutr", Json.obj("value" -> ""))()

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

          val res = postRequest("/ultimate-parent-company/1/ctutr", Json.obj("value" -> "1111111111"))()

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

    "GET /ultimate-parent-company/1/ctutr/change" when {

      "user is authorised" should {

        "user answer for parent company name exists" should {

          "return OK (200)" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = getRequest("/ultimate-parent-company/1/ctutr/change")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.parentCompanyCTUTR)
              )
            }
          }
        }

        "user answer for parent company name doesn't exist" should {

          "return INTERNAL_SERVER_ERROR (500)" in {

            AuthStub.authorised()

            val res = getRequest("/ultimate-parent-company/1/ctutr/change")()

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

          val res = getRequest("/ultimate-parent-company/1/ctutr/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /ultimate-parent-company/1/ctutr" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to CheckYourAnswers page" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/ultimate-parent-company/1/ctutr/change", Json.obj("value" -> "1111111111"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onPageLoad(1).url)
              )
            }
          }
        }

        "enters an invalid answer" when {

          "return a BAD_REQUEST (400)" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/ultimate-parent-company/1/ctutr/change", Json.obj("value" -> ""))()

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

          val res = postRequest("/ultimate-parent-company/1/ctutr/change", Json.obj("value" -> "1111111111"))()

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
