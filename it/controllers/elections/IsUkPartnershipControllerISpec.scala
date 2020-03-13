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
import assets.PartnershipsITConstants._
import models.NormalMode
import pages.elections.{PartnershipNamePage, PartnershipsPage}
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class IsUkPartnershipControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /elections/partnership/1/is-uk" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers
            .set(PartnershipsPage,
              partnershipModelUK.copy(
                isUkPartnership = None,
                sautr = None
              ),
              Some(1)).success.value
          )

          val res = getRequest("/elections/partnership/1/is-uk")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.isUkPartnership(partnerName))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/partnership/1/is-uk")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/partnership/1/is-uk" when {

      "user is authorised" when {

        "enters true" when {

          "redirect to PartnershipSAUTR page" in {

            AuthStub.authorised()
            setAnswers(emptyUserAnswers
              .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
            )

            val res = postRequest("/elections/partnership/1/is-uk", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.elections.routes.PartnershipSAUTRController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }

        "enters false" when {

          "redirect to Review list page" in {

            AuthStub.authorised()
            setAnswers(emptyUserAnswers
              .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
            )

            val res = postRequest("/elections/partnership/1/is-uk", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.elections.routes.PartnershipsReviewAnswersListController.onPageLoad().url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/elections/partnership/1/is-uk", Json.obj("value" -> 1))()

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

    "GET /elections/partnership/1/is-uk" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers
            .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
          )
          val res = getRequest("/elections/partnership/1/is-uk/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.isUkPartnership(partnerName))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/partnership/1/is-uk/change")()

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
