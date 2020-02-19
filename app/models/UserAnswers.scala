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

package models

import java.time.LocalDateTime

import pages._
import play.api.libs.json._

import scala.annotation.tailrec
import scala.util.{Failure, Success, Try}

final case class UserAnswers(
                              id: String,
                              data: JsObject = Json.obj(),
                              lastUpdated: LocalDateTime = LocalDateTime.now,
                              lastPageSaved: Option[Page] = None
                            ) {

  def get[A](page: QuestionPage[A])(implicit rds: Reads[A]): Option[A] =
    Reads.optionNoError(Reads.at(page.path)).reads(data).getOrElse(None)

  def addToList[A](path: JsPath, value: A)(implicit format: Format[A]): Try[UserAnswers] = {

    val updatedData = path.readNullable[Seq[A]].reads(data) match {
      case JsSuccess(Some(models), _) => setData(path, models :+ value)
      case JsSuccess(None, _) => setData(path, Seq(value))
      case JsError(errors) => Failure(JsResultException(errors))
    }

    updatedData.map( d => copy(data = d))
  }

  def set[A](page: QuestionPage[A], value: A)(implicit writes: Writes[A]): Try[UserAnswers] = {
    setData(page.path, value).map {
      d => copy (data = d, lastPageSaved = Some(page))
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

  def remove[A](page: QuestionPage[A]): Try[UserAnswers] = {

    val updatedData = data.removeObject(page.path) match {
      case JsSuccess(jsValue, _) =>
        Success(jsValue)
      case JsError(_) =>
        Success(data)
    }

    updatedData.map {
      d =>
        copy (data = d, lastPageSaved = Some(page))
    }
  }

  def remove(pages: Seq[QuestionPage[_]]): Try[UserAnswers] = recursivelyClearQuestions(pages, this)

  @tailrec
  private def recursivelyClearQuestions(pages: Seq[QuestionPage[_]], userAnswers: UserAnswers): Try[UserAnswers] = {
    if(pages.isEmpty) Success(userAnswers) else {
      userAnswers.remove(pages.head) match {
        case Success(answers) => recursivelyClearQuestions(pages.tail,answers)
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
      (__ \ "lastUpdated").read(MongoDateTimeFormats.localDateTimeRead) and
      (__ \ "lastPageSaved").readNullable[Page]
    ) (UserAnswers.apply _)
  }

  implicit lazy val writes: OWrites[UserAnswers] = {

    import play.api.libs.functional.syntax._

    (
      (__ \ "_id").write[String] and
      (__ \ "data").write[JsObject] and
      (__ \ "lastUpdated").write(MongoDateTimeFormats.localDateTimeWrite) and
      (__ \ "lastPageSaved").writeNullable[Page]
    ) (unlift(UserAnswers.unapply))
  }
}
