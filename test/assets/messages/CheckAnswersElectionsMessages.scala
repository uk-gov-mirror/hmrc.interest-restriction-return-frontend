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

package assets.messages

object CheckAnswersElectionsMessages {

  val heading = "Check your answers for this section"

  val groupRatioElection = "Elect group ratio"
  val angie = "ANGIE"
  val qngie = "QNGIE"
  val ebitda = "Group EBITDA"
  val groupRatioPercentage = "Group ratio percentage"
  val groupRatioBlended = "Elect group ratio (blended)"
  val investorGroupsHeading = "Group ratio (blended) investor groups"
  val investorGroupsValue: Int => String = i =>  s"$i investor group${if(i > 1) "s" else ""} added"
  val electedGroupEBITDABefore = "Elected group EBITDA (chargeable gains) before"
  val groupEBITDAElection = "Elect group EBITDA (chargeable gains)"
  val electedInterestAllowanceAlternativeCalcBefore = "Elected interest allowance (alternative calculation) before"
  val interestAllowanceAlternativeCalcElection = "Elect interest allowance (alternative calculation)"
  val interestAllowanceNonConsolidatedElection = "Elect interest allowance (non-consolidated investments)"
  val nonConsolidatedInvestmentsHeading = "Non-consolidated investments"
  val nonConsolidatedInvestmentsValue: Int => String = i =>  s"$i investment${if(i > 1) "s" else ""} added"
  val electedInterestAllowanceConsolidatedPshipBefore = "Elected interest allowance (consolidated partnerships) before"
  val interestAllowanceConsolidatedPshipElection = "Elect interest allowance (consolidated partnerships)"
  val consolidatedPartnershipsHeading = "Consolidated partnerships"
  val consolidatedPartnershipsValue: Int => String = i =>  s"$i partnership${if(i > 1) "s" else ""} added"

}
