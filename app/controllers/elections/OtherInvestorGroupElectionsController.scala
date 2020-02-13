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

package controllers.elections

import config.FrontendAppConfig
import controllers.actions._
import forms.elections.OtherInvestorGroupElectionsFormProvider
import javax.inject.Inject
import models.OtherInvestorGroupElections
import models.Mode
import pages.elections.{InvestorRatioMethodPage, OtherInvestorGroupElectionsPage}
import config.featureSwitch.FeatureSwitching
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import play.api.data.Form
import views.html.elections.OtherInvestorGroupElectionsView
import navigation.ElectionsNavigator
import services.QuestionDeletionLookupService
import controllers.BaseNavigationController
import handlers.ErrorHandler

import scala.concurrent.Future

class OtherInvestorGroupElectionsController @Inject()(
                                       override val messagesApi: MessagesApi,
                                       val sessionRepository: SessionRepository,
                                       val navigator: ElectionsNavigator,
                                       val questionDeletionLookupService: QuestionDeletionLookupService,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: OtherInvestorGroupElectionsFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: OtherInvestorGroupElectionsView
                                   )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(InvestorRatioMethodPage) { investorRatioMethod =>
      Future.successful(Ok(view(
        fillForm(OtherInvestorGroupElectionsPage, formProvider()),
        mode,
        investorRatioMethod
      )))
    }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        answerFor(InvestorRatioMethodPage) { investorRatioMethod =>
          Future.successful(BadRequest(view(formWithErrors, mode, investorRatioMethod)))
        },
      value =>
        saveAndRedirect(OtherInvestorGroupElectionsPage, value, mode)
    )
  }
}
