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

package controllers.aboutReturn

import java.time.{Instant, ZoneOffset}

import assets.{BaseITConstants, PageTitles}
import pages.aboutReturn.AccountingPeriodStartPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class AccountingPeriodEndControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate

  "in Normal mode" when {

    "GET /about-return/accounting-period-end" when {

      "user is authorised" should {

        "return OK (200)" in {

          setAnswers(AccountingPeriodStartPage,now)

          AuthStub.authorised()
          val res = getRequest("/about-return/accounting-period-end")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.accountingPeriodEnd)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/about-return/accounting-period-end")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /about-return/accounting-period-end" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to Under Construction Page" in {

            setAnswers(AccountingPeriodStartPage,now)

            AuthStub.authorised()

            val res = postRequest("/about-return/accounting-period-end",
              Json.obj("value.day" -> now.plusDays(1).getDayOfMonth,
                "value.month" -> now.plusDays(1).getMonthValue,
                "value.year" -> now.plusDays(1).getYear))()

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

          val res = postRequest("/about-return/accounting-period-end", Json.obj("value" -> ""))()

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

    "GET /accountingPeriodEnd/accounting-period-end" when {

      "user is authorised" should {

        "return OK (200)" in {

          setAnswers(AccountingPeriodStartPage,now)

          AuthStub.authorised()

          val res = getRequest("/about-return/accounting-period-end/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.accountingPeriodEnd)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/about-return/accounting-period-end/change")()

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
