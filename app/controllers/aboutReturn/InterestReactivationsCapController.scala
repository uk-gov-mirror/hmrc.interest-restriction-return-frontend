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

package controllers.aboutReturn

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseController
import controllers.actions._
import forms.aboutReturn.InterestReactivationsCapFormProvider
import javax.inject.Inject
import models.Mode
import navigation.AboutReturnNavigator
import pages.aboutReturn.InterestReactivationsCapPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.aboutReturn.InterestReactivationsCapView

import scala.concurrent.Future

class InterestReactivationsCapController @Inject()(
                                                    override val messagesApi: MessagesApi,
                                                    sessionRepository: SessionRepository,
                                                    navigator: AboutReturnNavigator,
                                                    identify: IdentifierAction,
                                                    getData: DataRetrievalAction,
                                                    requireData: DataRequiredAction,
                                                    formProvider: InterestReactivationsCapFormProvider,
                                                    val controllerComponents: MessagesControllerComponents,
                                                    view: InterestReactivationsCapView
                                                  )(implicit appConfig: FrontendAppConfig) extends BaseController  with FeatureSwitching {

  private def viewHtml(form: Form[_], mode: Mode)(implicit request: Request[_]) = Future.successful(view(form, mode))

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      viewHtml(fillForm(InterestReactivationsCapPage, formProvider()), mode).map(Ok(_))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      formProvider().bindFromRequest().fold(
        formWithErrors =>
          viewHtml(formWithErrors, mode).map(BadRequest(_)),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(InterestReactivationsCapPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(InterestReactivationsCapPage, mode, updatedAnswers))
      )
  }
}
