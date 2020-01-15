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

import controllers.aboutReturn.{routes => aboutReturnRoutes}
import controllers.routes
import javax.inject.{Inject, Singleton}
import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import models._
import pages._
import pages.aboutReturn._
import play.api.mvc.Call

@Singleton
class AboutReturnNavigator @Inject()() extends BaseNavigator {

  private val normalRoutes: Page => UserAnswers => Call = {
    case RevisingReturnPage => _.get(RevisingReturnPage) match {
      case Some(true) => ??? //TODO: Link to Revision Information Page when implemented
      case Some(false) => aboutReturnRoutes.InfrastructureCompanyElectionController.onPageLoad(NormalMode)
      case _ => aboutReturnRoutes.RevisingReturnController.onPageLoad(NormalMode)
    }
    case InfrastructureCompanyElectionPage => _.get(startReturn.FullOrAbbreviatedReturnPage) match {
      case Some(Full) => aboutReturnRoutes.ReturnContainEstimatesController.onPageLoad(NormalMode)
      case Some(Abbreviated) => ??? //TODO Link to abbreviated return section when implemented
      case _ => aboutReturnRoutes.InfrastructureCompanyElectionController.onPageLoad(NormalMode)
    }
    case ReturnContainEstimatesPage => _ => aboutReturnRoutes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)
    case GroupSubjectToRestrictionsPage => _ => aboutReturnRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
    case GroupSubjectToReactivationsPage => _.get(GroupSubjectToReactivationsPage) match {
      case Some(true) => aboutReturnRoutes.InterestReactivationsCapController.onPageLoad(NormalMode)
      case Some(false) => aboutReturnRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
      case _ => aboutReturnRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
    }
    case InterestReactivationsCapPage => _ => aboutReturnRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
    case InterestAllowanceBroughtForwardPage => _ => aboutReturnRoutes.GroupInterestAllowanceController.onPageLoad(NormalMode)
    case GroupInterestAllowancePage => _ => aboutReturnRoutes.GroupInterestCapacityController.onPageLoad(NormalMode)
    case GroupInterestCapacityPage => _ => nextSection(NormalMode)
    case _ => _ => routes.IndexController.onPageLoad()
  }

  private val checkRouteMap: Page => UserAnswers => Call = {
    _ => _ => routes.CheckYourAnswersController.onPageLoad()
  }

  private def nextSection(mode: Mode): Call = ??? //TODO: Link to About the Elections Section when implemented

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode => checkRouteMap(page)(userAnswers)
  }
}
