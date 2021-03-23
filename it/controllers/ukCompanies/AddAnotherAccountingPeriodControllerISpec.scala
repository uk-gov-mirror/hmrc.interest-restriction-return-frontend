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
import pages.ukCompanies.{UkCompaniesPage, CompanyAccountingPeriodEndDatePage}
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import models.NormalMode
import java.time.LocalDate

class AddAnotherAccountingPeriodControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val companyIdx = 1
  val restrictionIdx = 1

  "in Normal mode" when {

    "GET /uk-companies/:idx/add-another-accounting-period" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get)
          val res = getRequest(s"/uk-companies/$companyIdx/add-another-accounting-period")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.addAnotherAccountingPeriod)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get)

          val res = getRequest(s"/uk-companies/$companyIdx/add-another-accounting-period")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /uk-companies/:idx/add-another-accounting-period" when {

      val ap1EndDate = LocalDate.of(2017, 3, 1)
      val ap2EndDate = LocalDate.of(2017, 9, 1)

      "user is authorised" when {

        "enters true and only one accounting period exists" must {

          "redirect to the second CompanyAccountingPeriodEndDate page" in {

            val userAnswers = (for {
              ua  <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx))
              ua2 <- ua.set(CompanyAccountingPeriodEndDatePage(1,1), ap1EndDate)
            } yield ua2).get

            AuthStub.authorised()
            setAnswers(userAnswers)

            val res = postRequest(s"/uk-companies/$companyIdx/add-another-accounting-period", Json.obj("value" -> "true"))()
            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CompanyAccountingPeriodEndDateController.onPageLoad(1, 2, NormalMode).url)
              )
            }
          }
        }

        "enters true and two accounting periods exist" must {

          "redirect to the third CompanyAccountingPeriodEndDate page" in {

            val userAnswers = (for {
              ua  <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx))
              ua2 <- ua.set(CompanyAccountingPeriodEndDatePage(1,1), ap1EndDate)
              ua3 <- ua2.set(CompanyAccountingPeriodEndDatePage(1,2), ap2EndDate)
            } yield ua3).get

            AuthStub.authorised()
            setAnswers(userAnswers)

            val res = postRequest(s"/uk-companies/$companyIdx/add-another-accounting-period", Json.obj("value" -> "true"))()
            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CompanyAccountingPeriodEndDateController.onPageLoad(1, 3, NormalMode).url)
              )
            }
          }
        }        

        "enters false" must {

          "redirect to the second CompanyAccountingPeriodEndDate page" in {

            val userAnswers = (for {
              ua  <- emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx))
              ua2 <- ua.set(CompanyAccountingPeriodEndDatePage(1,1), ap1EndDate)
              ua3 <- ua2.set(CompanyAccountingPeriodEndDatePage(1,2), ap2EndDate)
            } yield ua3).get

            AuthStub.authorised()
            setAnswers(userAnswers)

            val res = postRequest(s"/uk-companies/$companyIdx/add-another-accounting-period", Json.obj("value" -> "false"))()
            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CompanyContainsEstimatesController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }     
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get)

          val res = postRequest(s"/uk-companies/$companyIdx/add-another-accounting-period", Json.obj("value" -> "true"))()

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

    "GET /uk-companies/:idx/add-another-accounting-period" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(companyIdx)).get)

          val res = getRequest(s"/uk-companies/$companyIdx/add-another-accounting-period/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.addAnotherAccountingPeriod)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest(s"/uk-companies/$companyIdx/add-another-accounting-period/change")()

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
