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

package controllers.elections

import config.FrontendAppConfig
import controllers.actions._
import forms.elections.InvestorGroupsDeletionConfirmationFormProvider
import javax.inject.Inject
import models.{Mode, NormalMode}
import pages.elections.{InvestmentNamePage, InvestmentsDeletionConfirmationPage, InvestorGroupsDeletionConfirmationPage, InvestorGroupsPage}
import config.featureSwitch.FeatureSwitching
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.elections.InvestorGroupsDeletionConfirmationView
import play.api.data.Form

import scala.concurrent.Future
import navigation.ElectionsNavigator
import services.{QuestionDeletionLookupService, UpdateSectionService}
import controllers.BaseNavigationController
import handlers.ErrorHandler

class InvestorGroupsDeletionConfirmationController @Inject()(override val messagesApi: MessagesApi,
                                                             override val sessionRepository: SessionRepository,
                                                             override val navigator: ElectionsNavigator,
                                                             override val questionDeletionLookupService: QuestionDeletionLookupService,
                                                             override val updateSectionService: UpdateSectionService,
                                                             identify: IdentifierAction,
                                                             getData: DataRetrievalAction,
                                                             requireData: DataRequiredAction,
                                                             formProvider: InvestorGroupsDeletionConfirmationFormProvider,
                                                             val controllerComponents: MessagesControllerComponents,
                                                             view: InvestorGroupsDeletionConfirmationView
                                                            )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler)
  extends BaseNavigationController {

  def onPageLoad(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(InvestorGroupsPage, idx) { investorGroup =>
      Future.successful(Ok(view(
        formProvider(),
        routes.InvestorGroupsDeletionConfirmationController.onSubmit(idx),
        investorGroup.investorName
      )))
    }
  }

  def onSubmit(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(InvestorGroupsPage, idx) { investorGroup =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(
            formWithErrors,
            routes.InvestmentsDeletionConfirmationController.onSubmit(idx),
            investorGroup.investorName
          ))),
        {
          case true =>
            remove(InvestorGroupsPage, NormalMode, Some(idx)).map{ userAnswers =>
              Redirect(navigator.nextPage(InvestorGroupsDeletionConfirmationPage, NormalMode, userAnswers))
            }
          case false =>
            Future.successful(Redirect(navigator.nextPage(InvestorGroupsDeletionConfirmationPage, NormalMode, request.userAnswers)))
        }
      )
    }
  }
}
