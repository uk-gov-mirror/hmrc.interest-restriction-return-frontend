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
import forms.ultimateParentCompany.ParentCompanyNameFormProvider
import handlers.ErrorHandler
import javax.inject.Inject
import models.Mode
import models.returnModels.{CompanyNameModel, DeemedParentModel}
import navigation.UltimateParentCompanyNavigator
import pages.ultimateParentCompany.{DeemedParentPage, HasDeemedParentPage, ParentCompanyNamePage}
import play.api.i18n.MessagesApi
import play.api.mvc._
import repositories.SessionRepository
import views.html.ultimateParentCompany.ParentCompanyNameView

import scala.concurrent.Future

class ParentCompanyNameController @Inject()(override val messagesApi: MessagesApi,
                                            sessionRepository: SessionRepository,
                                            navigator: UltimateParentCompanyNavigator,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            formProvider: ParentCompanyNameFormProvider,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: ParentCompanyNameView
                                           )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler) extends BaseController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    val companyName = getAnswer(DeemedParentPage, idx).map(_.companyName.name)

    answerFor(HasDeemedParentPage) { deemedParent =>
      val prefix : String = getPrefix(deemedParent, idx)
      val form = formProvider(s"parentCompanyName.${prefix}.error.required")
      Future.successful(Ok(view(companyName.fold(form)(form.fill), mode, s"parentCompanyName.${prefix}.heading", routes.ParentCompanyNameController.onSubmit(idx, mode))))
    }
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>

    answerFor(HasDeemedParentPage) { deemedParent =>
      val prefix : String = getPrefix(deemedParent, idx)

      formProvider(s"parentCompanyName.${prefix}.error.required").bindFromRequest().fold(
        formWithErrors =>
          Future.successful(BadRequest(view(formWithErrors, mode, s"parentCompanyName.${prefix}.heading", routes.ParentCompanyNameController.onSubmit(idx, mode)))),
        value => {
          val companyName = CompanyNameModel(value)
          val deemedParentModel = getAnswer(DeemedParentPage, idx).fold(DeemedParentModel(companyName))(_.copy(companyName = companyName))

          for {
            updatedAnswers <- Future.fromTry(request.userAnswers.set(DeemedParentPage, deemedParentModel, Some(idx)))
            _ <- sessionRepository.set(updatedAnswers)
          } yield Redirect(navigator.nextPage(ParentCompanyNamePage, mode, updatedAnswers, Some(idx)))
        }
      )
    }
  }

  private def getPrefix(deemedParent : Boolean, idx : Int): String = {
    deemedParent match {
      case false => "ultimate"
      case true => idx match {
        case 1 => "deemed.first"
        case 2 => "deemed.second"
        case _ => "deemed.third"
      }
    }
  }

}
