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

package controllers.reviewAndComplete

import config.FrontendAppConfig
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import javax.inject.Inject
import play.api.i18n.{I18nSupport, MessagesApi}
import play.api.mvc._
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController
import views.html.reviewAndComplete.ReviewAndCompleteView
import utils.ReviewAndCompleteHelper

import scala.concurrent.ExecutionContext

class ReviewAndCompleteController @Inject()(override val messagesApi: MessagesApi,
                                      identify: IdentifierAction,
                                      getData: DataRetrievalAction,
                                      requireData: DataRequiredAction,
                                      val controllerComponents: MessagesControllerComponents,
                                      view: ReviewAndCompleteView
                                     )(implicit ec: ExecutionContext, appConfig: FrontendAppConfig)
  extends FrontendBaseController with I18nSupport with FeatureSwitching {


  def onPageLoad: Action[AnyContent] = (identify andThen getData andThen requireData) { implicit request =>

    val reviewAndCompleteHelper = new ReviewAndCompleteHelper()

    Ok(view(reviewAndCompleteHelper.rows))
  }
}
