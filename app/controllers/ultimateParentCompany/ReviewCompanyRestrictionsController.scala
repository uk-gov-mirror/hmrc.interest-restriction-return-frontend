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

package controllers.ukCompanies

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.BaseController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import models.NormalMode
import navigation.UkCompaniesNavigator
import play.api.i18n.MessagesApi
import play.api.mvc._
import utils.ReviewCompanyRestrictionsHelper
import views.html.ukCompanies.ReviewCompanyRestrictionsView
import pages.ukCompanies.{UkCompaniesPage, ReviewCompanyRestrictionsPage}
import handlers.ErrorHandler

import scala.concurrent.Future

class ReviewCompanyRestrictionsController @Inject()(override val messagesApi: MessagesApi,
                                                navigator: UkCompaniesNavigator,
                                                identify: IdentifierAction,
                                                getData: DataRetrievalAction,
                                                requireData: DataRequiredAction,
                                                val controllerComponents: MessagesControllerComponents,
                                                view: ReviewCompanyRestrictionsView
                                               )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController {

  def onPageLoad(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request => 
    answerFor(UkCompaniesPage, idx: Int) { ukCompany =>
      val checkAnswersHelper = new ReviewCompanyRestrictionsHelper(idx, request.userAnswers)
      Future.successful(Ok(view(
        checkAnswersHelper.rows, 
        ukCompany.companyDetails.companyName, 
        controllers.ukCompanies.routes.ReviewCompanyRestrictionsController.onSubmit(idx))))
    }
  }

  def onSubmit(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request => Future.successful(Redirect(navigator.nextRestrictionPage(ReviewCompanyRestrictionsPage(idx), NormalMode, request.userAnswers)))
  }
}
