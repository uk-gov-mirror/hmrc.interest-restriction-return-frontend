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

import assets.PartnershipsITConstants._
import assets.{BaseITConstants, PageTitles}
import pages.elections.PartnershipsPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class PartnershipSAUTRControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /elections/partnership/1/sautr" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(emptyUserAnswers
            .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
          )

          val res = getRequest("/elections/partnership/1/sautr")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.partnershipSAUTR(partnerName))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/partnership/1/sautr")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/partnership/1/sautr" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to PartnershipSAUTR page" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
            )

            val res = postRequest("/elections/partnership/1/sautr", Json.obj("value" -> sautr))()

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

          val res = postRequest("/elections/partnership/1/sautr", Json.obj("value" -> sautr))()

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

    "GET /elections/partnership/1/sautr" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(emptyUserAnswers
            .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
          )
          val res = getRequest("/elections/partnership/1/sautr/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.partnershipSAUTR(partnerName))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/partnership/1/sautr/change")()

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
