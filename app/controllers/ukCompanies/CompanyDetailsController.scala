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

import controllers.actions._
import forms.ukCompanies.CompanyDetailsFormProvider
import javax.inject.Inject
import models.Mode
import pages.ukCompanies.{CompanyDetailsPage, UkCompaniesPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.ukCompanies.CompanyDetailsView
import config.FrontendAppConfig
import play.api.data.Form
import config.featureSwitch.FeatureSwitching

import scala.concurrent.Future
import navigation.UkCompaniesNavigator
import services.QuestionDeletionLookupService
import controllers.BaseNavigationController
import models.returnModels.CompanyNameModel

class CompanyDetailsController @Inject()(
                                       override val messagesApi: MessagesApi,
                                       val sessionRepository: SessionRepository,
                                       val navigator: UkCompaniesNavigator,
                                       val questionDeletionLookupService: QuestionDeletionLookupService,
                                       identify: IdentifierAction,
                                       getData: DataRetrievalAction,
                                       requireData: DataRequiredAction,
                                       formProvider: CompanyDetailsFormProvider,
                                       val controllerComponents: MessagesControllerComponents,
                                       view: CompanyDetailsView
                                    )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val form = formProvider()
    answerFor(UkCompaniesPage, idx) { ukCompanyModel =>
      Future.successful(Ok(view(
        form = (ukCompanyModel.companyName.map(_.name), ukCompanyModel.ctutr.map(_.utr)).fold(form)(form.fill),
        mode = mode,
        postAction = routes.CompanyDetailsController.onSubmit(idx, mode))
      ))
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(
          form = formWithErrors,
          mode = mode,
          postAction = routes.CompanyDetailsController.onSubmit(idx, mode)
        ))),
      value => {
        answerFor(UkCompaniesPage, idx) { deemedParentModel =>
          val updatedModel = deemedParentModel.copy(companyName = CompanyNameModel(value))
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(UkCompaniesPage, updatedModel, Some(idx)))
            _ <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(CompanyDetailsPage, mode, updatedAnswers, Some(idx)))
        }
      }
    )
  }
}
