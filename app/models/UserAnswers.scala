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

package models

import java.time.LocalDateTime

import pages._
import play.api.libs.json._

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

final case class UserAnswers(
                              id: String,
                              data: JsObject = Json.obj(),
                              lastUpdated: LocalDateTime = LocalDateTime.now
                            ) {

  private def path[A](page: QuestionPage[A], idx: Option[Int]) = idx.fold(page.path)(idx => page.path \ (idx - 1))

  def get[A](page: QuestionPage[A], idx: Option[Int] = None): Option[A] = {
    implicit val reads: Reads[A] = page.reads
    path(page, idx).readNullable[A].reads(data).getOrElse(None)
  }

  def getList[A](page: QuestionPage[A]): Seq[A] = {
    implicit val reads: Reads[A] = page.reads
    page.path.read[Seq[A]].reads(data).getOrElse(Seq.empty)
  }

  def set[A](page: QuestionPage[A], value: A, idx: Option[Int] = None): Try[UserAnswers] = {
    implicit val writes: Writes[A] = page.writes
    page.cleanup(Some(value),this).flatMap {
      cleanedUserAnswers => {
        cleanedUserAnswers.setData(path(page,idx),value).flatMap {
          d => Try(copy(data = d))
        }
      }
    }
  }

  def appendList[A](page: QuestionPage[A], value: A): Try[UserAnswers] = {
    implicit val writes: Writes[A] = page.writes
    page.cleanup(Some(value),this).flatMap {
      cleanedUserAnswers => {
        cleanedUserAnswers.setData(page.path, getList(page).+:(value)).flatMap {
          d => Try(copy(data = d))
        }
      }
    }
  }

  private def setData[A](path: JsPath, value: A)(implicit writes: Writes[A]): Try[JsObject] = {
    data.setObject(path, Json.toJson(value)) match {
      case JsSuccess(jsValue, _) =>
        Success(jsValue)
      case JsError(errors) =>
        Failure(JsResultException(errors))
    }
  }

  def remove[A](page: QuestionPage[A], idx: Option[Int] = None): Try[UserAnswers] = {

    val updatedData = data.removeObject(path(page, idx)) match {
      case JsSuccess(jsValue, _) =>
        Success(jsValue)
      case JsError(_) =>
        Success(data)
    }

    updatedData.flatMap {
      d =>
        val updatedAnswers = copy(data = d)
        page.cleanup(None, updatedAnswers)
    }
  }

  def remove(pages: Seq[QuestionPage[_]]): Try[UserAnswers] = recursivelyClearQuestions(pages, this)

  @tailrec
  private def recursivelyClearQuestions(pages: Seq[QuestionPage[_]], userAnswers: UserAnswers): Try[UserAnswers] = {
    if (pages.isEmpty) Success(userAnswers) else {
      userAnswers.remove(pages.head) match {
        case Success(answers) => recursivelyClearQuestions(pages.tail, answers)
        case failure@Failure(_) => failure
      }
    }
  }
}

object UserAnswers {

  implicit lazy val reads: Reads[UserAnswers] = {

    import play.api.libs.functional.syntax._

    (
      (__ \ "_id").read[String] and
        (__ \ "data").read[JsObject] and
        (__ \ "lastUpdated").read(MongoDateTimeFormats.localDateTimeRead)
      ) (UserAnswers.apply _)
  }

  implicit lazy val writes: OWrites[UserAnswers] = {

    import play.api.libs.functional.syntax._

    (
      (__ \ "_id").write[String] and
        (__ \ "data").write[JsObject] and
        (__ \ "lastUpdated").write(MongoDateTimeFormats.localDateTimeWrite)
      ) (unlift(UserAnswers.unapply))
  }
}
