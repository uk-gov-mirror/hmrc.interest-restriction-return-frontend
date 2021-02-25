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
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import scala.concurrent.duration._
import scala.concurrent.Await
import pages.elections.InvestmentNamePage

class InterestAllowanceNonConsolidatedInvestmentsElectionControllerISpec extends
  IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  "in Normal mode" when {

    "GET /elections/interest-allowance-non-consolidated-investments-election" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          val res = getRequest("/elections/interest-allowance-non-consolidated-investments-election")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.interestAllowanceNonConsolidatedInvestmentsElection)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/interest-allowance-non-consolidated-investments-election")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/interest-allowance-non-consolidated-investments-election" when {

      "user is authorised" when {

        "user answers yes" should {

          "redirect to Investmens List page" in {

            AuthStub.authorised()

            val res = postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.InvestmentsReviewAnswersListController.onPageLoad().url)
              )
            }
          }
        }

        "user answers no" should {

          "redirect to Elected Consolidated Pship Election Before page" in {

            AuthStub.authorised()

            val res = postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(NormalMode).url)
              )
            }
          }

          "clear down investments data" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers)

            val resultUserAnswers = for {
              _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> true))()
              _ <- postRequest("/elections/investment/1/name", Json.obj("value" -> "Investment 1"))()
              _ <- postRequest("/elections/investment/2/name", Json.obj("value" -> "Investment 2"))()
              _ <- postRequest("/elections/investment/3/name", Json.obj("value" -> "Investment 3"))()
              _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
              userAnswers <- getAnswersFuture("id")
            } yield userAnswers

            val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
            
            userAnswers.getList(InvestmentNamePage) shouldEqual Nil
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> true))()

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

    "GET /elections/interest-allowance-non-consolidated-investments-election" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/elections/interest-allowance-non-consolidated-investments-election/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.interestAllowanceNonConsolidatedInvestmentsElection)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/interest-allowance-non-consolidated-investments-election/change")()

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
