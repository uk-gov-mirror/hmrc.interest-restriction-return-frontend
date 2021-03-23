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

import akka.http.scaladsl.model.HttpHeader.ParsingResult.Ok
import base.SpecBase
import com.github.tomakehurst.wiremock.client.WireMock.{aResponse, post, stubFor, urlMatching}
import com.github.tomakehurst.wiremock.stubbing.StubMapping
import generators.ModelGenerators
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.http.Status._


class InterestRestrictionReturnConnectorSpec extends SpecBase  with ScalaCheckPropertyChecks with ModelGenerators {

  val stubUrl = "/internal/return/full"

  private lazy val connector = app.injector.instanceOf[InterestRestrictionReturnConnector]

  "Interest Restriction Return Connector" should {
    "Submit a payload" in {
      stubPost(stubUrl,200,"hi")

      forAll(fullReturnModel) {
        fullReturn =>
          val result = connector.submitFullReturn(fullReturn)
          result.futureValue.status mustBe OK
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
