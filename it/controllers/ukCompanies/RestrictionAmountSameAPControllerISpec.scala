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

import assets.UkCompanyITConstants.ukCompanyModelMax
import assets.AccountingPeriodITConstants.{startDate, endDate}
import assets.{BaseITConstants, PageTitles}
import pages.aboutReturn.AccountingPeriodPage
import pages.ukCompanies.UkCompaniesPage
import play.api.http.Status._
import play.api.libs.json.Json
import models.NormalMode
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import models.returnModels.AccountingPeriodModel

class RestrictionAmountSameAPControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /uk-companies/1/restriction-amount-same-accounting-period" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get)

          val res = getRequest("/uk-companies/1/restriction-amount-same-accounting-period")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.restrictionAmountSameAP)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies/1/restriction-amount-same-accounting-period")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /uk-companies/1/restriction-amount-same-accounting-period" when {

      "user is authorised" when {

        "enters a valid answer" should {

          "redirect to CompanyEstimatedFiguresController page" in {

            AuthStub.authorised()
            setAnswers(emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
              .set(AccountingPeriodPage, AccountingPeriodModel(startDate, endDate)).get
            )

            val res = postRequest("/uk-companies/1/restriction-amount-same-accounting-period", Json.obj("value" -> 1.01))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ukCompanies.routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/uk-companies/1/restriction-amount-same-accounting-period", Json.obj("value" -> 1.01))()

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

    "GET /uk-companies/1/restriction-amount-same-accounting-period/change" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get)

          val res = getRequest("/uk-companies/1/restriction-amount-same-accounting-period/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.restrictionAmountSameAP)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies/1/restriction-amount-same-accounting-period/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /uk-companies/1/restriction-amount-same-accounting-period/change" when {

      "user is authorised" when {

        "enters a valid answer" should {

          "redirect to UK Companies Check Your Answer page" in {

            AuthStub.authorised()
            setAnswers(emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
              .set(AccountingPeriodPage, AccountingPeriodModel(startDate, endDate)).get
            )

            val res = postRequest("/uk-companies/1/restriction-amount-same-accounting-period/change", Json.obj("value" -> 1.01))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CheckAnswersUkCompanyController.onPageLoad(1).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/uk-companies/1/restriction-amount-same-accounting-period", Json.obj("value" -> 1.01))()

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
