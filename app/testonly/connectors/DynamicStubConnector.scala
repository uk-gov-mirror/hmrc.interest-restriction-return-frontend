/*
 * Copyright 2019 HM Revenue & Customs
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

package testonly.connectors

import config.FrontendAppConfig
import javax.inject.Inject
import play.api.Logger
import play.api.libs.json.JsValue
import testonly.connectors.httpParsers.DynamicStubHttpParser.DynamicStubReads
import uk.gov.hmrc.http.{HeaderCarrier, HttpResponse}
import uk.gov.hmrc.play.bootstrap.http.HttpClient

import scala.concurrent.{ExecutionContext, Future}

class DynamicStubConnector @Inject()(httpClient: HttpClient,
                                     implicit val appConfig: FrontendAppConfig) {

  private[connectors] lazy val dynamicStub = s"${appConfig.dynamicStub}/setup"

  def addSchema(json: JsValue)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] = {
    Logger.debug(s"[DynamicStubConnector][addSchema] Json: $json")
    httpClient.POST(s"$dynamicStub/schema", json)(implicitly, DynamicStubReads, hc, ec)
  }

  def clearSchemas(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] =
    httpClient.DELETE(s"$dynamicStub/all-schemas")(DynamicStubReads, hc, ec)


  def addData(json: JsValue)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] =
    httpClient.POST(s"$dynamicStub/data", json)(implicitly, DynamicStubReads, hc, ec)

  def clearData(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[HttpResponse] =
    httpClient.DELETE(s"$dynamicStub/all-data")(DynamicStubReads, hc, ec)

}
