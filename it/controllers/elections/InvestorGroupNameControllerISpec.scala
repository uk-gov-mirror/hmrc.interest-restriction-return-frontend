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
import pages.elections.InvestorGroupsPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import assets.InvestorGroupITConstants._
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class InvestorGroupNameControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /elections/investor-group/1/name" when {

      "user is authorised" when {

        "answer for investor groups is retrieved" should {

          "return OK (200)" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(1)).success.value
              .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2)).success.value
            )

            val res = getRequest("/elections/investor-group/1/name")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.investorGroupName)
              )
            }
          }
        }

        "answer for investor groups is NOT retrieved" should {

          "return ISE (500)" in {

            AuthStub.authorised()

            val res = getRequest("/elections/investor-group/1/name")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.investorGroupName)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/investor-group/1/name")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/investor-group/1/name" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "investor group data is retrieved from mongo" must {

            "redirect to Investor Group Name page" in {

              AuthStub.authorised()

              setAnswers(emptyUserAnswers
                .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(1)).success.value
                .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2)).success.value
              )

              val res = postRequest("/elections/investor-group/1/name", Json.obj("value" -> "Investor Name"))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(routes.InvestorRatioMethodController.onPageLoad(1, NormalMode).url)
                )
              }
            }
          }

          "investor group data is NOT retrieved from mongo" must {

            "return ISE (500)" in {

              AuthStub.authorised()

              val res = postRequest("/elections/investor-group/1/name", Json.obj("value" -> "Investor Name"))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(routes.InvestorRatioMethodController.onPageLoad(1, NormalMode).url)
                )
              }
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/elections/investor-group/1/name", Json.obj("value" -> "Investor Name"))()

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

    "GET /elections/investor-group/1/name" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(emptyUserAnswers
            .set(InvestorGroupsPage, investorGroupsFixedRatioModel, Some(1)).success.value
            .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2)).success.value
          )

          val res = getRequest("/elections/investor-group/1/name/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.investorGroupName)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/investor-group/1/name/change")()

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
