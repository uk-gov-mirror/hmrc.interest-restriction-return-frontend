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
import models.{NormalMode, CheckMode}
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class ElectedInterestAllowanceAlternativeCalcBeforeControllerISpec
  extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /elections/elected-interest-allowance-alternative-calc-before" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          val res = getRequest("/elections/elected-interest-allowance-alternative-calc-before")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.electedInterestAllowanceAlternativeCalcBefore)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/elected-interest-allowance-alternative-calc-before")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/elected-interest-allowance-alternative-calc-before" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "the answer is false" should {

            "redirect to Interest Allowance Alternative Calculation Election page" in {

              AuthStub.authorised()

              val res = postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> false))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(routes.InterestAllowanceAlternativeCalcElectionController.onPageLoad(NormalMode).url)
                )
              }
            }
          }

          "the answer is true" should {

            "redirect to Interest Allowance Non Consolidated Investments Election page" in {

              AuthStub.authorised()

              val res = postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(routes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(NormalMode).url)
                )
              }
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()

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

    "GET /elections/elected-interest-allowance-alternative-calc-before" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/elections/elected-interest-allowance-alternative-calc-before/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.electedInterestAllowanceAlternativeCalcBefore)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/elected-interest-allowance-alternative-calc-before/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/elected-interest-allowance-alternative-calc-before" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to CheckYourAnswers page when true" in {
            AuthStub.authorised()

            val res = postRequest("/elections/elected-interest-allowance-alternative-calc-before/change", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.elections.routes.CheckAnswersElectionsController.onPageLoad().url))
            }
          }

          "redirect to InterestAllowanceAlternativeCalcElectionController page when false" in {
            AuthStub.authorised()

            val res = postRequest("/elections/elected-interest-allowance-alternative-calc-before/change", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.elections.routes.InterestAllowanceAlternativeCalcElectionController.onPageLoad(CheckMode).url))
            }
          }
        }
      }
    }
  }
}
