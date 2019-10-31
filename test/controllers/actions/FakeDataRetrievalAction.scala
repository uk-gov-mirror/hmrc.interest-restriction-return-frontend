/*
 * Copyright 2019 HM Revenue & Customs
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

package controllers.actions

import base.SpecBase
import models.UserAnswers
import models.requests.{IdentifierRequest, OptionalDataRequest}

import scala.concurrent.{ExecutionContext, Future}


trait FakeDataRetrievalAction extends SpecBase with DataRetrievalAction {

  val dataToReturn: Option[UserAnswers]

  override protected def transform[A](request: IdentifierRequest[A]): Future[OptionalDataRequest[A]] =
    dataToReturn match {
      case None =>
        Future(OptionalDataRequest(request.request, request.identifier, None))
      case Some(userAnswers) =>
        Future(OptionalDataRequest(request.request, request.identifier, Some(userAnswers)))
    }

  override protected implicit val executionContext: ExecutionContext =
    scala.concurrent.ExecutionContext.Implicits.global
}

object FakeDataRetrievalActionNone extends FakeDataRetrievalAction {
  override val dataToReturn: Option[UserAnswers] = None
}

object FakeDataRetrievalActionEmptyAnswers extends FakeDataRetrievalAction {
  override val dataToReturn: Option[UserAnswers] = Some(emptyUserAnswers)
}

case class FakeDataRetrievalActionGeneral(data: Option[UserAnswers]) extends FakeDataRetrievalAction {
  override val dataToReturn: Option[UserAnswers] = data
}