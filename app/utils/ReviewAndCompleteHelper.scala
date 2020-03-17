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

package utils

import models.{NormalMode, Section, UserAnswers}
import models.returnModels.ReviewAndCompleteModel
import pages.groupStructure.{HasDeemedParentPage, ReportingCompanySameAsParentPage}
import play.api.i18n.Messages
import viewmodels.TaskListRow

class ReviewAndCompleteHelper(implicit val messages: Messages) {

  def rows(reviewAndCompleteModel: ReviewAndCompleteModel, userAnswers: UserAnswers): Seq[TaskListRow] = {

    val ultimateParentLink = (userAnswers.get(ReportingCompanySameAsParentPage), userAnswers.get(HasDeemedParentPage)) match {
      case (Some(true), _) => controllers.groupStructure.routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
      case (Some(false), Some(true)) => controllers.groupStructure.routes.DeemedParentReviewAnswersListController.onPageLoad()
      case (_, _) => controllers.groupStructure.routes.CheckAnswersGroupStructureController.onPageLoad(1)
    }

    Seq(
      TaskListRow(
        messages(s"section.${Section.StartReturn}"),
        controllers.aboutReportingCompany.routes.CheckAnswersReportingCompanyController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.startReturn.status}")
      ),
      TaskListRow(
        messages(s"section.${Section.GroupStructure}"),
        ultimateParentLink,
        messages(s"reviewAndComplete.${reviewAndCompleteModel.groupStructure.status}")
      ),
      TaskListRow(
        messages(s"section.${Section.Elections}"),
        controllers.elections.routes.CheckAnswersElectionsController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.elections.status}")
      ),
      TaskListRow(
        messages(s"section.${Section.AboutReturn}"),
        controllers.routes.UnderConstructionController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.aboutReturn.status}")
      ),
      TaskListRow(
        messages(s"section.${Section.UkCompanies}"),
        controllers.ukCompanies.routes.UkCompaniesReviewAnswersListController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.ukCompanies.status}")
      ),
      TaskListRow(
        messages(s"section.${Section.CheckTotals}"),
        controllers.checkTotals.routes.DerivedCompanyController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.checkTotals.status}")
      )
    )
  }
}
