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

package controllers

import config.FrontendAppConfig
import controllers.actions._
import forms.RevisingReturnFormProvider
import javax.inject.Inject
import models.Mode
import navigation.HelloWorldNavigator
import pages.RevisingReturnPage
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.html.RevisingReturnView
import nunjucks.Renderer
import nunjucks.viewmodels.YesNoRadioViewModel
import nunjucks.RevisingReturnTemplate
import play.api.data.Form
import play.api.libs.json.Json
import uk.gov.hmrc.viewmodels.Radios

import scala.concurrent.Future

class RevisingReturnController @Inject()(
                                          override val messagesApi: MessagesApi,
                                          sessionRepository: SessionRepository,
                                          navigator: HelloWorldNavigator,
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: RevisingReturnFormProvider,
                                          val controllerComponents: MessagesControllerComponents,
                                          view: RevisingReturnView,
                                          renderer: Renderer
                                 )(implicit appConfig: FrontendAppConfig) extends BaseController with NunjucksSupport with FeatureSwitching {

  private def viewHtml(form: Form[Boolean], mode: Mode)(implicit request: Request[_]) = if(isEnabled(UseNunjucks)) {
      renderer.render(RevisingReturnTemplate, Json.toJsObject(YesNoRadioViewModel(form, mode)))
    } else {
      Future.successful(view(form, mode))
    }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      viewHtml(fillForm(RevisingReturnPage, formProvider()), mode).map(Ok(_))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      formProvider().bindFromRequest().fold(
        formWithErrors =>
          viewHtml(formWithErrors, mode).map(BadRequest(_)),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(RevisingReturnPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(RevisingReturnPage, mode, updatedAnswers))
      )
  }
}
