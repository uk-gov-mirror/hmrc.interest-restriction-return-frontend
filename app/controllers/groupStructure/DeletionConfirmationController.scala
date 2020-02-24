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

package controllers.groupStructure

import config.FrontendAppConfig
import controllers.actions._
import forms.groupStructure.DeletionConfirmationFormProvider
import javax.inject.Inject
import models.{Mode, NormalMode}
import pages.groupStructure.{DeemedParentPage, DeletionConfirmationPage}
import config.featureSwitch.FeatureSwitching
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.groupStructure.DeletionConfirmationView
import play.api.data.Form

import scala.concurrent.Future
import navigation.GroupStructureNavigator
import services.QuestionDeletionLookupService
import controllers.BaseNavigationController
import handlers.ErrorHandler

class DeletionConfirmationController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         val sessionRepository: SessionRepository,
                                         val navigator: GroupStructureNavigator,
                                         val questionDeletionLookupService: QuestionDeletionLookupService,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: DeletionConfirmationFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: DeletionConfirmationView
                                 )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

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
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        answerFor(DeemedParentPage, idx) { deemedParent =>
          Future.successful(BadRequest(view(formWithErrors, routes.DeletionConfirmationController.onSubmit(idx), deemedParent.companyName.name)))
        },
      value =>
        saveAndRedirect(DeletionConfirmationPage, value, NormalMode)
    )
  }
}
