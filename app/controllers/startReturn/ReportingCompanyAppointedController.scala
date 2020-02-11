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

package controllers.startReturn

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.startReturn.ReportingCompanyAppointedFormProvider
import javax.inject.Inject
import models.Mode
import navigation.StartReturnNavigator
import pages.startReturn.ReportingCompanyAppointedPage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.QuestionDeletionLookupService
import views.html.startReturn.ReportingCompanyAppointedView

import scala.concurrent.Future

class ReportingCompanyAppointedController @Inject()(override val messagesApi: MessagesApi,
                                                    val sessionRepository: SessionRepository,
                                                    val navigator: StartReturnNavigator,
                                                    val questionDeletionLookupService: QuestionDeletionLookupService,
                                                    identify: IdentifierAction,
                                                    getData: DataRetrievalAction,
                                                    requireData: DataRequiredAction,
                                                    formProvider: ReportingCompanyAppointedFormProvider,
                                                    val controllerComponents: MessagesControllerComponents,
                                                    view: ReportingCompanyAppointedView
                                                   )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(ReportingCompanyAppointedPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        saveAndRedirect(ReportingCompanyAppointedPage, value, mode)
    )
  }
}
