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

package controllers.reviewAndComplete

import assets.{BaseITConstants, PageTitles}
import models.FullOrAbbreviatedReturn.Full
import models.UserAnswers
import models.returnModels.{AccountingPeriodModel, CompanyNameModel, DeemedParentModel, ReviewAndCompleteModel, UTRModel}
import pages.aboutReturn.{AccountingPeriodPage, AgentActingOnBehalfOfCompanyPage, AgentNamePage, FullOrAbbreviatedReturnPage, ReportingCompanyAppointedPage, ReportingCompanyCTUTRPage, ReportingCompanyNamePage, TellUsWhatHasChangedPage}
import pages.elections.{ElectedInterestAllowanceAlternativeCalcBeforePage, ElectedInterestAllowanceConsolidatedPshipBeforePage, GroupRatioElectionPage, InterestAllowanceConsolidatedPshipElectionPage, InterestAllowanceNonConsolidatedInvestmentsElectionPage}
import pages.groupLevelInformation.{DisallowedAmountPage, EnterANGIEPage, GroupInterestAllowancePage, GroupInterestCapacityPage, GroupSubjectToRestrictionsPage, InterestAllowanceBroughtForwardPage, ReturnContainEstimatesPage, RevisingReturnPage}
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.ultimateParentCompany.{DeemedParentPage, HasDeemedParentPage, ReportingCompanySameAsParentPage}
import play.api.http.Status
import play.api.http.Status._
import play.api.libs.json.{JsString, Json}
import stubs.AuthStub
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}

import java.time.LocalDate

class ReviewAndCompleteControllerISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  val stubFullReturnUrl = "/internal/return/full"
  val successfulResponse = Json.obj("acknowledgementReference" -> "XAIRR00000012345678")


  "in Normal mode" when {

    "GET /review-and-complete" when {

      "user is authorised" should {

        "return OK (200)" in {

          AuthStub.authorised()
          setAnswers(
            emptyUserAnswers.set(ReviewAndCompletePage, ReviewAndCompleteModel()).success.value
          )

          val res = getRequest("/review-and-complete")()

          whenReady(res) { result =>
            result should have(
              httpStatus(OK),
              titleOf(PageTitles.reviewAndComplete)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = getRequest("/review-and-complete")()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.errors.routes.UnauthorisedController.onPageLoad().url)
            )
          }
        }
      }
    }

    "POST /review-and-complete" when {

      "user is authorised" when {

        "redirect to the next page" in {
          AuthStub.authorised()
          stubPost(stubFullReturnUrl,Status.OK, Json.stringify(successfulResponse))

          setAnswers(fullUserAnswers)

          val res = postRequest("/review-and-complete", JsString(""))()

          whenReady(res) { result =>
            result should have(
              httpStatus(SEE_OTHER),
              redirectLocation(controllers.routes.ConfirmationController.onPageLoad().url)
            )
          }
        }
      }

      "user not authorised" should {

        "return SEE_OTHER (303)" in {

          AuthStub.unauthorised()

          val res = postRequest("/review-and-complete", JsString(""))()

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
