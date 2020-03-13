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

package controllers.reviewAndComplete

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import handlers.ErrorHandler
import javax.inject.Inject
import models.NormalMode
import navigation.ReviewAndCompleteNavigator
import pages.reviewAndComplete.ReviewAndCompletePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionService}
import utils.ReviewAndCompleteHelper
import views.html.reviewAndComplete.ReviewAndCompleteView

import scala.concurrent.{ExecutionContext, Future}

class ReviewAndCompleteController @Inject()(override val messagesApi: MessagesApi,
                                            override val navigator: ReviewAndCompleteNavigator,
                                            override val sessionRepository: SessionRepository,
                                            override val questionDeletionLookupService: QuestionDeletionLookupService,
                                            override val updateSectionService: UpdateSectionService,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: ReviewAndCompleteView
                                           )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig, errorHandler: ErrorHandler)
  extends BaseNavigationController with I18nSupport with FeatureSwitching {


  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    val reviewAndCompleteHelper = new ReviewAndCompleteHelper()
    answerFor(ReviewAndCompletePage) { reviewAndCompleteModel =>
      Future.successful(Ok(view(reviewAndCompleteHelper.rows(reviewAndCompleteModel), routes.ReviewAndCompleteController.onSubmit())))
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request => Redirect(navigator.nextPage(ReviewAndCompletePage, NormalMode, request.userAnswers))
  }
}
