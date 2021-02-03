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

package sectionstatus

import models.SectionStatus._
import models.{SectionStatus, UserAnswers}
import pages.ultimateParentCompany._
import models.returnModels.DeemedParentModel

object UltimateParentCompanySectionStatus {
  
  def getStatus(userAnswers: UserAnswers): SectionStatus = {

    val initialPage = userAnswers.get(ReportingCompanySameAsParentPage)

    val pages = Seq(initialPage) ++ (initialPage match {
      case Some(false) => userAnswers.get(HasDeemedParentPage) match {
        case Some(false) => Seq(companySectionCompleted(userAnswers, 1))
        case Some(true) => Seq(companySectionCompleted(userAnswers, 1), companySectionCompleted(userAnswers, 2)) ++ companySectionCompletedIfStarted(userAnswers, 3)
        case None => Seq(None)
      }
      case _ => Nil
    })

    pages.flatten match {
      case result if result.isEmpty => NotStarted
      case result if result.size == pages.size => Completed
      case _ => InProgress
    }
  }

  def companySectionCompletedIfStarted(userAnswers: UserAnswers, idx: Int): Seq[Option[Any]] = 
    userAnswers.get(DeemedParentPage, Some(idx)) match {
      case Some(_) => Seq(companySectionCompleted(userAnswers, idx))
      case None => Nil
    }
  
  def companySectionCompleted(userAnswers: UserAnswers, idx: Int): Option[Any] = 
    userAnswers.get(DeemedParentPage, Some(idx)) match {
      case result @ Some(DeemedParentModel(_, ctutr @ Some(_), sautr @ _, country @ _, llp @ Some(false), taxInUk @ Some(true), _)) => result
      case result @ Some(DeemedParentModel(_, ctutr @ _, sautr @ Some(_), country @ _, llp @ Some(true), taxInUk @ Some(true), _)) => result
      case result @ Some(DeemedParentModel(_, ctutr @ _, sautr @ _, country @ Some(_), llp @ _, taxInUk @ Some(false), _)) => result
      case _ => None
    }

}