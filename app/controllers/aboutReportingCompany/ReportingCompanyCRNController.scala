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

package controllers.aboutReportingCompany

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import connectors.httpParsers.{InvalidCRN, ValidCRN}
import controllers.BaseNavigationController
import controllers.actions._
import forms.aboutReportingCompany.ReportingCompanyCRNFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.AboutReportingCompanyNavigator
import pages.aboutReportingCompany.ReportingCompanyCRNPage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.CRNValidationService
import views.html.aboutReportingCompany.ReportingCompanyCRNView

import scala.concurrent.Future

class ReportingCompanyCRNController @Inject()(override val messagesApi: MessagesApi,
                                              val sessionRepository: SessionRepository,
                                              val navigator: AboutReportingCompanyNavigator,
                                              identify: IdentifierAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              formProvider: ReportingCompanyCRNFormProvider,
                                              val controllerComponents: MessagesControllerComponents,
                                              view: ReportingCompanyCRNView,
                                              crnValidationService: CRNValidationService,
                                              errorHandler: ErrorHandler
                                             )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    Ok(view(fillForm(ReportingCompanyCRNPage, formProvider()), mode))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode))),
      value =>
        crnValidationService.validateCRN(value).flatMap {
          case Right(ValidCRN) => saveAndRedirect(ReportingCompanyCRNPage, value, mode)
          case Left(InvalidCRN) => {
            val formWithError = formProvider().withError("value", "reportingCompanyCRN.error.invalid").bind(Map("value" -> value))
            Future.successful(BadRequest(view(formWithError, mode)))
          }
          case _ => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
        }
    )
  }
}
