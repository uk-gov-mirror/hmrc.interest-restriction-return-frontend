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

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.BaseController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import handlers.ErrorHandler
import models.NormalMode
import models.Section.GroupStructure
import models.requests.DataRequest
import navigation.GroupStructureNavigator
import pages.groupStructure.{CheckAnswersGroupStructurePage, DeemedParentPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import services.DeemedParentService
import utils.CheckYourAnswersGroupStructureHelper
import views.html.CheckYourAnswersView

import scala.concurrent.{ExecutionContext, Future}

class CheckAnswersGroupStructureController @Inject()(override val messagesApi: MessagesApi,
                                                     identify: IdentifierAction,
                                                     getData: DataRetrievalAction,
                                                     requireData: DataRequiredAction,
                                                     val controllerComponents: MessagesControllerComponents,
                                                     navigator: GroupStructureNavigator,
                                                     view: CheckYourAnswersView,
                                                     deemedParentService: DeemedParentService
                                                    )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig, errorHandler: ErrorHandler)
  extends BaseController {

  def onPageLoad(id: Int): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      val checkYourAnswersHelper = new CheckYourAnswersGroupStructureHelper(request.userAnswers)
      Ok(view(checkYourAnswersHelper.rows(id), GroupStructure, controllers.groupStructure.routes.CheckAnswersGroupStructureController.onSubmit(id)))
  }

  def onSubmit(id: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      answerFor(DeemedParentPage) {
        case true =>
          deemedParentService.addDeemedParent(request.userAnswers).map {
            case Left(_) => InternalServerError(errorHandler.internalServerErrorTemplate)
            case Right(_) => nextPage(id)
          }
        case false => Future.successful(nextPage(id))
      }
  }

  private def nextPage(id: Int)(implicit request: DataRequest[_]): Result =
    Redirect(navigator.nextPage(CheckAnswersGroupStructurePage, NormalMode, request.userAnswers, Some(id)))
}
