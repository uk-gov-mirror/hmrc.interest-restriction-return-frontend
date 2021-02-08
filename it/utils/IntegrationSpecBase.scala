package utils

import java.util.concurrent.TimeUnit

import models.UserAnswers
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}
import org.scalatest.{TryValues, _}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import pages.QuestionPage
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.libs.json.Json
import play.api.{Application, Environment, Mode}
import repositories.SessionRepository
import stubs.AuthStub

import scala.concurrent.Await
import scala.concurrent.duration.Duration

trait IntegrationSpecBase extends WordSpec
  with GivenWhenThen with TestSuite with ScalaFutures with IntegrationPatience with Matchers
  with WiremockHelper
  with GuiceOneServerPerSuite with TryValues
  with BeforeAndAfterEach with BeforeAndAfterAll with Eventually with CreateRequestHelper with CustomMatchers {

  override implicit lazy val app: Application = new GuiceApplicationBuilder()
    .in(Environment.simple(mode = Mode.Dev))
    .configure(config)
    .build
  val mockHost = WiremockHelper.wiremockHost
  val mockPort = WiremockHelper.wiremockPort.toString
  val mockUrl = s"http://$mockHost:$mockPort"

  def config: Map[String, String] = Map(
    "play.filters.csrf.header.bypassHeaders.Csrf-Token" -> "nocheck",
    "play.http.router" -> "testOnlyDoNotUseInAppConf.Routes",
    "microservice.services.auth.host" -> mockHost,
    "microservice.services.auth.port" -> mockPort,
    "microservice.services.interest-restriction-return.host" -> mockHost,
    "microservice.services.interest-restriction-return.port" -> mockPort
  )

  lazy val mongo = app.injector.instanceOf[SessionRepository]

  val emptyUserAnswers = UserAnswers("id", Json.obj())

  def setAnswers(userAnswers: UserAnswers)(implicit timeout: Duration): Unit = Await.result(mongo.set(userAnswers), timeout)
  def getAnswers(id: String)(implicit timeout: Duration): Option[UserAnswers] = Await.result(mongo.get(id), timeout)
  def getAnswersFuture(id: String) = mongo.get(id)

  def setAnswers[A](page: QuestionPage[A], value: A): Unit =
    setAnswers(emptyUserAnswers.set(page, value).success.value)

  def appendList[A](page: QuestionPage[A], value: A): Unit = {
    setAnswers(emptyUserAnswers.appendList(page, value).success.value)
  }

  override def beforeEach(): Unit = {
    resetWiremock()
    AuthStub.authorised()
    Await.result(mongo.delete(UserAnswers("id")), Duration(5, TimeUnit.SECONDS))
    Await.result(mongo.set(UserAnswers("id")), Duration(5, TimeUnit.SECONDS))
  }

  override def beforeAll(): Unit = {
    super.beforeAll()
    startWiremock()
  }

  override def afterAll(): Unit = {
    stopWiremock()
    super.afterAll()
  }

}
