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

package utils

import base.SpecBase
import models.SectionStatus.{Completed, InProgress, NotStarted}
import models.returnModels.ReviewAndCompleteModel
import pages.aboutReturn.FullOrAbbreviatedReturnPage
import pages.ultimateParentCompany.{HasDeemedParentPage, ReportingCompanySameAsParentPage}
import models.FullOrAbbreviatedReturn._
import assets.constants.ReviewAndCompleteConstants._

class ReviewAndCompleteHelperSpec extends SpecBase with CurrencyFormatter {

  lazy val reviewAndCompleteModel = ReviewAndCompleteModel(
    aboutReturnStatus = NotStarted,
    electionsStatus = InProgress,
    groupLevelInformationStatus = NotStarted,
    ultimateParentCompanyStatus = Completed,
    ukCompaniesStatus = InProgress,
    checkTotalsStatus = Completed
  )

  "ReviewAndCompleteHelper().rows" when {

    "Reporting Company is same as Ultimate Parent" should {

      "return the correct summary list row models" in {

        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get
        val helper = new ReviewAndCompleteHelper()

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnNotStartedRow,
          sameParentCompanyCompletedRow,
          electionsInProgressRow,
          groupLevelInformationNotStartedRow,
          ukCompaniesInProgressRow,
          checkTotalsCompletedRow
        )
      }
    }

    "Reporting Company is not the same as Ultimate Parent" when {

      "Ultimate Parent is deemed" should {

        "return the correct summary list row models" in {

          val userAnswers = emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, true).get

          val helper = new ReviewAndCompleteHelper()

          helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
            aboutReturnNotStartedRow,
            deemedParentCompanyCompletedRow,
            electionsInProgressRow,
            groupLevelInformationNotStartedRow,
            ukCompaniesInProgressRow,
            checkTotalsCompletedRow
          )
        }
      }

      "Ultimate Parent is NOT deemed" should {

        "return the correct summary list row models" in {

          val userAnswers = emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, false).get

          val helper = new ReviewAndCompleteHelper()

          helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
            aboutReturnNotStartedRow,
            ultimateParentCompanyCompletedRow,
            electionsInProgressRow,
            groupLevelInformationNotStartedRow,
            ukCompaniesInProgressRow,
            checkTotalsCompletedRow
          )
        }

      }

      "Abbreviated return journey" should {

        "not return group level information or check totals" in {

          val userAnswers = emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Abbreviated).get

          val helper = new ReviewAndCompleteHelper()

          helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
            aboutReturnNotStartedRow,
            ultimateParentCompanyCompletedRow,
            electionsInProgressRow,
            ukCompaniesInProgressRow
          )
        }

      }

      "Start of a journey, before answering Full/Abbreviated" should {

        "not return group level information or check totals" in {

          val userAnswers = emptyUserAnswers

          val helper = new ReviewAndCompleteHelper()

          helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
            aboutReturnNotStartedRow,
            ultimateParentCompanyCompletedRow,
            electionsInProgressRow,
            ukCompaniesInProgressRow
          )
        }
      }
    }

    "aboutReturnStatus" when {
      "in progress should return the correct action" in {
        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get

        val helper = new ReviewAndCompleteHelper()
        val reviewAndCompleteModel = ReviewAndCompleteModel(aboutReturnStatus = InProgress)

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnInProgressRow,
          ultimateParentCompanyNotStartedRow,
          electionsNotStartedRow,
          groupLevelInformationNotStartedRow,
          ukCompaniesNotStartedRow,
          checkTotalsNotStartedRow
        )
      }

      "completed should return the correct action" in {
        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get

        val helper = new ReviewAndCompleteHelper()
        val reviewAndCompleteModel = ReviewAndCompleteModel(aboutReturnStatus = Completed)

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnCompletedRow,
          ultimateParentCompanyNotStartedRow,
          electionsNotStartedRow,
          groupLevelInformationNotStartedRow,
          ukCompaniesNotStartedRow,
          checkTotalsNotStartedRow
        )
      }
    }

    "ultimateParentStatus" when {
      "in progress should return the correct action" in {
        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get

        val helper = new ReviewAndCompleteHelper()
        val reviewAndCompleteModel = ReviewAndCompleteModel(ultimateParentCompanyStatus = InProgress)

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnNotStartedRow,
          ultimateParentCompanyInProgressRow,
          electionsNotStartedRow,
          groupLevelInformationNotStartedRow,
          ukCompaniesNotStartedRow,
          checkTotalsNotStartedRow
        )
      }
    }

    "electionsStatus" when {
      "in progress should return the correct action" in {
        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get

        val helper = new ReviewAndCompleteHelper()
        val reviewAndCompleteModel = ReviewAndCompleteModel(electionsStatus = InProgress)

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnNotStartedRow,
          ultimateParentCompanyNotStartedRow,
          electionsInProgressRow,
          groupLevelInformationNotStartedRow,
          ukCompaniesNotStartedRow,
          checkTotalsNotStartedRow
        )
      }

      "completed should return the correct action" in {
        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get

        val helper = new ReviewAndCompleteHelper()
        val reviewAndCompleteModel = ReviewAndCompleteModel(electionsStatus = Completed)

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnNotStartedRow,
          ultimateParentCompanyNotStartedRow,
          electionsCompletedRow,
          groupLevelInformationNotStartedRow,
          ukCompaniesNotStartedRow,
          checkTotalsNotStartedRow
        )
      }
    }

    "groupLevelInformationStatus" when {
      "in progress should return the correct action" in {
        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get

        val helper = new ReviewAndCompleteHelper()
        val reviewAndCompleteModel = ReviewAndCompleteModel(groupLevelInformationStatus = InProgress)

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnNotStartedRow,
          ultimateParentCompanyNotStartedRow,
          electionsNotStartedRow,
          groupLevelInformationInProgressRow,
          ukCompaniesNotStartedRow,
          checkTotalsNotStartedRow
        )
      }

      "completed should return the correct action" in {
        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get

        val helper = new ReviewAndCompleteHelper()
        val reviewAndCompleteModel = ReviewAndCompleteModel(groupLevelInformationStatus = Completed)

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnNotStartedRow,
          ultimateParentCompanyNotStartedRow,
          electionsNotStartedRow,
          groupLevelInformationCompletedRow,
          ukCompaniesNotStartedRow,
          checkTotalsNotStartedRow
        )
      }
    }

    "ukCompaniesStatus" when {
      "in progress should return the correct action" in {
        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get

        val helper = new ReviewAndCompleteHelper()
        val reviewAndCompleteModel = ReviewAndCompleteModel(ukCompaniesStatus = InProgress)

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnNotStartedRow,
          ultimateParentCompanyNotStartedRow,
          electionsNotStartedRow,
          groupLevelInformationNotStartedRow,
          ukCompaniesInProgressRow,
          checkTotalsNotStartedRow
        )
      }

      "completed should return the correct action" in {
        val userAnswers = 
          emptyUserAnswers
            .set(FullOrAbbreviatedReturnPage, Full).get
            .set(ReportingCompanySameAsParentPage, true).get

        val helper = new ReviewAndCompleteHelper()
        val reviewAndCompleteModel = ReviewAndCompleteModel(ukCompaniesStatus = Completed)

        helper.rows(reviewAndCompleteModel, userAnswers) mustBe Seq(
          aboutReturnNotStartedRow,
          ultimateParentCompanyNotStartedRow,
          electionsNotStartedRow,
          groupLevelInformationNotStartedRow,
          ukCompaniesCompletedRow,
          checkTotalsNotStartedRow
        )
      }
    }
  }
}
