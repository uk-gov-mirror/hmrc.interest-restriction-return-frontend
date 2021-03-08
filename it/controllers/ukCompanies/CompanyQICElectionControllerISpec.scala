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
import assets.UkCompanyITConstants.{ukCompanyModelMax, ukCompanyModelReactivationMaxIncome}
import models.NormalMode
import pages.aboutReturn.FullOrAbbreviatedReturnPage
import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import pages.ukCompanies.UkCompaniesPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import views.ViewUtils.addPossessive

class CompanyQICElectionControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /:idx/company-qic-election" when {

      "user is authorised" should {

        "return OK (200)" in {

          val idx = 1

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(idx)).get)
          val res = getRequest(s"/uk-companies/$idx/company-qic-election")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.companyQICElection)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          val idx = 1

          AuthStub.unauthorised()

          val res = getRequest(s"/uk-companies/$idx/company-qic-election")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /:idx/company-qic-election" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to companyTaxEBITDA page" in {

            val idx = 1

            AuthStub.authorised()
            setAnswers(emptyUserAnswers
              .set(FullOrAbbreviatedReturnPage, Full).get
              .set(UkCompaniesPage, ukCompanyModelMax, Some(idx)).get)

            val res = postRequest(s"/uk-companies/$idx/company-qic-election", Json.obj("value" -> "true"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.EnterCompanyTaxEBITDAController.onPageLoad(idx, NormalMode).url)
              )
            }
          }

          "redirect to companyTaxEBITDA page for index 2" in {
            
            val idx = 2

            AuthStub.authorised()
            setAnswers(emptyUserAnswers
              .set(FullOrAbbreviatedReturnPage, Full).get
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
              .set(UkCompaniesPage, ukCompanyModelMax, Some(2)).get)

            val res = postRequest(s"/uk-companies/$idx/company-qic-election", Json.obj("value" -> "true"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.EnterCompanyTaxEBITDAController.onPageLoad(idx, NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          val idx = 1

          AuthStub.unauthorised()

          val res = postRequest(s"/uk-companies/$idx/company-qic-election", Json.obj("value" -> 1))()

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

    "GET /:idx/company-qic-election" when {

      "user is authorised" should {

        "return OK (200)" in {

          val idx = 1

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(idx)).get)

          val res = getRequest(s"/uk-companies/$idx/company-qic-election/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.companyQICElection)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          val idx = 1
          AuthStub.unauthorised()

          val res = getRequest(s"/uk-companies/$idx/company-qic-election/change")()

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
