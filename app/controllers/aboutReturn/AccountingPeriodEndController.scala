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

package controllers.aboutReturn

import javax.inject.Inject
import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.aboutReturn.AccountingPeriodEndFormProvider
import models.Mode
import navigation.AboutReturnNavigator
import pages.aboutReturn.{AccountingPeriodEndPage, AccountingPeriodStartPage}
import play.api.Logging
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionStateService}
import views.html.aboutReturn.AccountingPeriodEndView

import scala.concurrent.Future

class AccountingPeriodEndController @Inject()(override val messagesApi: MessagesApi,
                                              override val sessionRepository: SessionRepository,
                                              override val navigator: AboutReturnNavigator,
                                              override val questionDeletionLookupService: QuestionDeletionLookupService,
                                              override val updateSectionService: UpdateSectionStateService,
                                              identify: IdentifierAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              formProvider: AccountingPeriodEndFormProvider,
                                              val controllerComponents: MessagesControllerComponents,
                                              view: AccountingPeriodEndView
                                             )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching with Logging {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    request.userAnswers.get(AccountingPeriodStartPage) match {
      case None => logger.debug(s"[AccountingPeriodEndController][onPageLoad] GET attempt to submit invalid AP end date")
        Redirect(controllers.routes.UnderConstructionController.onPageLoad())
      case Some(startDate) => Ok(view(fillForm(AccountingPeriodEndPage, formProvider(startDate)), mode))
    }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    request.userAnswers.get(AccountingPeriodStartPage) match {
      case None => logger.debug(s"[AccountingPeriodEndController][onSubmit] POST attempt to submit invalid AP end date")
        Future.successful(Redirect(controllers.routes.UnderConstructionController.onPageLoad()))
      case Some(startDate) => formProvider(startDate).bindFromRequest ().fold(
        formWithErrors => Future.successful(BadRequest(view(formWithErrors, mode))),
        value => saveAndRedirect(AccountingPeriodEndPage, value, mode)
      )
    }
  }
}
