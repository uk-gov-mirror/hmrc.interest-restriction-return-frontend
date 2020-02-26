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

package controllers.aboutReportingCompany

import assets.{BaseITConstants, PageTitles}
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.{AuthStub, CRNValidationStub}
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class ReportingCompanyCRNControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in normal mode" when {

    "GET /reporting-company/crn" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/reporting-company/crn")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.reportingCompanyCRN)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/reporting-company/crn")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /reporting-company/crn" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "NO_CONTENT (204) is returned from CRN validation" should {

            "redirect to CheckYourAnswers page" in {

              AuthStub.authorised()
              CRNValidationStub.validateCrn(NO_CONTENT)

              val res = postRequest("/reporting-company/crn", Json.obj("value" -> crnModel.crn))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(controllers.aboutReportingCompany.routes.CheckAnswersReportingCompanyController.onPageLoad().url)
                )
              }
            }
          }

          "BAD_REQUEST is returned from CRN validation" should {

            "reload page with crn validation error" in {

              AuthStub.authorised()
              CRNValidationStub.validateCrn(BAD_REQUEST)

              val res = postRequest("/reporting-company/crn", Json.obj("value" -> crnModel.crn))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(BAD_REQUEST),
                  titleOf("Error: Company Registration Number (CRN)")
                )
              }
            }
          }

          "another error is returned from CRN validation" should {

            "reload page with crn validation error" in {

              AuthStub.authorised()
              CRNValidationStub.validateCrn(INTERNAL_SERVER_ERROR)

              val res = postRequest("/reporting-company/crn", Json.obj("value" -> crnModel.crn))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(INTERNAL_SERVER_ERROR))
              }
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/reporting-company/crn", Json.obj("value" -> crnModel.crn))()

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

  "in change mode" when {

    "GET /reporting-company/crn" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/reporting-company/crn/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.reportingCompanyCRN)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/reporting-company/crn/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /reporting-company/crn" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "NO_CONTENT (204) is returned from CRN validation" should {

            "redirect to CheckYourAnswers page" in {

              AuthStub.authorised()
              CRNValidationStub.validateCrn(NO_CONTENT)

              val res = postRequest("/reporting-company/crn/change", Json.obj("value" -> crnModel.crn))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(SEE_OTHER),
                  redirectLocation(controllers.aboutReportingCompany.routes.CheckAnswersReportingCompanyController.onPageLoad().url)
                )
              }
            }
          }

          "BAD_REQUEST is returned from CRN validation" should {

            "reload page with crn validation error" in {

              AuthStub.authorised()
              CRNValidationStub.validateCrn(BAD_REQUEST)

              val res = postRequest("/reporting-company/crn/change", Json.obj("value" -> crnModel.crn))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(BAD_REQUEST),
                  titleOf("Error: Company Registration Number (CRN)")
                )
              }
            }
          }

          "another error is returned from CRN validation" should {

            "reload page with crn validation error" in {

              AuthStub.authorised()
              CRNValidationStub.validateCrn(INTERNAL_SERVER_ERROR)

              val res = postRequest("/reporting-company/crn/change", Json.obj("value" -> crnModel.crn))()

              whenReady(res) { result =>
                result should have(
                  httpStatus(INTERNAL_SERVER_ERROR))
              }
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/reporting-company/crn/change", Json.obj("value" -> crnModel.crn))()

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
