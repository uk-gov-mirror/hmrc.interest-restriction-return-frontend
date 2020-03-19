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

package controllers.ukCompanies

import config.FrontendAppConfig
import controllers.actions._
import forms.ukCompanies.CompanyAccountingPeriodSameAsGroupFormProvider
import javax.inject.Inject
import models.Mode
import pages.ukCompanies.CompanyAccountingPeriodSameAsGroupPage
import config.featureSwitch.{FeatureSwitching}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.CompanyAccountingPeriodSameAsGroupView
import play.api.data.Form
import scala.concurrent.Future
import navigation.UkCompaniesNavigator
import services.{QuestionDeletionLookupService, UpdateSectionStateService}
import controllers.BaseNavigationController

class CompanyAccountingPeriodSameAsGroupController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         override val sessionRepository: SessionRepository,
                                         override val navigator: UkCompaniesNavigator,
                                         override val questionDeletionLookupService: QuestionDeletionLookupService,
                                         override val updateSectionService: UpdateSectionStateService,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: CompanyAccountingPeriodSameAsGroupFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: CompanyAccountingPeriodSameAsGroupView
                                 )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(CompanyAccountingPeriodSameAsGroupPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        saveAndRedirect(CompanyAccountingPeriodSameAsGroupPage, value, mode)
    )
  }
}
