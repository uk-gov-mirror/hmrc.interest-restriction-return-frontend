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

import controllers.actions._
import forms.elections.InvestorGroupNameFormProvider
import javax.inject.Inject
import models.{Mode, NormalMode}
import pages.elections.{InvestorGroupNamePage, InvestorGroupsPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.elections.InvestorGroupNameView
import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching

import scala.concurrent.Future
import navigation.ElectionsNavigator
import services.UpdateSectionStateService
import controllers.BaseNavigationController
import models.returnModels.InvestorGroupModel

class InvestorGroupNameController @Inject()(override val messagesApi: MessagesApi,
                                            override val sessionRepository: SessionRepository,
                                            override val navigator: ElectionsNavigator,
                                            override val updateSectionService: UpdateSectionStateService,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            formProvider: InvestorGroupNameFormProvider,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: InvestorGroupNameView
                                           )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  private val form = formProvider()

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val investorName = getAnswer(InvestorGroupsPage, idx).map(_.investorName)
    Ok(view(investorName.fold(form)(form.fill), routes.InvestorGroupNameController.onSubmit(idx, mode)))
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    form.bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, routes.InvestorGroupNameController.onSubmit(idx, mode)))),
      value => {
        val investorGroup = getAnswer(InvestorGroupsPage, idx).fold(InvestorGroupModel(value))(_.copy(investorName = value))

        for {
          updatedAnswers <- Future.fromTry(request.userAnswers.set(InvestorGroupsPage, investorGroup, Some(idx)))
          _              <- sessionRepository.set(updatedAnswers)
        } yield Redirect(navigator.nextPage(InvestorGroupNamePage, mode, request.userAnswers, Some(idx)))
      }
    )
  }
}
