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

package controllers

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.ContinueSavedReturnFormProvider
import javax.inject.Inject
import models.ContinueSavedReturn.{ContinueReturn, NewReturn}
import models.NormalMode
import navigation.{AboutReturnNavigator, GroupLevelInformationNavigator}
import pages.ContinueSavedReturnPage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ContinueSavedReturnView

class ContinueSavedReturnController @Inject()(override val messagesApi: MessagesApi,
                                              sessionRepository: SessionRepository,
                                              identify: IdentifierAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              formProvider: ContinueSavedReturnFormProvider,
                                              val controllerComponents: MessagesControllerComponents,
                                              view: ContinueSavedReturnView,
                                              aboutReturnNavigator: AboutReturnNavigator,
                                              groupLevelInformationNavigator: GroupLevelInformationNavigator
                                             )(implicit appConfig: FrontendAppConfig) extends BaseController with FeatureSwitching {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(ContinueSavedReturnPage, formProvider()), NormalMode))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        BadRequest(view(formWithErrors, NormalMode)),
      {
        case NewReturn => Redirect(controllers.routes.SavedReturnController.deleteAndStartAgain())
        case ContinueReturn => Redirect(controllers.reviewAndComplete.routes.ReviewAndCompleteController.onPageLoad())
      }
    )
  }
}
