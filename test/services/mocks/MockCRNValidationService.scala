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

package services.mocks

import connectors.httpParsers.CRNValidationHttpParser.CRNValidationResponse
import models.requests.DataRequest
import org.scalamock.scalatest.MockFactory
import services.CRNValidationService
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}

trait MockCRNValidationService extends MockFactory {

  lazy val mockCRNValidationService: CRNValidationService = mock[CRNValidationService]

  def mockValidateCRN(crn: String)(response: CRNValidationResponse): Unit = {
    (mockCRNValidationService.validateCRN(_: String)(_: HeaderCarrier, _: ExecutionContext, _: DataRequest[_]))
      .expects(crn, *, *, *)
      .returns(Future.successful(response))
  }
}
