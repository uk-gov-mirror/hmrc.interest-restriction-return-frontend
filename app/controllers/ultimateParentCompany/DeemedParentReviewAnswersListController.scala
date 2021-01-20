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
import forms.ultimateParentCompany.DeemedParentReviewAnswersListFormProvider
import models.NormalMode
import models.requests.DataRequest
import navigation.UltimateParentCompanyNavigator
import pages.ultimateParentCompany.DeemedParentPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import utils.DeemedParentReviewAnswersListHelper
import views.html.ultimateParentCompany.DeemedParentReviewAnswersListView

import scala.concurrent.Future

class DeemedParentReviewAnswersListController @Inject()(override val messagesApi: MessagesApi,
                                                        override val sessionRepository: SessionRepository,
                                                        override val navigator: UltimateParentCompanyNavigator,
                                                        override val updateSectionService: UpdateSectionStateService,
                                                        identify: IdentifierAction,
                                                        getData: DataRetrievalAction,
                                                        requireData: DataRequiredAction,
                                                        val controllerComponents: MessagesControllerComponents,
                                                        formProvider: DeemedParentReviewAnswersListFormProvider,
                                                        view: DeemedParentReviewAnswersListView
                                                    )(implicit appConfig: FrontendAppConfig)
  extends BaseNavigationController {

  private def deemedParents(implicit request: DataRequest[_]) = new DeemedParentReviewAnswersListHelper(request.userAnswers).rows

  private def renderView(form: Form[Boolean] = formProvider())(implicit request: DataRequest[_]) =
    view(form, deemedParents, routes.DeemedParentReviewAnswersListController.onSubmit())

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    if(deemedParents.length > 0) {
      Ok(renderView())
    } else {
      Redirect(navigator.addParent(0))
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    if(deemedParents.length < 3) {
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(renderView(formWithErrors)))
        ,
        {
          case true => Future.successful(Redirect(navigator.addParent(deemedParents.length)))
          case false => Future.successful(Redirect(navigator.nextPage(DeemedParentPage,NormalMode,request.userAnswers)))
        }
      )
    } else {
      Future.successful(Redirect(navigator.nextPage(DeemedParentPage,NormalMode,request.userAnswers)))
    }
  }
}
