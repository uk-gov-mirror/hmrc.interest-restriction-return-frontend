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
import models.InvestorRatioMethod.FixedRatioMethod
import models.NormalMode
import models.OtherInvestorGroupElections.GroupEBITDA
import pages.elections.InvestorRatioMethodPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class OtherInvestorGroupElectionsControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /elections/other-investor-group-elections" when {

      "user is authorised" when {

        "answer for Investor Ratio Method page exists" should {

          "return OK (200)" in {

            AuthStub.authorised()

            setAnswers(InvestorRatioMethodPage, FixedRatioMethod)

            val res = getRequest("/elections/other-investor-group-elections")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.otherInvestorGroupElections)
              )
            }
          }
        }

        "answer for Investor Ratio Method page does NOT exist" should {

          "return ISE (500)" in {

            AuthStub.authorised()

            val res = getRequest("/elections/other-investor-group-elections")()

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

          val res = getRequest("/elections/other-investor-group-elections")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/other-investor-group-elections" when {

      "user is authorised" should {

        "when valid data is submitted" should {

          "return SEE_OTHER (303) and redirect to the Elected Group EBITDA Before page" in {

            AuthStub.authorised()
            val res = postRequest("/elections/other-investor-group-elections", Json.obj("value[0]" -> GroupEBITDA.toString))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/elections/other-investor-group-elections", Json.obj("value[0]" -> GroupEBITDA.toString))()

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

    "GET /elections/other-investor-group-elections" when {

      "answer for Investor Ratio Method page exists" should {

        "user is authorised" should {

          "return OK (200)" in {

            AuthStub.authorised()

            setAnswers(InvestorRatioMethodPage, FixedRatioMethod)

            val res = getRequest("/elections/other-investor-group-elections/change")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.otherInvestorGroupElections)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/other-investor-group-elections/change")()

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
