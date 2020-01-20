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

import play.api.Logger
import play.api.http.Status._
import uk.gov.hmrc.http.{HttpReads, HttpResponse}

object InterestRestrictionReturnHttpParser {

  implicit object CompaniesHouseReads {

    def read(method: String, url: String, response: HttpResponse) = {
      response.status match {
        case NO_CONTENT =>
          Logger.debug("[CompaniesHouseHttpParser][read]: Status OK")
          Logger.debug(s"[CompaniesHouseHttpParser][read]: Json Response: ${response.body}")
          None
        case NOT_FOUND =>
          Logger.debug("[CompaniesHouseHttpParser][read]: Status NOT FOUND")
          Logger.debug(s"[CompaniesHouseHttpParser][read]: Json Response: ${response.body}")
          Some(NOT_FOUND)
        case status =>
          Logger.warn(s"[CompaniesHouseReads][read]: Unexpected response, status $status returned")
          Some(INTERNAL_SERVER_ERROR)
      }
    }
  }
}
