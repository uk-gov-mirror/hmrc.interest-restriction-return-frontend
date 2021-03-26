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
import controllers.BaseController
import controllers.actions._
import forms.ukCompanies.UkCompaniesDeletionConfirmationFormProvider
import handlers.ErrorHandler

import javax.inject.Inject
import models.NormalMode
import navigation.UkCompaniesNavigator
import pages.ukCompanies.{UkCompaniesDeletionConfirmationPage, UkCompaniesPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.UkCompaniesDeletionConfirmationView
import pages.ukCompanies.RestrictionQueryHelper

import scala.concurrent.Future

class UkCompaniesDeletionConfirmationController @Inject()(override val messagesApi: MessagesApi,
                                                          sessionRepository: SessionRepository,
                                                          navigator: UkCompaniesNavigator,
                                                          identify: IdentifierAction,
                                                          getData: DataRetrievalAction,
                                                          requireData: DataRequiredAction,
                                                          formProvider: UkCompaniesDeletionConfirmationFormProvider,
                                                          val controllerComponents: MessagesControllerComponents,
                                                          view: UkCompaniesDeletionConfirmationView
                                                         )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController {

  def onPageLoad(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      Future.successful(Ok(view(
        formProvider(),
        routes.UkCompaniesDeletionConfirmationController.onSubmit(idx),
        ukCompany.companyDetails.companyName
      )))
    }
  }

  def onSubmit(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(
            form = formWithErrors,
            postAction = routes.UkCompaniesDeletionConfirmationController.onSubmit(idx),
            name = ukCompany.companyDetails.companyName
          )))
        ,
        {
          case true =>
            for {
              updatedAnswers      <- Future.fromTry(request.userAnswers.remove(RestrictionQueryHelper.restrictionPath(idx)))
              finalUpdatedAnswers <- Future.fromTry(updatedAnswers.remove(UkCompaniesPage,Some(idx)))
              _                   <- sessionRepository.set(finalUpdatedAnswers)
            } yield Redirect(navigator.nextPage(UkCompaniesDeletionConfirmationPage, NormalMode, finalUpdatedAnswers, Some(idx)))
          case false =>
            Future.successful(Redirect(navigator.nextPage(UkCompaniesDeletionConfirmationPage, NormalMode, request.userAnswers)))
        }
      )
    }
  }
}
