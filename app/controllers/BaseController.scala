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


import handlers.ErrorHandler
import models._
import models.requests.DataRequest
import pages.QuestionPage
import play.api.data.Form
import play.api.i18n.I18nSupport
import play.api.libs.json.{Format, Reads}
import play.api.mvc.{Request, Result}
import uk.gov.hmrc.play.bootstrap.controller.FrontendBaseController

import scala.concurrent.{ExecutionContext, Future}

trait BaseController extends FrontendBaseController with I18nSupport with Enumerable.Implicits {

  implicit val ec: ExecutionContext = controllerComponents.executionContext

  def fillForm[A](page: QuestionPage[A], form: Form[A])(implicit request: DataRequest[_], format: Format[A]): Form[A] =
    request.userAnswers.get(page).fold(form)(form.fill)

  def getAnswer[A](page: QuestionPage[A], idx: Int)(implicit request: DataRequest[_], reads: Reads[A]): Option[A] = getAnswer(page, Some(idx))
  def getAnswer[A](page: QuestionPage[A], idx: Option[Int] = None)(implicit request: DataRequest[_], reads: Reads[A]): Option[A] = request.userAnswers.get(page, idx)

  def fillForm[A](page: QuestionPage[A], form: Form[A], idx: Int)(implicit request: DataRequest[_], format: Format[A]): Form[A] =
    fillForm(page, form, Some(idx))

  def fillForm[A](page: QuestionPage[A], form: Form[A])(implicit request: DataRequest[_], format: Format[A]): Form[A] =
    fillForm(page, form, None)

  private def fillForm[A](page: QuestionPage[A], form: Form[A], idx: Option[Int] = None)(implicit request: DataRequest[_], format: Format[A]): Form[A] =
    request.userAnswers.get(page, idx).fold(form)(form.fill)

  def answerFor[A](page: QuestionPage[A], idx: Int)(f: A => Future[Result])
                  (implicit request: DataRequest[_], reads: Reads[A], errorHandler: ErrorHandler): Future[Result] = answerFor(page, Some(idx))(f)

  def answerFor[A](page: QuestionPage[A])(f: A => Future[Result])
                  (implicit request: DataRequest[_], reads: Reads[A], errorHandler: ErrorHandler): Future[Result] = answerFor(page, None)(f)

  private def answerFor[A](page: QuestionPage[A], idx: Option[Int])(f: A => Future[Result])
                          (implicit request: DataRequest[_], reads: Reads[A], errorHandler: ErrorHandler): Future[Result] =
    request.userAnswers.get(page, idx) match {
      case Some(ans) => f(ans)
      case _ => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
    }

  def getSessionData(key: String)(f: String => Future[Result])(implicit request: Request[_], errorHandler: ErrorHandler): Future[Result] =
    request.session.get(key) match {
      case Some(data) => f(data)
      case _ => Future.successful(InternalServerError(errorHandler.internalServerErrorTemplate))
    }
}
}