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

import models.UserAnswers
import pages.ultimateParentCompany._
import models.returnModels.DeemedParentModel
import models.sections.UltimateParentCompanySectionModel
import pages.Page._

object UltimateParentCompanySectionStatus extends CurrentSectionStatus[UltimateParentCompanySectionModel] {

  val pages = ultimateParentCompanySectionPages

  def isComplete(section: UltimateParentCompanySectionModel): Boolean = 
    section match {
      case UltimateParentCompanySectionModel(sameAsParent @ true, _, _) => true
      case UltimateParentCompanySectionModel(sameAsParent @ false, hasDeemed @ Some(false), ultimateParent :: Nil) => 
        companySectionCompleted(ultimateParent)
      case UltimateParentCompanySectionModel(sameAsParent @ false, hasDeemed @ Some(true), deemedParent1 :: deemedParent2 :: Nil) =>
        companySectionCompleted(deemedParent1) && companySectionCompleted(deemedParent2) 
      case UltimateParentCompanySectionModel(sameAsParent @ false, hasDeemed @ Some(true), deemedParent1 :: deemedParent2 :: deemedParent3 :: Nil) =>
        companySectionCompleted(deemedParent1) && companySectionCompleted(deemedParent2) && companySectionCompleted(deemedParent3) 
      case _ => false
    }

  def companySectionCompleted(parentCompany: DeemedParentModel): Boolean = 
    parentCompany match {
      case DeemedParentModel(_, ctutr @ Some(_), sautr @ _, country @ _, llp @ Some(false), taxInUk @ Some(true), _) => true
      case DeemedParentModel(_, ctutr @ _, sautr @ Some(_), country @ _, llp @ Some(true), taxInUk @ Some(true), _) => true
      case DeemedParentModel(_, ctutr @ _, sautr @ _, country @ Some(_), llp @ _, taxInUk @ Some(false), _) => true
      case _ => false
    }
  
  def currentSection(userAnswers: UserAnswers): Option[UltimateParentCompanySectionModel] = 
    for {
      reportingCompanySameAsParent <- userAnswers.get(ReportingCompanySameAsParentPage)
    } yield {

      val parentCompanies = Seq(
        userAnswers.get(DeemedParentPage, Some(1)),
        userAnswers.get(DeemedParentPage, Some(2)),
        userAnswers.get(DeemedParentPage, Some(3))
      ).flatten

      UltimateParentCompanySectionModel(
        reportingCompanySameAsParent = reportingCompanySameAsParent,
        hasDeemedParent = userAnswers.get(HasDeemedParentPage),
        parentCompanies = parentCompanies
      )
    }

}