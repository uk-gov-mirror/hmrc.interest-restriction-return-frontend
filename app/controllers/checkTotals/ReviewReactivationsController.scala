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

package controllers.checkTotals

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.BaseController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import handlers.ErrorHandler
import models.NormalMode
import navigation.CheckTotalsNavigator
import pages.aboutReturn.InterestReactivationsCapPage
import pages.checkTotals.ReviewReactivationsPage
import play.api.i18n.MessagesApi
import play.api.mvc._
import utils.ReviewReactivationsHelper
import views.html.checkTotals.ReviewReactivationsView

import scala.concurrent.{ExecutionContext, Future}

class ReviewReactivationsController @Inject()(override val messagesApi: MessagesApi,
                                              identify: IdentifierAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              val controllerComponents: MessagesControllerComponents,
                                              navigator: CheckTotalsNavigator,
                                              view: ReviewReactivationsView
                                         )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      answerFor(InterestReactivationsCapPage) { reactivationCap =>
        val reactivationsHelper = new ReviewReactivationsHelper(request.userAnswers)
        Future.successful(Ok(view(
          allocatedReactivations = reactivationsHelper.rows,
          postAction = routes.ReviewReactivationsController.onSubmit(),
          reactivationCapAmt = reactivationCap,
          totalReactivationAmt = reactivationsHelper.totalReactivations
        )))
      }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request => Redirect(navigator.nextPage(ReviewReactivationsPage, NormalMode, request.userAnswers))
  }
}
