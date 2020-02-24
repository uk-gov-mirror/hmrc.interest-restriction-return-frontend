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
import controllers.BaseNavigationController
import controllers.actions._
import forms.groupStructure.RegisteredForTaxInAnotherCountryFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import navigation.GroupStructureNavigator
import pages.groupStructure.{DeemedParentPage, ParentCompanyNamePage, RegisteredForTaxInAnotherCountryPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.QuestionDeletionLookupService
import views.html.groupStructure.RegisteredForTaxInAnotherCountryView

import scala.concurrent.Future

class RegisteredForTaxInAnotherCountryController @Inject()(override val messagesApi: MessagesApi,
                                                           val sessionRepository: SessionRepository,
                                                           val navigator: GroupStructureNavigator,
                                                           val questionDeletionLookupService: QuestionDeletionLookupService,
                                                           identify: IdentifierAction,
                                                           getData: DataRetrievalAction,
                                                           requireData: DataRequiredAction,
                                                           formProvider: RegisteredForTaxInAnotherCountryFormProvider,
                                                           val controllerComponents: MessagesControllerComponents,
                                                           view: RegisteredForTaxInAnotherCountryView
                                                          )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(DeemedParentPage, idx) { deemedParentModel =>
      Future.successful(
        Ok(view(
          form = deemedParentModel.registeredForTaxInAnotherCountry.fold(formProvider())(formProvider().fill),
          mode = mode,
          companyName = deemedParentModel.companyName.name,
          postAction = routes.RegisteredForTaxInAnotherCountryController.onSubmit(idx, mode)
        ))
      )
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    answerFor(DeemedParentPage, idx) { deemedParentModel =>
      formProvider().bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(
            form = formWithErrors,
            mode = mode,
            companyName = deemedParentModel.companyName.name,
            postAction = routes.RegisteredForTaxInAnotherCountryController.onSubmit(idx, mode)
          ))),
        value => {
          val updatedModel = deemedParentModel.copy(registeredForTaxInAnotherCountry = Some(value))
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(DeemedParentPage, updatedModel, Some(idx)))
            _ <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(RegisteredForTaxInAnotherCountryPage, mode, updatedAnswers, Some(idx)))
        }
      )
    }
  }
}
