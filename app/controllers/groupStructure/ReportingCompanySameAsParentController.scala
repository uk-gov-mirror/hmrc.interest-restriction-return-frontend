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

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseController
import controllers.actions._
import forms.groupStructure.ReportingCompanySameAsParentFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import models.requests.DataRequest
import navigation.GroupStructureNavigator
import pages.aboutReportingCompany.ReportingCompanyNamePage
import pages.groupStructure.ReportingCompanySameAsParentPage
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.groupStructure.ReportingCompanySameAsParentView

import scala.concurrent.Future

class ReportingCompanySameAsParentController @Inject()(
                                      override val messagesApi: MessagesApi,
                                      sessionRepository: SessionRepository,
                                      navigator: GroupStructureNavigator,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      formProvider: ReportingCompanySameAsParentFormProvider,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: ReportingCompanySameAsParentView,
                                      errorHandler: ErrorHandler
                                 )(implicit appConfig: FrontendAppConfig) extends BaseController  with FeatureSwitching {

  private def companyNamePredicate(f: String => Future[Result])(implicit request: DataRequest[_]) =
    request.userAnswers.get(ReportingCompanyNamePage) match {
      case Some(companyName) => f(companyName)
      case _ => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
    }

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request => companyNamePredicate { name =>
      Future.successful(Ok(view(fillForm(ReportingCompanySameAsParentPage, formProvider()), mode, name)))
    }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      formProvider().bindFromRequest().fold(
        formWithErrors =>
          companyNamePredicate { name =>
            Future.successful(BadRequest(view(formWithErrors, mode, name)))
          },
        value =>
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(ReportingCompanySameAsParentPage, value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(ReportingCompanySameAsParentPage, mode, updatedAnswers))
      )
  }
}
