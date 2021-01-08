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

package controllers.checkTotals

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseNavigationController
import controllers.actions._
import javax.inject.Inject
import models.NormalMode
import navigation.CheckTotalsNavigator
import pages.ukCompanies.{DerivedCompanyPage, UkCompaniesPage}
import play.api.Logging
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.UpdateSectionStateService
import utils.CheckTotalsHelper
import views.html.checkTotals.DerivedCompanyView

class DerivedCompanyController @Inject()(override val messagesApi: MessagesApi,
                                         override val sessionRepository: SessionRepository,
                                         override val navigator: CheckTotalsNavigator,
                                         override val updateSectionService: UpdateSectionStateService,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: DerivedCompanyView,
                                         checkTotalsHelper: CheckTotalsHelper
                                        )(implicit appConfig: FrontendAppConfig)
  extends FeatureSwitching with BaseNavigationController with Logging {

  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    request.userAnswers.getList(UkCompaniesPage) match {
      case seq if seq.isEmpty => logger.debug(s"[DerivedCompanyController][onPageLoad] GET attempt to check totals page without data")
        Redirect(controllers.routes.UnderConstructionController.onPageLoad())
      case ukCompanies => Ok(view(checkTotalsHelper.constructTotalsTable(ukCompanies),controllers.checkTotals.routes.DerivedCompanyController.onSubmit))
    }
  }

  def onSubmit: Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request => updateState(DerivedCompanyPage, NormalMode, request.userAnswers).map{ userAnswers =>
      Redirect(navigator.nextPage(DerivedCompanyPage, NormalMode, userAnswers))
    }
  }
}
