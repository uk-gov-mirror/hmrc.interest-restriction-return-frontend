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

import assets.DeemedParentITConstants.deemedParentModelNonUkCompany
import assets.{BaseITConstants, PageTitles}
import controllers.ultimateParentCompany.{routes => ultimateParentCompanyRoutes}
import models.{CheckMode, NormalMode}
import models.returnModels.UTRModel
import pages.ultimateParentCompany.DeemedParentPage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class LimitedLiabilityPartnershipControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /ultimate-parent-company/1/limited-liability-partnership" when {

      "user is authorised" should {

        "user answer for parent company name exists" should {

          "return OK (200)" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = getRequest("/ultimate-parent-company/1/limited-liability-partnership")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.limitedLiabilityPartnership)
              )
            }
          }
        }

        "user answer for parent company name doesn't exist" should {

          "return INTERNAL_SERVER_ERROR (500)" in {

            AuthStub.authorised()

            val res = getRequest("/ultimate-parent-company/1/limited-liability-partnership")()

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

          val res = getRequest("/ultimate-parent-company/1/limited-liability-partnership")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /ultimate-parent-company/1/limited-liability-partnership" when {

      "user is authorised" when {

        "enters true" when {

          "redirect to ParentCompanySAUTR page" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(ultimateParentCompanyRoutes.ParentCompanySAUTRController.onPageLoad(1, NormalMode).url)
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

            val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(ultimateParentCompanyRoutes.ParentCompanyCTUTRController.onPageLoad(1, NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()

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

          val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> ""))()

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

    "GET /ultimate-parent-company/1/limited-liability-partnership" when {

      "user is authorised" should {

        "user answer for parent company name exists" should {

          "return OK (200)" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = getRequest("/ultimate-parent-company/1/limited-liability-partnership/change")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.limitedLiabilityPartnership)
              )
            }
          }
        }

        "user answer for parent company name doesn't exist" should {

          "return INTERNAL_SERVER_ERROR (500)" in {

            AuthStub.authorised()

            val res = getRequest("/ultimate-parent-company/1/limited-liability-partnership/change")()

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

          val res = getRequest("/ultimate-parent-company/1/limited-liability-partnership/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /ultimate-parent-company/1/limited-liability-partnership" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to CheckYourAnswers page when user selects `yes` and there is a sautr" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany.copy(sautr = Some(UTRModel("test"))), Some(1)).success.value
            )

            val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership/change", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onPageLoad(1).url)
              )
            }
          }

          "redirect to SAUTR page when user selects `yes` and there is no sautr" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership/change", Json.obj("value" -> true))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ultimateParentCompany.routes.ParentCompanySAUTRController.onPageLoad(1,CheckMode).url)
              )
            }
          }

          "redirect to CheckYourAnswers page when user selects `no` and there is a ctutr" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany.copy(ctutr = Some(UTRModel("test"))), Some(1)).success.value
            )

            val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership/change", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onPageLoad(1).url)
              )
            }
          }

          "redirect to CTUTR page when user selects `no` and there is no ctutr" in {

            AuthStub.authorised()
            setAnswers(
              emptyUserAnswers.set(DeemedParentPage, deemedParentModelNonUkCompany, Some(1)).success.value
            )

            val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership/change", Json.obj("value" -> false))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.ultimateParentCompany.routes.ParentCompanyCTUTRController.onPageLoad(1,CheckMode).url)
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

            val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership/change", Json.obj("value" -> ""))()

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

          val res = postRequest("/ultimate-parent-company/1/limited-liability-partnership/change", Json.obj("value" -> true))()

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
