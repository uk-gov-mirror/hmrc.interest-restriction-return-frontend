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

import assets.DeemedParentITConstants.deemedParentModelNonUkCompany
import assets.{BaseITConstants, PageTitles}
import controllers.groupStructure.{routes => groupStructureRoutes}
import models.NormalMode
import pages.groupStructure.DeemedParentPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class LimitedLiabilityPartnershipControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /group-structure/parent-company/1/limited-liability-partnership" when {

      "user is authorised" should {

        "user answer for parent company name exists" should {

          "return OK (200)" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = getRequest("/group-structure/parent-company/1/limited-liability-partnership")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.limitedLiabilityPartnership(parentCompanyName.name))
              )
            }
          }
        }

        "user answer for parent company name doesn't exist" should {

          "return INTERNAL_SERVER_ERROR (500)" in {

            AuthStub.authorised()

            val res = getRequest("/group-structure/parent-company/1/limited-liability-partnership")()

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

          val res = getRequest("/group-structure/parent-company/1/limited-liability-partnership")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /group-structure/parent-company/1/limited-liability-partnership" when {

      "user is authorised" when {

        "enters true" when {

          "redirect to ParentCompanySAUTR page" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/group-structure/parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(groupStructureRoutes.ParentCompanySAUTRController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }

        "enters false" when {

          "redirect to ParentCompanyCTUTR page" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/group-structure/parent-company/1/limited-liability-partnership", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(groupStructureRoutes.ParentCompanyCTUTRController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/group-structure/parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }

      "enters an invalid answer" when {

        "return a BAD_REQUEST (400)" in {

          AuthStub.authorised()
          setAnswers(
            emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
          )

          val res = postRequest("/group-structure/parent-company/1/limited-liability-partnership", Json.obj("value" -> ""))()

          whenReady(res) { result =>
            result should have(
              httpStatus(BAD_REQUEST)
            )
          }
        }
      }
    }
  }

  "in Change mode" when {

    "GET /group-structure/parent-company/1/limited-liability-partnership" when {

      "user is authorised" should {

        "user answer for parent company name exists" should {

          "return OK (200)" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = getRequest("/group-structure/parent-company/1/limited-liability-partnership/change")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.limitedLiabilityPartnership(parentCompanyName.name))
              )
            }
          }
        }

        "user answer for parent company name doesn't exist" should {

          "return INTERNAL_SERVER_ERROR (500)" in {

            AuthStub.authorised()

            val res = getRequest("/group-structure/parent-company/1/limited-liability-partnership/change")()

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

          val res = getRequest("/group-structure/parent-company/1/limited-liability-partnership/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /group-structure/parent-company/1/limited-liability-partnership" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to CheckYourAnswers page" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/group-structure/parent-company/1/limited-liability-partnership/change", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.groupStructure.routes.CheckAnswersGroupStructureController.onPageLoad(1).url)
              )
            }
          }
        }

        "enters an invalid answer" when {

          "return a BAD_REQUEST (400)" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/group-structure/parent-company/1/limited-liability-partnership/change", Json.obj("value" -> ""))()

            whenReady(res) { result =>
              result should have(
                httpStatus(BAD_REQUEST)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/group-structure/parent-company/1/limited-liability-partnership/change", Json.obj("value" -> true))()

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
