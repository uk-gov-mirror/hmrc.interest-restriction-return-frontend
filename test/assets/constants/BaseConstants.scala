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

package assets.constants

import models.FullOrAbbreviatedReturn.Full
import models.UserAnswers
import models.returnModels.{AccountingPeriodModel, CRNModel, CompanyNameModel, CountryCodeModel, DeemedParentModel, UTRModel}
import pages.aboutReturn.{AccountingPeriodPage, AgentActingOnBehalfOfCompanyPage, AgentNamePage, FullOrAbbreviatedReturnPage, ReportingCompanyAppointedPage, ReportingCompanyCTUTRPage, ReportingCompanyNamePage, TellUsWhatHasChangedPage}
import pages.elections.{ElectedInterestAllowanceAlternativeCalcBeforePage, ElectedInterestAllowanceConsolidatedPshipBeforePage, GroupRatioElectionPage, InterestAllowanceConsolidatedPshipElectionPage, InterestAllowanceNonConsolidatedInvestmentsElectionPage}
import pages.groupLevelInformation.{DisallowedAmountPage, EnterANGIEPage, GroupInterestAllowancePage, GroupInterestCapacityPage, GroupSubjectToRestrictionsPage, InterestAllowanceBroughtForwardPage, ReturnContainEstimatesPage, RevisingReturnPage}
import pages.ultimateParentCompany.{DeemedParentPage, HasDeemedParentPage, ReportingCompanySameAsParentPage}

import java.time.LocalDate

trait BaseConstants {

  val ctutrModel = UTRModel("9999999999")
  val sautrModel = UTRModel("9999999999")
  val invalidUtr = UTRModel("1999999999")
  val crnModel = CRNModel("12345678")
  val crnLetters = CRNModel("AB123456")
  val companyNameModel = CompanyNameModel("Company Name ltd")
  val companyNameModelS = CompanyNameModel("Company Name ltds")
  val knownAs = "something"
  val companyNameMaxLength = 160
  val companyNameTooLong = CompanyNameModel("a" * (companyNameMaxLength + 1))
  val knownAsTooLong = "a" * (companyNameMaxLength + 1)
  val invalidCrn = CRNModel("AAAA1234")
  val nonUkCrn = "1234567890"
  val nonUkCountryCode = CountryCodeModel("US", "United States of America")
  val invalidCountryCode = CountryCodeModel("AA", "Invalid")
  val agentName = "Agent A"
  val angie = BigDecimal(120000.23)
  val qngie = BigDecimal(240000.99)
  val ebitda = BigDecimal(5000000)
  val groupRatioPercentage = BigDecimal(12)
  val groupInterestAllowance = BigDecimal(9231)
  val groupInterestCapacity = BigDecimal(34567)
  val interestReactivationCap = BigDecimal(8765)
  val interestAllowanceBroughtForward = BigDecimal(76969)
  val disallowedAmount = BigDecimal(134.56)
  val sampleDate = LocalDate.of(2020,1,1)
  val deemedParentModel =
    DeemedParentModel(
      companyName = CompanyNameModel("Name"),
      sautr = Some(UTRModel("1123456789")),
      limitedLiabilityPartnership = Some(true),
      payTaxInUk = Some(true),
      reportingCompanySameAsParent = Some(false)
    )

  lazy val completeUserAnswers = UserAnswers("id")
    .set(ReportingCompanyAppointedPage, true).get
    .set(AgentActingOnBehalfOfCompanyPage, true).get
    .set(AgentNamePage, agentName).get
    .set(FullOrAbbreviatedReturnPage, Full).get
    .set(RevisingReturnPage, true).get
    .set(TellUsWhatHasChangedPage, "Something has changed").get
    .set(ReportingCompanyNamePage, companyNameModel.name).get
    .set(ReportingCompanyCTUTRPage, ctutrModel.utr).get
    .set(AccountingPeriodPage, AccountingPeriodModel(sampleDate, sampleDate.plusMonths(1))).get
    .set(GroupRatioElectionPage, false).get
    .set(GroupSubjectToRestrictionsPage, true).get
    .set(DisallowedAmountPage, BigDecimal(123.12)).get
    .set(InterestAllowanceBroughtForwardPage, BigDecimal(123.12)).get
    .set(GroupInterestAllowancePage, BigDecimal(123.12)).get
    .set(GroupInterestCapacityPage, BigDecimal(123.12)).get
    .set(EnterANGIEPage, BigDecimal(123.12)).get
    .set(ReturnContainEstimatesPage, false).get
    .set(ReportingCompanySameAsParentPage, false).get
    .set(HasDeemedParentPage, false).get
    .set(DeemedParentPage, deemedParentModel, Some(1)).get
    .set(GroupRatioElectionPage, false).get
    .set(ElectedInterestAllowanceAlternativeCalcBeforePage,true).get
    .set(InterestAllowanceNonConsolidatedInvestmentsElectionPage,false).get
    .set(ElectedInterestAllowanceConsolidatedPshipBeforePage,false).get
    .set(InterestAllowanceConsolidatedPshipElectionPage,false).get

}
