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

import assets.UkCompanyITConstants._
import assets.{BaseITConstants, PageTitles}
import pages.ukCompanies.UkCompaniesPage
import controllers.ukCompanies.{routes => ukCompanies}
import models.NormalMode
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class EnterCompanyTaxEBITDAControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /uk-companies/1/enter-company-tax-ebitda" when {

      "user is authorised" when {

        "data is retrieved for the uk companies model" should {

          "return OK (200)" in {

            AuthStub.authorised()
            setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get)

            val res = getRequest("/uk-companies/1/enter-company-tax-ebitda")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.companyTaxEBITDA)
              )
            }
          }
        }

        "no data is retrieved for the uk companies model" should {

          "return ISE (500)" in {

            AuthStub.authorised()

            val res = getRequest("/uk-companies/1/enter-company-tax-ebitda")()

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

          val res = getRequest("/uk-companies/1/enter-company-tax-ebitda")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /uk-companies/1/enter-company-tax-ebitda" when {

      "user is authorised" when {

        "data is retrieved for the uk companies model" should {

          "enters a valid answer" when {

            //TODO: Update as part of routing sub-task
            "redirect to Under Construction page" in {

              AuthStub.authorised()
              setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get)

              val res = postRequest("/uk-companies/1/enter-company-tax-ebitda", Json.obj("value" -> 1))()
              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(controllers.routes.UnderConstructionController.onPageLoad().url)
                )
              }
            }
          }
        }

        "NO data is retrieved for the uk companies model" should {

          "Return ISE (500)" in {

            AuthStub.authorised()

            val res = postRequest("/uk-companies/1/enter-company-tax-ebitda", Json.obj("value" -> 1))()
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

          val res = postRequest("/uk-companies/1/enter-company-tax-ebitda", Json.obj("value" -> 1))()

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

    "GET /uk-companies/1/enter-company-tax-ebitda/change" when {

      "user is authorised" should {

        "data is retrieved for the uk companies model" should {

          "return OK (200)" in {

            AuthStub.authorised()
            setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get)

            val res = getRequest("/uk-companies/1/enter-company-tax-ebitda/change")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.companyTaxEBITDA)
              )
            }
          }
        }

        "no data is retrieved for the uk companies model" should {

          "return ISE (500)" in {

            AuthStub.authorised()

            val res = getRequest("/uk-companies/1/enter-company-tax-ebitda/change")()

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

          val res = getRequest("/uk-companies/1/enter-company-tax-ebitda/change")()

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
