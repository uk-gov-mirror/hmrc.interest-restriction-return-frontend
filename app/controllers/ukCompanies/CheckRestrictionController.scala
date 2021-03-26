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
import handlers.ErrorHandler
import models.NormalMode
import navigation.UkCompaniesNavigator
import pages.ukCompanies.{CheckRestrictionPage, UkCompaniesPage}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.frontend.controller.FrontendBaseController
import utils.CheckYourAnswersRestrictionHelper
import views.html.CheckYourAnswersView

import javax.inject.Inject
import scala.concurrent.Future

class CheckRestrictionController @Inject()(override val messagesApi: MessagesApi,
                                           navigator: UkCompaniesNavigator,
                                           identify: IdentifierAction,
                                           getData: DataRetrievalAction,
                                           requireData: DataRequiredAction,
                                           val controllerComponents: MessagesControllerComponents,
                                           view: CheckYourAnswersView
                                          )(implicit appConfig: FrontendAppConfig, errorHandler: ErrorHandler)
  extends FrontendBaseController with I18nSupport with FeatureSwitching with BaseController {

  def onPageLoad(companyIdx: Int, restrictionIdx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>
      val checkYourAnswersHelper = new CheckYourAnswersRestrictionHelper(request.userAnswers)
      answerFor(UkCompaniesPage, companyIdx) { ukCompany =>
        Future.successful(Ok(view(
          answers = checkYourAnswersHelper.rows(companyIdx, restrictionIdx),
          section = "restriction",
          postAction = controllers.ukCompanies.routes.CheckRestrictionController.onSubmit(companyIdx, restrictionIdx),
          subheader = Some(ukCompany.companyDetails.companyName),
          sectionMsgArgs = Seq(ukCompany.companyDetails.companyName),
          buttonMsg = "site.continue"
        )))
      }
  }

  def onSubmit(companyIdx: Int, restrictionIdx: Int): Action[AnyContent] = (identify andThen getData andThen requireData).async { implicit request =>
    Future.successful(Redirect(navigator.nextRestrictionPage(CheckRestrictionPage(companyIdx: Int, restrictionIdx: Int), NormalMode, request.userAnswers)))
  }
}
