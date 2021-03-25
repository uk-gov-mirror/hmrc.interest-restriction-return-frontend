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

package mocks

import connectors.InterestRestrictionReturnConnector
import models.{FullReturnModel, SuccessResponse, UserAnswers}
import org.scalamock.scalatest.MockFactory
import repositories.SessionRepository
import uk.gov.hmrc.http.HeaderCarrier

import scala.concurrent.Future

trait MockSessionRepository extends MockFactory {

  val mockSessionRepository = mock[SessionRepository]

  def mockGetAnswers(result: Option[UserAnswers]): Unit = {
    (mockSessionRepository.get(_: String))
      .expects(*)
      .returns(Future.successful(result))
  }

  def mockSetAnswers(): Unit = {
    (mockSessionRepository.set(_: UserAnswers))
      .expects(*)
      .returns(Future.successful(true))
  }

  def mockDeleteAnswers(): Unit = {
    (mockSessionRepository.delete(_: UserAnswers))
      .expects(*)
      .returns(Future.successful(true))
  }
}

trait MockInterestRestrictionReturnConnector extends MockFactory {
  val mockConnector = mock[InterestRestrictionReturnConnector]

  def mockSubmitReturn()(implicit hc:HeaderCarrier): Unit = {
    (mockConnector.submitFullReturn(_:FullReturnModel)).expects(_:FullReturnModel).returns(Future.successful(SuccessResponse("test")))
  }
}
