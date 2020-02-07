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

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import javax.inject.Inject
import navigation.{AboutReportingCompanyNavigator, AboutReturnNavigator, StartReturnNavigator}
import pages.{IndexPage, Page}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.SavedReturnView

import scala.concurrent.{ExecutionContext, Future}

class SavedReturnController @Inject()(override val messagesApi: MessagesApi,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      val controllerComponents: MessagesControllerComponents,
                                      val sessionRepository: SessionRepository,
                                      view: SavedReturnView,
                                      startReturnNavigator: StartReturnNavigator,
                                      aboutReportingCompanyNavigator: AboutReportingCompanyNavigator,
                                      aboutReturnNavigator: AboutReturnNavigator
                                     )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig) extends BaseController {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
      val savedTilDate = LocalDate.now().plusDays(appConfig.cacheTtlDays).format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))
      Ok(view(savedTilDate))
  }

  def nextUnansweredPage: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    val lastSavedPage = request.userAnswers.lastPageSaved.fold[Page](IndexPage)(page => page)
    val allRoutesMap = startReturnNavigator.normalRoutes ++
      aboutReportingCompanyNavigator.normalRoutes ++
      aboutReturnNavigator.normalRoutes

    Redirect(allRoutesMap(lastSavedPage)(request.userAnswers))
  }

  def deleteAndStartAgain: Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    sessionRepository.delete(request.userAnswers).map(_ =>
      Redirect(controllers.routes.IndexController.onPageLoad())
    )
  }
}
