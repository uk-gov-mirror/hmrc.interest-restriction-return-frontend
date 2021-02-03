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

package controllers.elections

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseController
import controllers.actions._
import forms.elections.PartnershipSAUTRFormProvider
import handlers.ErrorHandler

import javax.inject.Inject
import models.Mode
import models.returnModels.UTRModel
import navigation.ElectionsNavigator
import pages.elections.{PartnershipSAUTRPage, PartnershipsPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.elections.PartnershipSAUTRView

import scala.concurrent.Future

class PartnershipSAUTRController @Inject()(override val messagesApi: MessagesApi,
                                           sessionRepository: SessionRepository,
                                           navigator: ElectionsNavigator,
                                           identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           formProvider: PartnershipSAUTRFormProvider,
                                           val controllerComponents: MessagesControllerComponents,
                                           view: PartnershipSAUTRView
                                          )(implicit errorHandler: ErrorHandler, appConfig: FrontendAppConfig) extends BaseController with FeatureSwitching {

  private val form = formProvider()

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(PartnershipsPage, idx) { partnership =>
      Future.successful(
        Ok(view(
          form = partnership.sautr.map(_.utr).fold(form)(form.fill),
          postAction = routes.PartnershipSAUTRController.onSubmit(idx, mode),
          partnershipName = partnership.name
        ))
      )
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(PartnershipsPage, idx) { partnership =>
      form.bindFromRequest().fold(
        formWithErrors =>
          Future.successful(
            BadRequest(view(
              form = formWithErrors,
              postAction = routes.PartnershipSAUTRController.onSubmit(idx, mode),
              partnershipName = partnership.name
            ))
          ),
        value => {
          val updatedModel = partnership.copy(sautr = Some(UTRModel(value)))
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(PartnershipsPage, updatedModel, Some(idx)))
            _ <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(PartnershipSAUTRPage, mode, updatedAnswers, Some(idx)))
        }
      )
    }
  }
}
