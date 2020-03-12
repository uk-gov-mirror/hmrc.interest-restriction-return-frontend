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
import forms.elections.PartnershipSAUTRFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.ElectionsNavigator
import pages.elections.{PartnershipNamePage, PartnershipSAUTRPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionService}
import views.html.elections.PartnershipSAUTRView

import scala.concurrent.Future

class PartnershipSAUTRController @Inject()(override val messagesApi: MessagesApi,
                                           override val sessionRepository: SessionRepository,
                                           override val navigator: ElectionsNavigator,
                                           override val questionDeletionLookupService: QuestionDeletionLookupService,
                                           override val updateSectionService: UpdateSectionService,
                                           identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           formProvider: PartnershipSAUTRFormProvider,
                                           val controllerComponents: MessagesControllerComponents,
                                           view: PartnershipSAUTRView
                                          )(implicit errorHandler: ErrorHandler, appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(PartnershipNamePage) { name =>
      Future.successful(Ok(view(fillForm(PartnershipSAUTRPage, formProvider()), mode, name)))
    }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors => answerFor(PartnershipNamePage) { name =>
        Future.successful(BadRequest(view(formWithErrors, mode, name)))
      },
      value =>
        saveAndRedirect(PartnershipSAUTRPage, value, mode)
    )
  }
}
