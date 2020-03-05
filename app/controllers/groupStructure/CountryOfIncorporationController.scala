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
import forms.groupStructure.CountryOfIncorporationFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import models.returnModels.CountryCodeModel
import navigation.GroupStructureNavigator
import pages.groupStructure.{CountryOfIncorporationPage, DeemedParentPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import services.QuestionDeletionLookupService
import views.html.groupStructure.CountryOfIncorporationView

import scala.concurrent.Future

class CountryOfIncorporationController @Inject()(override val messagesApi: MessagesApi,
                                                 val sessionRepository: SessionRepository,
                                                 val navigator: GroupStructureNavigator,
                                                 val questionDeletionLookupService: QuestionDeletionLookupService,
                                                 identify: IdentifierAction,
                                                 getData: DataRetrievalAction,
                                                 requireData: DataRequiredAction,
                                                 formProvider: CountryOfIncorporationFormProvider,
                                                 val controllerComponents: MessagesControllerComponents,
                                                 view: CountryOfIncorporationView
                                                )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseNavigationController {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val form = formProvider()
    answerFor(DeemedParentPage, idx) { deemedParentModel =>
      Future.successful(
        Ok(view(
          form = deemedParentModel.countryOfIncorporation.map(_.country).fold(form)(form.fill),
          mode = mode,
           companyName = deemedParentModel.companyName.name,
          postAction = routes.CountryOfIncorporationController.onSubmit(idx, mode)
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
            postAction = routes.CountryOfIncorporationController.onSubmit(idx, mode)
          ))),
        value => {
          val countryModel = if(value.isEmpty) None else Some(CountryCodeModel(appConfig.countryCodeMap.map(_.swap).apply(value), value))
          val updatedModel = deemedParentModel.copy(countryOfIncorporation = countryModel)
          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(DeemedParentPage, updatedModel, Some(idx)))
            _ <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(CountryOfIncorporationPage, mode, updatedAnswers, Some(idx)))
        }
      )
    }
  }
}
