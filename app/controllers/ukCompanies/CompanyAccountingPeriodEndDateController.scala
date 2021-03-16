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
import controllers.actions._
import forms.ukCompanies.CompanyAccountingPeriodEndDateFormProvider
import javax.inject.Inject
import models.Mode
import pages.ukCompanies.{CompanyAccountingPeriodEndDatePage, UkCompaniesPage}
import pages.aboutReturn.AccountingPeriodPage
import config.featureSwitch.{FeatureSwitching}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.CompanyAccountingPeriodEndDateView
import navigation.UkCompaniesNavigator
import scala.concurrent.Future
import controllers.BaseController
import handlers.ErrorHandler

class CompanyAccountingPeriodEndDateController @Inject()(
                                         override val messagesApi: MessagesApi,
                                         sessionRepository: SessionRepository,
                                         navigator: UkCompaniesNavigator,
                                         identify: IdentifierAction,
                                         getData: DataRetrievalAction,
                                         requireData: DataRequiredAction,
                                         formProvider: CompanyAccountingPeriodEndDateFormProvider,
                                         val controllerComponents: MessagesControllerComponents,
                                         view: CompanyAccountingPeriodEndDateView
                                 )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController with FeatureSwitching {

  def onPageLoad(idx: Int, restrictionIdx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      answerFor(AccountingPeriodPage) { accountingPeriod =>
        Future.successful(Ok(view(
          form = fillForm(CompanyAccountingPeriodEndDatePage(idx, restrictionIdx), formProvider(accountingPeriod.startDate)), 
          companyName = ukCompany.companyDetails.companyName,
          postAction = routes.CompanyAccountingPeriodEndDateController.onSubmit(idx, restrictionIdx, mode)
        )))
      }
    }
  }

  def onSubmit(idx: Int, restrictionIdx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      answerFor(AccountingPeriodPage) { accountingPeriod =>
        formProvider(accountingPeriod.startDate).bindFromRequest().fold(
          formWithErrors =>
            Future.successful(
              BadRequest(view(
                form = formWithErrors, 
                companyName = ukCompany.companyDetails.companyName,
                postAction = routes.CompanyAccountingPeriodEndDateController.onSubmit(idx, restrictionIdx, mode)
              ))
            ),
          value =>
              for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(CompanyAccountingPeriodEndDatePage(idx, restrictionIdx), value))
              _              <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextRestrictionPage(CompanyAccountingPeriodEndDatePage(idx, restrictionIdx), mode, updatedAnswers))
        )
      }
    }
  }
}
