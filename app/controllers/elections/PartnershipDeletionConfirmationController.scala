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
import controllers.actions._
import forms.elections.PartnershipDeletionConfirmationFormProvider
import javax.inject.Inject
import models.{Mode, NormalMode}
import pages.elections.{PartnershipDeletionConfirmationPage, PartnershipsPage}
import config.featureSwitch.FeatureSwitching
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.elections.PartnershipDeletionConfirmationView
import play.api.data.Form

import scala.concurrent.Future
import navigation.ElectionsNavigator
import services.QuestionDeletionLookupService
import controllers.BaseNavigationController
import handlers.ErrorHandler

class PartnershipDeletionConfirmationController @Inject()(
                                                           override val messagesApi: MessagesApi,
                                                           val sessionRepository: SessionRepository,
                                                           val navigator: ElectionsNavigator,
                                                           val questionDeletionLookupService: QuestionDeletionLookupService,
                                                           identify: IdentifierAction,
                                                           getData: DataRetrievalAction,
                                                           requireData: DataRequiredAction,
                                                           formProvider: PartnershipDeletionConfirmationFormProvider,
                                                           val controllerComponents: MessagesControllerComponents,
                                                           view: PartnershipDeletionConfirmationView
                                                         )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  val form = formProvider()

  def onPageLoad(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(PartnershipsPage, idx) { partnership =>
      Future.successful(Ok(view(
        form,
        routes.PartnershipDeletionConfirmationController.onSubmit(idx),
        partnership.name
      )))
    }
  }

  def onSubmit(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(PartnershipsPage, idx) { partnership =>
      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(
            formWithErrors,
            routes.PartnershipDeletionConfirmationController.onSubmit(idx),
            partnership.name
          ))),
        {
          case true =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.remove(PartnershipsPage, Some(idx)))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(PartnershipDeletionConfirmationPage, NormalMode, updatedAnswers))
          case false =>
            Future.successful(Redirect(navigator.nextPage(PartnershipDeletionConfirmationPage, NormalMode, request.userAnswers)))
        }
      )
    }
  }
}
