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

package assets.constants

import models.{SectionStatus, NormalMode}
import models.SectionStatus.{Completed, InProgress, NotStarted}
import models.returnModels.ReviewAndCompleteModel
import play.api.libs.json.{JsObject, Json}
import assets.messages.{ReviewAndCompleteMessages, SectionHeaderMessages}
import viewmodels.TaskListRow

object ReviewAndCompleteConstants {

  val reviewAndCompleteModel: ReviewAndCompleteModel = ReviewAndCompleteModel(
    aboutReturnStatus = NotStarted,
    electionsStatus = InProgress,
    groupLevelInformationStatus = Completed,
    ultimateParentCompanyStatus = NotStarted,
    ukCompaniesStatus = InProgress,
    checkTotalsStatus = Completed
  )

  val reviewAndCompleteJson: JsObject = Json.obj(
    "aboutReturnStatus" -> SectionStatus.NotStarted.toString,
    "electionsStatus" -> SectionStatus.InProgress.toString,
    "groupLevelInformationStatus" -> SectionStatus.Completed.toString,
    "ultimateParentCompanyStatus" -> SectionStatus.NotStarted.toString,
    "ukCompaniesStatus" -> SectionStatus.InProgress.toString,
    "checkTotalsStatus" -> SectionStatus.Completed.toString
  )

  val aboutReturnNotStartedRow = TaskListRow(
    SectionHeaderMessages.aboutReturn,
    controllers.aboutReturn.routes.ReportingCompanyAppointedController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.notStarted
  )
  
  val aboutReturnInProgressRow = TaskListRow(
    SectionHeaderMessages.aboutReturn,
    controllers.aboutReturn.routes.ReportingCompanyAppointedController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.inProgress
  )

  val aboutReturnCompletedRow = TaskListRow(
    SectionHeaderMessages.aboutReturn,
    controllers.aboutReturn.routes.CheckAnswersAboutReturnController.onPageLoad(),
    ReviewAndCompleteMessages.completed
  )

  val ultimateParentCompanyNotStartedRow = TaskListRow(
    SectionHeaderMessages.ultimateParentCompany,
    controllers.ultimateParentCompany.routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.notStarted
  )

  val ultimateParentCompanyInProgressRow = TaskListRow(
    SectionHeaderMessages.ultimateParentCompany,
    controllers.ultimateParentCompany.routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.inProgress
  )

  val ultimateParentCompanyCompletedRow = TaskListRow(
    SectionHeaderMessages.ultimateParentCompany,
    controllers.ultimateParentCompany.routes.CheckAnswersGroupStructureController.onPageLoad(1),
    ReviewAndCompleteMessages.completed
  )

  val sameParentCompanyNotStartedRow = TaskListRow(
    SectionHeaderMessages.ultimateParentCompany,
    controllers.ultimateParentCompany.routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.notStarted
  )

  val sameParentCompanyInProgressRow = TaskListRow(
    SectionHeaderMessages.ultimateParentCompany,
    controllers.ultimateParentCompany.routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.inProgress
  )

  val sameParentCompanyCompletedRow = TaskListRow(
    SectionHeaderMessages.ultimateParentCompany,
    controllers.ultimateParentCompany.routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.completed
  )

  val deemedParentCompanyNotStartedRow = TaskListRow(
    SectionHeaderMessages.ultimateParentCompany,
    controllers.ultimateParentCompany.routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.notStarted
  )

  val deemedParentCompanyInProgressRow = TaskListRow(
    SectionHeaderMessages.ultimateParentCompany,
    controllers.ultimateParentCompany.routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.inProgress
  )

  val deemedParentCompanyCompletedRow = TaskListRow(
    SectionHeaderMessages.ultimateParentCompany,
    controllers.ultimateParentCompany.routes.DeemedParentReviewAnswersListController.onPageLoad(),
    ReviewAndCompleteMessages.completed
  )

  val electionsNotStartedRow = TaskListRow(
    SectionHeaderMessages.elections,
    controllers.elections.routes.GroupRatioElectionController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.notStarted
  )

  val electionsInProgressRow = TaskListRow(
    SectionHeaderMessages.elections,
    controllers.elections.routes.GroupRatioElectionController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.inProgress
  )

  val electionsCompletedRow = TaskListRow(
    SectionHeaderMessages.elections,
    controllers.elections.routes.CheckAnswersElectionsController.onPageLoad(),
    ReviewAndCompleteMessages.completed
  )

  val groupLevelInformationNotStartedRow = TaskListRow(
    SectionHeaderMessages.groupLevelInformation,
    controllers.groupLevelInformation.routes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.notStarted
  )

  val groupLevelInformationInProgressRow = TaskListRow(
    SectionHeaderMessages.groupLevelInformation,
    controllers.groupLevelInformation.routes.GroupSubjectToRestrictionsController.onPageLoad(NormalMode),
    ReviewAndCompleteMessages.inProgress
  )

  val groupLevelInformationCompletedRow = TaskListRow(
    SectionHeaderMessages.groupLevelInformation,
    controllers.groupLevelInformation.routes.CheckAnswersGroupLevelController.onPageLoad(),
    ReviewAndCompleteMessages.completed
  )

  val ukCompaniesNotStartedRow = TaskListRow(
    SectionHeaderMessages.ukCompanies,
    controllers.ukCompanies.routes.AboutAddingUKCompaniesController.onPageLoad(),
    ReviewAndCompleteMessages.notStarted
  )

  val ukCompaniesInProgressRow = TaskListRow(
    SectionHeaderMessages.ukCompanies,
    controllers.ukCompanies.routes.AboutAddingUKCompaniesController.onPageLoad(),
    ReviewAndCompleteMessages.inProgress
  )

  val ukCompaniesCompletedRow = TaskListRow(
    SectionHeaderMessages.ukCompanies,
    controllers.ukCompanies.routes.UkCompaniesReviewAnswersListController.onPageLoad(),
    ReviewAndCompleteMessages.completed
  )

  val checkTotalsNotStartedRow = TaskListRow(
    SectionHeaderMessages.checkTotals,
    controllers.checkTotals.routes.DerivedCompanyController.onPageLoad(),
    ReviewAndCompleteMessages.notStarted
  )

  val checkTotalsInProgressRow = TaskListRow(
    SectionHeaderMessages.checkTotals,
    controllers.checkTotals.routes.DerivedCompanyController.onPageLoad(),
    ReviewAndCompleteMessages.inProgress
  )

  val checkTotalsCompletedRow = TaskListRow(
    SectionHeaderMessages.checkTotals,
    controllers.checkTotals.routes.DerivedCompanyController.onPageLoad(),
    ReviewAndCompleteMessages.completed
  )

}
