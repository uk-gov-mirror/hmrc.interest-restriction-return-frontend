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

package controllers.elections

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.elections.InvestmentsDeletionConfirmationFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.NormalMode
import navigation.ElectionsNavigator
import pages.elections.{InvestmentNamePage, InvestmentsDeletionConfirmationPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.QuestionDeletionLookupService
import views.html.elections.InvestmentsDeletionConfirmationView

import scala.concurrent.Future

class InvestmentsDeletionConfirmationController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         val sessionRepository: SessionRepository,
                                         val navigator: ElectionsNavigator,
                                         val questionDeletionLookupService: QuestionDeletionLookupService,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: InvestmentsDeletionConfirmationFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: InvestmentsDeletionConfirmationView
                                 )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(InvestmentNamePage, idx) { investment =>
      Future.successful(Ok(view(
        formProvider(),
        routes.InvestmentsDeletionConfirmationController.onSubmit(idx),
        investment
      )))
    }
  }

  def onSubmit(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(InvestmentNamePage, idx) { investment =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(
            formWithErrors,
            routes.InvestmentsDeletionConfirmationController.onSubmit(idx),
            investment
          ))),
        {
          case true =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.remove(InvestmentNamePage, Some(idx)))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(InvestmentsDeletionConfirmationPage, NormalMode, updatedAnswers))
          case false =>
            Future.successful(Redirect(navigator.nextPage(InvestmentsDeletionConfirmationPage, NormalMode, request.userAnswers)))
        }
      )
    }
  }
}
