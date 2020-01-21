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

package connectors.httpParsers

import base.SpecBase
import connectors.httpParsers.InterestRestrictionReturnHttpParser.InterestRestrictionReturnReads
import uk.gov.hmrc.http.HttpResponse
import play.api.http.Status

class InterestRestrictionReturnHttpParserSpec extends SpecBase {

  "CompaniesHouseHttpParser.CompaniesHouseReads" when {

    "given an (200)" should {

      "return a Right(ValidCRN)" in {

        val expectedResult = Right(ValidCRN)
        val actualResult = InterestRestrictionReturnReads.read("", "", HttpResponse(Status.NO_CONTENT))

        actualResult mustBe expectedResult
      }
    }

    "given an (404) Not Found" should {

      "return a Left(InvalidCRN)" in {

        val expectedResult = Left(InvalidCRN)
        val actualResult = InterestRestrictionReturnReads.read("", "", HttpResponse(Status.BAD_REQUEST))

        actualResult mustBe expectedResult
      }
    }


    "given any other status" should {

      "return a Left(UnexpectedFailure)" in {

        val expectedResult = Left(UnexpectedFailure(Status.INTERNAL_SERVER_ERROR, s"Unexpected response, status ${Status.INTERNAL_SERVER_ERROR} returned"))
        val actualResult = InterestRestrictionReturnReads.read("", "", HttpResponse(Status.INTERNAL_SERVER_ERROR))

        actualResult mustBe expectedResult
      }
    }
  }
}