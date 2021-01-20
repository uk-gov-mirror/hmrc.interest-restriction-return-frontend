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
import controllers.BaseController
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import forms.elections.InvestmentsReviewAnswersListFormProvider
import models.NormalMode
import models.requests.DataRequest
import navigation.ElectionsNavigator
import pages.elections.InvestmentsReviewAnswersListPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc._
import utils.InvestmentsReviewAnswersListHelper
import views.html.elections.InvestmentsReviewAnswersListView

import scala.concurrent.Future

class InvestmentsReviewAnswersListController @Inject()(override val messagesApi: MessagesApi,
                                                       navigator: ElectionsNavigator,
                                                       identify: IdentifierAction,
                                                       getData: DataRetrievalAction,
                                                       requireData: DataRequiredAction,
                                                       val controllerComponents: MessagesControllerComponents,
                                                       formProvider: InvestmentsReviewAnswersListFormProvider,
                                                       view: InvestmentsReviewAnswersListView
                                                    )(implicit appConfig: FrontendAppConfig)
  extends BaseController {

  private def investments(implicit request: DataRequest[_]) = new InvestmentsReviewAnswersListHelper(request.userAnswers).rows

  private def renderView(form: Form[Boolean] = formProvider())(implicit request: DataRequest[_]) =
    view(form, investments, routes.InvestmentsReviewAnswersListController.onSubmit())

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    if(investments.nonEmpty) {
      Ok(renderView())
    } else {
      Redirect(navigator.addInvestment(0))
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(renderView(formWithErrors)))
      ,
      {
        case true => Future.successful(Redirect(navigator.addInvestment(investments.length)))
        case false => Future.successful(Redirect(navigator.nextPage(InvestmentsReviewAnswersListPage,NormalMode,request.userAnswers)))
      }
    )
  }
}
