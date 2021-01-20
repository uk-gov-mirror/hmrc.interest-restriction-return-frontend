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

package controllers.ukCompanies

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import handlers.ErrorHandler
import javax.inject.Inject
import models.NormalMode
import models.Section.UkCompanies
import navigation.UkCompaniesNavigator
import pages.ukCompanies.{CheckAnswersUkCompanyPage, UkCompaniesPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import utils.CheckYourAnswersUkCompanyHelper
import views.ViewUtils._
import views.html.CheckYourAnswersView

import scala.concurrent.Future

class CheckAnswersUkCompanyController @Inject()(override val messagesApi: MessagesApi,
                                                override val sessionRepository: SessionRepository,
                                                override val navigator: UkCompaniesNavigator,
                                                override val updateSectionService: UpdateSectionStateService,
                                                identify: IdentifierAction,
                                                getData: DataRetrievalAction,
                                                requireData: DataRequiredAction,
                                                val controllerComponents: MessagesControllerComponents,
                                                view: CheckYourAnswersView
                                               )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler)
  extends FrontendBaseController with I18nSupport with FeatureSwitching with BaseNavigationController {

  def onPageLoad(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      val checkYourAnswersHelper = new CheckYourAnswersUkCompanyHelper(request.userAnswers)
      answerFor(UkCompaniesPage, idx) { ukCompany =>
        Future.successful(Ok(view(
          answers = checkYourAnswersHelper.rows(idx),
          section = UkCompanies,
          postAction = controllers.ukCompanies.routes.CheckAnswersUkCompanyController.onSubmit(idx),
          headingMsgArgs = Seq(addPossessive(ukCompany.companyDetails.companyName)),
          buttonMsg = "ukCompanies.checkYourAnswers.button"
        )))
      }
  }

  def onSubmit(idx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request => Future.successful(Redirect(navigator.nextPage(CheckAnswersUkCompanyPage,NormalMode,request.userAnswers)))
  }
}
