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

import models.Section
import models.returnModels.ReviewAndCompleteModel
import play.api.i18n.Messages
import viewmodels.TaskListRow

class ReviewAndCompleteHelper(implicit val messages: Messages) {

  def rows(reviewAndCompleteModel: ReviewAndCompleteModel): Seq[TaskListRow] = {

    Seq(
      TaskListRow(
        messages(s"section.${Section.StartReturn}"),
        controllers.aboutReportingCompany.routes.CheckAnswersReportingCompanyController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.startReturn}")
      ),
      TaskListRow(
        messages(s"section.${Section.Elections}"),
        controllers.elections.routes.CheckAnswersElectionsController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.elections}")
      ),
      TaskListRow(
        messages(s"section.${Section.GroupStructure}"),
        controllers.groupStructure.routes.DeemedParentReviewAnswersListController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.groupStructure}")
      ),
      TaskListRow(
        messages(s"section.${Section.AboutReturn}"),
        controllers.routes.UnderConstructionController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.aboutReturn}")
      ),
      TaskListRow(
        messages(s"section.${Section.UkCompanies}"),
        controllers.ukCompanies.routes.UkCompaniesReviewAnswersListController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.ukCompanies}")
      ),
      TaskListRow(
        messages(s"section.${Section.CheckTotals}"),
        controllers.checkTotals.routes.DerivedCompanyController.onPageLoad(),
        messages(s"reviewAndComplete.${reviewAndCompleteModel.checkTotals}")
      )
    )
  }
}
