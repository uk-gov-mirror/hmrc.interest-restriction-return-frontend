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
import controllers.BaseController
import controllers.actions._
import forms.elections.PartnershipsReviewAnswersListFormProvider

import javax.inject.Inject
import models.NormalMode
import models.requests.DataRequest
import navigation.ElectionsNavigator
import pages.elections.PartnershipsReviewAnswersListPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc._
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import utils.PartnershipsReviewAnswersListHelper
import views.html.elections.PartnershipsReviewAnswersListView

class PartnershipsReviewAnswersListController @Inject()(override val messagesApi: MessagesApi,
                                                        navigator: ElectionsNavigator,
                                                        identify: IdentifierAction,
                                                        getData: DataRetrievalAction,
                                                        requireData: DataRequiredAction,
                                                        formProvider: PartnershipsReviewAnswersListFormProvider,
                                                        val controllerComponents: MessagesControllerComponents,
                                                        view: PartnershipsReviewAnswersListView
                                                       )(implicit appConfig: FrontendAppConfig) extends BaseController with FeatureSwitching {

  val form: Form[Boolean] = formProvider()

  private def partnerships(implicit request: DataRequest[_]): Seq[SummaryListRow] = new PartnershipsReviewAnswersListHelper(request.userAnswers).rows

  private def renderView(form: Form[Boolean] = form)(implicit request: DataRequest[_]) =
  view(form, partnerships, routes.PartnershipsReviewAnswersListController.onSubmit())

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    if(partnerships.nonEmpty) {
      Ok(renderView())
    } else {
      Redirect(navigator.addPartnership(idx = 0))
    }
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        BadRequest(renderView(formWithErrors)),
      {
        case true => Redirect(navigator.addPartnership(partnerships.length))
        case false => Redirect(navigator.nextPage(PartnershipsReviewAnswersListPage, NormalMode, request.userAnswers))
      }
    )
  }
}
