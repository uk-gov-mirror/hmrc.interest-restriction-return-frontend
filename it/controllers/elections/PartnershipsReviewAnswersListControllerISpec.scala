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
import models.NormalMode
import pages.elections.PartnershipsPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class PartnershipsReviewAnswersListControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /elections/partnerships/review" when {

      "user is authorised" when {

        "there are investments in the list" should {

          "return OK (200)" in {

            AuthStub.authorised()

            setAnswers(
              emptyUserAnswers
                .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value
                .set(PartnershipsPage, partnershipModelNonUk, Some(2)).success.value
                .set(PartnershipsPage, partnershipModelUK, Some(3)).success.value
            )

            val res = getRequest("/elections/partnerships/review")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.partnershipsReviewAnswersList(3))
              )
            }
          }
        }

        "there are NO investments in the list" should {

          "return SEE_OTHER (303)" in {

            AuthStub.authorised()

            val res = getRequest("/elections/partnerships/review")()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.PartnershipNameController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/partnerships/review")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /elections/investments" when {

      "user is authorised" should {

        "Add another partnership is true" must {

          "redirect to the next PartnershipName page" in {

            AuthStub.authorised()

            val userAnswers = emptyUserAnswers
              .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value

            setAnswers(userAnswers)

            val res = postRequest("/elections/partnerships/review", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.PartnershipNameController.onPageLoad(2, NormalMode).url)
              )
            }
          }
        }

        "Add another partnership is false" must {

          "redirect to Check Answers page" in {

            AuthStub.authorised()

            val userAnswers = emptyUserAnswers
              .set(PartnershipsPage, partnershipModelUK, Some(1)).success.value

            setAnswers(userAnswers)

            val res = postRequest("/elections/partnerships/review", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CheckAnswersElectionsController.onPageLoad().url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/elections/partnerships/review")()

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
