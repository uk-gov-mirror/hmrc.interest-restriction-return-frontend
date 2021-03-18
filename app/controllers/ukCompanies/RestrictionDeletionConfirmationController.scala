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

package controllers.ukCompanies

import config.FrontendAppConfig
import controllers.actions._
import forms.ukCompanies.RestrictionDeletionConfirmationFormProvider
import javax.inject.Inject
import models.Mode
import pages.ukCompanies.RestrictionDeletionConfirmationPage
import config.featureSwitch.{FeatureSwitching}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.RestrictionDeletionConfirmationView
import scala.concurrent.Future
import navigation.UkCompaniesNavigator
import controllers.BaseController
import pages.ukCompanies.RestrictionQueryHelper

class RestrictionDeletionConfirmationController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: UkCompaniesNavigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: RestrictionDeletionConfirmationFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: RestrictionDeletionConfirmationView
                                 )(implicit appConfig: FrontendAppConfig) extends BaseController with FeatureSwitching {

  def onPageLoad(idx: Int, restrictionIdx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(
      form = fillForm(RestrictionDeletionConfirmationPage(idx, restrictionIdx), formProvider()), 
      postAction = routes.RestrictionDeletionConfirmationController.onSubmit(idx, restrictionIdx, mode)
    ))
  }

  def onSubmit(idx: Int, restrictionIdx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(
          form = formWithErrors, 
          postAction = routes.RestrictionDeletionConfirmationController.onSubmit(idx, restrictionIdx, mode)
        ))),
      {
        case true =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.remove(RestrictionQueryHelper.restrictionPath(idx, restrictionIdx)))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextRestrictionPage(RestrictionDeletionConfirmationPage(idx, restrictionIdx), mode, updatedAnswers))
        case false => 
          Future.successful(Redirect(navigator.nextRestrictionPage(RestrictionDeletionConfirmationPage(idx, restrictionIdx), mode, request.userAnswers)))
      }
    )
  }
}
