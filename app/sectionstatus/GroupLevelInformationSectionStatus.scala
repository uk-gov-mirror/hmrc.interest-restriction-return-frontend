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

import pages.groupLevelInformation._
import pages.elections.GroupRatioElectionPage
import models.sections.{GroupLevelInformationSectionModel, RestrictionReactivationJourneyModel, GroupRatioJourneyModel}
import models._
import pages.Page._

object GroupLevelInformationSectionStatus extends CurrentSectionStatus[GroupLevelInformationSectionModel] {

  val pages = groupLevelInformationSectionPages

  def isComplete(section: GroupLevelInformationSectionModel): Boolean = 
    isSubjectToRestrictionsJourneyComplete(section.restrictionReactivationJourney) && 
    isGroupRatioJourneyComplete(section.groupRatioElection, section.groupRatioJourney)

  def isSubjectToRestrictionsJourneyComplete(journey: RestrictionReactivationJourneyModel): Boolean = journey match {
    case RestrictionReactivationJourneyModel(subjectRestrictions @ true, totalDisallowed @ Some(_), _, _) => true
    case RestrictionReactivationJourneyModel(subjectRestrictions @ false, _, subjectReactivations @ Some(true), cap @ Some(_)) => true
    case RestrictionReactivationJourneyModel(subjectRestrictions @ false, _, subjectReactivations @ Some(false), _) => true
    case _ => false
  }

  def isGroupRatioJourneyComplete(groupRatioElection: Boolean, journey: GroupRatioJourneyModel): Boolean = {
    (groupRatioElection, journey) match {
      case (true, GroupRatioJourneyModel(angie @ _, qngie @ Some(_), groupEBITDA @ Some(_), groupRatioPercentage @ Some(_))) => true
      case (false, GroupRatioJourneyModel(angie @ _, qngie @ _, groupEBITDA @ _, groupRatioPercentage @ _)) => true
      case (_, _) => false
    }
  }
  
  def currentSection(userAnswers: UserAnswers): Option[GroupLevelInformationSectionModel] = 
    for {
      subjectToRestrictions             <- userAnswers.get(GroupSubjectToRestrictionsPage)
      interestAllowanceBroughtForward   <- userAnswers.get(InterestAllowanceBroughtForwardPage)
      interestAllowanceForReturnPeriod  <- userAnswers.get(GroupInterestAllowancePage)
      interestCapacityForReturnPeriod   <- userAnswers.get(GroupInterestCapacityPage)
      angie                             <- userAnswers.get(EnterANGIEPage)
      estimates                         <- userAnswers.get(ReturnContainEstimatesPage)
      groupRatioElection                <- userAnswers.get(GroupRatioElectionPage)
    } yield {

      val restrictionReactivationJourney = RestrictionReactivationJourneyModel(
        subjectToRestrictions   = subjectToRestrictions,
        totalDisallowedAmount   = userAnswers.get(DisallowedAmountPage),
        subjectToReactivations  = userAnswers.get(GroupSubjectToReactivationsPage),
        reactivationCap         = userAnswers.get(InterestReactivationsCapPage)
      )

      val groupRatioJourney = GroupRatioJourneyModel(
        angie                 = angie,
        qngie                 = userAnswers.get(EnterQNGIEPage),
        groupEBITDA           = userAnswers.get(GroupEBITDAPage),
        groupRatioPercentage  = userAnswers.get(GroupRatioPercentagePage)
      )

      GroupLevelInformationSectionModel(
        restrictionReactivationJourney    = restrictionReactivationJourney,
        interestAllowanceBroughtForward   = interestAllowanceBroughtForward,
        interestAllowanceForReturnPeriod  = interestAllowanceForReturnPeriod,
        interestCapacityForReturnPeriod   = interestCapacityForReturnPeriod,
        groupRatioJourney                 = groupRatioJourney,
        estimates                         = estimates,
        groupRatioElection                = groupRatioElection
      )
    }
  
}
