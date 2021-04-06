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
import pages.aboutReturn.AccountingPeriodPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import models.returnModels.AccountingPeriodModel

class AccountingPeriodControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate

  "in Normal mode" when {

    "GET /about-the-return/accounting-period" when {

      "user is authorised" should {

        "return OK (200)" in {

          setAnswers(AccountingPeriodPage, AccountingPeriodModel(now, now.plusDays(1L)))

          AuthStub.authorised()
          val res = getRequest("/about-the-return/accounting-period")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.accountingPeriod)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/about-the-return/accounting-period")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /about-the-return/accounting-period" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to Check answer Page" in {

            AuthStub.authorised()

            val res = postRequest("/about-the-return/accounting-period",
              Json.obj("startValue.day" -> now.getDayOfMonth,
                "startValue.month" -> now.getMonthValue,
                "startValue.year" -> now.getYear,
                "endValue.day" -> now.plusDays(1).getDayOfMonth,
                "endValue.month" -> now.plusDays(1).getMonthValue,
                "endValue.year" -> now.plusDays(1).getYear))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.aboutReturn.routes.CheckAnswersAboutReturnController.onPageLoad().url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/about-the-return/accounting-period", Json.obj("value" -> ""))()

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

    "GET /accountingPeriod/accounting-period" when {

      "user is authorised" should {

        "return OK (200)" in {

          setAnswers(AccountingPeriodPage, AccountingPeriodModel(now, now.plusDays(1L)))

          AuthStub.authorised()

          val res = getRequest("/about-the-return/accounting-period/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.accountingPeriod)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/about-the-return/accounting-period/change")()

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
