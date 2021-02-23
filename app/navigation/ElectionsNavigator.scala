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

import controllers.elections.routes
import javax.inject.{Inject, Singleton}
import models._
import models.FullOrAbbreviatedReturn._
import pages._
import pages.aboutReturn.FullOrAbbreviatedReturnPage
import pages.elections.{PartnershipNamePage, _}
import play.api.mvc.Call

@Singleton
class ElectionsNavigator @Inject()() extends Navigator {

  //TODO update with next page
  val normalRoutes: Map[Page, (UserAnswers => Call)] = Map(
    GroupRatioElectionPage -> (_.get(GroupRatioElectionPage) match {
      case Some(true) => routes.GroupRatioBlendedElectionController.onPageLoad(NormalMode)
      case Some(false) => routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode)
      case _ => routes.GroupRatioElectionController.onPageLoad(NormalMode)
    }),
    GroupRatioBlendedElectionPage -> (_.get(GroupRatioBlendedElectionPage) match {
      case Some(true) => routes.AddInvestorGroupController.onPageLoad(NormalMode)
      case Some(false) => routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode)
      case _ => routes.GroupRatioBlendedElectionController.onPageLoad(NormalMode)
    }),
    ElectedGroupEBITDABeforePage -> (_.get(ElectedGroupEBITDABeforePage) match {
      case Some(true) => routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode)
      case Some(false) => routes.GroupEBITDAChargeableGainsElectionController.onPageLoad(NormalMode)
      case _ => routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode)
    }),
    GroupEBITDAChargeableGainsElectionPage -> (_ => routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode)),
    ElectedInterestAllowanceAlternativeCalcBeforePage -> (_.get(ElectedInterestAllowanceAlternativeCalcBeforePage) match {
      case Some(true) => routes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(NormalMode)
      case Some(false) => routes.InterestAllowanceAlternativeCalcElectionController.onPageLoad(NormalMode)
      case _ => routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode)
    }),
    InterestAllowanceAlternativeCalcElectionPage -> (_ => routes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(NormalMode)),
    InterestAllowanceNonConsolidatedInvestmentsElectionPage -> (_.get(InterestAllowanceNonConsolidatedInvestmentsElectionPage) match {
      case Some(true) => routes.InvestmentsReviewAnswersListController.onPageLoad()
      case Some(false) => routes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(NormalMode)
      case _ => routes.InterestAllowanceNonConsolidatedInvestmentsElectionController.onPageLoad(NormalMode)
    }),
    ElectedInterestAllowanceConsolidatedPshipBeforePage -> (_ => routes.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(NormalMode)),
    InterestAllowanceConsolidatedPshipElectionPage -> (_.get(InterestAllowanceConsolidatedPshipElectionPage) match {
      case Some(true) => routes.PartnershipsReviewAnswersListController.onPageLoad()
      case Some(false) => checkYourAnswers
      case _ => routes.InterestAllowanceConsolidatedPshipElectionController.onPageLoad(NormalMode)
    }),
    PartnershipsReviewAnswersListPage -> (_ => checkYourAnswers),
    PartnershipDeletionConfirmationPage -> (_ => routes.PartnershipsReviewAnswersListController.onPageLoad()),
    AddInvestorGroupPage -> (_.get(AddInvestorGroupPage) match {
      case Some(true) => routes.InvestorGroupsReviewAnswersListController.onPageLoad()
      case Some(false) => routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode)
      case _ => routes.AddInvestorGroupController.onPageLoad(NormalMode)
    }),
    OtherInvestorGroupElectionsPage -> (_ => routes.InvestorGroupsReviewAnswersListController.onPageLoad()),
    InvestorGroupsPage -> (_ => routes.ElectedGroupEBITDABeforeController.onPageLoad(NormalMode)),
    CheckAnswersElectionsPage -> (userAnswers => nextSection(NormalMode, userAnswers)),
    InvestmentsReviewAnswersListPage -> (_ => routes.ElectedInterestAllowanceConsolidatedPshipBeforeController.onPageLoad(NormalMode)),
    InvestmentsDeletionConfirmationPage -> (_ => routes.InvestmentsReviewAnswersListController.onPageLoad()),
    InvestorGroupsDeletionConfirmationPage -> (_ => routes.InvestorGroupsReviewAnswersListController.onPageLoad())
  )

  val idxRoutes: Map[Page, (Int, UserAnswers) => Call] = Map(
    InvestorGroupNamePage -> ((idx, _) => routes.InvestorRatioMethodController.onPageLoad(idx, NormalMode)),
    InvestorRatioMethodPage -> ((idx, userAnswers) => userAnswers.get(InvestorGroupsPage, Some(idx)) match {
      case Some(investorGroup) =>
        investorGroup.ratioMethod match {
          case Some(investorGroup) if investorGroup == InvestorRatioMethod.PreferNotToAnswer => routes.InvestorGroupsReviewAnswersListController.onPageLoad()
          case Some(_) => routes.OtherInvestorGroupElectionsController.onPageLoad(idx, NormalMode)
          case _ => routes.InvestorRatioMethodController.onPageLoad(idx, NormalMode)
        }
      case _ => routes.InvestorRatioMethodController.onPageLoad(idx, NormalMode)
    }),
    PartnershipNamePage -> ((idx, _) => routes.IsUkPartnershipController.onPageLoad(idx, NormalMode)),
    IsUkPartnershipPage -> ((idx, userAnswers) => userAnswers.get(PartnershipsPage, Some(idx)) match {
      case Some(partnership) =>
        partnership.isUkPartnership match {
          case Some(isUk) if isUk == IsUKPartnershipOrPreferNotToAnswer.IsUkPartnership => routes.PartnershipSAUTRController.onPageLoad(idx, NormalMode)
          case _ => routes.PartnershipsReviewAnswersListController.onPageLoad()
        }
      case _ => routes.IsUkPartnershipController.onPageLoad(idx, NormalMode)
    }),
    PartnershipSAUTRPage -> ((_, _) => routes.PartnershipsReviewAnswersListController.onPageLoad()),
    InvestmentNamePage -> ((_, _) => routes.InvestmentsReviewAnswersListController.onPageLoad())
  )


  val checkRouteMap: Map[Page, UserAnswers => Call] = Map[Page, UserAnswers => Call](
    GroupRatioElectionPage -> (userAnswer => userAnswer.get(GroupRatioElectionPage) match {
      case Some(true) => routes.GroupRatioBlendedElectionController.onPageLoad(NormalMode)
      case Some(false) => routes.ElectedInterestAllowanceAlternativeCalcBeforeController.onPageLoad(NormalMode)
      case _ => routes.GroupRatioElectionController.onPageLoad(NormalMode)
    }),
    GroupRatioBlendedElectionPage -> (userAnswers => userAnswers.get(GroupRatioBlendedElectionPage) match {
      case Some(true) => userAnswers.get(AddInvestorGroupPage) match {
        case Some(_) => checkYourAnswers
        case None => normalRoutes(GroupRatioBlendedElectionPage)(userAnswers)
      }
      case _ => checkYourAnswers
    }),
    ElectedInterestAllowanceAlternativeCalcBeforePage -> (_.get(ElectedInterestAllowanceAlternativeCalcBeforePage) match {
      case Some(false) => routes.InterestAllowanceAlternativeCalcElectionController.onPageLoad(CheckMode)
      case _ => checkYourAnswers
    }),
    InvestmentNamePage -> (_ => routes.InvestmentsReviewAnswersListController.onPageLoad()),
    ElectedGroupEBITDABeforePage -> (userAnswers => userAnswers.get(ElectedGroupEBITDABeforePage) match {
      case Some(false) => routes.GroupEBITDAChargeableGainsElectionController.onPageLoad(CheckMode)
      case _ => checkYourAnswers
    })
  ).withDefaultValue(_ => checkYourAnswers)

  private def checkYourAnswers: Call = routes.CheckAnswersElectionsController.onPageLoad()

  def addInvestment(idx: Int): Call = routes.InvestmentNameController.onPageLoad(idx + 1, NormalMode)

  def addInvestorGroup(idx: Int): Call = routes.InvestorGroupNameController.onPageLoad(idx + 1, NormalMode)

  def addPartnership(idx: Int): Call = routes.PartnershipNameController.onPageLoad(idx + 1, NormalMode)

  def nextSection(mode: Mode, userAnswers: UserAnswers): Call =
    userAnswers.get(FullOrAbbreviatedReturnPage) match {
      case Some(Full) => controllers.groupLevelInformation.routes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode)
      case Some(Abbreviated) => controllers.ukCompanies.routes.AboutAddingUKCompaniesController.onPageLoad()
      case _ => controllers.routes.UnderConstructionController.onPageLoad() // TODO not sure what should happen in this instance
    }

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, id: Option[Int] = None): Call = mode match {
    case NormalMode => id.fold(normalRoutes(page)(userAnswers))(idx => idxRoutes(page)(idx, userAnswers))
    case CheckMode => checkRouteMap(page)(userAnswers)
    case ReviewMode => id.fold(normalRoutes(page)(userAnswers))(idx => idxRoutes(page)(idx, userAnswers))
  }
}
