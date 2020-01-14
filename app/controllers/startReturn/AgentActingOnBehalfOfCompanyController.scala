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
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import controllers.BaseController
import controllers.actions._
import forms.startReturn.AgentActingOnBehalfOfCompanyFormProvider
import javax.inject.Inject
import models.Mode
import navigation.HelloWorldNavigator
import nunjucks.viewmodels.YesNoRadioViewModel
import nunjucks.{AgentActingOnBehalfOfCompanyTemplate, Renderer}
import pages.startReturn.AgentActingOnBehalfOfCompanyPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.html.startReturn.AgentActingOnBehalfOfCompanyView

import scala.concurrent.Future

class AgentActingOnBehalfOfCompanyController @Inject()(
                                                        override val messagesApi: MessagesApi,
                                                        sessionRepository: SessionRepository,
                                                        navigator: HelloWorldNavigator,
                                                        identify: IdentifierAction,
                                                        getData: DataRetrievalAction,
                                                        requireData: DataRequiredAction,
                                                        formProvider: AgentActingOnBehalfOfCompanyFormProvider,
                                                        val controllerComponents: MessagesControllerComponents,
                                                        view: AgentActingOnBehalfOfCompanyView,
                                                        renderer: Renderer
                                 )(implicit appConfig: FrontendAppConfig) extends BaseController with NunjucksSupport with FeatureSwitching {

  private def viewHtml(form: Form[Boolean], mode: Mode)(implicit request: Request[_]) = if(isEnabled(UseNunjucks)) {
      renderer.render(AgentActingOnBehalfOfCompanyTemplate, Json.toJsObject(YesNoRadioViewModel(form, mode)))
    } else {
      Future.successful(view(form, mode))
    }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      viewHtml(fillForm(AgentActingOnBehalfOfCompanyPage, formProvider()), mode).map(Ok(_))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      formProvider().bindFromRequest().fold(
        formWithErrors =>
          viewHtml(formWithErrors, mode).map(BadRequest(_)),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(AgentActingOnBehalfOfCompanyPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(AgentActingOnBehalfOfCompanyPage, mode, updatedAnswers))
      )
  }
}
