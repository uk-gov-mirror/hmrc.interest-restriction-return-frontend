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

package testonly.controllers

import config.FrontendAppConfig
import controllers.BaseController
import javax.inject.Inject
import play.api.libs.json.JsValue
import play.api.mvc.{Action, AnyContent, MessagesControllerComponents, Result}
import testonly.connectors.DynamicStubConnector
import uk.gov.hmrc.http.HttpResponse

class DynamicStubController @Inject()(dynamicStubConnector: DynamicStubConnector,
                                      override val controllerComponents: MessagesControllerComponents,
                                      implicit val appConfig: FrontendAppConfig) extends BaseController {

  private val handleResponse: HttpResponse => Result = response => Status(response.status)(response.body)

  def addSchema: Action[JsValue] = Action.async(parse.json) { implicit request =>
    dynamicStubConnector.addSchema(request.body) map handleResponse
  }

  def clearSchemas: Action[AnyContent] = Action.async { implicit request =>
    dynamicStubConnector.clearSchemas map handleResponse
  }


  def addData: Action[JsValue] = Action.async(parse.json) { implicit request =>
    dynamicStubConnector.addData(request.body) map handleResponse
  }

  def clearData: Action[AnyContent] = Action.async { implicit request =>
    dynamicStubConnector.clearData map handleResponse
  }
}
