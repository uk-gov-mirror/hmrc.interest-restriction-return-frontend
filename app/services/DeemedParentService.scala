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

package services

import javax.inject.Inject
import models.returnModels.DeemedParentModel
import models.{ErrorModel, UserAnswers}
import pages.groupStructure._
import play.api.libs.json.__
import repositories.SessionRepository

import scala.concurrent.{ExecutionContext, Future}
import scala.util.{Failure, Success}

class DeemedParentService @Inject()(sessionRepository: SessionRepository){

  def addDeemedParent(userAnswers: UserAnswers)(implicit ec: ExecutionContext): Future[Either[ErrorModel, AddedDeemedParentSuccess.type]] = {
    DeemedParentModel(userAnswers) match {
      case Right(model) =>
        userAnswers.addToList(__ \ "deemedParents", model) match {

          case Success(updatedAnswers) =>
            updatedAnswers.remove(Seq(
              ParentCompanyNamePage,
              ParentCompanyCTUTRPage,
              ParentCRNPage,
              ParentCompanySAUTRPage,
              PayTaxInUkPage,
              LimitedLiabilityPartnershipPage,
              RegisteredCompaniesHousePage,
              RegisteredForTaxInAnotherCountryPage,
              ReportingCompanySameAsParentPage
            )) match {
              case Success(cleansedAnswers) => sessionRepository.set(cleansedAnswers).map(_ => Right(AddedDeemedParentSuccess))
              case Failure(_) => Future.successful(Left(ErrorModel("Failed to clear parent company answers")))
            }

          case Failure(_) =>
            Future.successful(Left(ErrorModel("Failed to add to list of deemed parents")))
        }
      case Left(error) => Future.successful(Left(error))
    }
  }
}

case object AddedDeemedParentSuccess
