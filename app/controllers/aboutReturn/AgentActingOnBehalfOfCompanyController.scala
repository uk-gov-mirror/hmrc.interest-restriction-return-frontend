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

package controllers.aboutReturn

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import controllers.BaseController
import forms.aboutReturn.AgentActingOnBehalfOfCompanyFormProvider
import javax.inject.Inject
import models.Mode
import navigation.AboutReturnNavigator
import pages.aboutReturn.{AgentActingOnBehalfOfCompanyPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.aboutReturn.AgentActingOnBehalfOfCompanyView

import scala.concurrent.Future

class AgentActingOnBehalfOfCompanyController @Inject()(override val messagesApi: MessagesApi,
                                                       sessionRepository: SessionRepository,
                                                       navigator: AboutReturnNavigator,
                                                       identify: IdentifierAction,
                                                       getData: DataRetrievalAction,
                                                       requireData: DataRequiredAction,
                                                       formProvider: AgentActingOnBehalfOfCompanyFormProvider,
                                                       val controllerComponents: MessagesControllerComponents,
                                                       view: AgentActingOnBehalfOfCompanyView
                                                      )(implicit appConfig: FrontendAppConfig) extends BaseController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(AgentActingOnBehalfOfCompanyPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        for {
          updatedAnswers <- Future.fromTry(request.userAnswers.set(AgentActingOnBehalfOfCompanyPage, value))
          _              <- sessionRepository.set(updatedAnswers)
        } yield Redirect(navigator.nextPage(AgentActingOnBehalfOfCompanyPage, mode, updatedAnswers))
    )
  }
}
