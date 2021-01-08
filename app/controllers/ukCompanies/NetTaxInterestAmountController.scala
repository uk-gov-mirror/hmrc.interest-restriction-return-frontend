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

package controllers.ukCompanies

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.ukCompanies.NetTaxInterestAmountFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.UkCompaniesNavigator
import pages.ukCompanies.{NetTaxInterestAmountPage, UkCompaniesPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.UpdateSectionStateService
import views.html.ukCompanies.NetTaxInterestAmountView

import scala.concurrent.Future

class NetTaxInterestAmountController @Inject()(override val messagesApi: MessagesApi,
                                               override val sessionRepository: SessionRepository,
                                               override val navigator: UkCompaniesNavigator,
                                               override val updateSectionService: UpdateSectionStateService,
                                               identify: IdentifierAction,
                                               getData: DataRetrievalAction,
                                               requireData: DataRequiredAction,
                                               formProvider: NetTaxInterestAmountFormProvider,
                                               val controllerComponents: MessagesControllerComponents,
                                               view: NetTaxInterestAmountView
                                              )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      ukCompany.netTaxInterestIncomeOrExpense match {
        case Some(incomeOrExpense) =>
          Future.successful(Ok(view(
          form = ukCompany.netTaxInterest.fold(formProvider(incomeOrExpense))(formProvider(incomeOrExpense).fill),
            mode = mode,
            companyName = ukCompany.companyDetails.companyName,
            incomeOrExpense = incomeOrExpense,
            postAction = routes.NetTaxInterestAmountController.onSubmit(idx, mode)
          )))
        case _ => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
      }
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      ukCompany.netTaxInterestIncomeOrExpense match {
        case Some(incomeOrExpense) =>
          formProvider(incomeOrExpense).bindFromRequest().fold(
            formWithErrors =>
              Future.successful(BadRequest(view(
                form = formWithErrors,
                mode = mode,
                companyName = ukCompany.companyDetails.companyName,
                incomeOrExpense = incomeOrExpense,
                postAction = routes.NetTaxInterestAmountController.onSubmit(idx, mode)
              ))),
            value => {
              val updatedModel = ukCompany.copy(netTaxInterest = Some(value))
              save(UkCompaniesPage, updatedModel, mode, Some(idx)).map { cleanedAnswers =>
                Redirect(navigator.nextPage(NetTaxInterestAmountPage, mode, cleanedAnswers, Some(idx)))
              }
            }
          )
        case _ => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
      }
    }
  }
}
