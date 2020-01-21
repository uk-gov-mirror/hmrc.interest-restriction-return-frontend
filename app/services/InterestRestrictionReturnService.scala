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

import connectors.InterestRestrictionReturnConnector
import connectors.httpParsers.InterestRestrictionReturnHttpParser.InterestRestrictionReturnResponse
import javax.inject.Inject
import models.requests.DataRequest
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.{ExecutionContext, Future}
class InterestRestrictionReturnService @Inject()(interestRestrictionReturnConnector: InterestRestrictionReturnConnector) {

  def validateCRN(crn: String)
                 (implicit hc: HeaderCarrier, ec: ExecutionContext, request: DataRequest[_]): Future[InterestRestrictionReturnResponse] = {
    interestRestrictionReturnConnector.validateCRN(crn)
  }
}
