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
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.groupStructure.ParentCRNFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import models.returnModels.CRNModel
import navigation.GroupStructureNavigator
import pages.groupStructure.{DeemedParentPage, ParentCRNPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.QuestionDeletionLookupService
import views.html.groupStructure.ParentCRNView

import scala.concurrent.Future

class ParentCRNController @Inject()(override val messagesApi: MessagesApi,
                                    val sessionRepository: SessionRepository,
                                    val navigator: GroupStructureNavigator,
                                    val questionDeletionLookupService: QuestionDeletionLookupService,
                                    identify: IdentifierAction,
                                    getData: DataRetrievalAction,
                                    requireData: DataRequiredAction,
                                    formProvider: ParentCRNFormProvider,
                                    val controllerComponents: MessagesControllerComponents,
                                    view: ParentCRNView
                                   )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val crn = getAnswer(DeemedParentPage, idx).flatMap(_.crn.map(_.crn))
    val form = formProvider()
    Ok(view(crn.fold(form)(form.fill), mode, routes.ParentCRNController.onSubmit(idx, mode)))
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode, routes.ParentCompanySAUTRController.onSubmit(idx, mode)))),
      value => {
        getAnswer(DeemedParentPage, idx).map(_.copy(crn = Some(CRNModel(value)))) match {
          case Some(deemedParentModel) =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(DeemedParentPage, deemedParentModel, Some(idx)))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(ParentCRNPage, mode, updatedAnswers, Some(idx)))
          case _ =>
            Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
        }
      }
    )
  }
}
