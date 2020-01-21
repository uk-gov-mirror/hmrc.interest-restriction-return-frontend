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

package services

import base.SpecBase
import connectors.httpParsers.{InvalidCRN, UnexpectedFailure, ValidCRN}
import connectors.mocks.MockInterestRestrictionReturnConnector
import play.api.http.Status


class InterestRestrictionReturnServiceSpec extends SpecBase with MockInterestRestrictionReturnConnector {

  object TestInterestRestrictionReturnService extends InterestRestrictionReturnService(mockInterestRestrictionReturnConnector)

  "InterestRestrictionReturnService" when {

    "given a valid crn" should {

      "return a Right(ValidCRN)" in {

        mockInterestRestrictionReturn("AA111111")(Right(ValidCRN))

        val expectedResult = Right(ValidCRN)
        val actualResult = TestInterestRestrictionReturnService.validateCRN("AA111111")(hc, ec, fakeDataRequest)

        await(actualResult) mustBe expectedResult
      }
    }

    "an InvalidCrn is returned from the connector" should {

      "return a Left(InvalidCRN)" in {

        mockInterestRestrictionReturn("AA111111")(Left(InvalidCRN))

        val expectedResult = Left(InvalidCRN)
        val actualResult = TestInterestRestrictionReturnService.validateCRN("AA111111")(hc, ec, fakeDataRequest)

        await(actualResult) mustBe expectedResult
      }
    }

    "an UnexpectedFailure is returned from the connector" should {

      "return a Left(UnexpectedFailure)" in {

        mockInterestRestrictionReturn("AA111111")(Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "Error")))

        val expectedResult = Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, "Error"))
        val actualResult = TestInterestRestrictionReturnService.validateCRN("AA111111")(hc, ec, fakeDataRequest)

        await(actualResult) mustBe expectedResult
      }
    }
  }
}
