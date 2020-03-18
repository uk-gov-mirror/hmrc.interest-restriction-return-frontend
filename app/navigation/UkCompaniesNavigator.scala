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

import controllers.ukCompanies.routes
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.aboutReturn._
import pages.ukCompanies._
import play.api.mvc.Call

@Singleton
class UkCompaniesNavigator @Inject()() extends Navigator {

  val normalRoutes: Map[Page, (Int, UserAnswers) => Call] = Map(
    AboutAddingUKCompaniesPage -> ((idx, _) => routes.CompanyDetailsController.onPageLoad(idx, NormalMode)),
    CompanyDetailsPage -> ((idx, _) => routes.EnterCompanyTaxEBITDAController.onPageLoad(idx, NormalMode)),
    EnterCompanyTaxEBITDAPage -> ((idx, _) => routes.NetTaxInterestIncomeOrExpenseController.onPageLoad(idx, NormalMode)),
    NetTaxInterestIncomeOrExpensePage -> ((idx, _) => routes.NetTaxInterestAmountController.onPageLoad(idx, NormalMode)),
    NetTaxInterestAmountPage -> ((idx, _) => routes.ConsentingCompanyController.onPageLoad(idx, NormalMode)),
    ConsentingCompanyPage -> ((idx, userAnswers) => userAnswers.get(GroupSubjectToRestrictionsPage) match {
      case Some(true) => checkYourAnswers(idx)
      case Some(false) => userAnswers.get(GroupSubjectToReactivationsPage) match {
        case Some(true) => routes.AddAnReactivationQueryController.onPageLoad(idx, NormalMode)
        case Some(false) => checkYourAnswers(idx)
        case _ => routes.ConsentingCompanyController.onPageLoad(idx, NormalMode)
      }
      case _ => routes.ConsentingCompanyController.onPageLoad(idx, NormalMode)
    }),
    AddAnReactivationQueryPage -> ((idx, userAnswers) => userAnswers.get(UkCompaniesPage, Some(idx)).flatMap(_.reactivation) match {
      case Some(true) => routes.ReactivationAmountController.onPageLoad(idx, NormalMode)
      case Some(false) => checkYourAnswers(idx)
      case _ => routes.AddAnReactivationQueryController.onPageLoad(idx, NormalMode)
    }),
    ReactivationAmountPage -> ((idx,_) => checkYourAnswers(idx)),
    CheckAnswersUkCompanyPage -> ((_,_) => routes.UkCompaniesReviewAnswersListController.onPageLoad()),
    UkCompaniesPage -> ((_,_) => nextSection(NormalMode)),
    UkCompaniesDeletionConfirmationPage -> ((_, _) => routes.UkCompaniesReviewAnswersListController.onPageLoad()),
    AddRestrictionPage -> ((_, _) => controllers.routes.UnderConstructionController.onPageLoad()) //TODO: Update as part of routing subtask
  )

  val checkRouteMap: Map[Page, (Int, UserAnswers) => Call] = Map().withDefaultValue((idx, _) => checkYourAnswers(idx))

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
}
