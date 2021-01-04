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

package controllers.elections

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.elections.InvestorRatioMethodFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.{Mode, NormalMode}
import navigation.ElectionsNavigator
import pages.elections.{InvestorGroupsPage, InvestorRatioMethodPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionStateService}
import views.html.elections.InvestorRatioMethodView

import scala.concurrent.Future

class InvestorRatioMethodController @Inject()(
                                               override val messagesApi: MessagesApi,
                                               override val sessionRepository: SessionRepository,
                                               override val navigator: ElectionsNavigator,
                                               override val questionDeletionLookupService: QuestionDeletionLookupService,
                                               override val updateSectionService: UpdateSectionStateService,
                                               identify: IdentifierAction,
                                               getData: DataRetrievalAction,
                                               requireData: DataRequiredAction,
                                               formProvider: InvestorRatioMethodFormProvider,
                                               val controllerComponents: MessagesControllerComponents,
                                               view: InvestorRatioMethodView
                                 )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  private val form = formProvider()

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(InvestorGroupsPage, idx) { investorGroups =>
      Future.successful(Ok(view(
        form = investorGroups.ratioMethod.fold(form)(form.fill),
        postAction = routes.InvestorRatioMethodController.onSubmit(idx, mode)
      )))
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(
          form = formWithErrors,
          postAction = routes.InvestorRatioMethodController.onSubmit(idx, mode)
        ))),
      value => {
        answerFor(InvestorGroupsPage, idx) { investorGroups =>
          val updatedModel = investorGroups.copy(ratioMethod = Some(value))
          save(InvestorGroupsPage, updatedModel, NormalMode, Some(idx)).map { userAnswers =>
            Redirect(navigator.nextPage(InvestorRatioMethodPage, mode, userAnswers, Some(idx)))
          }
        }
      }
    )
  }
}
