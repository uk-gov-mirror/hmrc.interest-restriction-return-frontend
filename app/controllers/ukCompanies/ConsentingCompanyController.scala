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

package controllers.ukCompanies

import config.FrontendAppConfig
import controllers.actions._
import forms.ukCompanies.ConsentingCompanyFormProvider
import javax.inject.Inject
import models.Mode
import pages.ukCompanies.{ConsentingCompanyPage, UkCompaniesPage}
import config.featureSwitch.FeatureSwitching
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.ConsentingCompanyView
import play.api.data.Form
import handlers.ErrorHandler

import scala.concurrent.Future
import navigation.UkCompaniesNavigator
import services.QuestionDeletionLookupService
import controllers.BaseNavigationController

class ConsentingCompanyController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         val sessionRepository: SessionRepository,
                                         val navigator: UkCompaniesNavigator,
                                         val questionDeletionLookupService: QuestionDeletionLookupService,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: ConsentingCompanyFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: ConsentingCompanyView
                                 )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage) { ukCompany =>
      Future.successful(
    Ok(view(fillForm(ConsentingCompanyPage, formProvider()), mode = mode,
      companyName = ukCompany.companyName.name,
      postAction = routes.ConsentingCompanyController.onSubmit(mode)
    ))
      )
    }
  }

  def onSubmit(mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage) { ukCompany =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(
            BadRequest(view(
              form = formWithErrors,
              mode = mode,
              companyName = ukCompany.companyName.name,
              postAction = routes.ConsentingCompanyController.onSubmit(mode)
            ))
          ),
        value => {
          val updatedModel = ukCompany.copy(consenting = Some(value))
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(UkCompaniesPage, updatedModel))
            _ <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(ConsentingCompanyPage, mode, updatedAnswers))
        }
      )
    }
  }
}
