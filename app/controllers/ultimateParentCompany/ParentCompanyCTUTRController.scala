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

package controllers.ultimateParentCompany

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.ultimateParentCompany.ParentCompanyCTUTRFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.{Mode, NormalMode}
import models.returnModels.UTRModel
import navigation.ultimateParentCompanyNavigator
import pages.ultimateParentCompany.{DeemedParentPage, ParentCompanyCTUTRPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionStateService}
import views.html.ultimateParentCompany.ParentCompanyCTUTRView

import scala.concurrent.Future

class ParentCompanyCTUTRController @Inject()(override val messagesApi: MessagesApi,
                                             override val sessionRepository: SessionRepository,
                                             override val navigator: ultimateParentCompanyNavigator,
                                             override val questionDeletionLookupService: QuestionDeletionLookupService,
                                             override val updateSectionService: UpdateSectionStateService,
                                             identify: IdentifierAction,
                                             getData: DataRetrievalAction,
                                             requireData: DataRequiredAction,
                                             formProvider: ParentCompanyCTUTRFormProvider,
                                             val controllerComponents: MessagesControllerComponents,
                                             view: ParentCompanyCTUTRView
                                            )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val form = formProvider()
    answerFor(DeemedParentPage, idx) { deemedParentModel =>
      Future.successful(Ok(view(
        form = deemedParentModel.ctutr.map(_.utr).fold(form)(form.fill),
        mode = mode,
        postAction = routes.ParentCompanyCTUTRController.onSubmit(idx, mode))
      ))
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(
          form = formWithErrors,
          mode = mode,
          postAction = routes.ParentCompanyCTUTRController.onSubmit(idx, mode)
        ))),
      value => {
        answerFor(DeemedParentPage, idx) { deemedParentModel =>
          val updatedModel = deemedParentModel.copy(ctutr = Some(UTRModel(value)))
          save(DeemedParentPage, updatedModel, NormalMode, Some(idx)).map { userAnswers =>
            Redirect(navigator.nextPage(ParentCompanyCTUTRPage, mode, userAnswers, Some(idx)))
          }
        }
      }
    )
  }
}
