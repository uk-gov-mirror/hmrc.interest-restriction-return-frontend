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
import assets.UkCompanyITConstants.ukCompanyModelMax
import pages.ukCompanies.{CompanyAccountingPeriodEndDatePage, UkCompaniesPage}
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

import java.time.LocalDate

class RestrictionAmountForAccountingPeriodControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val companyIdx = 1
  val restrictionIdx = 1
  val endDate = LocalDate.of(2021, 1, 1)


  "in Normal mode" when {

    "GET /:idx/restrictions/:restrictionIdx/restriction-amount-for-accounting-period" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(
            emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get
              .set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), endDate).get
          )

          val res = getRequest(s"/uk-companies/$companyIdx/restrictions/$restrictionIdx/restriction-amount-for-accounting-period")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.restrictionAmountForAccountingPeriod(endDate))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()
          setAnswers(
            emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get
              .set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), endDate).get
          )

          val res = getRequest(s"/uk-companies/$companyIdx/restrictions/$restrictionIdx/restriction-amount-for-accounting-period")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /:idx/restrictions/:restrictionIdx/restriction-amount-for-accounting-period" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to RestrictionAmountForAccountingPeriod page" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers
                .set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get
                .set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), endDate).get
            )

            val res = postRequest(s"/uk-companies/$companyIdx/restrictions/$restrictionIdx/restriction-amount-for-accounting-period", Json.obj("value" -> 1))()
            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ukCompanies.routes.CheckRestrictionController.onPageLoad(1,1).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()
          setAnswers(emptyUserAnswers
            .set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get
            .set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), endDate).get
          )

          val res = postRequest(s"/uk-companies/$companyIdx/restrictions/$restrictionIdx/restriction-amount-for-accounting-period", Json.obj("value" -> 1))()

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

    "GET /:idx/restrictions/:restrictionIdx/restriction-amount-for-accounting-period" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(
            emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get
              .set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), endDate).get
          )

          val res = getRequest(s"/uk-companies/$companyIdx/restrictions/$restrictionIdx/restriction-amount-for-accounting-period/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.restrictionAmountForAccountingPeriod(endDate))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()
          setAnswers(
            emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get
              .set(CompanyAccountingPeriodEndDatePage(companyIdx, restrictionIdx), endDate).get
          )

          val res = getRequest(s"/uk-companies/$companyIdx/restrictions/$restrictionIdx/restriction-amount-for-accounting-period/change")()

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
