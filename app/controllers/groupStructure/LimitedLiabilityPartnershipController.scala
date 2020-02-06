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

package controllers.groupStructure

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.LimitedLiabilityPartnershipFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.GroupStructureNavigator
import pages.groupStructure.{LimitedLiabilityPartnershipPage, ParentCompanyNamePage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.groupStructure.LimitedLiabilityPartnershipView

import scala.concurrent.Future

class LimitedLiabilityPartnershipController @Inject()(override val messagesApi: MessagesApi,
                                                      val sessionRepository: SessionRepository,
                                                      val navigator: GroupStructureNavigator,
                                                      identify: IdentifierAction,
                                                      getData: DataRetrievalAction,
                                                      requireData: DataRequiredAction,
                                                      formProvider: LimitedLiabilityPartnershipFormProvider,
                                                      val controllerComponents: MessagesControllerComponents,
                                                      view: LimitedLiabilityPartnershipView
                                                     )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(ParentCompanyNamePage) { name =>
      Future.successful(Ok(view(fillForm(LimitedLiabilityPartnershipPage, formProvider()), mode, name)))
    }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        answerFor(ParentCompanyNamePage) { name =>
          Future.successful(BadRequest(view(formWithErrors, mode, name)))
        },
      value =>
        saveAndRedirect(LimitedLiabilityPartnershipPage, value, mode)
    )
  }
}
