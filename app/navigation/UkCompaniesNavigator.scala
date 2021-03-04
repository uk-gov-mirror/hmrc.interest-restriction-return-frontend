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

import controllers.ukCompanies.routes

import javax.inject.{Inject, Singleton}
import models._
import pages.{ukCompanies, _}
import pages.groupLevelInformation._
import pages.ukCompanies._
import play.api.mvc.Call
import models.NetTaxInterestIncomeOrExpense._

@Singleton
class UkCompaniesNavigator @Inject()() extends Navigator {

  val normalRoutes: Map[Page, (Int, UserAnswers) => Call] = Map(
    AboutAddingUKCompaniesPage -> ((idx, _) => routes.CompanyDetailsController.onPageLoad(idx, NormalMode)),
    CompanyDetailsPage -> ((idx, _) => routes.EnterCompanyTaxEBITDAController.onPageLoad(idx, NormalMode)),
    EnterCompanyTaxEBITDAPage -> ((idx, _) => routes.NetTaxInterestIncomeOrExpenseController.onPageLoad(idx, NormalMode)),
    NetTaxInterestIncomeOrExpensePage -> ((idx, _) => routes.NetTaxInterestAmountController.onPageLoad(idx, NormalMode)),
    NetTaxInterestAmountPage -> afterNetTaxInterestPagesNormalRoute,
    ConsentingCompanyPage -> ((idx, userAnswers) => userAnswers.get(GroupSubjectToRestrictionsPage) match {
      case Some(true) => checkYourAnswers(idx)
      case _ => userAnswers.get(GroupSubjectToReactivationsPage) match {
        case Some(true) => routes.AddAnReactivationQueryController.onPageLoad(idx, NormalMode)
        case _ => checkYourAnswers(idx)
      }
    }),
    AddAnReactivationQueryPage -> ((idx, userAnswers) => userAnswers.get(UkCompaniesPage, Some(idx)).flatMap(_.reactivation) match {
      case Some(true) => routes.ReactivationAmountController.onPageLoad(idx, NormalMode)
      case Some(false) => routes.CompanyContainsEstimatesController.onPageLoad(idx, NormalMode)
      case _ => routes.AddAnReactivationQueryController.onPageLoad(idx, NormalMode)
    }),
    ReactivationAmountPage -> ((idx,_) => routes.CompanyContainsEstimatesController.onPageLoad(idx, NormalMode)),
    CheckAnswersUkCompanyPage -> ((_,_) => routes.UkCompaniesReviewAnswersListController.onPageLoad()),
    UkCompaniesPage -> ((_,_) => nextSection(NormalMode)),
    UkCompaniesDeletionConfirmationPage -> ((_, _) => routes.UkCompaniesReviewAnswersListController.onPageLoad()),
    AddRestrictionPage -> ((_, _) => controllers.routes.UnderConstructionController.onPageLoad()), //TODO: Update as part of routing subtask
    CompanyAccountingPeriodSameAsGroupPage -> ((_, _) => controllers.routes.UnderConstructionController.onPageLoad()), //TODO: Update as part of routing subtask
    RestrictionAmountSameAPPage -> ((_, _) => controllers.routes.UnderConstructionController.onPageLoad()), //TODO: Update as part of routing subtask
    CompanyQICElectionPage -> ((_, _) => controllers.routes.UnderConstructionController.onPageLoad()), //TODO: Update as part of routing subtask
    CompanyContainsEstimatesPage -> ((idx, userAnswers) => userAnswers.get(UkCompaniesPage, Some(idx)).flatMap(_.containsEstimates) match {
      case Some(true) => routes.CompanyEstimatedFiguresController.onPageLoad(idx, NormalMode)
      case Some(false) => checkYourAnswers(idx)
      case _ => routes.CompanyContainsEstimatesController.onPageLoad(idx, NormalMode)
    }),
    AddNetTaxInterestPage -> ((idx, userAnswers) => {
      val ukCompany = userAnswers.get(UkCompaniesPage, Some(idx))
      ukCompany.flatMap(_.addNetTaxInterest) match {
        case Some(true) => routes.NetTaxInterestIncomeOrExpenseController.onPageLoad(idx, NormalMode)
        case Some(false) => afterNetTaxInterestPagesNormalRoute(idx, userAnswers)
        case _ => routes.AddNetTaxInterestController.onPageLoad(idx, NormalMode)
      }
    }),
    CompanyEstimatedFiguresPage -> ((idx, _) => checkYourAnswers(idx))
  )

  val checkRouteMap: Map[Page, (Int, UserAnswers) => Call] = Map[Page, (Int, UserAnswers) => Call](
    CompanyContainsEstimatesPage -> ((idx, userAnswers) => {
      val ukCompany = userAnswers.get(UkCompaniesPage, Some(idx))
      ukCompany.flatMap(_.containsEstimates) match {
        case Some(true) if ukCompany.flatMap(_.estimatedFigures).isEmpty => routes.CompanyEstimatedFiguresController.onPageLoad(idx, CheckMode)
        case _ => checkYourAnswers(idx)
      }
    }),
    AddAnReactivationQueryPage -> ((idx, userAnswers) => {
      val ukCompany = userAnswers.get(UkCompaniesPage, Some(idx))
      ukCompany.flatMap(_.reactivation) match {
        case Some(true) if ukCompany.flatMap(_.allocatedReactivations).isEmpty => routes.ReactivationAmountController.onPageLoad(idx, CheckMode)
        case _ => checkYourAnswers(idx)
      }
    })
  ).withDefaultValue((idx, _) => checkYourAnswers(idx))

  val reviewRouteMap: Map[Page, (Int, UserAnswers) => Call] = Map[Page, (Int, UserAnswers) => Call](
    ReactivationAmountPage -> ((_,_) => controllers.checkTotals.routes.ReviewReactivationsController.onPageLoad()),
    EnterCompanyTaxEBITDAPage -> ((_,_) => controllers.checkTotals.routes.ReviewTaxEBITDAController.onPageLoad()),
    NetTaxInterestAmountPage -> ((_,_) => controllers.checkTotals.routes.ReviewNetTaxInterestController.onPageLoad())
  ).withDefaultValue((_, _) => controllers.reviewAndComplete.routes.ReviewAndCompleteController.onPageLoad())

  private def checkYourAnswers(idx: Int): Call = routes.CheckAnswersUkCompanyController.onPageLoad(idx)

  def nextSection(mode: Mode): Call = controllers.checkTotals.routes.DerivedCompanyController.onPageLoad()
  def addCompany(numberOfCompanies: Int): Call = routes.CompanyDetailsController.onPageLoad(numberOfCompanies + 1, NormalMode)

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, idx: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(idx.getOrElse[Int](1), userAnswers)
    case CheckMode => checkRouteMap(page)(idx.getOrElse[Int](1), userAnswers)
    case ReviewMode => reviewRouteMap(page)(idx.getOrElse[Int](1), userAnswers)
  }

  def afterNetTaxInterestPagesNormalRoute(idx: Int, userAnswers: UserAnswers): Call = {
    val groupSubjectToRestrictions = userAnswers.get(GroupSubjectToRestrictionsPage)
    val incomeOrExpense = userAnswers.get(UkCompaniesPage, Some(idx)).flatMap(_.netTaxInterestIncomeOrExpense)
    val groupSubjectToReactivations = userAnswers.get(GroupSubjectToReactivationsPage)

    (groupSubjectToRestrictions, incomeOrExpense, groupSubjectToReactivations) match {
      case (Some(true), Some(NetTaxInterestExpense), _) => routes.AddRestrictionController.onPageLoad(idx, NormalMode)
      case (_, _, Some(true)) => routes.AddAnReactivationQueryController.onPageLoad(idx, NormalMode)
      case _ => routes.CompanyContainsEstimatesController.onPageLoad(idx, NormalMode)
    }
  }
}
