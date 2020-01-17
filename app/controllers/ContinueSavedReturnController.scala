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
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import controllers.actions._
import forms.ContinueSavedReturnFormProvider
import javax.inject.Inject
import models.ContinueSavedReturn.{ContinueReturn, NewReturn}
import models.{ContinueSavedReturn, Mode, NormalMode}
import navigation.{AboutReportingCompanyNavigator, AboutReturnNavigator, Navigator, StartReturnNavigator}
import nunjucks.{ContinueSavedReturnTemplate, Renderer}
import nunjucks.viewmodels.RadioOptionsViewModel
import pages.ContinueSavedReturnPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.html.ContinueSavedReturnView

import scala.concurrent.Future

class ContinueSavedReturnController @Inject()(
                                               override val messagesApi: MessagesApi,
                                               sessionRepository: SessionRepository,
                                               navigator: Navigator,
                                               identify: IdentifierAction,
                                               getData: DataRetrievalAction,
                                               requireData: DataRequiredAction,
                                               formProvider: ContinueSavedReturnFormProvider,
                                               val controllerComponents: MessagesControllerComponents,
                                               view: ContinueSavedReturnView,
                                               renderer: Renderer,
                                               startReturnNavigator: StartReturnNavigator,
                                               aboutReportingCompanyNavigator: AboutReportingCompanyNavigator,
                                               aboutReturnNavigator: AboutReturnNavigator
                                             )(implicit appConfig: FrontendAppConfig) extends BaseController with NunjucksSupport with FeatureSwitching {

  private def viewHtml(form: Form[ContinueSavedReturn])(implicit request: Request[_]) = if (isEnabled(UseNunjucks)) {
    renderer.render(ContinueSavedReturnTemplate, Json.toJsObject(RadioOptionsViewModel(
      ContinueSavedReturn.options(form),
      form,
      NormalMode
    )))
  } else {
    Future.successful(view(form, NormalMode))
  }

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      viewHtml(fillForm(ContinueSavedReturnPage, formProvider())).map(Ok(_))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      formProvider().bindFromRequest().fold(
        formWithErrors =>
          viewHtml(formWithErrors).map(BadRequest(_)),
        {
          case NewReturn => Future.successful(Redirect(controllers.routes.SavedReturnController.deleteAndStartAgain()))
          case ContinueReturn => Future.successful(Redirect(controllers.routes.SavedReturnController.nextUnansweredPage()))
        }
      )
  }
}
