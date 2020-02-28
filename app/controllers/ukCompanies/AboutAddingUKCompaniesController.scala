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

import config.FrontendAppConfig
import controllers.BaseController
import controllers.actions._
import javax.inject.Inject
import models.NormalMode
import navigation.UkCompaniesNavigator
import pages.ukCompanies.AboutAddingUKCompaniesPage
import play.api.i18n.MessagesApi
import play.api.mvc._
import views.html.ukCompanies.AboutAddingUKCompaniesView

import scala.concurrent.ExecutionContext

class AboutAddingUKCompaniesController @Inject()(override val messagesApi: MessagesApi,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: AboutAddingUKCompaniesView,
                                      val navigator: UkCompaniesNavigator)(implicit ec: ExecutionContext, appConfig: FrontendAppConfig) extends BaseController {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(routes.AboutAddingUKCompaniesController.onSubmit()))
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Redirect(navigator.nextPage(AboutAddingUKCompaniesPage, NormalMode, request.userAnswers))
  }
}
