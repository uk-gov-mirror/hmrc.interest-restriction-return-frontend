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

import models.UserAnswers
import pages.elections._
import models.returnModels._
import models.IsUKPartnershipOrPreferNotToAnswer._
import pages.Page._
import models.sections.ElectionsSectionModel

object ElectionsSectionStatus extends CurrentSectionStatus[ElectionsSectionModel] {

  val pages = electionsSectionPages
  
  def currentSection(userAnswers: UserAnswers): Option[ElectionsSectionModel] = {
    
    for {
      groupRatioIsElected                     <- userAnswers.get(GroupRatioElectionPage)
      interestAllowanceAlternativeCalcActive  <- userAnswers.get(ElectedInterestAllowanceAlternativeCalcBeforePage)
      nonConsolidatedInvestmentsIsElected     <- userAnswers.get(InterestAllowanceNonConsolidatedInvestmentsElectionPage)
      consolidatedPartnershipsActive          <- userAnswers.get(ElectedInterestAllowanceConsolidatedPshipBeforePage)
    } yield {

      val groupRatioBlended = userAnswers.get(GroupRatioBlendedElectionPage).map { isElected =>
        val investorGroups = userAnswers.getList(InvestorGroupsPage)
        GroupRatioBlendedModel(isElected = isElected, investorGroups = Some(investorGroups))
      }

      val consolidatedPartnerships = userAnswers.get(InterestAllowanceConsolidatedPshipElectionPage).map { isElected =>
        val partnerships = userAnswers.getList(PartnershipsPage)
        ConsolidatedPartnershipModel(isElected = isElected, consolidatedPartnerships = Some(partnerships))
      }

      ElectionsSectionModel(
        groupRatioIsElected = groupRatioIsElected,
        groupRatioBlended = groupRatioBlended,
        groupEBITDAChargeableGainsActive = userAnswers.get(ElectedGroupEBITDABeforePage),
        groupEBITDAChargeableGainsIsElected = userAnswers.get(GroupEBITDAChargeableGainsElectionPage),
        interestAllowanceAlternativeCalcActive = interestAllowanceAlternativeCalcActive,
        interestAllowanceAlternativeCalcIsElected = userAnswers.get(InterestAllowanceAlternativeCalcElectionPage),
        nonConsolidatedInvestmentsIsElected = nonConsolidatedInvestmentsIsElected,
        nonConsolidatedInvestmentNames = nonConsolidatedInvestmentsIsElected match {
          case true => Some(userAnswers.getList(InvestmentNamePage))
          case false => None
        },
        consolidatedPartnershipsActive = consolidatedPartnershipsActive, 
        consolidatedPartnerships = consolidatedPartnerships
      )
    }
  }

  def isComplete(section: ElectionsSectionModel): Boolean = {
    isGroupRatioPathComplete(section) && 
    isAlternativeCalculationPathComplete(section) && 
    nonConsolidatedInvestmentsComplete(section) && 
    isPartnershipElectionComplete(section.consolidatedPartnerships)
  }

  def isGroupRatioPathComplete(section: ElectionsSectionModel): Boolean = section match {
    case ElectionsSectionModel(groupRatio @ false, _, _, _, _, _, _, _, _, _) => true
    case ElectionsSectionModel(groupRatio @ true, Some(GroupRatioBlendedModel(false, _)), ebitdaActive @ Some(true), _, _, _, _, _, _, _) => true
    case ElectionsSectionModel(groupRatio @ true, Some(GroupRatioBlendedModel(false, _)), ebitdaActive @ Some(false), ebitdaElected @ Some(_), _, _, _, _, _, _) => true
    case ElectionsSectionModel(groupRatio @ true, Some(GroupRatioBlendedModel(true, investorGroups)), ebitdaActive @ Some(true), _, _, _, _, _, _, _) => 
      investorGroups.map(_.forall(isInvestorGroupComplete)).getOrElse(true)
    case ElectionsSectionModel(groupRatio @ true, Some(GroupRatioBlendedModel(true, investorGroups)), ebitdaActive @ Some(false), ebitdaElected @ Some(_), _, _, _, _, _, _) => 
      investorGroups.map(_.forall(isInvestorGroupComplete)).getOrElse(true)
    case _ => false
  }

  def isAlternativeCalculationPathComplete(section: ElectionsSectionModel): Boolean = section match {
    case ElectionsSectionModel(_, _, _, _, alternativeCalcActive @ true, _, _, _, _, _) => true
    case ElectionsSectionModel(_, _, _, _, alternativeCalcActive @ false, alternativeCalcIsElected @ Some(_), _, _, _, _) => true
    case _ => false
  }

  def nonConsolidatedInvestmentsComplete(section: ElectionsSectionModel): Boolean = section match {
    case ElectionsSectionModel(_, _, _, _, _, _, nonConsolidatedInvestmentIsElected @ false, _, _, _) => true
    case ElectionsSectionModel(_, _, _, _, _, _, nonConsolidatedInvestmentIsElected @ true, Some(Nil), _, _) => false
    case ElectionsSectionModel(_, _, _, _, _, _, nonConsolidatedInvestmentIsElected @ true, Some(investments), _, _) => true
    case _ => false
  }

  def isPartnershipElectionComplete(consolidatedPartnershipModel: Option[ConsolidatedPartnershipModel]): Boolean = consolidatedPartnershipModel match {
    case Some(ConsolidatedPartnershipModel(isElected @ false, _)) => true
    case Some(ConsolidatedPartnershipModel(isElected @ true, Some(Nil))) => false
    case Some(ConsolidatedPartnershipModel(isElected @ true, Some(partnerships))) => 
      partnerships.forall(isPartnershipComplete)
    case _ => false
  }


  def isInvestorGroupComplete(investorGroup: InvestorGroupModel): Boolean =
    investorGroup match {
      case InvestorGroupModel(name, method @ Some(_), otherElections @ _) => true
      case _ => false
    }

  def isPartnershipComplete(partnership: PartnershipModel): Boolean =
    partnership match {
      case PartnershipModel(name, isUkPartnership @ Some(PreferNotToAnswer), sautr) => true
      case PartnershipModel(name, isUkPartnership @ Some(IsUkPartnership), sautr @ Some(_)) => true
      case PartnershipModel(name, isUkPartnership @ Some(IsNotUkPartnership), sautr @ _) => true
      case _ => false
    }

}