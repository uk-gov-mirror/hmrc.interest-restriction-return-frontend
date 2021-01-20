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

package assets.messages

object CheckAnswersElectionsMessages {

  val heading = "Check your answers for this section"

  val groupRatioElection = "Group ratio election"
  val groupRatioBlended = "Blended group ratio election"
  val investorGroupsHeading = "Investor Groups"
  val investorGroupsValue: Int => String = i =>  s"$i investor group${if(i > 1) "s" else ""} added"
  val investorGroupsReview = "Review"
  val electedGroupEBITDABefore = "Previous group-EBITDA (chargeable gains) election"
  val groupEBITDAElection = "Make group-EBITDA (chargeable gains) election"
  val electedInterestAllowanceAlternativeCalcBefore = "Make interest allowance (alternative calculation) election"
  val interestAllowanceAlternativeCalcElection = "Elect interest allowance (alternative calculation)"
  val interestAllowanceNonConsolidatedElection = "Elect interest allowance (non-consolidated investments)"
  val nonConsolidatedInvestmentsHeading = "Investments"
  val nonConsolidatedInvestmentsValue: Int => String = i =>  s"$i investment${if(i > 1) "s" else ""} added"
  val nonConsolidatedInvestmentsReview = "Review"
  val electedInterestAllowanceConsolidatedPshipBefore = "Elected interest allowance (consolidated partnerships) before"
  val interestAllowanceConsolidatedPshipElection = "Elect interest allowance (consolidated partnerships)"
  val qicElection = "Qualified Infrastructure Election"
  val consolidatedPartnershipsHeading = "Partnerships"
  val consolidatedPartnershipsValue: Int => String = i =>  s"$i partnership${if(i > 1) "s" else ""} added"
  val consolidatedPartnershipsReview = "Review"

}
