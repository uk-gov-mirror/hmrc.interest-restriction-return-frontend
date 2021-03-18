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
import models.NormalMode
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import controllers.ultimateParentCompany.{routes => ultimateParentCompanyRoutes}
import pages.ultimateParentCompany.HasDeemedParentPage

class ParentCompanyNameControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /ultimate-parent-company/1/name" when {

      "user is authorised" should {

        "return OK (200) with 1 index" in {

          val pageTitle : String = "What is the name of the first entity in the deemed parent? - Interest Restriction Return - GOV.UK"

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(HasDeemedParentPage, true).get)
          val res = getRequest("/ultimate-parent-company/1/name")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(pageTitle)
            )
          }
        }

        "return OK (200) with 2 index" in {

          val pageTitle : String = "What is the name of the second entity in the deemed parent? - Interest Restriction Return - GOV.UK"

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(HasDeemedParentPage, true).get)
          val res = getRequest("/ultimate-parent-company/2/name")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(pageTitle)
            )
          }
        }

        "return OK (200) with 3 index" in {

          val pageTitle : String =
            "What is the name of the third entity in the deemed parent? - Interest Restriction Return - GOV.UK"

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(HasDeemedParentPage, true).get)
          val res = getRequest("/ultimate-parent-company/3/name")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(pageTitle)
            )
          }
        }

        "return OK (200) with false deemed parent" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(HasDeemedParentPage, false).get)
          val res = getRequest("/ultimate-parent-company/1/name")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.parentCompanyName)
            )
          }
        }

      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/ultimate-parent-company/1/name")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /ultimate-parent-company/1/name" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to PayTaxInUk page" in {

            AuthStub.authorised()
            setAnswers(emptyUserAnswers.set(HasDeemedParentPage, true).get)
            val res = postRequest("/ultimate-parent-company/1/name", Json.obj("value" -> companyName))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(ultimateParentCompanyRoutes.PayTaxInUkController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/ultimate-parent-company/1/name", Json.obj("value" -> companyName))()

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

    "GET /ultimate-parent-company/1/name" when {

      "user is authorised" should {

        "return OK (200) with 1 index" in {

          val pageTitle: String = "What is the name of the first entity in the deemed parent? - Interest Restriction Return - GOV.UK"

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(HasDeemedParentPage, true).get)
          val res = getRequest("/ultimate-parent-company/1/name/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(pageTitle)
            )
          }
        }

        "return OK (200) with 2 index" in {

          val pageTitle: String = "What is the name of the second entity in the deemed parent? - Interest Restriction Return - GOV.UK"

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(HasDeemedParentPage, true).get)
          val res = getRequest("/ultimate-parent-company/2/name/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(pageTitle)
            )
          }
        }

        "return OK (200) with 3 index" in {

          val pageTitle: String = "What is the name of the third entity in the deemed parent? - Interest Restriction Return - GOV.UK"

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(HasDeemedParentPage, true).get)
          val res = getRequest("/ultimate-parent-company/3/name/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(pageTitle)
            )
          }
        }

        "return OK (200) with false deemed parent" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(HasDeemedParentPage, false).get)
          val res = getRequest("/ultimate-parent-company/1/name/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.parentCompanyName)
            )
          }
        }

      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/ultimate-parent-company/1/name/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /ultimate-parent-company/1/name" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to CheckYourAnswers page" in {

            AuthStub.authorised()
            setAnswers(emptyUserAnswers.set(HasDeemedParentPage, true).get)
            val res = postRequest("/ultimate-parent-company/1/name/change", Json.obj("value" -> companyName))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onPageLoad(1).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/ultimate-parent-company/1/name/change", Json.obj("value" -> companyName))()

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
