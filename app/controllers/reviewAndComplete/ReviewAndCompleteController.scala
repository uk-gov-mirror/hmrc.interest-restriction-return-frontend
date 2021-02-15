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

package controllers.reviewAndComplete

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseController
import controllers.actions._

import javax.inject.Inject
import models.NormalMode
import models.returnModels.ReviewAndCompleteModel
import navigation.ReviewAndCompleteNavigator
import pages.reviewAndComplete.ReviewAndCompletePage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import utils.ReviewAndCompleteHelper
import views.html.reviewAndComplete.ReviewAndCompleteView

import scala.concurrent.Future

class ReviewAndCompleteController @Inject()(override val messagesApi: MessagesApi,
                                            navigator: ReviewAndCompleteNavigator,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: ReviewAndCompleteView
                                           )(implicit appConfig: FrontendAppConfig)
  extends BaseController with I18nSupport with FeatureSwitching {


  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val reviewAndCompleteHelper = new ReviewAndCompleteHelper()
    val reviewAndCompleteModel = ReviewAndCompleteModel(request.userAnswers)

    Future.successful(Ok(view(
      taskListRows = reviewAndCompleteHelper.rows(reviewAndCompleteModel, request.userAnswers),
      postAction = routes.ReviewAndCompleteController.onSubmit()
    )))

  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Redirect(navigator.nextPage(ReviewAndCompletePage, NormalMode, request.userAnswers))
  }
}
