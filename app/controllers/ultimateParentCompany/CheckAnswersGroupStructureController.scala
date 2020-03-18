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

package controllers.ultimateParentCompany

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.BaseNavigationController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import handlers.ErrorHandler
import models.NormalMode
import models.Section.UltimateParentCompany
import navigation.ultimateParentCompanyNavigator
import pages.ultimateParentCompany.CheckAnswersGroupStructurePage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionStateService}
import utils.CheckYourAnswersUltimateParentCompanyHelper
import views.html.CheckYourAnswersView

import scala.concurrent.ExecutionContext

class CheckAnswersGroupStructureController @Inject()(override val messagesApi: MessagesApi,
                                                     override val sessionRepository: SessionRepository,
                                                     override val navigator: ultimateParentCompanyNavigator,
                                                     override val questionDeletionLookupService: QuestionDeletionLookupService,
                                                     override val updateSectionService: UpdateSectionStateService,
                                                     identify: IdentifierAction,
                                                     getData: DataRetrievalAction,
                                                     requireData: DataRequiredAction,
                                                     val controllerComponents: MessagesControllerComponents,
                                                     view: CheckYourAnswersView
                                                    )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig, errorHandler: ErrorHandler)
  extends BaseNavigationController {

  def onPageLoad(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      val checkYourAnswersHelper = new CheckYourAnswersUltimateParentCompanyHelper(request.userAnswers)
      Ok(view(checkYourAnswersHelper.rows(idx), UltimateParentCompany, controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onSubmit(idx)))
  }

  def onSubmit(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request => saveAndRedirect(CheckAnswersGroupStructurePage, NormalMode, Some(idx))
  }
}
