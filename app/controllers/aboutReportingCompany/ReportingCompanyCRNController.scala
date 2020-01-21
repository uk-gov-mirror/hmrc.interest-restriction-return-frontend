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
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import connectors.httpParsers.{InvalidCRN, ValidCRN}
import controllers.BaseController
import controllers.actions._
import forms.aboutReportingCompany.ReportingCompanyCRNFormProvider
import javax.inject.Inject
import models.Mode
import models.requests.DataRequest
import navigation.AboutReportingCompanyNavigator
import nunjucks.viewmodels.BasicFormViewModel
import nunjucks.{Renderer, ReportingCompanyCRNTemplate}
import pages.aboutReportingCompany.ReportingCompanyCRNPage
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.libs.json.Json
import play.api.mvc._
import repositories.SessionRepository
import services.InterestRestrictionReturnService
import uk.gov.hmrc.nunjucks.NunjucksSupport
import views.html.aboutReportingCompany.ReportingCompanyCRNView
import handlers.ErrorHandler

import scala.concurrent.Future

class ReportingCompanyCRNController @Inject()(override val messagesApi: MessagesApi,
                                              sessionRepository: SessionRepository,
                                              navigator: AboutReportingCompanyNavigator,
                                              identify: IdentifierAction,
                                              getData: DataRetrievalAction,
                                              requireData: DataRequiredAction,
                                              formProvider: ReportingCompanyCRNFormProvider,
                                              val controllerComponents: MessagesControllerComponents,
                                              view: ReportingCompanyCRNView,
                                              renderer: Renderer,
                                              interestRestrictionReturnService: InterestRestrictionReturnService,
                                              errorHandler: ErrorHandler
                                             )(implicit appConfig: FrontendAppConfig) extends BaseController with NunjucksSupport with FeatureSwitching {

  private def viewHtml(form: Form[_], mode: Mode)(implicit request: Request[_]) = if(isEnabled(UseNunjucks)) {
    renderer.render(ReportingCompanyCRNTemplate, Json.toJsObject(BasicFormViewModel(form, mode)))
  } else {
    Future.successful(view(form, mode))
  }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      viewHtml(fillForm(ReportingCompanyCRNPage, formProvider()), mode).map(Ok(_))
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      formProvider().bindFromRequest().fold(
        formWithErrors =>
          viewHtml(formWithErrors, mode).map(BadRequest(_)),

        value =>
          interestRestrictionReturnService.validateCRN(value).flatMap{
            case Right(ValidCRN) =>
              for {
                updatedAnswers <- Future.fromTry(request.userAnswers.set(ReportingCompanyCRNPage, value))
                _              <- sessionRepository.set(updatedAnswers)
              } yield Redirect(navigator.nextPage(ReportingCompanyCRNPage, mode, updatedAnswers))
            case Left(InvalidCRN) => viewHtml(formProvider(), mode).map(BadRequest(_))
            case _ => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
          }
      )
  }
}
