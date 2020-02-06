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

package controllers

import assets.{BaseITConstants, PageTitles}
import models.NormalMode
import pages.startReturn.ReportingCompanyAppointedPage
import play.api.http.Status._
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class SavedReturnControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val ackRef = "abc123"

  "GET /saved-return" when {

    "user is authorised" when {

      "return OK (200)" in {

        AuthStub.authorised()

        val res = getRequest("/saved-return")()

        whenReady(res) { result =>
          result should have(
            httpStatus(OK),
            titleOf(PageTitles.savedReturn)
          )
        }
      }
    }

    "user not authorised" should {

      "return SEE_OTHER (303)" in {

        AuthStub.unauthorised()

        val res = getRequest("/saved-return")()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
          )
        }
      }
    }
  }

  "GET /continue-from-save" when {

    "user is authorised" when {

      "no previous answers" must {

        "return SEE_OTHER (303)" in {

          AuthStub.authorised()

          val res = getRequest("/continue-from-save")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.startReturn.routes.ReportingCompanyAppointedController.onPageLoad(NormalMode).url)
            )
          }
        }
      }

      "existing previous answers" must {

        "return SEE_OTHER (303)" in {

          AuthStub.authorised()

          setAnswers(ReportingCompanyAppointedPage, true)

          val res = getRequest("/continue-from-save")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.startReturn.routes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode).url)
            )
          }
        }
      }
    }

    "user not authorised" should {

      "return SEE_OTHER (303)" in {

        AuthStub.unauthorised()

        val res = getRequest("/continue-from-save")()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
          )
        }
      }
    }
  }

  "GET /delete-answers" when {

    "user is authorised" when {

      "return SEE_OTHER (303)" in {

        AuthStub.authorised()

        val res = getRequest("/delete-answers")()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.routes.IndexController.onPageLoad().url)
          )
        }
      }
    }

    "user not authorised" should {

      "return SEE_OTHER (303)" in {

        AuthStub.unauthorised()

        val res = getRequest("/delete-answers")()

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
