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

package connectors

import akka.http.scaladsl.model.HttpResponse
import base.SpecBase
import connectors.httpParsers.{InvalidCRN, UnexpectedFailure, ValidCRN}
import connectors.mocks.MockHttp
import play.api.http.Status

import scala.concurrent.Future


class InterestRestrictionReturnConnectorSpec extends SpecBase with MockHttp {

  object TestInterestRestrictionReturnConnector extends InterestRestrictionReturnConnector(mockHttp, frontendAppConfig)

  "InterestRestrictionReturnConnector" when {

    "given a valid CRN" should {

      "return a Right(ValidCrn)" in {

        setupMockHttpGet(TestInterestRestrictionReturnConnector.validateCrnUrl("AA111111"))(
          Future.successful(Right(ValidCRN))
        )

        val expectedResult = Right(ValidCRN)
        val actualResult = TestInterestRestrictionReturnConnector.validateCRN("AA111111")(implicitly, implicitly, fakeDataRequest)

        await(actualResult) mustBe expectedResult
      }
    }

    "given a invalid CRN" should {

      "return a Left(InvalidCrn)" in {

        setupMockHttpGet(TestInterestRestrictionReturnConnector.validateCrnUrl("AA111111"))(
          Future.successful(Left(InvalidCRN))
        )

        val expectedResult = Left(InvalidCRN)
        val actualResult = TestInterestRestrictionReturnConnector.validateCRN("AA111111")(implicitly, implicitly, fakeDataRequest)

        await(actualResult) mustBe expectedResult
      }
    }

    "an error is returned" should {

      "return a Left(UnexpectedFailure)" in {

        setupMockHttpGet(TestInterestRestrictionReturnConnector.validateCrnUrl("AA111111"))(
          Future.successful(Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "Error")))
        )

        val expectedResult = Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "Error"))
        val actualResult = TestInterestRestrictionReturnConnector.validateCRN("AA111111")(implicitly, implicitly, fakeDataRequest)

        await(actualResult) mustBe expectedResult
      }
    }
  }
}