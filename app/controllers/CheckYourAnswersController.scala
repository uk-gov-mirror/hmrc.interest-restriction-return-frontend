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

package controllers

import com.google.inject.Inject
import config.FrontendAppConfig
import config.featureSwitch.{FeatureSwitching, UseNunjucks}
import controllers.actions.{DataRequiredAction, DataRetrievalAction, IdentifierAction}
import nunjucks.{CheckYourAnswersTemplate, Renderer}
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.libs.json.Json
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Request}
import play.twirl.api.Html
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import uk.gov.hmrc.nunjucks.NunjucksSupport
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import utils.CheckYourAnswersHelper
import views.html.CheckYourAnswersView

import scala.concurrent.{ExecutionContext, Future}

class CheckYourAnswersController @Inject()(
                                            override val messagesApi: MessagesApi,
                                            identify: IdentifierAction,
                                            getData: DataRetrievalAction,
                                            requireData: DataRequiredAction,
                                            val controllerComponents: MessagesControllerComponents,
                                            view: CheckYourAnswersView,
                                            renderer: Renderer
                                          )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig)
  extends FrontendBaseController with I18nSupport with NunjucksSupport with FeatureSwitching {

  def onPageLoad(): Action[AnyContent] = (identify andThen getData andThen requireData).async {
    implicit request =>

      val checkYourAnswersHelper = new CheckYourAnswersHelper(request.userAnswers)

      val sections = Seq(
        checkYourAnswersHelper.helloWorldYesNo,
        checkYourAnswersHelper.helloWorldYesNoNunjucks
      ).flatten

      renderView(sections).map(Ok(_))
  }

  private def renderView(answers: Seq[SummaryListRow])(implicit request: Request[_]): Future[Html] =
    if(isEnabled(UseNunjucks)) {
      renderer.render(CheckYourAnswersTemplate, Json.obj(
        "rows" -> answers
      ))
    } else {
      Future.successful(view(answers))
    }
}
