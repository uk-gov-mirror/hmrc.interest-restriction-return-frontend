package utils

import org.scalatest._
import org.scalatest.concurrent.{Eventually, IntegrationPatience, ScalaFutures}
import org.scalatestplus.play.guice.GuiceOneServerPerSuite
import play.api.http.Status.OK
import play.api.inject.guice.GuiceApplicationBuilder
import play.api.{Application, Environment, Mode}
import stubs.AuthStub

trait IntegrationSpecBase extends WordSpec
  with GivenWhenThen with TestSuite with ScalaFutures with IntegrationPatience with Matchers
  with WiremockHelper
  with GuiceOneServerPerSuite
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
    "application.router" -> "testOnlyDoNotUseInAppConf.Routes",
    "microservice.services.auth.host" -> mockHost,
    "microservice.services.auth.port" -> mockPort,
    "microservice.services.interest-restriction-return.host" -> mockHost,
    "microservice.services.interest-restriction-return.port" -> mockPort
  )

  override def beforeEach(): Unit = {
    resetWiremock()

    AuthStub.authorised()

    whenReady(getRequest("/", follow = true)) { result =>
      result should have(
        httpStatus(OK)
      )
    }
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
