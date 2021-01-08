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

package controllers.elections

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import forms.elections.PartnershipNameFormProvider
import javax.inject.Inject
import models.Mode
import navigation.ElectionsNavigator
import pages.elections.{PartnershipNamePage, PartnershipsPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.UpdateSectionStateService
import views.html.elections.PartnershipNameView
import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching

import scala.concurrent.Future
import navigation.ElectionsNavigator
import controllers.BaseNavigationController
import models.returnModels.PartnershipModel

class PartnershipNameController @Inject()(override val messagesApi: MessagesApi,
                                          override val sessionRepository: SessionRepository,
                                          override val navigator: ElectionsNavigator,
                                          override val updateSectionService: UpdateSectionStateService,
                                          identify: IdentifierAction,
                                          getData: DataRetrievalAction,
                                          requireData: DataRequiredAction,
                                          formProvider: PartnershipNameFormProvider,
                                          val controllerComponents: MessagesControllerComponents,
                                          view: PartnershipNameView
                                         )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  private val form = formProvider()

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val partnershipName = getAnswer(PartnershipsPage, idx).map(_.name)
    Ok(view(partnershipName.fold(form)(form.fill), routes.PartnershipNameController.onSubmit(idx, mode)))
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, routes.PartnershipNameController.onSubmit(idx, mode)))),
      value => {
        val partnership = getAnswer(PartnershipsPage, idx).fold(PartnershipModel(value))(_.copy(name = value))
        for {
          updatedAnswers <- Future.fromTry(request.userAnswers.set(PartnershipsPage, partnership, Some(idx)))
          _ <- sessionRepository.set(updatedAnswers)
        } yield Redirect(navigator.nextPage(PartnershipNamePage, mode, updatedAnswers, Some(idx)))
      }
    )
  }
}
