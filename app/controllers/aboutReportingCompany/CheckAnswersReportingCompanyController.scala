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
import controllers.BaseNavigationController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import models.NormalMode
import models.Section.ReportingCompany
import navigation.AboutReportingCompanyNavigator
import pages.aboutReportingCompany.CheckAnswersReportingCompanyPage
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionService}
import utils.CheckYourAnswersAboutReportingCompanyHelper
import views.html.CheckYourAnswersView

import scala.concurrent.ExecutionContext

class CheckAnswersReportingCompanyController @Inject()(override val messagesApi: MessagesApi,
                                                       override val sessionRepository: SessionRepository,
                                                       override val navigator: AboutReportingCompanyNavigator,
                                                       override val questionDeletionLookupService: QuestionDeletionLookupService,
                                                       override val updateSectionService: UpdateSectionService,
                                                       identify: IdentifierAction,
                                                       getData: DataRetrievalAction,
                                                       requireData: DataRequiredAction,
                                                       val controllerComponents: MessagesControllerComponents,
                                                       view: CheckYourAnswersView
                                                      )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig) extends BaseNavigationController {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val checkAnswersHelper = new CheckYourAnswersAboutReportingCompanyHelper(request.userAnswers)
    Ok(view(checkAnswersHelper.rows, ReportingCompany, controllers.aboutReportingCompany.routes.CheckAnswersReportingCompanyController.onSubmit()))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    saveAndRedirect(CheckAnswersReportingCompanyPage, NormalMode)
  }
}

