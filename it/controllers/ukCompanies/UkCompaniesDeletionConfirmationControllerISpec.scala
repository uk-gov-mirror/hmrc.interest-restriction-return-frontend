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
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class UkCompaniesDeletionConfirmationControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /uk-companies/2/deletion-confirmation" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(emptyUserAnswers
            .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).success.value
            .set(UkCompaniesPage, ukCompanyModelMax, Some(2)).success.value
          )

          val res = getRequest("/uk-companies/2/deletion-confirmation")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.ukCompaniesDeletionConfirmation(ukCompanyModelMax.companyDetails.companyName))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies/2/deletion-confirmation")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /uk-companies/2/deletion-confirmation" when {

      "user is authorised" when {

        "enters a valid answer of 'Yes'" when {

          "delete the selected uk company and redirect to the list view" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).success.value
              .set(UkCompaniesPage, ukCompanyModelMax, Some(2)).success.value
            )

            val res = postRequest("/uk-companies/2/deletion-confirmation", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.UkCompaniesReviewAnswersListController.onPageLoad().url)
              )

              val updatedAnswers = getAnswers(emptyUserAnswers.id).fold(emptyUserAnswers)(x => x)
              updatedAnswers.getList(UkCompaniesPage) shouldBe Seq(ukCompanyModelMax)
            }
          }
        }

        "enters a valid answer of 'No'" when {

          "redirect to DeletionConfirmation page" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).success.value
              .set(UkCompaniesPage, ukCompanyModelMax, Some(2)).success.value
            )

            val res = postRequest("/uk-companies/2/deletion-confirmation", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.UkCompaniesReviewAnswersListController.onPageLoad().url)
              )

              val updatedAnswers = getAnswers(emptyUserAnswers.id).fold(emptyUserAnswers)(x => x)
              updatedAnswers.getList(UkCompaniesPage) shouldBe Seq(ukCompanyModelMax, ukCompanyModelMax)
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/uk-companies/2/deletion-confirmation", Json.obj("value" -> true))()

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
