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

package models.returnModels

import java.time.LocalDate
import assets.constants.ReviewAndCompleteConstants._
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json.Json
import pages.aboutReturn._
import pages.groupLevelInformation._
import pages.ultimateParentCompany._
import pages.elections._
import pages.groupLevelInformation._
import pages.ukCompanies._
import models.SectionStatus._
import models.FullOrAbbreviatedReturn.Full
import models.{CompanyDetailsModel, UserAnswers}
import models.returnModels.fullReturn.UkCompanyModel
import assets.constants.BaseConstants

class ReviewAndCompleteModelSpec extends WordSpec with MustMatchers with BaseConstants {

  "ReviewAndCompleteModel" must {

    "correctly write to json" in {

      val expectedValue = reviewAndCompleteJson
      val actualValue = Json.toJson(reviewAndCompleteModel)

      actualValue mustBe expectedValue
    }

    "correctly reads from json" in {

      val expectedValue = reviewAndCompleteModel
      val actualValue = reviewAndCompleteJson.as[ReviewAndCompleteModel]

      actualValue mustBe expectedValue
    }

    "determine current statuses as not started when instantiated at the start of the journey" in {

      val userAnswers = UserAnswers("id")

      val expectedModel = ReviewAndCompleteModel(
        aboutReturnStatus = NotStarted,
        electionsStatus = NotStarted,
        groupLevelInformationStatus = NotStarted,
        ultimateParentCompanyStatus = NotStarted,
        ukCompaniesStatus = NotStarted,
        checkTotalsStatus = NotStarted
      )

      ReviewAndCompleteModel(userAnswers) mustBe expectedModel

    }

    "determine current statuses when instantiated" in {

      val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
      val date = LocalDate.of(2020,1,1)

      val userAnswers = UserAnswers("id")
        .set(ReportingCompanyAppointedPage, true).get
        .set(AgentActingOnBehalfOfCompanyPage, true).get
        .set(AgentNamePage, agentName).get
        .set(FullOrAbbreviatedReturnPage, Full).get
        .set(RevisingReturnPage, true).get
        .set(TellUsWhatHasChangedPage, "Something has changed").get
        .set(ReportingCompanyNamePage, companyNameModel.name).get
        .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
        .set(AccountingPeriodPage, AccountingPeriodModel(date, date.plusMonths(1))).get
        .set(ReportingCompanySameAsParentPage, true).get
        .set(GroupRatioElectionPage, true).get
        .set(GroupSubjectToRestrictionsPage, true).get
        .set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(1)).get

      val expectedModel = ReviewAndCompleteModel(
        aboutReturnStatus = Completed,
        electionsStatus = InProgress,
        groupLevelInformationStatus = InProgress,
        ultimateParentCompanyStatus = Completed,
        ukCompaniesStatus = InProgress,
        checkTotalsStatus = NotStarted
      )

      ReviewAndCompleteModel(userAnswers) mustBe expectedModel

    }
  }
}
