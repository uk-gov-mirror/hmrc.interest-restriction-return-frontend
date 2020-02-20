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
import forms.groupStructure.ParentCompanySAUTRFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import models.returnModels.{CompanyNameModel, DeemedParentModel, UTRModel}
import navigation.GroupStructureNavigator
import pages.groupStructure.{DeemedParentPage, ParentCompanyNamePage, ParentCompanySAUTRPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.QuestionDeletionLookupService
import views.html.groupStructure.ParentCompanySAUTRView

import scala.concurrent.Future

class ParentCompanySAUTRController @Inject()(override val messagesApi: MessagesApi,
                                             val sessionRepository: SessionRepository,
                                             val navigator: GroupStructureNavigator,
                                             val questionDeletionLookupService: QuestionDeletionLookupService,
                                             identify: IdentifierAction,
                                             getData: DataRetrievalAction,
                                             requireData: DataRequiredAction,
                                             formProvider: ParentCompanySAUTRFormProvider,
                                             val controllerComponents: MessagesControllerComponents,
                                             view: ParentCompanySAUTRView
                                            )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val sautr = getAnswer(DeemedParentPage, idx).flatMap(_.sautr.map(_.utr))
    val form = formProvider()
    Ok(view(sautr.fold(form)(form.fill), mode, routes.ParentCompanyNameController.onSubmit(idx, mode)))
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode, routes.ParentCompanySAUTRController.onSubmit(idx, mode)))),
      value => {
        getAnswer(DeemedParentPage, idx).map(_.copy(sautr = Some(UTRModel(value)))) match {
          case Some(deemedParentModel) =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(DeemedParentPage, deemedParentModel, Some(idx)))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(ParentCompanySAUTRPage, mode, updatedAnswers, Some(idx)))
          case _ =>
            Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
        }
      }
    )
  }
}
