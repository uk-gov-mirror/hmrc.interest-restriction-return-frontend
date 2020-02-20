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
import forms.groupStructure.LimitedLiabilityPartnershipFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.GroupStructureNavigator
import pages.groupStructure.{DeemedParentPage, LimitedLiabilityPartnershipPage, ParentCompanyNamePage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.QuestionDeletionLookupService
import views.html.groupStructure.LimitedLiabilityPartnershipView

import scala.concurrent.Future

class LimitedLiabilityPartnershipController @Inject()(override val messagesApi: MessagesApi,
                                                      val sessionRepository: SessionRepository,
                                                      val navigator: GroupStructureNavigator,
                                                      val questionDeletionLookupService: QuestionDeletionLookupService,
                                                      identify: IdentifierAction,
                                                      getData: DataRetrievalAction,
                                                      requireData: DataRequiredAction,
                                                      formProvider: LimitedLiabilityPartnershipFormProvider,
                                                      val controllerComponents: MessagesControllerComponents,
                                                      view: LimitedLiabilityPartnershipView
                                                     )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val limitedLiabilityPartnership = getAnswer(DeemedParentPage, idx).flatMap(_.limitedLiabilityPartnership)
    val form = formProvider()
    answerFor(ParentCompanyNamePage, idx) { name =>
      Future.successful(
        Ok(view(limitedLiabilityPartnership.fold(form)(form.fill), mode, name, routes.LocalRegistrationNumberController.onSubmit(idx, mode)))
      )
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        answerFor(ParentCompanyNamePage, idx) { name =>
          Future.successful(BadRequest(view(formWithErrors, mode, name, routes.LimitedLiabilityPartnershipController.onSubmit(idx, mode))))
        },
      value => {
        getAnswer(DeemedParentPage, idx).map(_.copy(limitedLiabilityPartnership = Some(value))) match {
          case Some(deemedParentModel) =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(DeemedParentPage, deemedParentModel, Some(idx)))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(LimitedLiabilityPartnershipPage, mode, updatedAnswers, Some(idx)))
          case _ =>
            Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
        }
      }
    )
  }
}
