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
import controllers.BaseController
import forms.ukCompanies.AddRestrictionAmountFormProvider
import handlers.ErrorHandler
import models.Mode
import navigation.UkCompaniesNavigator
import pages.ukCompanies.{AddRestrictionAmountPage, UkCompaniesPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.AddRestrictionAmountView

import javax.inject.Inject
import scala.concurrent.Future

class AddRestrictionAmountController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: UkCompaniesNavigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: AddRestrictionAmountFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: AddRestrictionAmountView
                                 )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController with FeatureSwitching {

  def onPageLoad(idx: Int, restrictionIdx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      Future.successful(Ok(view(
        form = fillForm(AddRestrictionAmountPage(idx, restrictionIdx), formProvider()), 
        companyName = ukCompany.companyDetails.companyName,
        postAction = routes.AddRestrictionAmountController.onSubmit(idx, restrictionIdx, mode)
      )))
    }
  }

  def onSubmit(idx: Int, restrictionIdx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(
            BadRequest(view(
              form = formWithErrors, 
              companyName = ukCompany.companyDetails.companyName,
              postAction = routes.AddRestrictionAmountController.onSubmit(idx, restrictionIdx, mode)
            ))
          ),
        value =>
            for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(AddRestrictionAmountPage(idx, restrictionIdx), value))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextRestrictionPage(AddRestrictionAmountPage(idx, restrictionIdx), mode, updatedAnswers))
      )
    }
  }
}
