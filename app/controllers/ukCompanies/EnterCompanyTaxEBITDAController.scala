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
import forms.ukCompanies.EnterCompanyTaxEBITDAFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.UkCompaniesNavigator
import pages.ukCompanies.{EnterCompanyTaxEBITDAPage, UkCompaniesPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.EnterCompanyTaxEBITDAView

import scala.concurrent.Future

class EnterCompanyTaxEBITDAController @Inject()(override val messagesApi: MessagesApi,
                                                override val sessionRepository: SessionRepository,
                                                override val navigator: UkCompaniesNavigator,
                                                override val updateSectionService: UpdateSectionStateService,
                                                identify: IdentifierAction,
                                                getData: DataRetrievalAction,
                                                requireData: DataRequiredAction,
                                                formProvider: EnterCompanyTaxEBITDAFormProvider,
                                                val controllerComponents: MessagesControllerComponents,
                                                view: EnterCompanyTaxEBITDAView
                                               )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx: Int) { ukCompany =>
      Future.successful(
        Ok(view(
          form = ukCompany.taxEBITDA.fold(formProvider())(formProvider().fill),
          mode = mode,
          companyName = ukCompany.companyDetails.companyName,
          postAction = routes.EnterCompanyTaxEBITDAController.onSubmit(idx, mode)
        ))
      )
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(
            BadRequest(view(
              form = formWithErrors,
              mode = mode,
              companyName = ukCompany.companyDetails.companyName,
              postAction = routes.EnterCompanyTaxEBITDAController.onSubmit(idx, mode)
            ))
          ),
        value => {
          val updatedModel = ukCompany.copy(taxEBITDA = Some(value))

          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(UkCompaniesPage, updatedModel, Some(idx)))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(EnterCompanyTaxEBITDAPage, mode, updatedAnswers))
        }
      )
    }
  }
}