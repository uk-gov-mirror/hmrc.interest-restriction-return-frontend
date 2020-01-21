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
import connectors.httpParsers.ValidCRN
import connectors.mocks.MockInterestRestrictionReturnConnector

class InterestRestrictionReturnServiceSpec extends SpecBase with MockInterestRestrictionReturnConnector {

  object TestInterestRestrictionReturnService extends InterestRestrictionReturnService(mockInterestRestrictionReturnConnector)

  "InterestRestrictionReturnService" when {

    "given a valid crn" should {

      "return a Right(ValidCRN)" in {

        val expectedResult = Right(ValidCRN)
        val actualResult = TestInterestRestrictionReturnService.validateCRN("AA111111")
      }
    }

    "an InvalidCrn is returned from the connector" should {

      "return a Left(InvalidCRN)" in {

      }
    }

    "an UnexpectedFailure is returned from the connector" should {

      "return a Left(UnexpectedFailure)" in {

      }
    }
  }
}
