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
import models.OtherInvestorGroupElections.GroupEBITDA

class ElectionsSectionStatusISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  "ElectionsSectionStatus" should {

    "return InProgress" when {

      "UK Consolidated partnership is missing SAUTR" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/partnership/1/name", Json.obj("value" -> "Name"))()
          _ <- postRequest("/elections/partnership/1/is-uk", Json.obj("value" -> true))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

      "No active alternative calculation but hasn't answered Alternative Calc Elect" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

      "Answered true for non consolidated investments but none added" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

      "Answered true for consolidated partnerships but none added" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> true))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

      "Answered true for group ratio and false for active EBIDTA but EBIDTA elect not completed" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/group-ratio-blended-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-group-ebitda-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-alternative-calc-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

    }
    
    "return Completed" when {
      "Not group ratio / Active interest allowance (alt) / No non-con investments / No consolidated partnerships" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Group ratio / Not blended / Active EBITDA / Active interest allowance (alt) / Not interest allowance non-con / Consolidated partnerships" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/group-ratio-blended-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-group-ebitda-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Group ratio / Not blended / No active EBITDA / Active interest allowance (alt) / Not interest allowance non-con / Consolidated partnerships" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/group-ratio-blended-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-group-ebitda-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/group-ebitda-chargeable-gains-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Group ratio / Not blended / No active EBITDA / No active interest allowance (alt) / Not interest allowance non-con / Consolidated partnerships" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/group-ratio-blended-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-group-ebitda-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/group-ebitda-chargeable-gains-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-alternative-calc-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Group ratio / Blended / No active EBITDA / No active interest allowance (alt) / Not interest allowance non-con / Consolidated partnerships" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/group-ratio-blended-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/elected-group-ebitda-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/group-ebitda-chargeable-gains-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-alternative-calc-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Group ratio / Blended with investor group / No active EBITDA / No active interest allowance (alt) / Not interest allowance non-con / Consolidated partnerships" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/group-ratio-blended-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/investor-group/1/name", Json.obj("value" -> "Investor Name"))()
          _ <- postRequest("/elections/investor-group/1/ratio-method", Json.obj("value" -> "true"))()
          _ <- postRequest("/elections/investor-group/1/other-elections", Json.obj("value[0]" -> GroupEBITDA.toString))()
          _ <- postRequest("/elections/elected-group-ebitda-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/group-ebitda-chargeable-gains-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-alternative-calc-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Not group ratio / Active interest allowance (alt) / Non-con investments / No consolidated partnerships" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/investment/1/name", Json.obj("value" -> "name"))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Not group ratio / Active interest allowance (alt) / No non-con investments / Consolidated partnerships (UK)" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/partnership/1/name", Json.obj("value" -> "Name"))()
          _ <- postRequest("/elections/partnership/1/is-uk", Json.obj("value" -> true))()
          _ <- postRequest("/elections/partnership/1/sautr", Json.obj("value" -> sautr))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Not group ratio / Active interest allowance (alt) / No non-con investments / Consolidated partnerships (Non UK)" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/elections/group-ratio-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-alternative-calc-before", Json.obj("value" -> true))()
          _ <- postRequest("/elections/interest-allowance-non-consolidated-investments-election", Json.obj("value" -> false))()
          _ <- postRequest("/elections/elected-interest-allowance-consolidated-pship-before", Json.obj("value" -> false))()
          _ <- postRequest("/elections/interest-allowance-consolidated-pship-election", Json.obj("value" -> true))()
          _ <- postRequest("/elections/partnership/1/name", Json.obj("value" -> "Name"))()
          _ <- postRequest("/elections/partnership/1/is-uk", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        ElectionsSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }
    }
  }
}