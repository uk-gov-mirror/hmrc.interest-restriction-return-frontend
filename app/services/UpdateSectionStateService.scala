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

import javax.inject.{Inject, Singleton}
import models.SectionStatus.{Completed, InProgress}
import models.UserAnswers
import models.returnModels.ReviewAndCompleteModel
import pages.Page
import pages.aboutReportingCompany.CheckAnswersReportingCompanyPage
import pages.elections.CheckAnswersElectionsPage
import pages.groupStructure.{CheckAnswersGroupStructurePage, DeemedParentPage, HasDeemedParentPage, ReportingCompanySameAsParentPage}
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.ukCompanies.{CheckAnswersUkCompanyPage, DerivedCompanyPage, UkCompaniesPage}

@Singleton
class UpdateSectionStateService @Inject()() {

  def updateState(userAnswers: UserAnswers, page: Page): ReviewAndCompleteModel =
    userAnswers.get(ReviewAndCompletePage).fold(ReviewAndCompleteModel()) { model =>
      Page.sections.find {
        section => section._2.contains(page)
      }.fold(model) { section =>
        val status = if (completionPages(userAnswers).contains(page)) Completed else InProgress
        model.update(section._1, status, page)
      }
    }

  private def completionPages(userAnswers: UserAnswers): List[Page] =
    List(
      CheckAnswersElectionsPage,
      CheckAnswersUkCompanyPage,
      CheckAnswersReportingCompanyPage,
      UkCompaniesPage,
      DerivedCompanyPage
    ) :+ ((userAnswers.get(HasDeemedParentPage), userAnswers.get(ReportingCompanySameAsParentPage)) match {
      case (Some(true), _) => DeemedParentPage
      case (_, Some(true)) => ReportingCompanySameAsParentPage
      case _ => CheckAnswersGroupStructurePage
    })
}
