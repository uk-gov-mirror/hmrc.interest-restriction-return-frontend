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
import models.NormalMode
import pages.ukCompanies.UkCompaniesPage
import play.api.http.Status._
import play.api.libs.json.Json
import assets.UkCompanyITConstants._
import pages.groupLevelInformation.{GroupSubjectToReactivationsPage, GroupSubjectToRestrictionsPage}
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class ConsentingCompanyControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /uk-companies/1/consenting-company" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get)

          val res = getRequest("/uk-companies/1/consenting-company")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.consentingCompany)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies/1/consenting-company")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /uk-Companies/consenting-company" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to add activation page in subject to reactivation" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
              .set(GroupSubjectToReactivationsPage, true).get
              .set(GroupSubjectToRestrictionsPage, false).get
            )

            val res = postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.AddAnReactivationQueryController.onPageLoad(1,NormalMode).url)
              )
            }
          }

          "redirect to Checkanswers page in subject to restriction" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
              .set(GroupSubjectToRestrictionsPage, true).get
            )

            val res = postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> false))()
            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CheckAnswersUkCompanyController.onPageLoad(1).url)
              )
            }
          }

          "redirect to Checkanswers page in no subject to reactivation or to restriction" in {

            AuthStub.authorised()

            setAnswers(emptyUserAnswers
              .set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get
              .set(GroupSubjectToReactivationsPage, false).get
              .set(GroupSubjectToRestrictionsPage, false).get
            )

            val res = postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> false))()
            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(routes.CheckAnswersUkCompanyController.onPageLoad(1).url)
              )
            }
          }

        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> 1))()

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

    "GET /uk-companies/1/consenting-company" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          setAnswers(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get)

          val res = getRequest("/uk-companies/1/consenting-company/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.consentingCompany)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/uk-companies/1/consenting-company/change")()

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
