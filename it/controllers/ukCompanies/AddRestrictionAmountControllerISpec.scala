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

package controllers.ukCompanies

import assets.{BaseITConstants, PageTitles}
import models.NormalMode
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class AddRestrictionAmountControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /uk-companies/add-restriction-amount" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          val res = getRequest("/uk-companies/add-restriction-amount")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.addRestrictionAmount)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies/add-restriction-amount")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /uk-companies/add-restriction-amount" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to AddRestrictionAmount page" in {

            AuthStub.authorised()

            val res = postRequest("/uk-companies/add-restriction-amount", Json.obj("value" -> 1))()

            //TODO
//            whenReady(res) { result =>
//              result should have(
//                httpStatus(SEE_OTHER),
//                redirectLocation(controllers.ukCompanies.routes.AddRestrictionAmountController.onPageLoad(NormalMode).url)
//              )
//          }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/uk-companies/add-restriction-amount", Json.obj("value" -> 1))()

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

    "GET /uk-companies/add-restriction-amount" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/uk-companies/add-restriction-amount/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.addRestrictionAmount)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies/add-restriction-amount/change")()

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
