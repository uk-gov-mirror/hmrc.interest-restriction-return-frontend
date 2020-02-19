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

package controllers

import models._
import models.requests.DataRequest
import navigation.Navigator
import pages.QuestionPage
import play.api.libs.json.Writes
import play.api.mvc.Result
import repositories.SessionRepository
import services.QuestionDeletionLookupService

import scala.concurrent.Future

trait BaseNavigationController extends BaseController {

  val sessionRepository: SessionRepository
  val navigator: Navigator
  val questionDeletionLookupService: QuestionDeletionLookupService

  def saveAndRedirect[A](page: QuestionPage[A], answer: A, mode: Mode, id: Option[Int] = None)
                        (implicit request: DataRequest[_], writes: Writes[A]): Future[Result] =
    for {
      updatedAnswers <- Future.fromTry(request.userAnswers.set(page, answer, id))
      pagesToDelete  = questionDeletionLookupService.getPagesToRemove(page)(updatedAnswers)
      cleanedAnswers <- Future.fromTry(updatedAnswers.remove(pagesToDelete))
      _              <- sessionRepository.set(cleanedAnswers)
    } yield Redirect(navigator.nextPage(page, mode, updatedAnswers, id))
}