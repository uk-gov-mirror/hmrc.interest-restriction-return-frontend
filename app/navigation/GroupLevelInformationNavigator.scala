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

package navigation

import controllers.groupLevelInformation.{routes => groupLevelInformationRoutes}
import controllers.ukCompanies.{routes => ukCompaniesRoutes}
import models.SectionStatus.{InProgress, NotStarted}
import models._
import pages._
import pages.elections.GroupRatioElectionPage
import pages.groupLevelInformation._
import play.api.mvc.Call
import sectionstatus.GroupLevelInformationSectionStatus

import javax.inject.{Inject, Singleton}

@Singleton
class GroupLevelInformationNavigator @Inject()() extends Navigator {

  val normalRoutes: Map[Page, UserAnswers => Call] = Map(
    GroupSubjectToRestrictionsPage -> (_.get(GroupSubjectToRestrictionsPage) match {
      case Some(true) => groupLevelInformationRoutes.DisallowedAmountController.onPageLoad(NormalMode)
      case Some(false) => groupLevelInformationRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
      case _ => groupLevelInformationRoutes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)
    }),
    GroupSubjectToReactivationsPage -> (_.get(GroupSubjectToReactivationsPage) match {
      case Some(true) => groupLevelInformationRoutes.InterestReactivationsCapController.onPageLoad(NormalMode)
      case Some(false) => groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
      case _ => groupLevelInformationRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
    }),
    InterestReactivationsCapPage -> (_ => groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)),
    DisallowedAmountPage -> (_ => groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)),
    InterestAllowanceBroughtForwardPage -> (_ => groupLevelInformationRoutes.GroupInterestAllowanceController.onPageLoad(NormalMode)),
    GroupInterestAllowancePage -> (_ => groupLevelInformationRoutes.GroupInterestCapacityController.onPageLoad(NormalMode)),
    GroupInterestCapacityPage -> (_ => groupLevelInformationRoutes.EnterANGIEController.onPageLoad(NormalMode)),
    EnterANGIEPage -> (_.get(GroupRatioElectionPage) match {
      case Some(true) => groupLevelInformationRoutes.EnterQNGIEController.onPageLoad(NormalMode)
      case _ => groupLevelInformationRoutes.ReturnContainEstimatesController.onPageLoad(NormalMode)
    }),
    EnterQNGIEPage -> (_ => groupLevelInformationRoutes.GroupEBITDAController.onPageLoad(NormalMode)),
    GroupEBITDAPage -> (_ => groupLevelInformationRoutes.GroupRatioPercentageController.onPageLoad(NormalMode)),
    GroupRatioPercentagePage -> (_ => groupLevelInformationRoutes.ReturnContainEstimatesController.onPageLoad(NormalMode)),
    ReturnContainEstimatesPage -> (_.get(ReturnContainEstimatesPage) match {
      case Some(true) => groupLevelInformationRoutes.EstimatedFiguresController.onPageLoad(NormalMode)
      case _ => checkAnswers
    }),
    EstimatedFiguresPage -> (_ => checkAnswers),
    CheckAnswersGroupLevelPage -> (_ => nextSection(NormalMode))
  )

  val checkRouteMap: Map[Page, UserAnswers => Call] =
    Map[Page, UserAnswers => Call](
      GroupSubjectToRestrictionsPage -> groupSubjectToRestrictionsCheckRoute,
      GroupSubjectToReactivationsPage -> groupSubjectToReactivationsCheckRoute,
      ReturnContainEstimatesPage -> (userAnswers => userAnswers.get(ReturnContainEstimatesPage) match {
        case Some(true) => userAnswers.get(EstimatedFiguresPage) match {
          case None => groupLevelInformationRoutes.EstimatedFiguresController.onPageLoad(CheckMode)
          case Some(_) => checkAnswers
        }
        case _ => checkAnswers
      })
    ).withDefaultValue(_ => checkAnswers)

  private def checkAnswers = groupLevelInformationRoutes.CheckAnswersGroupLevelController.onPageLoad()

  private def nextSection(mode: Mode): Call = ukCompaniesRoutes.AboutAddingUKCompaniesController.onPageLoad()

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, id: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode => checkRouteMap(page)(userAnswers)
    case ReviewMode => normalRoutes(page)(userAnswers)
  }

  def groupSubjectToRestrictionsCheckRoute(userAnswers: UserAnswers): Call = {
    val groupSubjectToRestrictionsPage = userAnswers.get(GroupSubjectToRestrictionsPage)
    val groupSubjectToReactivationsNotPopulated = userAnswers.get(GroupSubjectToReactivationsPage).isEmpty
    val disallowedAmountNotPopulated = userAnswers.get(DisallowedAmountPage).isEmpty

    groupSubjectToRestrictionsPage match {
      case Some(true) if disallowedAmountNotPopulated => groupLevelInformationRoutes.DisallowedAmountController.onPageLoad(NormalMode)
      case Some(false) if groupSubjectToReactivationsNotPopulated => groupLevelInformationRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
      case _ => checkAnswers
    }
  }

  def groupSubjectToReactivationsCheckRoute(userAnswers: UserAnswers): Call = {
    val sectionStatus = GroupLevelInformationSectionStatus.getStatus(userAnswers)
    val groupSubjectToReactivationsPage = userAnswers.get(GroupSubjectToReactivationsPage)

    (sectionStatus,groupSubjectToReactivationsPage) match {
      case (NotStarted | InProgress, Some(true)) => groupLevelInformationRoutes.InterestReactivationsCapController.onPageLoad(NormalMode)
      case (NotStarted | InProgress, Some(false)) => groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
      case _ => checkAnswers
    }
  }
}
