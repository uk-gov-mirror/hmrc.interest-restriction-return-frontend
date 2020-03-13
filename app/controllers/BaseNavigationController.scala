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
import pages.{Page, QuestionPage}
import pages.reviewAndComplete.ReviewAndCompletePage
import play.api.libs.json.Writes
import play.api.mvc.Result
import repositories.SessionRepository
import services.{QuestionDeletionLookupService, UpdateSectionService}

import scala.concurrent.Future

trait BaseNavigationController extends BaseController {

  val sessionRepository: SessionRepository
  val navigator: Navigator
  val questionDeletionLookupService: QuestionDeletionLookupService
  val updateSectionService: UpdateSectionService

  def saveAndRedirect[A](page: QuestionPage[A], answer: A, mode: Mode, idx: Int)
                        (implicit request: DataRequest[_], writes: Writes[A]): Future[Result] = save(page, answer, mode, Some(idx)).map{ updatedAnswers =>
    Redirect(navigator.nextPage(page, mode, updatedAnswers, Some(idx)))
  }

  def saveAndRedirect[A](page: QuestionPage[A], answer: A, mode: Mode)
                        (implicit request: DataRequest[_], writes: Writes[A]): Future[Result] = save(page, answer, mode, None).map{ updatedAnswers =>
    Redirect(navigator.nextPage(page, mode, updatedAnswers, None))
  }

  def saveAndRedirect(page: QuestionPage[_], mode: Mode, idx: Option[Int] = None)
                     (implicit request: DataRequest[_]): Future[Result] = updateState(page, mode, request.userAnswers).map{ updatedAnswers =>
    Redirect(navigator.nextPage(page, mode, updatedAnswers, idx))
  }


  def remove[A](page: QuestionPage[A], mode: Mode, idx: Option[Int] = None)
             (implicit request: DataRequest[_], writes: Writes[A]): Future[UserAnswers] =
    for {
      updatedAnswers <- Future.fromTry(request.userAnswers.remove(page, idx))
      updatedSectionAnswers <- updateState(page, mode, updatedAnswers)
    } yield updatedSectionAnswers

  def save[A](page: QuestionPage[A], answer: A, mode: Mode, idx: Option[Int] = None)
             (implicit request: DataRequest[_], writes: Writes[A]): Future[UserAnswers] =
    for {
      updatedAnswers <- Future.fromTry(request.userAnswers.set(page, answer, idx))
      updatedSectionAnswers <- updateState(page, mode, updatedAnswers)
    } yield updatedSectionAnswers

  def updateState(page: Page, mode: Mode, userAnswers: UserAnswers)
                 (implicit request: DataRequest[_]) = {
    val reviewModel = updateSectionService.updateState(userAnswers, page)
    val pagesToDelete = questionDeletionLookupService.getPagesToRemove(page)(userAnswers)
    for {
      cleanedAnswers <- Future.fromTry(userAnswers.remove(pagesToDelete))
      updatedSectionAnswers <- Future.fromTry(cleanedAnswers.set(ReviewAndCompletePage, reviewModel))
      _ <- sessionRepository.set(updatedSectionAnswers)
    } yield updatedSectionAnswers
  }

}