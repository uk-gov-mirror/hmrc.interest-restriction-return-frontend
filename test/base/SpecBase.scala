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

package base

import config.FrontendAppConfig
import models.UserAnswers
import org.scalatest.TryValues
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.libs.json.Json
import play.api.mvc.MessagesControllerComponents
import play.api.test.FakeRequest
import repositories.DefaultSessionRepository
import uk.gov.hmrc.http.SessionKeys

import scala.concurrent.ExecutionContext

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite with TryValues with ScalaFutures with IntegrationPatience with MaterializerSupport {

  val userAnswersId = "id"

  val emptyUserAnswers = UserAnswers(userAnswersId, Json.obj())

  val fakeRequest = FakeRequest("", "").withSession(SessionKeys.sessionId -> "foo")

  val injector: Injector = app.injector

  implicit lazy val frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  implicit lazy val ec: ExecutionContext = injector.instanceOf[ExecutionContext]

  lazy val sessionRepository = injector.instanceOf[DefaultSessionRepository]

  lazy val messagesControllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]

  lazy val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  implicit val messages: Messages = messagesApi.preferred(fakeRequest)

}
