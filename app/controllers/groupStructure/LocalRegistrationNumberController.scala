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
import forms.groupStructure.LocalRegistrationNumberFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import models.returnModels.UTRModel
import navigation.GroupStructureNavigator
import pages.groupStructure.{DeemedParentPage, LocalRegistrationNumberPage, ParentCompanyNamePage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.QuestionDeletionLookupService
import views.html.groupStructure.LocalRegistrationNumberView

import scala.concurrent.Future

class LocalRegistrationNumberController @Inject()(
                                                   override val messagesApi: MessagesApi,
                                                   val sessionRepository: SessionRepository,
                                                   val navigator: GroupStructureNavigator,
                                                   identify: IdentifierAction,
                                                   getData: DataRetrievalAction,
                                                   requireData: DataRequiredAction,
                                                   formProvider: LocalRegistrationNumberFormProvider,
                                                   val controllerComponents: MessagesControllerComponents,
                                                   view: LocalRegistrationNumberView,
                                                   override val questionDeletionLookupService: QuestionDeletionLookupService
                                                 )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val nonUkCrn = getAnswer(DeemedParentPage, idx).flatMap(_.nonUkCrn)
    val form = formProvider()
    answerFor(ParentCompanyNamePage, idx) { name =>
      Future.successful(
        Ok(view(nonUkCrn.fold(form)(form.fill), mode, name, routes.LocalRegistrationNumberController.onSubmit(idx, mode)))
      )
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      formProvider().bindFromRequest().fold(
        formWithErrors =>
          answerFor(ParentCompanyNamePage, idx) { name =>
            Future.successful(BadRequest(view(formWithErrors, mode, name, routes.LocalRegistrationNumberController.onSubmit(idx, mode))))
          },
        value => {
          getAnswer(DeemedParentPage, idx).map(_.copy(nonUkCrn = Some(value))) match {
            case Some(deemedParentModel) =>
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.set(DeemedParentPage, deemedParentModel, Some(idx)))
                _ <- sessionRepository.set(updatedAnswers)
              } yield Redirect(navigator.nextPage(LocalRegistrationNumberPage, mode, updatedAnswers, Some(idx)))
            case _ =>
              Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
          }
        }
      )
  }
}
