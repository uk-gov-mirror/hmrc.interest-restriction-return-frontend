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

package controllers.ukCompanies

import assets.{BaseITConstants, PageTitles}
import assets.UkCompanyITConstants._
import pages.ukCompanies.UkCompaniesPage
import play.api.http.Status._
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import views.ViewUtils.addPossessive

class CheckAnswersUkCompanyControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /uk-companies/1/check-answers-uk-company" when {

      "user is authorised" should {

        "return OK (200)" in {

          lazy val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1)).success.value

          setAnswers(userAnswers)

          AuthStub.authorised()
          val res = getRequest("/uk-companies/1/check-answers-uk-company")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.checkAnswersUkCompany(addPossessive(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)))
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies/1/check-answers-uk-company")()

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
