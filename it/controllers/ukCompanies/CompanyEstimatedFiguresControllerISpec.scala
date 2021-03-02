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
import play.api.http.Status._
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import models.returnModels.fullReturn.UkCompanyModel
import models._
import pages.ukCompanies.UkCompaniesPage
import play.api.libs.json.Json
import assets.UkCompanyITConstants._

class CompanyEstimatedFiguresControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /uk-companies/company-estimated-figures" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
            .copy(taxEBITDA = Some(BigDecimal(123)), netTaxInterest = Some(BigDecimal(123)))
          val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
          setAnswers(userAnswers)

          val res = getRequest("/uk-companies/1/company-estimated-figures/")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.companyEstimatedFigures)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
            .copy(taxEBITDA = Some(BigDecimal(123)), netTaxInterest = Some(BigDecimal(123)))
          val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
          setAnswers(userAnswers)

          val res = getRequest("/uk-companies/1/company-estimated-figures/")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /uk-Companies/company-estimated-figures" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to CYA page" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
            )

            val res = postRequest("/uk-companies/1/company-estimated-figures", Json.obj("value" -> CompanyEstimatedFigures.values))()
            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CheckAnswersUkCompanyController.onPageLoad(1).url)
              )
            }
          }
        }
      }
    }
  }

  "in Change mode" when {

    "GET /uk-companies/company-estimated-figures" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
            .copy(taxEBITDA = Some(BigDecimal(123)), netTaxInterest = Some(BigDecimal(123)))
          val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
          setAnswers(userAnswers)

          val res = getRequest("/uk-companies/1/company-estimated-figures/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.companyEstimatedFigures)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val company = UkCompanyModel(CompanyDetailsModel("123", "1123456789"))
            .copy(taxEBITDA = Some(BigDecimal(123)), netTaxInterest = Some(BigDecimal(123)))
          val userAnswers = UserAnswers("id").set(UkCompaniesPage, company, Some(1)).get
          setAnswers(userAnswers)

          val res = getRequest("/uk-companies/1/company-estimated-figures/change")()

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
