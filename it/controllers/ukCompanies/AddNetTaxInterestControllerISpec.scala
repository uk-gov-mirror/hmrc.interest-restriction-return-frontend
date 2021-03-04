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
import pages.ukCompanies.UkCompaniesPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class AddNetTaxInterestControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val idx = 1

  "in Normal mode" when {

    "GET /:idx/add-net-tax-interest" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(idx)).get)

          val res = getRequest(s"/uk-companies/$idx/add-net-tax-interest")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.addNetTaxInterest)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest(s"/uk-companies/$idx/add-net-tax-interest")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /:idx/add-net-tax-interest" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to AddNetTaxInterest page" in {

            AuthStub.authorised()
            setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(idx)).get)

            val res = postRequest(s"/uk-companies/$idx/add-net-tax-interest", Json.obj("value" -> "true"))()
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

          val res = postRequest(s"/uk-companies/$idx/add-net-tax-interest", Json.obj("value" -> "true"))()

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

    "GET /:idx/add-net-tax-interest" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(idx)).get)

          val res = getRequest(s"/uk-companies/$idx/add-net-tax-interest/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.addNetTaxInterest)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest(s"/uk-companies/$idx/add-net-tax-interest/change")()

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
