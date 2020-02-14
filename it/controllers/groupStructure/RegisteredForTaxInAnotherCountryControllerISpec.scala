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

import assets.{BaseITConstants, PageTitles}
import models.NormalMode
import pages.groupStructure.ParentCompanyNamePage
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class RegisteredForTaxInAnotherCountryControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /group-structure/registered-for-tax-in-another-country" when {

      "user is authorised" should {

        "user answer for parent company name exists" should {

          "return OK (200)" in {

            AuthStub.authorised()
            setAnswers(ParentCompanyNamePage, companyName)

            val res = getRequest("/group-structure/registered-for-tax-in-another-country")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.registeredForTaxInAnotherCountry(companyName))
              )
            }
          }
        }

        "user answer for parent company name does NOT exist" should {

          "return ISE (500)" in {

            AuthStub.authorised()

            val res = getRequest("/group-structure/registered-for-tax-in-another-country")()

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

          val res = getRequest("/group-structure/registered-for-tax-in-another-country")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /group-structure/registered-for-tax-in-another-country" when {

      "user is authorised" when {

        "enters a valid answer of true" should {

          "redirect to Country Of Incorporation page" in {

            AuthStub.authorised()

            val res = postRequest("/group-structure/registered-for-tax-in-another-country", Json.obj("value" -> "true"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CountryOfIncorporationController.onPageLoad(NormalMode).url)
              )
            }
          }
        }

        "enters a valid answer of false" should {

          "redirect to Check Your Answers page" in {

            AuthStub.authorised()

            val res = postRequest("/group-structure/registered-for-tax-in-another-country", Json.obj("value" -> "false"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CheckAnswersGroupStructureController.onPageLoad().url)
              )
            }
          }
        }

        "enters an invalid answer" when {

          "user answer for parent company name exists" should {

            "return a BAD_REQUEST (400)" in {

              AuthStub.authorised()
              setAnswers(ParentCompanyNamePage, companyName)

              val res = postRequest("/group-structure/registered-for-tax-in-another-country", Json.obj("value" -> ""))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(BAD_REQUEST)
                )
              }
            }
          }

          "user answer for parent company name does NOT exist" should {

            "return a ISE (500)" in {

              AuthStub.authorised()

              val res = postRequest("/group-structure/registered-for-tax-in-another-country", Json.obj("value" -> ""))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(INTERNAL_SERVER_ERROR)
                )
              }
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/group-structure/registered-for-tax-in-another-country", Json.obj("value" -> companyName))()

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

    "GET /group-structure/registered-for-tax-in-another-country/change" when {

      "user is authorised" should {

        "user answer for parent company name exists" should {

          "return OK (200)" in {

            AuthStub.authorised()
            setAnswers(ParentCompanyNamePage, companyName)

            val res = getRequest("/group-structure/registered-for-tax-in-another-country/change")()

            whenReady(res) { result =>
              result should have(
                httpStatus(OK),
                titleOf(PageTitles.registeredForTaxInAnotherCountry(companyName))
              )
            }
          }
        }

        "user answer for parent company name does NOT exist" should {

          "return ISE (500)" in {

            AuthStub.authorised()

            val res = getRequest("/group-structure/registered-for-tax-in-another-country/change")()

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

          val res = getRequest("/group-structure/registered-for-tax-in-another-country/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /group-structure/registered-for-tax-in-another-country" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to CheckYourAnswers page" in {

            AuthStub.authorised()

            val res = postRequest("/group-structure/registered-for-tax-in-another-country/change", Json.obj("value" -> "true"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.groupStructure.routes.CheckAnswersGroupStructureController.onPageLoad().url)
              )
            }
          }
        }

        "enters an invalid answer" when {

          "user answer for parent company name exists" should {

            "return a BAD_REQUEST (400)" in {

              AuthStub.authorised()
              setAnswers(ParentCompanyNamePage, companyName)

              val res = postRequest("/group-structure/registered-for-tax-in-another-country/change", Json.obj("value" -> ""))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(BAD_REQUEST)
                )
              }
            }
          }

          "user answer for parent company name does NOT exist" should {

            "return a ISE (500)" in {

              AuthStub.authorised()

              val res = postRequest("/group-structure/registered-for-tax-in-another-country/change", Json.obj("value" -> ""))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(INTERNAL_SERVER_ERROR)
                )
              }
            }
          }
        }

      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/group-structure/registered-for-tax-in-another-country/change", Json.obj("value" -> companyName))()

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