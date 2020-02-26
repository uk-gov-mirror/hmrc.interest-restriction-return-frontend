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

package controllers.ukCompanies

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.BaseController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import forms.ukCompanies.UkCompaniesReviewAnswersListFormProvider
import handlers.ErrorHandler
import models.NormalMode
import models.requests.DataRequest
import navigation.UkCompaniesNavigator
import pages.ukCompanies.UkCompaniesPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc._
import utils.UkCompaniesReviewAnswersListHelper
import views.html.ukCompanies.UkCompaniesReviewAnswersListView

import scala.concurrent.ExecutionContext

class UkCompaniesReviewAnswersListController @Inject()(override val messagesApi: MessagesApi,
                                                       identify: IdentifierAction,
                                                       getData: DataRetrievalAction,
                                                       requireData: DataRequiredAction,
                                                       val controllerComponents: MessagesControllerComponents,
                                                       navigator: UkCompaniesNavigator,
                                                       formProvider: UkCompaniesReviewAnswersListFormProvider,
                                                       view: UkCompaniesReviewAnswersListView
                                                    )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController {

  private def ukCompanies(implicit request: DataRequest[_]) = new UkCompaniesReviewAnswersListHelper(request.userAnswers).rows

  private def renderView(form: Form[Boolean] = formProvider())(implicit request: DataRequest[_]) =
    view(form, ukCompanies, routes.UkCompaniesReviewAnswersListController.onSubmit())

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    if(ukCompanies.nonEmpty) {
      Ok(renderView())
    } else {
      Redirect(navigator.addCompany(0))
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        BadRequest(renderView(formWithErrors))
      ,
      {
        case true => Redirect(navigator.addCompany(ukCompanies.length))
        case false => Redirect(navigator.nextPage(UkCompaniesPage, NormalMode, request.userAnswers))
      }
    )
  }
}
