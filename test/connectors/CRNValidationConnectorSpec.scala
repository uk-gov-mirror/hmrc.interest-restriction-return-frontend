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

import assets.constants.BaseConstants
import base.SpecBase
import connectors.httpParsers.{InvalidCRN, UnexpectedFailure, ValidCRN}
import connectors.mocks.MockHttp
import play.api.http.Status

import scala.concurrent.Future


class CRNValidationConnectorSpec extends SpecBase with MockHttp with BaseConstants {

  object TestCRNValidationConnector extends CRNValidationConnector(mockHttp, frontendAppConfig)

  "ValidateCRNConnector" when {

    "given a valid CRN" should {

      "return a Right(ValidCrn)" in {

        setupMockHttpGet(TestCRNValidationConnector.validateCrnUrl(crn))(
          Future.successful(Right(ValidCRN))
        )

        val expectedResult = Right(ValidCRN)
        val actualResult = TestCRNValidationConnector.validateCRN(crn)(implicitly, implicitly, fakeDataRequest)

        await(actualResult) mustBe expectedResult
      }
    }

    "given a invalid CRN" should {

      "return a Left(InvalidCrn)" in {

        setupMockHttpGet(TestCRNValidationConnector.validateCrnUrl(crn))(
          Future.successful(Left(InvalidCRN))
        )

        val expectedResult = Left(InvalidCRN)
        val actualResult = TestCRNValidationConnector.validateCRN(crn)(implicitly, implicitly, fakeDataRequest)

        await(actualResult) mustBe expectedResult
      }
    }

    "an error is returned" should {

      "return a Left(UnexpectedFailure)" in {

        setupMockHttpGet(TestCRNValidationConnector.validateCrnUrl(crn))(
          Future.successful(Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "Error")))
        )

        val expectedResult = Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "Error"))
        val actualResult = TestCRNValidationConnector.validateCRN(crn)(implicitly, implicitly, fakeDataRequest)

        await(actualResult) mustBe expectedResult
      }
    }
  }
}