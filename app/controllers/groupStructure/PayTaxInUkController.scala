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
import forms.groupStructure.PayTaxInUkFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.GroupStructureNavigator
import pages.groupStructure.{ParentCompanyNamePage, PayTaxInUkPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.QuestionDeletionLookupService
import views.html.groupStructure.PayTaxInUkView

import scala.concurrent.Future

class PayTaxInUkController @Inject()(override val messagesApi: MessagesApi,
                                     val sessionRepository: SessionRepository,
                                     val navigator: GroupStructureNavigator,
                                     val questionDeletionLookupService: QuestionDeletionLookupService,
                                     identify: IdentifierAction,
                                     getData: DataRetrievalAction,
                                     requireData: DataRequiredAction,
                                     formProvider: PayTaxInUkFormProvider,
                                     val controllerComponents: MessagesControllerComponents,
                                     view: PayTaxInUkView
                                    )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(id: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(ParentCompanyNamePage, Some(id)) { name =>
      Future.successful(Ok(view(fillForm(PayTaxInUkPage, formProvider()), mode, name, routes.PayTaxInUkController.onSubmit(id, mode))))
    }
  }

  def onSubmit(id: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        answerFor(ParentCompanyNamePage) { name =>
          Future.successful(BadRequest(view(formWithErrors, mode, name, routes.PayTaxInUkController.onSubmit(id, mode))))
        },
      value =>
        saveAndRedirect(PayTaxInUkPage, value, mode, Some(id))
    )
  }
}
