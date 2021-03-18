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

import controllers.actions._
import forms.ukCompanies.RestrictionAmountForAccountingPeriodFormProvider

import javax.inject.Inject
import models.Mode
import pages.ukCompanies.{CompanyAccountingPeriodEndDatePage, RestrictionAmountForAccountingPeriodPage, UkCompaniesPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.RestrictionAmountForAccountingPeriodView
import config.FrontendAppConfig
import play.api.data.Form
import controllers.BaseController
import config.featureSwitch.FeatureSwitching
import handlers.ErrorHandler

import scala.concurrent.Future
import navigation.UkCompaniesNavigator

class RestrictionAmountForAccountingPeriodController @Inject()(
                                       override val messagesApi: MessagesApi,
                                       sessionRepository: SessionRepository,
                                       navigator: UkCompaniesNavigator,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: RestrictionAmountForAccountingPeriodFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: RestrictionAmountForAccountingPeriodView
                                     )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController with FeatureSwitching {

  def onPageLoad(idx: Int, restrictionIdx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      answerFor(CompanyAccountingPeriodEndDatePage(idx, restrictionIdx)) { endDate =>
        Future.successful(Ok(view(
          form = fillForm(RestrictionAmountForAccountingPeriodPage(idx, restrictionIdx), formProvider()),
          companyName = ukCompany.companyDetails.companyName,
          endDate = endDate,
          postAction = routes.RestrictionAmountForAccountingPeriodController.onSubmit(idx, restrictionIdx, mode)
        )))
      }
    }

  }

  def onSubmit(idx: Int, restrictionIdx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      answerFor(CompanyAccountingPeriodEndDatePage(idx, restrictionIdx)) { endDate =>
        formProvider().bindFromRequest().fold(
          formWithErrors =>
            Future.successful(
              BadRequest(view(
                form = formWithErrors,
                companyName = ukCompany.companyDetails.companyName,
                endDate = endDate,
                postAction = routes.RestrictionAmountForAccountingPeriodController.onSubmit(idx, restrictionIdx, mode)))),
          value =>
            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(RestrictionAmountForAccountingPeriodPage(idx, restrictionIdx), value))
              _ <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextRestrictionPage(RestrictionAmountForAccountingPeriodPage(idx, restrictionIdx), mode, updatedAnswers))
        )
      }
    }
  }
}
