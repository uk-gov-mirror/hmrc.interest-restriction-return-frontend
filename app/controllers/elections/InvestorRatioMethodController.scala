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
import config.featureSwitch.{FeatureSwitching}
import controllers.actions._
import forms.elections.InvestorRatioMethodFormProvider
import javax.inject.Inject
import models.{InvestorRatioMethod, Mode}
import pages.elections.InvestorRatioMethodPage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.elections.InvestorRatioMethodView
import play.api.data.Form

import scala.concurrent.Future
import navigation.ElectionsNavigator
import services.QuestionDeletionLookupService
import controllers.BaseNavigationController

class InvestorRatioMethodController @Inject()(
                                  override val messagesApi: MessagesApi,
                                  val sessionRepository: SessionRepository,
                                  val navigator: ElectionsNavigator,
                                  val questionDeletionLookupService: QuestionDeletionLookupService,
                                  identify: IdentifierAction,
                                  getData: DataRetrievalAction,
                                  requireData: DataRequiredAction,
                                  formProvider: InvestorRatioMethodFormProvider,
                                  val controllerComponents: MessagesControllerComponents,
                                  view: InvestorRatioMethodView
                                 )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(InvestorRatioMethodPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        saveAndRedirect(InvestorRatioMethodPage, value, mode)
    )
  }
}
