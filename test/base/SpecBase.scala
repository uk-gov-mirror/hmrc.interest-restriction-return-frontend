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

package base

import java.time.LocalDate
import java.time.format.DateTimeFormatter

import config.FrontendAppConfig
import controllers.actions.DataRequiredActionImpl
import handlers.ErrorHandler
import models.UserAnswers
import models.requests.DataRequest
import org.jsoup.Jsoup
import org.scalatest.TryValues
import org.scalatest.concurrent.{IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.PlaySpec
import org.scalatestplus.play.guice._
import play.api.i18n.{Messages, MessagesApi}
import play.api.inject.Injector
import play.api.libs.json.Json
import play.api.mvc.{AnyContentAsEmpty, Call, MessagesControllerComponents}
import play.api.test.CSRFTokenHelper._
import play.api.test.FakeRequest
import repositories.DefaultSessionRepository
import services.UpdateSectionStateService
import uk.gov.hmrc.http.{HeaderCarrier, SessionKeys}

import scala.concurrent.duration.{Duration, FiniteDuration, _}
import scala.concurrent.{Await, ExecutionContext, Future}

trait SpecBase extends PlaySpec with GuiceOneAppPerSuite with TryValues with ScalaFutures with IntegrationPatience with MaterializerSupport {

  def onwardRoute = Call("GET", "/foo")

  val userAnswersId = "id"

  val emptyUserAnswers = UserAnswers(userAnswersId, Json.obj())

  lazy val fakeRequest = FakeRequest("", "").withSession(SessionKeys.sessionId -> "foo").withCSRFToken.asInstanceOf[FakeRequest[AnyContentAsEmpty.type]]

  lazy val fakeDataRequest = DataRequest(fakeRequest,"id", emptyUserAnswers)

  implicit val defaultTimeout: FiniteDuration = 5.seconds
  def await[A](future: Future[A])(implicit timeout: Duration): A = Await.result(future, timeout)

  def title(heading: String, section: Option[String] = None)(implicit messages: Messages) =
    s"$heading - ${section.fold("")(_ + " - ")}${messages("service.name")} - ${messages("site.govuk")}"

  def titleOf(result: String): String = Jsoup.parse(result).title

  lazy val injector: Injector = app.injector

  implicit lazy val frontendAppConfig: FrontendAppConfig = injector.instanceOf[FrontendAppConfig]

  implicit lazy val ec: ExecutionContext = injector.instanceOf[ExecutionContext]

  lazy val sessionRepository = injector.instanceOf[DefaultSessionRepository]

  lazy val messagesControllerComponents: MessagesControllerComponents = injector.instanceOf[MessagesControllerComponents]

  lazy val messagesApi: MessagesApi = injector.instanceOf[MessagesApi]

  implicit val messages: Messages = messagesApi.preferred(fakeRequest)

  val savedTilDate = LocalDate.now().plusDays(frontendAppConfig.cacheTtlDays).format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))

  implicit lazy val errorHandler = injector.instanceOf[ErrorHandler]

  lazy val dataRequiredAction = injector.instanceOf[DataRequiredActionImpl]

  lazy val updateSectionService = injector.instanceOf[UpdateSectionStateService]

  implicit val hc = HeaderCarrier()

}
