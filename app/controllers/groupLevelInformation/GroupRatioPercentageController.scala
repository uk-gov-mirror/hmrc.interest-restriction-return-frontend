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

package controllers.groupLevelInformation

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseController
import controllers.actions._

import forms.groupLevelInformation.GroupRatioPercentageFormProvider
import javax.inject.Inject
import models.Mode
import pages.groupLevelInformation.GroupRatioPercentagePage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import views.html.groupLevelInformation.GroupRatioPercentageView
import config.FrontendAppConfig
import play.api.data.Form

import config.featureSwitch.{FeatureSwitching}
import scala.concurrent.Future
import navigation.GroupLevelInformationNavigator

class GroupRatioPercentageController @Inject()(override val messagesApi: MessagesApi,
                                               sessionRepository: SessionRepository,
                                               navigator: GroupLevelInformationNavigator,
                                               identify: IdentifierAction,
                                               getData: DataRetrievalAction,
                                               requireData: DataRequiredAction,
                                               formProvider: GroupRatioPercentageFormProvider,
                                               val controllerComponents: MessagesControllerComponents,
                                               view: GroupRatioPercentageView
                                              )(implicit appConfig: FrontendAppConfig) extends BaseController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(GroupRatioPercentagePage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        for {
          updatedAnswers <- Future.fromTry(request.userAnswers.set(GroupRatioPercentagePage, value))
          _              <- sessionRepository.set(updatedAnswers)
        } yield Redirect(navigator.nextPage(GroupRatioPercentagePage, mode, updatedAnswers))
    )
  }
}
