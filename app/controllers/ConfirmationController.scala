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

import config.{FrontendAppConfig, SessionKeys}
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import controllers.actions._
import handlers.ErrorHandler
import javax.inject.Inject
import models.requests.DataRequest
import nunjucks.viewmodels.ConfirmationViewModel
import nunjucks.{ConfirmationTemplate, Renderer}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.ConfirmationView

import scala.concurrent.{ExecutionContext, Future}

class ConfirmationController @Inject()(override val messagesApi: MessagesApi,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: ConfirmationView,
                                       renderer: Renderer,
                                       errorHandler: ErrorHandler
                                      )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig)
  extends FrontendBaseController with I18nSupport with FeatureSwitching {

  private def renderView(reference: String)(implicit request: DataRequest[_]) =
    if (isEnabled(UseNunjucks)) {
      renderer.render(ConfirmationTemplate, Json.toJsObject(ConfirmationViewModel(reference, appConfig.exitSurveyUrl)))
    } else {
      Future.successful(view(reference))
    }

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      request.session.get(SessionKeys.acknowledgementReference) match {
        case Some(ref) => renderView(ref).map(Ok(_))
        case _ => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
      }
  }
}
