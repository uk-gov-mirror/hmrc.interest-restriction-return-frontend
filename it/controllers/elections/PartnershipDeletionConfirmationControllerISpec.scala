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

package controllers.elections

import assets.PartnershipsITConstants._
import assets.{BaseITConstants, PageTitles}
import pages.elections.PartnershipsPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class PartnershipDeletionConfirmationControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /elections/partnership/2/deletion-confirmation" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(emptyUserAnswers
            .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
            .set(PartnershipsPage, partnershipModelNonUk, Some(2)).success.value
            .set(PartnershipsPage, partnershipModelUK, Some(3)).success.value
          )
          
          val res = getRequest("/elections/partnership/2/deletion-confirmation")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.partnershipDeletionConfirmation(partnerName))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/partnership/2/deletion-confirmation")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/partnership/2/deletion-confirmation" when {

      "user is authorised" when {

        "enters a valid answer of 'Yes'" when {

          "delete the selected partnership and redirect to the list view" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
              .set(PartnershipsPage, partnershipModelNonUk, Some(2)).success.value
              .set(PartnershipsPage, partnershipModelUK, Some(3)).success.value
            )

            val res = postRequest("/elections/partnership/2/deletion-confirmation", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.PartnershipsReviewAnswersListController.onPageLoad().url)
              )

              val updatedAnswers = getAnswers(emptyUserAnswers.id).fold(emptyUserAnswers)(x => x)
              updatedAnswers.getList(PartnershipsPage) shouldBe Seq(partnershipModelUK, partnershipModelUK)
            }
          }
        }
        "enters a valid answer of 'No'" when {

          "redirect to Review Answers List page" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
              .set(PartnershipsPage, partnershipModelNonUk, Some(2)).success.value
              .set(PartnershipsPage, partnershipModelUK, Some(3)).success.value
            )

            val res = postRequest("/elections/partnership/2/deletion-confirmation", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.PartnershipsReviewAnswersListController.onPageLoad().url)
              )

              val updatedAnswers = getAnswers(emptyUserAnswers.id).fold(emptyUserAnswers)(x => x)
              updatedAnswers.getList(PartnershipsPage) shouldBe Seq(partnershipModelUK, partnershipModelNonUk, partnershipModelUK)
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/elections/partnership/2/deletion-confirmation", Json.obj("value" -> 1))()

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
