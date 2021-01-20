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

package controllers.ultimateParentCompany

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import models.NormalMode
import models.Section.UltimateParentCompany
import navigation.UltimateParentCompanyNavigator
import pages.ultimateParentCompany.{CheckAnswersGroupStructurePage, DeemedParentPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import utils.CheckYourAnswersUltimateParentCompanyHelper
import views.html.CheckYourAnswersView
import handlers.ErrorHandler
import scala.concurrent.Future

class CheckAnswersGroupStructureController @Inject()(override val messagesApi: MessagesApi,
                                                     override val sessionRepository: SessionRepository,
                                                     override val navigator: UltimateParentCompanyNavigator,
                                                     override val updateSectionService: UpdateSectionStateService,
                                                     identify: IdentifierAction,
                                                     getData: DataRetrievalAction,
                                                     requireData: DataRequiredAction,
                                                     val controllerComponents: MessagesControllerComponents,
                                                     view: CheckYourAnswersView
                                                    )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler)
  extends BaseNavigationController {

  def onPageLoad(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async {implicit request =>
    val checkYourAnswersHelper = new CheckYourAnswersUltimateParentCompanyHelper(request.userAnswers)
    answerFor(DeemedParentPage, idx) { deemedParentModel =>
      Future.successful(
        Ok(view(
          answers = checkYourAnswersHelper.rows(idx), 
          section = UltimateParentCompany, 
          postAction = controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onSubmit(idx),
          subheader = Some(deemedParentModel.companyName.name)
        ))
      )
    }
  }

  def onSubmit(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request => Future.successful(Redirect(navigator.nextPage(CheckAnswersGroupStructurePage, NormalMode, request.userAnswers,Some(idx))))
  }
}
