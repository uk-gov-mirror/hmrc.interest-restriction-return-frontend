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

package controllers.groupLevelInformation

import assets.{BaseITConstants, PageTitles}
import models.NormalMode
import pages.elections.GroupRatioElectionPage
import pages.groupLevelInformation.{EnterANGIEPage, GroupInterestAllowancePage, GroupInterestCapacityPage, GroupSubjectToReactivationsPage, GroupSubjectToRestrictionsPage, InterestAllowanceBroughtForwardPage, InterestReactivationsCapPage, ReturnContainEstimatesPage}
import play.api.http.Status._
import play.api.libs.json.Json
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

class GroupSubjectToReactivationsControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  "in Normal mode" when {

    "GET /group-level-information/group-subject-to-reactivations" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/group-level-information/group-subject-to-reactivations")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.groupSubjectToReactivations)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/group-level-information/group-subject-to-reactivations")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /group-level-information/group-subject-to-reactivations" when {

      "user is authorised" when {

        "enters a valid answer" when {

          "redirect to InterestReactivationsCap page when answered true" in {

            AuthStub.authorised()

            val res = postRequest("/group-level-information/group-subject-to-reactivations", Json.obj("value" -> "true"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.groupLevelInformation.routes.InterestReactivationsCapController.onPageLoad(NormalMode).url)
              )
            }
          }

          "redirect to InterestAllowanceBroughtForward page when answered false" in {

            AuthStub.authorised()

            val res = postRequest("/group-level-information/group-subject-to-reactivations", Json.obj("value" -> "false"))()

            whenReady(res) { result =>
              result should have(
                httpStatus(SEE_OTHER),
                redirectLocation(controllers.groupLevelInformation.routes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode).url)
              )
            }
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/group-level-information/group-subject-to-reactivations", Json.obj("value" -> "true"))()

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

    "GET /group-level-information/group-subject-to-reactivations" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()

          val res = getRequest("/group-level-information/group-subject-to-reactivations/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.groupSubjectToReactivations)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/group-level-information/group-subject-to-reactivations/change")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "User selects `false`" when {
      "section is complete it should redirect back to CYA" in {
        AuthStub.authorised()

        setAnswers(
          emptyUserAnswers.set(GroupRatioElectionPage, false).get
            .set(GroupSubjectToRestrictionsPage, false).get
            .set(GroupSubjectToReactivationsPage, false).get
            .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
            .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
            .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
            .set(EnterANGIEPage, BigDecimal(123.12)).get
            .set(ReturnContainEstimatesPage, false).get
        )

        val res = postRequest("/group-level-information/group-subject-to-reactivations/change", Json.obj("value" -> "false"))()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.groupLevelInformation.routes.CheckAnswersGroupLevelController.onPageLoad().url)
          )
        }
      }

      "section is not complete it should redirect back to interest allowance brought forward" in {
        AuthStub.authorised()

        setAnswers(
          emptyUserAnswers.set(GroupRatioElectionPage, false).get
            .set(GroupSubjectToRestrictionsPage, false).get
            .set(GroupSubjectToReactivationsPage, false).get
        )

        val res = postRequest("/group-level-information/group-subject-to-reactivations/change", Json.obj("value" -> "false"))()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.groupLevelInformation.routes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode).url)
          )
        }
      }
    }

    "User selects `true`" when {
      "section is complete it should redirect back to CYA" in {
        AuthStub.authorised()

        setAnswers(
          emptyUserAnswers.set(GroupRatioElectionPage, false).get
            .set(GroupSubjectToRestrictionsPage, false).get
            .set(GroupSubjectToReactivationsPage, true).get
            .set(InterestReactivationsCapPage, BigDecimal(123.12)).get
            .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
            .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
            .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
            .set(EnterANGIEPage, BigDecimal(123.12)).get
            .set(ReturnContainEstimatesPage, false).get
        )

        val res = postRequest("/group-level-information/group-subject-to-reactivations/change", Json.obj("value" -> "true"))()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.groupLevelInformation.routes.CheckAnswersGroupLevelController.onPageLoad().url)
          )
        }
      }

      "section is not complete it should redirect to CAP restrictions" in {
        AuthStub.authorised()

        setAnswers(
          emptyUserAnswers.set(GroupRatioElectionPage, false).get
            .set(GroupSubjectToRestrictionsPage, false).get
            .set(GroupSubjectToReactivationsPage, true).get
        )

        val res = postRequest("/group-level-information/group-subject-to-reactivations/change", Json.obj("value" -> "true"))()

        whenReady(res) { result =>
          result should have(
            httpStatus(SEE_OTHER),
            redirectLocation(controllers.groupLevelInformation.routes.InterestReactivationsCapController.onPageLoad(NormalMode).url)
          )
        }
      }
    }
  }
}
