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
import forms.elections.AddInvestorGroupFormProvider
import javax.inject.Inject
import models.Mode
import navigation.ElectionsNavigator
import pages.elections.AddInvestorGroupPage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionStateService}
import views.html.elections.AddInvestorGroupView

import scala.concurrent.Future

class AddInvestorGroupController @Inject()(override val messagesApi: MessagesApi,
                                           override val sessionRepository: SessionRepository,
                                           override val navigator: ElectionsNavigator,
                                           override val questionDeletionLookupService: QuestionDeletionLookupService,
                                           override val updateSectionService: UpdateSectionStateService,
                                           identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           formProvider: AddInvestorGroupFormProvider,
                                           val controllerComponents: MessagesControllerComponents,
                                           view: AddInvestorGroupView
                                          )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(AddInvestorGroupPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        saveAndRedirect(AddInvestorGroupPage, value, mode)
    )
  }
}
