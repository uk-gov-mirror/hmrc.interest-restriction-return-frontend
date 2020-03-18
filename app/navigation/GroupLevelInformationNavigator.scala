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
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import controllers.groupLevelInformation.{routes => groupLevelInformationRoutes}
import controllers.ukCompanies.{routes => ukCompaniesRoutes}
import controllers.routes
import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import models._
import pages._
import pages.aboutReturn.FullOrAbbreviatedReturnPage
import pages.groupLevelInformation._
import play.api.mvc.Call

@Singleton
class GroupLevelInformationNavigator @Inject()() extends Navigator {

  val normalRoutes: Map[Page, UserAnswers => Call] = Map(
    InfrastructureCompanyElectionPage -> (_.get(FullOrAbbreviatedReturnPage) match {
      case Some(Full) => groupLevelInformationRoutes.ReturnContainEstimatesController.onPageLoad(NormalMode)
      case Some(Abbreviated) => routes.UnderConstructionController.onPageLoad() //TODO Link to abbreviated return section when implemented
      case _ => groupLevelInformationRoutes.InfrastructureCompanyElectionController.onPageLoad(NormalMode)
    }),
    ReturnContainEstimatesPage -> (_ => groupLevelInformationRoutes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)),
    GroupSubjectToRestrictionsPage -> (_.get(GroupSubjectToRestrictionsPage) match {
      case Some(true) => groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
      case Some(false) => groupLevelInformationRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
      case _ => groupLevelInformationRoutes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)
    }),
    GroupSubjectToReactivationsPage -> (_.get(GroupSubjectToReactivationsPage) match {
      case Some(true) => groupLevelInformationRoutes.InterestReactivationsCapController.onPageLoad(NormalMode)
      case Some(false) => groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)
      case _ => groupLevelInformationRoutes.GroupSubjectToReactivationsController.onPageLoad(NormalMode)
    }),
    InterestReactivationsCapPage -> (_ => groupLevelInformationRoutes.InterestAllowanceBroughtForwardController.onPageLoad(NormalMode)),
    InterestAllowanceBroughtForwardPage -> (_ => groupLevelInformationRoutes.GroupInterestAllowanceController.onPageLoad(NormalMode)),
    GroupInterestAllowancePage -> (_ => groupLevelInformationRoutes.GroupInterestCapacityController.onPageLoad(NormalMode)),
    GroupInterestCapacityPage -> (_ => nextSection(NormalMode))
  )

  val checkRouteMap: Map[Page, UserAnswers => Call] =
    Map().withDefaultValue(_ => controllers.routes.UnderConstructionController.onPageLoad()) //TODO: Add Check Your Answers)

  private def nextSection(mode: Mode): Call = ukCompaniesRoutes.AboutAddingUKCompaniesController.onPageLoad()

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, id: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode => checkRouteMap(page)(userAnswers)
  }
}
