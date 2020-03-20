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

package controllers.ultimateParentCompany

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.ultimateParentCompany.ReportingCompanySameAsParentFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.UltimateParentCompanyNavigator
import pages.aboutReturn.ReportingCompanyNamePage
import pages.ultimateParentCompany.ReportingCompanySameAsParentPage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionStateService}
import views.html.ultimateParentCompany.ReportingCompanySameAsParentView

import scala.concurrent.Future

class ReportingCompanySameAsParentController @Inject()(override val messagesApi: MessagesApi,
                                                       override val sessionRepository: SessionRepository,
                                                       override val navigator: UltimateParentCompanyNavigator,
                                                       override val questionDeletionLookupService: QuestionDeletionLookupService,
                                                       override val updateSectionService: UpdateSectionStateService,
                                                       identify: IdentifierAction,
                                                       getData: DataRetrievalAction,
                                                       requireData: DataRequiredAction,
                                                       formProvider: ReportingCompanySameAsParentFormProvider,
                                                       val controllerComponents: MessagesControllerComponents,
                                                       view: ReportingCompanySameAsParentView
                                                      )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler)
  extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(ReportingCompanyNamePage) { name =>
      Future.successful(Ok(view(
        fillForm(ReportingCompanySameAsParentPage, formProvider()), mode, name, routes.ReportingCompanySameAsParentController.onSubmit(mode)
      )))
    }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        answerFor(ReportingCompanyNamePage) { name =>
          Future.successful(BadRequest(view(formWithErrors, mode, name, routes.ReportingCompanySameAsParentController.onSubmit(mode))))
        },
      value =>
        saveAndRedirect(ReportingCompanySameAsParentPage, value, mode)
    )
  }
}
