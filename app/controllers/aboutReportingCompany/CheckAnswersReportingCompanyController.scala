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

package controllers.aboutReportingCompany

import com.google.inject.Inject
import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import models.NormalMode
import models.Section.ReportingCompany
import navigation.AboutReportingCompanyNavigator
import nunjucks.{CheckYourAnswersTemplate, Renderer}
import pages.aboutReportingCompany.CheckAnswersReportingCompanyPage
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, Call, MessagesControllerComponents, Request}
import play.twirl.api.Html
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import utils.CheckYourAnswersHelper
import views.html.CheckYourAnswersView

import scala.concurrent.{ExecutionContext, Future}

class CheckAnswersReportingCompanyController @Inject()(
                                                        override val messagesApi: MessagesApi,
                                                        identify: IdentifierAction,
                                                        getData: DataRetrievalAction,
                                                        requireData: DataRequiredAction,
                                                        val controllerComponents: MessagesControllerComponents,
                                                        navigator: AboutReportingCompanyNavigator,
                                                        view: CheckYourAnswersView,
                                                        renderer: Renderer
                                                      )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig)
  extends FrontendBaseController with I18nSupport with NunjucksSupport with FeatureSwitching {

  private def renderView(answers: Seq[SummaryListRow], postAction: Call)(implicit request: Request[_]): Future[Html] =
    if (isEnabled(UseNunjucks)) {
      renderer.render(CheckYourAnswersTemplate, Json.obj(
        "rows" -> answers,
        "section" -> ReportingCompany,
        "postAction" -> postAction.url
      ))
    } else {
      Future.successful(view(answers, ReportingCompany, postAction))
    }

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      val checkYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)

      val sections = Seq(
        checkYourAnswersHelper.reportingCompanyName,
        checkYourAnswersHelper.reportingCompanyCTUTR,
        checkYourAnswersHelper.reportingCompanyCRN
      ).flatten

      renderView(sections, controllers.aboutReportingCompany.routes.CheckAnswersReportingCompanyController.onSubmit()).map(Ok(_))
  }

  def onSubmit(): Action[AnyContent] = (identify andThen getData andThen requireData) {
    implicit request =>
      Redirect(navigator.nextPage(CheckAnswersReportingCompanyPage, NormalMode, request.userAnswers))
  }
}