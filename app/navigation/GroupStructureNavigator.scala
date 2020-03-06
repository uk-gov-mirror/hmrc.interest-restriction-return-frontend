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

import controllers.elections.{routes => electionsRoutes}
import controllers.groupStructure.routes
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.groupStructure._
import play.api.mvc.Call

@Singleton
class GroupStructureNavigator @Inject()() extends Navigator {

  //TODO update with next page
  val normalRoutes: Map[Page, (Int, UserAnswers) => Call] = Map(
    ReportingCompanySameAsParentPage -> ((_, userAnswers) => userAnswers.get(ReportingCompanySameAsParentPage) match {
      case Some(false) => routes.HasDeemedParentController.onPageLoad(NormalMode)
      case Some(true) => nextSection(NormalMode)
      case _ => routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
    }),
    HasDeemedParentPage -> ((idx, _) => routes.ParentCompanyNameController.onPageLoad(idx, NormalMode)),
    ParentCompanyNamePage -> ((idx, _) => routes.PayTaxInUkController.onPageLoad(idx, NormalMode)),
    PayTaxInUkPage -> ((idx, userAnswers) => userAnswers.get(DeemedParentPage, Some(idx)).flatMap(_.payTaxInUk) match {
      case Some(true) => routes.LimitedLiabilityPartnershipController.onPageLoad(idx, NormalMode)
      case Some(false) => routes.CountryOfIncorporationController.onPageLoad(idx, NormalMode)
      case _ => routes.PayTaxInUkController.onPageLoad(idx, NormalMode)
    }),
    LimitedLiabilityPartnershipPage -> ((idx, userAnswers) => userAnswers.get(DeemedParentPage, Some(idx)).flatMap(_.limitedLiabilityPartnership) match {
      case Some(true) => routes.ParentCompanySAUTRController.onPageLoad(idx, NormalMode)
      case Some(false) => routes.ParentCompanyCTUTRController.onPageLoad(idx, NormalMode)
      case _ => routes.LimitedLiabilityPartnershipController.onPageLoad(idx, NormalMode)
    }),
    ParentCompanyCTUTRPage -> ((idx,_) => checkYourAnswers(idx)),
    ParentCompanySAUTRPage -> ((idx,_) => checkYourAnswers(idx)),
    CountryOfIncorporationPage -> ((idx,_) => checkYourAnswers(idx)),
    CheckAnswersGroupStructurePage -> ((_, userAnswers) => userAnswers.get(HasDeemedParentPage) match {
      case Some(true) => routes.DeemedParentReviewAnswersListController.onPageLoad()
      case Some(false) => nextSection(NormalMode)
      case _ => routes.HasDeemedParentController.onPageLoad(NormalMode)
    }),
    DeletionConfirmationPage -> ((_, _) => routes.DeemedParentReviewAnswersListController.onPageLoad())
  )

  val checkRouteMap: Map[Page, (Int, UserAnswers) => Call] = Map().withDefaultValue((idx, _) => checkYourAnswers(idx))

  private def checkYourAnswers(idx: Int): Call = routes.CheckAnswersGroupStructureController.onPageLoad(idx)

  def nextSection(mode: Mode): Call = electionsRoutes.GroupRatioElectionController.onPageLoad(mode)

  def addParent(numberOfParents: Int): Call = routes.ParentCompanyNameController.onPageLoad(numberOfParents + 1, NormalMode)

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, idx: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(idx.getOrElse[Int](1), userAnswers)
    case CheckMode => checkRouteMap(page)(idx.getOrElse[Int](1), userAnswers)
  }
}
