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
import models.{NormalMode, SectionStatus}
import models.returnModels.ReviewAndCompleteModel
import pages.groupStructure.HasDeemedParentPage
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.ukCompanies.UkCompaniesPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class UkCompaniesReviewAnswersListControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /uk-companies" when {

      "user is authorised" when {

        "there are companies in the list" should {

          "return OK (200)" in {

            AuthStub.authorised()

            setAnswers(
              emptyUserAnswers.set(HasDeemedParentPage, true).success.value
                .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).success.value
                .set(UkCompaniesPage, ukCompanyModelMax, Some(2)).success.value
                .set(UkCompaniesPage, ukCompanyModelMax, Some(3)).success.value
            )

            val res = getRequest("/uk-companies")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.ukCompaniesReviewAnswersList(3))
              )
            }
          }
        }

        "there are NO companies in the list" should {

          "return SEE_OTHER (303) and redirect to the add Company Details (Name & UTR) page" in {

            AuthStub.authorised()

            val res = getRequest("/uk-companies")()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CompanyDetailsController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /uk-companies" when {

      "user is authorised" when {

        "Add another company is true" must {

          "redirect to the next Company Name & UTR page" in {

            AuthStub.authorised()

            val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, true).success.value
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).success.value

            setAnswers(userAnswers)

            val res = postRequest("/uk-companies", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CompanyDetailsController.onPageLoad(2, NormalMode).url)
              )
            }
          }
        }

        "Add another company is false" must {

          "redirect to Under Construction page" in {

            AuthStub.authorised()

            val userAnswers = emptyUserAnswers
              .set(HasDeemedParentPage, false).success.value
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).success.value
              .set(ReviewAndCompletePage, ReviewAndCompleteModel(ukCompanies = SectionStatus.InProgress)).success.value


            setAnswers(userAnswers)

            val res = postRequest("/uk-companies", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.checkTotals.routes.DerivedCompanyController.onPageLoad().url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/uk-companies", Json.obj("value" -> true))()

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
