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

import assets.InvestorGroupITConstants.{investorGroupsFixedRatioModel, investorGroupsGroupRatioModel}
import assets.{BaseITConstants, PageTitles}
import models.NormalMode
import pages.elections.InvestorGroupsPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class InvestorRatioMethodControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /elections/investor-group/1/ratio-method" when {

      "user is authorised" when {

        "answers are retrieved for investor groups" must {

          "return OK (200)" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(1)).success.value
              .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2)).success.value
            )

            val res = getRequest("/elections/investor-group/1/ratio-method")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.investorRatioMethod)
              )
            }
          }
        }

        "answers are NOT retrieved for investor groups" must {

          "return ISE (500)" in {

            AuthStub.authorised()

            val res = getRequest("/elections/investor-group/1/ratio-method")()

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

          val res = getRequest("/elections/investor-group/1/ratio-method")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/investor-group/1/ratio-method" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to Other Investor Group Elections page" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(1)).success.value
              .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2)).success.value
            )

            val res = postRequest("/elections/investor-group/1/ratio-method", Json.obj("value" -> "true"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.OtherInvestorGroupElectionsController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/elections/investor-group/1/ratio-method", Json.obj("value" -> 1))()

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

    "GET /elections/investor-group/1/ratio-method" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(emptyUserAnswers
            .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(1)).success.value
            .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2)).success.value
          )

          val res = getRequest("/elections/investor-group/1/ratio-method/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.investorRatioMethod)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/investor-group/1/ratio-method/change")()

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
