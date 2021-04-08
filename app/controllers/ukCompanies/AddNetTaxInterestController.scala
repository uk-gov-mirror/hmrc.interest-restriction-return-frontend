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
import controllers.actions._
import controllers.BaseController
import forms.ukCompanies.AddNetTaxInterestFormProvider
import handlers.ErrorHandler
import models.Mode
import navigation.UkCompaniesNavigator
import pages.ukCompanies.{AddNetTaxInterestPage, UkCompaniesPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.AddNetTaxInterestView
import javax.inject.Inject
import models.CompanyEstimatedFigures.NetTaxInterest

import scala.concurrent.Future

class AddNetTaxInterestController @Inject()(
                                             override val messagesApi: MessagesApi,
                                             sessionRepository: SessionRepository,
                                             navigator: UkCompaniesNavigator,
                                             identify: IdentifierAction,
                                             getData: DataRetrievalAction,
                                             requireData: DataRequiredAction,
                                             formProvider: AddNetTaxInterestFormProvider,
                                             val controllerComponents: MessagesControllerComponents,
                                             view: AddNetTaxInterestView
                                           )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      Future.successful(
        Ok(view(
          form = ukCompany.addNetTaxInterest.fold(formProvider())(formProvider().fill),
          companyName = ukCompany.companyDetails.companyName,
          postAction = routes.AddNetTaxInterestController.onSubmit(idx, mode)
        ))
      )
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(
            BadRequest(view(
              form = formWithErrors,
              companyName = ukCompany.companyDetails.companyName,
              postAction = routes.AddNetTaxInterestController.onSubmit(idx, mode)
            ))
          ),
        value => {
          val updatedModel = value match {
            case true => ukCompany.copy(addNetTaxInterest = Some(value))
            case false =>
              val estimatedFigures = ukCompany.estimatedFigures.map(_.filterNot(_ == NetTaxInterest))
              ukCompany.copy(addNetTaxInterest = Some(value), netTaxInterest = None,
                netTaxInterestIncomeOrExpense = None, estimatedFigures = estimatedFigures)
          }
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(UkCompaniesPage, updatedModel, Some(idx)))
            _ <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(AddNetTaxInterestPage, mode, updatedAnswers, Some(idx)))
        }
      )
    }
  }
}
