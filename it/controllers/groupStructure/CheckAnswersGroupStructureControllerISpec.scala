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

package controllers.groupStructure

import assets.DeemedParentITConstants._
import assets.{BaseITConstants, PageTitles}
import models.NormalMode
import pages.groupStructure.{DeemedParentPage, HasDeemedParentPage}
import play.api.http.Status._
import play.api.libs.json.{JsString, Json}
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class CheckAnswersGroupStructureControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {
    
    "GET /group-structure/parent-company/1/check-answers" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/group-structure/parent-company/1/check-answers")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.checkAnswersGroupStructure)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/group-structure/parent-company/1/check-answers")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /group-structure/parent-company/1/check-answers" when {

      "user is authorised" when {

        "deemed parent answer is true" must {

          "enters a valid answer" when {

            "redirect to Revising Return page" in {

              AuthStub.authorised()

              val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, true).success.value
                  .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).success.value

              setAnswers(userAnswers)

              val res = postRequest("/group-structure/parent-company/1/check-answers", JsString(""))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(routes.DeemedParentReviewAnswersListController.onPageLoad().url)
                )
              }
            }
          }
        }

        "deemed parent answer is false" must {

          "enters a valid answer" when {

            "redirect to Revising Return page" in {

              AuthStub.authorised()

              setAnswers(HasDeemedParentPage, false)

              val res = postRequest("/group-structure/parent-company/1/check-answers", JsString(""))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(controllers.aboutReturn.routes.RevisingReturnController.onPageLoad(NormalMode).url)
                )
              }
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/group-structure/parent-company/1/check-answers", JsString(""))()

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
