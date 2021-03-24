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

package connectors

import base.SpecBase
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, stubFor, urlMatching}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import generators.ModelGenerators
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.{Application, Configuration}
import play.api.inject.guice.GuiceApplicationBuilder
import com.github.tomakehurst.wiremock.client.WireMock
import org.scalatest.RecoverMethods.recoverToSucceededIf
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import play.api.libs.json.{JsResultException, Json}
import play.api.http.Status


class InterestRestrictionReturnConnectorSpec extends SpecBase  with ScalaCheckPropertyChecks
  with ModelGenerators with BeforeAndAfterAll with BeforeAndAfterEach {

  val stubFullReturnUrl = "/internal/return/full"
  val stubAbbreviatedReturnUrl = "/internal/return/abbreviated"
  val successfulResponse = Json.obj("acknowledgementReference" -> "XAIRR00000012345678")
  val wireMockPort = 1111

  protected val server: WireMockServer = new WireMockServer(wireMockPort)

  lazy val test: Application =
    new GuiceApplicationBuilder()
      .configure(Configuration(
        "microservice.services.interest-restriction-return.port" -> wireMockPort
      )).build()

  private lazy val connector = test.injector.instanceOf[InterestRestrictionReturnConnector]

  override def beforeAll(): Unit = {
    server.start()
    super.beforeAll()
  }

  override def beforeEach(): Unit = {
    server.resetAll()
    super.beforeEach()
  }

  override def afterAll(): Unit = {
    super.afterAll()
    server.stop()
  }

  "Interest Restriction Return Connector" when {
    WireMock.configureFor("localhost", wireMockPort)

    "Submitting a successfull payload" should {
      "Return the acknowledgement reference for Full Return" in {
        stubPost(stubFullReturnUrl,Status.OK, Json.stringify(successfulResponse))

        forAll(fullReturnModel) {
          fullReturn =>
            val result = connector.submitFullReturn(fullReturn)
            result.futureValue mustBe SuccessResponse("XAIRR00000012345678")
        }
      }

      "Return the acknowledgement reference for Abbreviated Return" in {
        stubPost(stubAbbreviatedReturnUrl,Status.OK, Json.stringify(successfulResponse))

        forAll(abbreviatedReturnModel) {
          fullReturn =>
            val result = connector.submitFullReturn(fullReturn)
            result.futureValue mustBe SuccessResponse("XAIRR00000012345678")
        }
      }

      "Throw IllegalArgumentException if the response status is not 200" in {
        stubPost(stubFullReturnUrl,Status.CREATED,"Test")

        forAll(fullReturnModel) {
          fullReturn =>
            recoverToSucceededIf[IllegalArgumentException] {
              connector.submitFullReturn(fullReturn)
            }
        }
      }

      "Throw a JSONParseError if the payload received is not recognised" in {
        stubPost(stubFullReturnUrl,Status.OK,"{Invalid Payload}")

        forAll(fullReturnModel) {
          fullReturn =>
            recoverToSucceededIf[JsResultException] {
              connector.submitFullReturn(fullReturn)
            }
        }
      }
    }

    "Submitting a payload and something goes wrong" should {
      "Throw SubmissionUnsuccessful exception for a 400 response" in {
        stubPost(stubFullReturnUrl,Status.BAD_REQUEST,"Errors")

        forAll(fullReturnModel) {
          fullReturn =>
            recoverToSucceededIf[InvalidPayloadException] {
              connector.submitFullReturn(fullReturn)
            }
        }
      }

      "Throw SubmissionFailureException exception for anything other than a 400" in {
        stubPost(stubFullReturnUrl,Status.INTERNAL_SERVER_ERROR,"Errors")

        forAll(fullReturnModel) {
          fullReturn =>
            recoverToSucceededIf[SubmissionFailureException] {
              connector.submitFullReturn(fullReturn)
            }
        }
      }
    }
  }
  
  def stubPost(url: String, status: Integer, responseBody: String = ""): StubMapping =
    stubFor(post(urlMatching(url))
      .willReturn(
        aResponse().
          withStatus(status).
          withBody(responseBody)
      )
    )
}
