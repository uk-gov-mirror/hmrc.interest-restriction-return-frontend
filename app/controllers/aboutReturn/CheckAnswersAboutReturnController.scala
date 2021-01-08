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

package controllers.aboutReturn

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.BaseNavigationController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import models.NormalMode
import models.Section.AboutReturn
import navigation.AboutReturnNavigator
import pages.aboutReturn.CheckAnswersReportingCompanyPage
import play.api.i18n.MessagesApi
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents}
import repositories.SessionRepository
import services.UpdateSectionStateService
import utils.CheckYourAnswersAboutReturnCompanyHelper
import views.html.CheckYourAnswersView

class CheckAnswersAboutReturnController @Inject()(override val messagesApi: MessagesApi,
                                                  override val sessionRepository: SessionRepository,
                                                  override val navigator: AboutReturnNavigator,
                                                  override val updateSectionService: UpdateSectionStateService,
                                                  identify: IdentifierAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  val controllerComponents: MessagesControllerComponents,
                                                  view: CheckYourAnswersView
                                                      )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val checkAnswersHelper = new CheckYourAnswersAboutReturnCompanyHelper(request.userAnswers)
    Ok(view(checkAnswersHelper.rows, AboutReturn, controllers.aboutReturn.routes.CheckAnswersAboutReturnController.onSubmit()))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    saveAndRedirect(CheckAnswersReportingCompanyPage, NormalMode)
  }
}

