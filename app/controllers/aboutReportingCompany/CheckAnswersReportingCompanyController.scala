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

package controllers.aboutReportingCompany

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.BaseController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import models.NormalMode
import models.Section.ReportingCompany
import navigation.AboutReportingCompanyNavigator
import pages.aboutReportingCompany.CheckAnswersReportingCompanyPage
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import utils.CheckYourAnswersAboutReportingCompanyHelper
import views.html.CheckYourAnswersView

import scala.concurrent.ExecutionContext

class CheckAnswersReportingCompanyController @Inject()(override val messagesApi: MessagesApi,
                                                       identify: IdentifierAction,
                                                       getData: DataRetrievalAction,
                                                       requireData: DataRequiredAction,
                                                       val controllerComponents: MessagesControllerComponents,
                                                       navigator: AboutReportingCompanyNavigator,
                                                       view: CheckYourAnswersView
                                                      )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig) extends BaseController {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val checkAnswersHelper = new CheckYourAnswersAboutReportingCompanyHelper(request.userAnswers)
    Ok(view(checkAnswersHelper, ReportingCompany, controllers.aboutReportingCompany.routes.CheckAnswersReportingCompanyController.onSubmit()))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Redirect(navigator.nextPage(CheckAnswersReportingCompanyPage, NormalMode, request.userAnswers))
  }
}
