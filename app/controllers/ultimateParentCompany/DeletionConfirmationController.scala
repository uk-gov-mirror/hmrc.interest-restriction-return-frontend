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

package controllers.ultimateParentCompany

import config.FrontendAppConfig
import controllers.actions._
import forms.ultimateParentCompany.DeletionConfirmationFormProvider
import javax.inject.Inject
import models.NormalMode
import pages.ultimateParentCompany.{DeemedParentPage, DeletionConfirmationPage}
import config.featureSwitch.FeatureSwitching
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ultimateParentCompany.DeletionConfirmationView

import scala.concurrent.Future
import navigation.UltimateParentCompanyNavigator
import handlers.ErrorHandler

class DeletionConfirmationController @Inject()(override val messagesApi: MessagesApi,
                                               override val sessionRepository: SessionRepository,
                                               override val navigator: UltimateParentCompanyNavigator,
                                               override val updateSectionService: UpdateSectionStateService,
                                               identify: IdentifierAction,
                                               getData: DataRetrievalAction,
                                               requireData: DataRequiredAction,
                                               formProvider: DeletionConfirmationFormProvider,
                                               val controllerComponents: MessagesControllerComponents,
                                               view: DeletionConfirmationView
                                              )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(DeemedParentPage, idx) { deemedParent =>
      Future.successful(Ok(view(
        formProvider(),
        routes.DeletionConfirmationController.onSubmit(idx),
        deemedParent.companyName.name
      )))
    }
  }

  def onSubmit(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(DeemedParentPage, idx) { deemedParent =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, routes.DeletionConfirmationController.onSubmit(idx), deemedParent.companyName.name)))
        ,
        {
          case true =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.remove(DeemedParentPage,Some(idx)))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(DeletionConfirmationPage, NormalMode, updatedAnswers))
          case false =>
            Future.successful(Redirect(navigator.nextPage(DeletionConfirmationPage, NormalMode, request.userAnswers, Some(idx))))
        }
      )
    }
  }
}
