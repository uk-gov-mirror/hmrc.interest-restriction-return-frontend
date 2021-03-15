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

import controllers.elections.{routes => electionsRoutes}
import controllers.ultimateParentCompany.routes
import models._
import pages._
import pages.ultimateParentCompany._
import play.api.mvc.Call

import javax.inject.{Inject, Singleton}

@Singleton
class UltimateParentCompanyNavigator @Inject()() extends Navigator {

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
    ParentCompanyCTUTRPage -> ((idx, _) => checkYourAnswers(idx)),
    ParentCompanySAUTRPage -> ((idx, _) => checkYourAnswers(idx)),
    CountryOfIncorporationPage -> ((idx, _) => checkYourAnswers(idx)),
    CheckAnswersGroupStructurePage -> ((_, userAnswers) => userAnswers.get(HasDeemedParentPage) match {
      case Some(true) =>
        userAnswers.get(DeemedParentPage, Some(2)) match {
          case Some(_) => routes.DeemedParentReviewAnswersListController.onPageLoad()
          case _ => routes.ParentCompanyNameController.onPageLoad(2, NormalMode)
        }
      case Some(false) => nextSection(NormalMode)
      case _ => routes.HasDeemedParentController.onPageLoad(NormalMode)
    }),
    DeemedParentPage -> ((_, _) => nextSection(NormalMode)),
    DeletionConfirmationPage -> ((_, _) => routes.DeemedParentReviewAnswersListController.onPageLoad())
  )

  val checkRouteMap: Map[Page, (Int, UserAnswers) => Call] = Map[Page, (Int, UserAnswers) => Call](
    PayTaxInUkPage -> ((idx, userAnswers) =>
      userAnswers.get(DeemedParentPage, Some(idx)).fold(checkYourAnswers(idx))(deemedParent => {
        deemedParent.payTaxInUk match {
          case payTaxInUk if deemedParent.limitedLiabilityPartnership.isEmpty && payTaxInUk.contains(true) =>
            routes.LimitedLiabilityPartnershipController.onPageLoad(idx, CheckMode)
          case payTaxInUk if deemedParent.countryOfIncorporation.isEmpty && payTaxInUk.contains(false) =>
            routes.CountryOfIncorporationController.onPageLoad(idx, CheckMode)
          case _ => checkYourAnswers(idx)
        }
      })),
    LimitedLiabilityPartnershipPage -> ((idx, userAnswers) =>
      userAnswers.get(DeemedParentPage, Some(idx)).fold(checkYourAnswers(idx))(deemedParent => {
        deemedParent.limitedLiabilityPartnership match {
          case llp if deemedParent.sautr.isEmpty && llp.contains(true) => routes.ParentCompanySAUTRController.onPageLoad(idx, CheckMode)
          case llp if deemedParent.ctutr.isEmpty && llp.contains(false) => routes.ParentCompanyCTUTRController.onPageLoad(idx, CheckMode)
          case _ => checkYourAnswers(idx)
        }
      })),
    ReportingCompanySameAsParentPage -> ((idx, userAnswers) => userAnswers.get(ReportingCompanySameAsParentPage) match {
      case Some(true) => nextSection(NormalMode)
      case Some(false) => {
        val hasDeemedParentPage = userAnswers.get(HasDeemedParentPage)

        if (hasDeemedParentPage.isDefined) checkYourAnswers(idx) else routes.HasDeemedParentController.onPageLoad(NormalMode)
      }
      case _ => checkYourAnswers(idx)
    }),
    HasDeemedParentPage -> ((idx, userAnswers) => hasDeemedParentCheckRoute(idx, userAnswers))
  ).withDefaultValue((idx, _) => checkYourAnswers(idx))

  def nextSection(mode: Mode): Call = electionsRoutes.GroupRatioElectionController.onPageLoad(mode)

  def addParent(numberOfParents: Int): Call = routes.ParentCompanyNameController.onPageLoad(numberOfParents + 1, NormalMode)

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, idx: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(idx.getOrElse[Int](1), userAnswers)
    case CheckMode => checkRouteMap(page)(idx.getOrElse[Int](1), userAnswers)
    case ReviewMode => normalRoutes(page)(idx.getOrElse[Int](1), userAnswers)
  }

  private def hasDeemedParentCheckRoute(idx: Int, userAnswers: UserAnswers): Call = {
    userAnswers.get(ParentCompanyNamePage) match {
      case Some(_) => checkYourAnswers(idx)
      case _ => routes.ParentCompanyNameController.onPageLoad(idx, NormalMode)
    }
  }

  private def checkYourAnswers(idx: Int): Call = routes.CheckAnswersGroupStructureController.onPageLoad(idx)
}
