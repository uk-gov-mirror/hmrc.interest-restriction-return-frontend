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

package assets.constants

import assets.constants.InvestorGroupConstants.investorGroupsGroupRatioModel
import assets.constants.NonConsolidatedInvestmentConstants.investmentName
import assets.constants.PartnershipsConstants.partnershipModelUK
import pages.elections._
import views.behaviours.ViewBehaviours

trait ElectionsCheckYourAnswersConstants extends ViewBehaviours with BaseConstants {

  val allElections = emptyUserAnswers
    .set(GroupRatioElectionPage, true).get
    .set(EnterANGIEPage, angie).get
    .set(EnterQNGIEPage, qngie).get
    .set(GroupEBITDAPage, ebitda).get
    .set(GroupRatioPercentagePage, groupRatioPercentage).get
    .set(GroupRatioBlendedElectionPage, true).get
    .set(AddInvestorGroupPage, true).get
    .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).get
    .set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(2)).get
    .set(ElectedGroupEBITDABeforePage, false).get
    .set(GroupEBITDAChargeableGainsElectionPage, true).get
    .set(ElectedInterestAllowanceAlternativeCalcBeforePage, false).get
    .set(InterestAllowanceAlternativeCalcElectionPage, true).get
    .set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, true).get
    .set(InvestmentNamePage, investmentName, Some(1)).get
    .set(InvestmentNamePage, investmentName, Some(2)).get
    .set(InvestmentNamePage, investmentName, Some(3)).get
    .set(ElectedInterestAllowanceConsolidatedPshipBeforePage, false).get
    .set(InterestAllowanceConsolidatedPshipElectionPage, true).get
    .set(PartnershipsPage, partnershipModelUK, Some(1)).get
    .set(PartnershipsPage, partnershipModelUK, Some(2)).get
    .set(PartnershipsPage, partnershipModelUK, Some(3)).get
    .set(PartnershipsPage, partnershipModelUK, Some(4)).get


  val minElections = emptyUserAnswers
    .set(GroupRatioElectionPage, false).get
    .set(EnterANGIEPage, angie).get
    .set(ElectedInterestAllowanceAlternativeCalcBeforePage, true).get
    .set(InterestAllowanceNonConsolidatedInvestmentsElectionPage, false).get
    .set(ElectedInterestAllowanceConsolidatedPshipBeforePage, true).get

}
