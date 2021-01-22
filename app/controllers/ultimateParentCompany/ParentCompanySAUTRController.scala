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

package controllers.ultimateParentCompany

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.BaseController
import controllers.actions._
import forms.ultimateParentCompany.ParentCompanySAUTRFormProvider
import handlers.ErrorHandler

import javax.inject.Inject
import models.{Mode, NormalMode}
import models.returnModels.UTRModel
import navigation.UltimateParentCompanyNavigator
import pages.ultimateParentCompany.{DeemedParentPage, ParentCompanySAUTRPage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ultimateParentCompany.ParentCompanySAUTRView

import scala.concurrent.Future

class ParentCompanySAUTRController @Inject()(override val messagesApi: MessagesApi,
                                             sessionRepository: SessionRepository,
                                             navigator: UltimateParentCompanyNavigator,
                                             identify: IdentifierAction,
                                             getData: DataRetrievalAction,
                                             requireData: DataRequiredAction,
                                             formProvider: ParentCompanySAUTRFormProvider,
                                             val controllerComponents: MessagesControllerComponents,
                                             view: ParentCompanySAUTRView
                                            )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val form = formProvider()
    answerFor(DeemedParentPage, idx) { deemedParentModel =>
      Future.successful(Ok(view(
        form = deemedParentModel.sautr.map(_.utr).fold(form)(form.fill),
        mode = mode,
        companyName = deemedParentModel.companyName.name,
        postAction = routes.ParentCompanySAUTRController.onSubmit(idx, mode))
      ))
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
            postAction = routes.ParentCompanySAUTRController.onSubmit(idx, mode)
          ))),
        value => {
          val updatedModel = deemedParentModel.copy(sautr = Some(UTRModel(value)))

          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(DeemedParentPage, updatedModel, Some(idx)))
            _              <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(ParentCompanySAUTRPage, mode, updatedAnswers, Some(idx)))
        }
      )
    }
  }
}
