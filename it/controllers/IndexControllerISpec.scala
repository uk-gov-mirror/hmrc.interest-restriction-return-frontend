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

import assets.BaseITConstants
import models.NormalMode
import pages.startReturn.ReportingCompanyAppointedPage
import play.api.http.Status._
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class IndexControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val ackRef = "abc123"

  "GET /" when {

    "user is authorised" when {

      "return SEE_OTHER (303)" in {

        AuthStub.authorised()

        val res = getRequest("/")()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.startReturn.routes.ReportingCompanyAppointedController.onPageLoad(NormalMode).url)
          )
        }
      }

      "there are existing user answers" should {

        "return SEE_OTHER (303)" in {

          AuthStub.authorised()

          setAnswers(ReportingCompanyAppointedPage, true)

          val res = getRequest("/")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.routes.ContinueSavedReturnController.onPageLoad().url)
            )
          }
        }
      }
    }

    "user not authorised" should {

      "return SEE_OTHER (303)" in {

        AuthStub.unauthorised()

        val res = getRequest("/")()

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
