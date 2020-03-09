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

package navigation

import javax.inject.{Inject, Singleton}

import controllers.aboutReportingCompany.{routes => aboutReportingCompanyRoutes}
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import controllers.elections.{routes => electionRoutes}
import controllers.routes
import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import models._
import pages._
import pages.aboutReturn._
import play.api.mvc.Call

@Singleton
class AboutReturnNavigator @Inject()() extends Navigator {

  val normalRoutes: Map[Page, UserAnswers => Call] = Map(
    InfrastructureCompanyElectionPage -> (_.get(startReturn.FullOrAbbreviatedReturnPage) match {
      case Some(Full) => aboutReturnRoutes.ReturnContainEstimatesController.onPageLoad(NormalMode)
      case Some(Abbreviated) => routes.UnderConstructionController.onPageLoad() //TODO Link to abbreviated return section when implemented
      case _ => aboutReturnRoutes.InfrastructureCompanyElectionController.onPageLoad(NormalMode)
    }),
    ReturnContainEstimatesPage -> (_ => aboutReturnRoutes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)),
    GroupSubjectToRestrictionsPage -> (_.get(GroupSubjectToRestrictionsPage) match {
      case Some(true) => aboutReturnRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
      case Some(false) => aboutReturnRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
      case _ => aboutReturnRoutes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)
    }),
    GroupSubjectToReactivationsPage -> (_.get(GroupSubjectToReactivationsPage) match {
      case Some(true) => aboutReturnRoutes.InterestReactivationsCapController.onPageLoad(NormalMode)
      case Some(false) => aboutReturnRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
      case _ => aboutReturnRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
    }),
    InterestReactivationsCapPage -> (_ => aboutReturnRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)),
    InterestAllowanceBroughtForwardPage -> (_ => aboutReturnRoutes.GroupInterestAllowanceController.onPageLoad(NormalMode)),
    GroupInterestAllowancePage -> (_ => aboutReturnRoutes.GroupInterestCapacityController.onPageLoad(NormalMode)),
    GroupInterestCapacityPage -> (_ => nextSection(NormalMode)),
    RevisingReturnPage -> (_.get(RevisingReturnPage) match {
      case Some(true) => routes.UnderConstructionController.onPageLoad() //TODO: Link to Revision Information Page when implemented
      case Some(false) => aboutReportingCompanyRoutes.ReportingCompanyNameController.onPageLoad(NormalMode)
      case _ => aboutReturnRoutes.RevisingReturnController.onPageLoad(NormalMode)
    })
  )

  val checkRouteMap: Map[Page, UserAnswers => Call] = Map().withDefaultValue(_ => ???) //TODO: Add Check Your Answers)

  private def nextSection(mode: Mode): Call = electionRoutes.GroupRatioElectionController.onPageLoad(NormalMode)

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, id: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode => checkRouteMap(page)(userAnswers)
  }
}
