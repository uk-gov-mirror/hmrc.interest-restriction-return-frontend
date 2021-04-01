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

class UkCompaniesSectionStatusISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  "UkCompaniesSectionStatus" should {

    "return InProgress" when {

      "Abbreviated" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/about-return/full-or-abbreviated-return", Json.obj("value" -> "abbreviated"))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UkCompaniesSectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

    }

    "return Completed" when {

      "Abbreviated" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/about-return/full-or-abbreviated-return", Json.obj("value" -> "abbreviated"))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-qic-election", Json.obj("value" -> true))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UkCompaniesSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

    }
  }
}