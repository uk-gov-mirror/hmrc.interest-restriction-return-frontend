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

package controllers.groupStructure

import javax.inject.Inject

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseController
import controllers.actions._
import forms.groupStructure.RegisteredForTaxInAnotherCountryFormProvider
import models.Mode
import navigation.GroupStructureNavigator
import pages.groupStructure.RegisteredForTaxInAnotherCountryPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.groupStructure.RegisteredForTaxInAnotherCountryView

import scala.concurrent.Future

class RegisteredForTaxInAnotherCountryController @Inject()(
                                                            override val messagesApi: MessagesApi,
                                                            sessionRepository: SessionRepository,
                                                            navigator: GroupStructureNavigator,
                                                            identify: IdentifierAction,
                                                            getData: DataRetrievalAction,
                                                            requireData: DataRequiredAction,
                                                            formProvider: RegisteredForTaxInAnotherCountryFormProvider,
                                                            val controllerComponents: MessagesControllerComponents,
                                                            view: RegisteredForTaxInAnotherCountryView
                                 )(implicit appConfig: FrontendAppConfig) extends BaseController with FeatureSwitching {

  private def viewHtml(form: Form[Boolean], mode: Mode)(implicit request: Request[_]) = Future.successful(view(form, mode))

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      viewHtml(fillForm(RegisteredForTaxInAnotherCountryPage, formProvider()), mode).map(Ok(_))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      formProvider().bindFromRequest().fold(
        formWithErrors =>
          viewHtml(formWithErrors, mode).map(BadRequest(_)),

        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(RegisteredForTaxInAnotherCountryPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(RegisteredForTaxInAnotherCountryPage, mode, updatedAnswers))
      )
  }
}
