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
import models.{Mode, NormalMode}
import navigation.GroupStructureNavigator
import pages.groupStructure.{DeemedParentPage, LimitedLiabilityPartnershipPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionStateService}
import views.html.groupStructure.LimitedLiabilityPartnershipView

import scala.concurrent.Future

class LimitedLiabilityPartnershipController @Inject()(override val messagesApi: MessagesApi,
                                                      override val sessionRepository: SessionRepository,
                                                      override val navigator: GroupStructureNavigator,
                                                      override val questionDeletionLookupService: QuestionDeletionLookupService,
                                                      override val updateSectionService: UpdateSectionStateService,
                                                      identify: IdentifierAction,
                                                      getData: DataRetrievalAction,
                                                      requireData: DataRequiredAction,
                                                      formProvider: LimitedLiabilityPartnershipFormProvider,
                                                      val controllerComponents: MessagesControllerComponents,
                                                      view: LimitedLiabilityPartnershipView
                                                     )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(DeemedParentPage, idx) { deemedParentModel =>
      Future.successful(
        Ok(view(
          form = deemedParentModel.limitedLiabilityPartnership.fold(formProvider())(formProvider().fill),
          mode = mode,
          companyName = deemedParentModel.companyName.name,
          postAction = routes.LimitedLiabilityPartnershipController.onSubmit(idx, mode)
        ))
      )
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(DeemedParentPage, idx) { deemedParentModel =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(
            form = formWithErrors,
            mode = mode,
            companyName = deemedParentModel.companyName.name,
            postAction = routes.LimitedLiabilityPartnershipController.onSubmit(idx, mode)
          ))),
        value => {
          val updatedModel = deemedParentModel.copy(limitedLiabilityPartnership = Some(value))
          save(DeemedParentPage, updatedModel, NormalMode, Some(idx)).map { userAnswers =>
            Redirect(navigator.nextPage(LimitedLiabilityPartnershipPage, mode, userAnswers, Some(idx)))
          }
        }
      )
    }
  }
}
