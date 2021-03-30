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
import controllers.BaseController
import controllers.actions._
import forms.ukCompanies.RestrictionAmountSameAPFormProvider
import handlers.ErrorHandler

import javax.inject.Inject
import models.Mode
import navigation.UkCompaniesNavigator
import pages.aboutReturn.AccountingPeriodPage
import pages.ukCompanies.{RestrictionAmountSameAPPage, UkCompaniesPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ukCompanies.RestrictionAmountSameAPView
import models.returnModels.fullReturn.AllocatedRestrictionsModel

import scala.concurrent.Future

class RestrictionAmountSameAPController @Inject()(
                                       override val messagesApi: MessagesApi,
                                       sessionRepository: SessionRepository,
                                       navigator: UkCompaniesNavigator,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: RestrictionAmountSameAPFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: RestrictionAmountSameAPView
                                     )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(UkCompaniesPage, idx) { ukCompany =>
      val netTaxInterestExpense : BigDecimal = ukCompany.netTaxInterest.getOrElse(0)

      Future.successful(
        Ok(view(
          form = ukCompany.allocatedRestrictions.flatMap(_.disallowanceAp1).fold(formProvider(netTaxInterestExpense))(formProvider(netTaxInterestExpense).fill),
          companyName = ukCompany.companyDetails.companyName,
          postAction = routes.RestrictionAmountSameAPController.onSubmit(idx, mode)
        ))
      )
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(AccountingPeriodPage) { groupAccountPeriod =>
      answerFor(UkCompaniesPage, idx) { ukCompany =>
        val netTaxInterestExpense : BigDecimal = ukCompany.netTaxInterest.getOrElse(0)

        formProvider(netTaxInterestExpense).bindFromRequest().fold(
          formWithErrors =>
            Future.successful(
              BadRequest(view(
                form = formWithErrors,
                companyName = ukCompany.companyDetails.companyName,
                postAction = routes.RestrictionAmountSameAPController.onSubmit(idx, mode)
              ))
            ),
          value => {
            val updatedRestrictions = ukCompany.allocatedRestrictions match {
              case Some(restrictions) => Some(restrictions.setRestriction(1, groupAccountPeriod.endDate, value))
              case None => Some(AllocatedRestrictionsModel().setRestriction(1, groupAccountPeriod.endDate, value))
            }
            val updatedModel = ukCompany.copy(allocatedRestrictions = updatedRestrictions)

            for {
              updatedAnswers <- Future.fromTry(request.userAnswers.set(UkCompaniesPage, updatedModel, Some(idx)))
              _              <- sessionRepository.set(updatedAnswers)
            } yield Redirect(navigator.nextPage(RestrictionAmountSameAPPage, mode, updatedAnswers, Some(idx)))
          }
        )
      }
    }
  }
}
