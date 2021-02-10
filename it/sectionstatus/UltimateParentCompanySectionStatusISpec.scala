/*
 * Copyright 2021 HM Revenue & Customs
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

package sectionstatus

import models.SectionStatus._
import assets.BaseITConstants
import utils.{CreateRequestHelper, CustomMatchers, IntegrationSpecBase}
import stubs.AuthStub
import play.api.libs.json._
import scala.concurrent.duration._
import scala.concurrent.Await

class UltimateParentCompanySectionStatusISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  "UltimateParentCompanySectionStatus" should {

    "return InProgress" when {
      "has deemed parents is set to false and all but one ultimate parent info are entered for a UK LLP" in {

        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/ultimate-parent-company/reporting-company-same-as-parent", Json.obj("value" -> "false"))()
          _ <- postRequest("/ultimate-parent-company/deemed-parent", Json.obj("value" -> false))()
          _ <- postRequest("/ultimate-parent-company/1/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/1/pay-tax-in-uk", Json.obj("value" -> "true"))()
          _ <- postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

      "has deemed parents is set to true and only one deemed parent is entered" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/ultimate-parent-company/reporting-company-same-as-parent", Json.obj("value" -> "false"))()
          _ <- postRequest("/ultimate-parent-company/deemed-parent", Json.obj("value" -> true))()
          _ <- postRequest("/ultimate-parent-company/1/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/1/pay-tax-in-uk", Json.obj("value" -> "true"))()
          _ <- postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()
          _ <- postRequest("/ultimate-parent-company/1/sautr", Json.obj("value" -> "1111111111"))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UltimateParentCompanySectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

      "has deemed parents is set to true and three deemed parents are added but data is missing for the third" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/ultimate-parent-company/reporting-company-same-as-parent", Json.obj("value" -> "false"))()
          _ <- postRequest("/ultimate-parent-company/deemed-parent", Json.obj("value" -> true))()
          _ <- postRequest("/ultimate-parent-company/1/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/1/pay-tax-in-uk", Json.obj("value" -> "true"))()
          _ <- postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()
          _ <- postRequest("/ultimate-parent-company/1/sautr", Json.obj("value" -> "1111111111"))()
          _ <- postRequest("/ultimate-parent-company/2/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/2/pay-tax-in-uk", Json.obj("value" -> "true"))()
          _ <- postRequest("/ultimate-parent-company/2/limited-liability-partnership", Json.obj("value" -> false))()
          _ <- postRequest("/ultimate-parent-company/2/ctutr", Json.obj("value" -> "1111111111"))()
          _ <- postRequest("/ultimate-parent-company/3/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/3/pay-tax-in-uk", Json.obj("value" -> "false"))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UltimateParentCompanySectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }
    }
    
    "return Completed" when {
      "has deemed parents is set to false and all ultimate parent info is entered for a UK LLP" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/ultimate-parent-company/reporting-company-same-as-parent", Json.obj("value" -> "false"))()
          _ <- postRequest("/ultimate-parent-company/deemed-parent", Json.obj("value" -> false))()
          _ <- postRequest("/ultimate-parent-company/1/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/1/pay-tax-in-uk", Json.obj("value" -> "true"))()
          _ <- postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()
          _ <- postRequest("/ultimate-parent-company/1/sautr", Json.obj("value" -> "1111111111"))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UltimateParentCompanySectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "has deemed parents is set to true and two deemed parents are added" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/ultimate-parent-company/reporting-company-same-as-parent", Json.obj("value" -> "false"))()
          _ <- postRequest("/ultimate-parent-company/deemed-parent", Json.obj("value" -> true))()
          _ <- postRequest("/ultimate-parent-company/1/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/1/pay-tax-in-uk", Json.obj("value" -> "true"))()
          _ <- postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()
          _ <- postRequest("/ultimate-parent-company/1/sautr", Json.obj("value" -> "1111111111"))()
          _ <- postRequest("/ultimate-parent-company/2/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/2/pay-tax-in-uk", Json.obj("value" -> "true"))()
          _ <- postRequest("/ultimate-parent-company/2/limited-liability-partnership", Json.obj("value" -> false))()
          _ <- postRequest("/ultimate-parent-company/2/ctutr", Json.obj("value" -> "1111111111"))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UltimateParentCompanySectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "has deemed parents is set to true and three deemed parents are added" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/ultimate-parent-company/reporting-company-same-as-parent", Json.obj("value" -> "false"))()
          _ <- postRequest("/ultimate-parent-company/deemed-parent", Json.obj("value" -> true))()
          _ <- postRequest("/ultimate-parent-company/1/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/1/pay-tax-in-uk", Json.obj("value" -> "true"))()
          _ <- postRequest("/ultimate-parent-company/1/limited-liability-partnership", Json.obj("value" -> true))()
          _ <- postRequest("/ultimate-parent-company/1/sautr", Json.obj("value" -> "1111111111"))()
          _ <- postRequest("/ultimate-parent-company/2/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/2/pay-tax-in-uk", Json.obj("value" -> "true"))()
          _ <- postRequest("/ultimate-parent-company/2/limited-liability-partnership", Json.obj("value" -> false))()
          _ <- postRequest("/ultimate-parent-company/2/ctutr", Json.obj("value" -> "1111111111"))()
          _ <- postRequest("/ultimate-parent-company/3/name", Json.obj("value" -> companyName))()
          _ <- postRequest("/ultimate-parent-company/3/pay-tax-in-uk", Json.obj("value" -> "false"))()
          _ <- postRequest("/ultimate-parent-company/3/country-of-incorporation", Json.obj("value" -> countryOfIncorporation.country))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UltimateParentCompanySectionStatus.getStatus(userAnswers) shouldEqual Completed
      }
    }
  }
}