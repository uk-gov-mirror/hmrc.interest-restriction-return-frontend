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
import forms.HelloWorldYesNoFormProvider
import javax.inject.Inject
import models.Mode
import navigation.Navigator
import nunjucks._
import nunjucks.viewmodels.YesNoRadioViewModel
import pages.HelloWorldYesNoPageNunjucks
import play.api.Logger
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc._
import play.twirl.api.Html
import repositories.SessionRepository
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.viewmodels.Radios

import scala.concurrent.{ExecutionContext, Future}

class HelloWorldYesNoNunjucksController @Inject()(override val messagesApi: MessagesApi,
                                                  sessionRepository: SessionRepository,
                                                  navigator: Navigator,
                                                  identify: IdentifierAction,
                                                  getData: DataRetrievalAction,
                                                  requireData: DataRequiredAction,
                                                  formProvider: HelloWorldYesNoFormProvider,
                                                  renderer: Renderer,
                                                  val controllerComponents: MessagesControllerComponents
                                                 )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig) extends BaseController with NunjucksSupport {

  private def viewHtml(form: Form[Boolean], mode: Mode)(implicit request: Request[_]): Future[Html] =
    renderer.render(HelloWorldYesNoTemplate, Json.toJsObject(YesNoRadioViewModel(form, mode)))

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    viewHtml(fillForm(HelloWorldYesNoPageNunjucks, formProvider()), mode).map(Ok(_))
  }


  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors => {
        viewHtml(formWithErrors, mode).map(BadRequest(_))
      },
      value =>
        for {
          updatedAnswers <- Future.fromTry(request.userAnswers.set(HelloWorldYesNoPageNunjucks, value))
          _              <- sessionRepository.set(updatedAnswers)
        } yield Redirect(navigator.nextPage(HelloWorldYesNoPageNunjucks, mode, updatedAnswers))
    )
  }
}
