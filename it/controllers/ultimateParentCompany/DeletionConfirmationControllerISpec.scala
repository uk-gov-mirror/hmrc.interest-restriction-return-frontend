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

package controllers.ultimateParentCompany

import assets.{BaseITConstants, PageTitles}
import assets.DeemedParentITConstants._
import models.NormalMode
import pages.ultimateParentCompany.{DeemedParentPage, HasDeemedParentPage}
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class DeletionConfirmationControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /ultimate-parent-company/2/deletion-confirmation" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(emptyUserAnswers
            .set(HasDeemedParentPage, true).success.value
            .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value
            .set(DeemedParentPage, deemedParentModelUkCompany, Some(2)).success.value
          )

          val res = getRequest("/ultimate-parent-company/2/deletion-confirmation")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.deletionConfirmation(deemedParentModelUkCompany.companyName.name))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/ultimate-parent-company/2/deletion-confirmation")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /ultimate-parent-company/2/deletion-confirmation" when {

      "user is authorised" when {

        "enters a valid answer of 'Yes'" when {

          "delete the selected deemed parent and redirect to add deemed parent" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(HasDeemedParentPage, true).success.value
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(2)).success.value
            )

            val res = postRequest("/ultimate-parent-company/2/deletion-confirmation", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ultimateParentCompany.routes.ParentCompanyNameController.onPageLoad(2, NormalMode).url)
              )

              val updatedAnswers = getAnswers(emptyUserAnswers.id).fold(emptyUserAnswers)(x => x)
              updatedAnswers.getList(DeemedParentPage) shouldBe Seq(deemedParentModelUkCompany)
            }
          }

          "delete the selected deemed parent when you have third deemed parents and redirect to review list" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(HasDeemedParentPage, true).success.value
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(2)).success.value
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(3)).success.value
            )

            val res = postRequest("/ultimate-parent-company/2/deletion-confirmation", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ultimateParentCompany.routes.DeemedParentReviewAnswersListController.onPageLoad().url)
              )

              val updatedAnswers = getAnswers(emptyUserAnswers.id).fold(emptyUserAnswers)(x => x)
              updatedAnswers.getList(DeemedParentPage) shouldBe Seq(deemedParentModelUkCompany, deemedParentModelUkCompany)
            }
          }
        }

        "enters a valid answer of 'No'" when {

          "redirect to DeletionConfirmation page" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(HasDeemedParentPage, true).success.value
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value
              .set(DeemedParentPage, deemedParentModelUkCompany, Some(2)).success.value
            )

            val res = postRequest("/ultimate-parent-company/2/deletion-confirmation", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ultimateParentCompany.routes.DeemedParentReviewAnswersListController.onPageLoad().url)
              )

              val updatedAnswers = getAnswers(emptyUserAnswers.id).fold(emptyUserAnswers)(x => x)
              updatedAnswers.getList(DeemedParentPage) shouldBe Seq(deemedParentModelUkCompany, deemedParentModelUkCompany)
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/ultimate-parent-company/2/deletion-confirmation", Json.obj("value" -> true))()

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
