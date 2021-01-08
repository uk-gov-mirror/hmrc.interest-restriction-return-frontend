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

import com.google.inject.Inject
import config.FrontendAppConfig
import controllers.BaseNavigationController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import forms.elections.InvestorGroupsReviewAnswersListFormProvider
import models.NormalMode
import models.requests.DataRequest
import navigation.ElectionsNavigator
import pages.elections.InvestorGroupsPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.UpdateSectionStateService
import utils.InvestorGroupsReviewAnswersListHelper
import views.html.elections.InvestorGroupsReviewAnswersListView

import scala.concurrent.Future

class InvestorGroupsReviewAnswersListController @Inject()(override val messagesApi: MessagesApi,
                                                          override val sessionRepository: SessionRepository,
                                                          override val navigator: ElectionsNavigator,
                                                          override val updateSectionService: UpdateSectionStateService,
                                                          identify: IdentifierAction,
                                                          getData: DataRetrievalAction,
                                                          requireData: DataRequiredAction,
                                                          val controllerComponents: MessagesControllerComponents,
                                                          formProvider: InvestorGroupsReviewAnswersListFormProvider,
                                                          view: InvestorGroupsReviewAnswersListView
                                                    )(implicit appConfig: FrontendAppConfig)
  extends BaseNavigationController {

  private def investorGroups(implicit request: DataRequest[_]) = new InvestorGroupsReviewAnswersListHelper(request.userAnswers).rows

  private def renderView(form: Form[Boolean] = formProvider())(implicit request: DataRequest[_]) =
    view(form, investorGroups, routes.InvestorGroupsReviewAnswersListController.onSubmit())

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    if(investorGroups.nonEmpty) {
      Ok(renderView())
    } else {
      Redirect(navigator.addInvestorGroup(0))
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(renderView(formWithErrors)))
      ,
      {
        case true => Future.successful(Redirect(navigator.addInvestorGroup(investorGroups.length)))
        case false => saveAndRedirect(InvestorGroupsPage, NormalMode)
      }
    )
  }
}
