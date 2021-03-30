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

import models.sections.UkCompaniesSectionModel
import pages.ukCompanies.UkCompaniesPage
import models._
import models.returnModels.fullReturn._
import pages.ukCompanies._
import pages.aboutReturn._
import pages.groupLevelInformation._
import pages.Page._

object UkCompaniesSectionStatus extends CurrentSectionStatus[UkCompaniesSectionModel] {

  val pages = ukCompaniesSectionPages

  override def isNotStarted(userAnswers: UserAnswers): Boolean = userAnswers.getList(UkCompaniesPage).isEmpty

  def isComplete(section: UkCompaniesSectionModel): Boolean = {
    false
  }
  
  def currentSection(userAnswers: UserAnswers): Option[UkCompaniesSectionModel] = {
    val companies = userAnswers.getList(UkCompaniesPage).zipWithIndex
    val enrichedCompanies = companies.map{ case (ukCompany, companyIdx) => enrichRestrictions(userAnswers, ukCompany, companyIdx) }

    enrichedCompanies match {
      case Nil => None
      case _ => 
        for {
          fullOrAbbreviatedReturn <- userAnswers.get(FullOrAbbreviatedReturnPage)
          subjectToRestrictions   <- userAnswers.get(GroupSubjectToRestrictionsPage)
          subjectToReactivations  = userAnswers.get(GroupSubjectToReactivationsPage)    
        } yield UkCompaniesSectionModel(enrichedCompanies, fullOrAbbreviatedReturn, subjectToRestrictions, subjectToReactivations)
    }

  }

  def enrichRestrictions(userAnswers: UserAnswers, ukCompanyModel: UkCompanyModel, companyIdx: Int): UkCompanyModel = {
    val firstAccountperiodRestrictions = getAccountingPeriodRestriction(userAnswers, AllocatedRestrictionsModel(), companyIdx, 1)
    firstAccountperiodRestrictions match {
      case Some(restrictions) => 
        val withAp2And3 = getAccountingperiodRestrictions(userAnswers, restrictions, companyIdx)
        ukCompanyModel.copy(allocatedRestrictions = Some(withAp2And3))
      case None => ukCompanyModel
    }
    
  }

  def getAccountingPeriodRestriction(userAnswers: UserAnswers, allocatedRestrictions: AllocatedRestrictionsModel, companyIdx: Int, apIdx: Int): Option[AllocatedRestrictionsModel] = 
    userAnswers.get(CompanyAccountingPeriodEndDatePage(companyIdx, apIdx)).map { endDate =>
      val amount = userAnswers.get(RestrictionAmountForAccountingPeriodPage(companyIdx, apIdx)).getOrElse(BigDecimal(0))
      allocatedRestrictions.setRestriction(1, endDate, amount)
    }

  def getAccountingperiodRestrictions(userAnswers: UserAnswers, allocatedRestrictions: AllocatedRestrictionsModel, companyIdx: Int): AllocatedRestrictionsModel = {
    getAccountingPeriodRestriction(userAnswers, allocatedRestrictions, companyIdx, 2)
      .flatMap(restrictions => getAccountingPeriodRestriction(userAnswers, restrictions, companyIdx, 3))
      .getOrElse(allocatedRestrictions)
  }

}