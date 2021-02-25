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
import controllers.actions._
import forms.ukCompanies.CompanyEstimatedFiguresFormProvider
import javax.inject.Inject
import models.Mode
import pages.ukCompanies.{UkCompaniesPage, CompanyEstimatedFiguresPage}
import config.featureSwitch.{FeatureSwitching}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.CompanyEstimatedFiguresView
import navigation.UkCompaniesNavigator
import scala.concurrent.Future
import controllers.BaseController
import models.CompanyEstimatedFigures
import handlers.ErrorHandler

class CompanyEstimatedFiguresController @Inject()(
                                       override val messagesApi: MessagesApi,
                                       sessionRepository: SessionRepository,
                                       navigator: UkCompaniesNavigator,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: CompanyEstimatedFiguresFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: CompanyEstimatedFiguresView
                                   )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx: Int) { ukCompany =>
      val form = ukCompany.estimatedFigures.fold(formProvider())(formProvider().fill)
      val formOptions = CompanyEstimatedFigures.options(form, idx, request.userAnswers)
      Future.successful(Ok(view(form, mode, formOptions, routes.CompanyEstimatedFiguresController.onSubmit(idx, mode))))
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      formProvider().bindFromRequest().fold(
        formWithErrors => {
          val formOptions = CompanyEstimatedFigures.options(formWithErrors, idx, request.userAnswers)
          Future.successful(BadRequest(view(formWithErrors, mode, formOptions, routes.CompanyEstimatedFiguresController.onSubmit(idx, mode)))),
        },
        value => {
          val updatedModel = ukCompany.copy(estimatedFigures = Some(value))
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(UkCompaniesPage, updatedModel, Some(idx)))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(CompanyEstimatedFiguresPage, mode, updatedAnswers, Some(idx)))
        }
      )
    }
  }
}
