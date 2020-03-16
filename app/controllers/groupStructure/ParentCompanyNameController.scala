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
import config.featureSwitch.FeatureSwitching
import controllers.{BaseController, BaseNavigationController}
import controllers.actions._
import forms.groupStructure.ParentCompanyNameFormProvider
import javax.inject.Inject
import models.{Mode, NormalMode}
import models.requests.DataRequest
import models.returnModels.{CompanyNameModel, DeemedParentModel}
import navigation.GroupStructureNavigator
import pages.QuestionPage
import pages.groupStructure.{DeemedParentPage, ParentCompanyNamePage}
import play.api.data.Form
import play.api.i18n.MessagesApi
import play.api.libs.json.Reads
import play.api.mvc._
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionStateService}
import views.html.groupStructure.ParentCompanyNameView

import scala.concurrent.Future

class ParentCompanyNameController @Inject()(override val messagesApi: MessagesApi,
                                            override val sessionRepository: SessionRepository,
                                            override val navigator: GroupStructureNavigator,
                                            override val questionDeletionLookupService: QuestionDeletionLookupService,
                                            override val updateSectionService: UpdateSectionStateService,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            formProvider: ParentCompanyNameFormProvider,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: ParentCompanyNameView
                                           )(implicit appConfig: FrontendAppConfig) extends BaseNavigationController with FeatureSwitching {

  def onPageLoad(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>
    val companyName = getAnswer(DeemedParentPage, idx).map(_.companyName.name)
    val form = formProvider()
    Ok(view(companyName.fold(form)(form.fill), mode, routes.ParentCompanyNameController.onSubmit(idx, mode)))
  }

  def onSubmit(idx: Int, mode: Mode): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    formProvider().bindFromRequest().fold(
      formWithErrors =>
        Future.successful(BadRequest(view(formWithErrors, mode, routes.ParentCompanyNameController.onSubmit(idx, mode)))),
      value => {
        val companyName = CompanyNameModel(value)
        val deemedParentModel = getAnswer(DeemedParentPage, idx).fold(DeemedParentModel(companyName))(_.copy(companyName = companyName))
        save(DeemedParentPage, deemedParentModel, NormalMode, Some(idx)).map { userAnswers =>
          Redirect(navigator.nextPage(ParentCompanyNamePage, mode, userAnswers, Some(idx)))
        }
      }
    )
  }
}
