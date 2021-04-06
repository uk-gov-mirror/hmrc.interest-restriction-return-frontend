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
import models.NetTaxInterestIncomeOrExpense.NetTaxInterestExpense

import java.time.{Instant, ZoneOffset}

class UkCompaniesSectionStatusISpec extends IntegrationSpecBase with CreateRequestHelper with CustomMatchers with BaseITConstants {

  implicit val ec: scala.concurrent.ExecutionContext = scala.concurrent.ExecutionContext.global

  "UkCompaniesSectionStatus" should {

    "return InProgress" when {

      "Abbreviated" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/about-the-return/full-or-abbreviated-return", Json.obj("value" -> "abbreviated"))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UkCompaniesSectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

      "Full no reactivations/restrictions" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/about-the-return/full-or-abbreviated-return", Json.obj("value" -> "full"))()
          _ <- postRequest("/group-level-information/group-subject-to-restrictions", Json.obj("value" -> false))()
          _ <- postRequest("/group-level-information/group-subject-to-reactivations", Json.obj("value" -> false))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-qic-election", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/enter-company-tax-ebitda", Json.obj("value" -> "123"))()
          _ <- postRequest("/uk-companies/1/add-net-tax-interest", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UkCompaniesSectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

      "Full with reactivations" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/about-the-return/full-or-abbreviated-return", Json.obj("value" -> "full"))()
          _ <- postRequest("/group-level-information/group-subject-to-restrictions", Json.obj("value" -> false))()
          _ <- postRequest("/group-level-information/group-subject-to-reactivations", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-qic-election", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/enter-company-tax-ebitda", Json.obj("value" -> "123"))()
          _ <- postRequest("/uk-companies/1/add-net-tax-interest", Json.obj("value" -> false))()
          _ <- postRequest("/uk-companies/1/company-contains-estimates", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UkCompaniesSectionStatus.getStatus(userAnswers) shouldEqual InProgress
      }

      "Full with restrictions" in {
        AuthStub.authorised()

        val now = Instant.now().atOffset(ZoneOffset.UTC).toLocalDate

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/about-the-return/full-or-abbreviated-return", Json.obj("value" -> "full"))()
          _ <- postRequest("/about-the-return/accounting-period",
              Json.obj("startValue.day" -> now.getDayOfMonth,
                "startValue.month" -> now.getMonthValue,
                "startValue.year" -> now.getYear,
                "endValue.day" -> now.plusDays(1).getDayOfMonth,
                "endValue.month" -> now.plusDays(1).getMonthValue,
                "endValue.year" -> now.plusDays(1).getYear))()
          _ <- postRequest("/group-level-information/group-subject-to-restrictions", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-qic-election", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/enter-company-tax-ebitda", Json.obj("value" -> 123))()
          _ <- postRequest("/uk-companies/1/add-net-tax-interest", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/net-tax-interest-income-or-expense", Json.obj("value" -> NetTaxInterestExpense.toString))()
          _ <- postRequest("/uk-companies/1/net-tax-interest-amount", Json.obj("value" -> 123))()
          _ <- postRequest("/uk-companies/1/add-restriction", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-accounting-period-same-as-group", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-contains-estimates", Json.obj("value" -> false))()
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
          _ <- postRequest("/about-the-return/full-or-abbreviated-return", Json.obj("value" -> "abbreviated"))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-qic-election", Json.obj("value" -> true))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get

        UkCompaniesSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Full no reactivations/restrictions" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/about-the-return/full-or-abbreviated-return", Json.obj("value" -> "full"))()
          _ <- postRequest("/group-level-information/group-subject-to-restrictions", Json.obj("value" -> false))()
          _ <- postRequest("/group-level-information/group-subject-to-reactivations", Json.obj("value" -> false))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-qic-election", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/enter-company-tax-ebitda", Json.obj("value" -> "123"))()
          _ <- postRequest("/uk-companies/1/add-net-tax-interest", Json.obj("value" -> false))()
          _ <- postRequest("/uk-companies/1/company-contains-estimates", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UkCompaniesSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Full with reactivations" in {
        AuthStub.authorised()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/about-the-return/full-or-abbreviated-return", Json.obj("value" -> "full"))()
          _ <- postRequest("/group-level-information/group-subject-to-restrictions", Json.obj("value" -> false))()
          _ <- postRequest("/group-level-information/group-subject-to-reactivations", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-qic-election", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/enter-company-tax-ebitda", Json.obj("value" -> "123"))()
          _ <- postRequest("/uk-companies/1/add-net-tax-interest", Json.obj("value" -> false))()
          _ <- postRequest("/uk-companies/1/add-an-reactivation-query", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/reactivation-amount", Json.obj("value" -> "123"))()
          _ <- postRequest("/uk-companies/1/company-contains-estimates", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get
        UkCompaniesSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

      "Full with restrictions" in {
        AuthStub.authorised()

        val start = Instant.now().atOffset(ZoneOffset.UTC).toLocalDateTime()

        setAnswers(emptyUserAnswers)

        val resultUserAnswers = for {
          _ <- postRequest("/about-the-return/full-or-abbreviated-return", Json.obj("value" -> "full"))()
          _ <- postRequest("/about-the-return/accounting-period",
              Json.obj("startValue.day" -> start.getDayOfMonth,
                "startValue.month" -> start.getMonthValue,
                "startValue.year" -> start.getYear,
                "endValue.day" -> start.plusDays(1).getDayOfMonth,
                "endValue.month" -> start.plusDays(1).getMonthValue,
                "endValue.year" -> start.plusDays(1).getYear))()
          _ <- postRequest("/group-level-information/group-subject-to-restrictions", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-details", Json.obj("companyNameValue" -> "Company 1", "ctutrValue" -> 1123456789))()
          _ <- postRequest("/uk-companies/1/consenting-company", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-qic-election", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/enter-company-tax-ebitda", Json.obj("value" -> 123))()
          _ <- postRequest("/uk-companies/1/add-net-tax-interest", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/net-tax-interest-income-or-expense", Json.obj("value" -> NetTaxInterestExpense.toString))()
          _ <- postRequest("/uk-companies/1/net-tax-interest-amount", Json.obj("value" -> 123))()
          _ <- postRequest("/uk-companies/1/add-restriction", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/company-accounting-period-same-as-group", Json.obj("value" -> true))()
          _ <- postRequest("/uk-companies/1/restriction-amount-same-accounting-period", Json.obj("value" -> 1.01))()
          _ <- postRequest("/uk-companies/1/company-contains-estimates", Json.obj("value" -> false))()
          userAnswers <- getAnswersFuture("id")
        } yield userAnswers

        val userAnswers = Await.result(resultUserAnswers, 2.seconds).get

        UkCompaniesSectionStatus.getStatus(userAnswers) shouldEqual Completed
      }

    }
  }
}