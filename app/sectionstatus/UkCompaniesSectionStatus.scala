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
import models.FullOrAbbreviatedReturn
import models.NetTaxInterestIncomeOrExpense._

object UkCompaniesSectionStatus extends CurrentSectionStatus[UkCompaniesSectionModel] {

  val pages = ukCompaniesSectionPages

  override def isNotStarted(userAnswers: UserAnswers): Boolean = userAnswers.getList(UkCompaniesPage).isEmpty

  def isComplete(section: UkCompaniesSectionModel): Boolean = {
    section.ukCompanies.forall(isCompanyComplete(_, section))
  }

  def isCompanyComplete(ukCompany: UkCompanyModel, section: UkCompaniesSectionModel): Boolean =
    section.fullOrAbbreviatedReturn match {
      case FullOrAbbreviatedReturn.Abbreviated => mainRouteComplete(ukCompany)
      case FullOrAbbreviatedReturn.Full => 
        val fullPages = mainRouteComplete(ukCompany) && fullRouteComplete(ukCompany) && netTaxInterestComplete(ukCompany)
        (fullPages, section.subjectToRestrictions, section.subjectToReactivations) match {
          case (false, _, _)          => false
          case (true, Some(true), _)  => restrictionsRouteComplete(ukCompany)
          case (true, _, Some(true))  => reactivationsRouteComplete(ukCompany)
          case (true, _, _)           => true
        }
    }

  def mainRouteComplete(ukCompany: UkCompanyModel): Boolean =
    (ukCompany.consenting, ukCompany.qicElection) match {
      case (Some(_), Some(_)) => true
      case _ => false
    }  
  
  def fullRouteComplete(ukCompany: UkCompanyModel): Boolean =
    (ukCompany.taxEBITDA, ukCompany.containsEstimates, ukCompany.estimatedFigures) match {
      case (Some(_), Some(true), Some(_)) => true
      case (Some(_), Some(false), _) => true
      case _ => false
    }

  def netTaxInterestComplete(ukCompany: UkCompanyModel): Boolean =
    (ukCompany.addNetTaxInterest, ukCompany.netTaxInterestIncomeOrExpense, ukCompany.netTaxInterest) match {
      case (Some(true), Some(_), Some(_)) => true
      case (Some(false), _, _) => true
      case _ => false
    }

  def restrictionsRouteComplete(ukCompany: UkCompanyModel): Boolean =
    (ukCompany.netTaxInterestIncomeOrExpense, ukCompany.restriction, ukCompany.allocatedRestrictions) match {
      case (Some(NetTaxInterestExpense), Some(true), Some(AllocatedRestrictionsModel(Some(_), _, _, Some(_), _, _))) => true
      case (Some(NetTaxInterestExpense), Some(false), _) => true
      case (Some(NetTaxInterestIncome), _, _) => true
      case (None, _, _) => true
      case _ => false
    }

  def reactivationsRouteComplete(ukCompany: UkCompanyModel): Boolean =
    (ukCompany.reactivation, ukCompany.allocatedReactivations) match {
      case (Some(true), Some(_)) => true
      case (Some(false), _) => true
      case _ => false
    }
  
  def currentSection(userAnswers: UserAnswers): Option[UkCompaniesSectionModel] = {
    val companies = userAnswers.getList(UkCompaniesPage).zipWithIndex
    val enrichedCompanies = companies.map{ case (ukCompany, companyIdx) => enrichRestrictions(userAnswers, ukCompany, companyIdx + 1) }

    enrichedCompanies match {
      case Nil => None
      case _ => 
        for {
          fullOrAbbreviatedReturn <- userAnswers.get(FullOrAbbreviatedReturnPage)
          subjectToRestrictions   = userAnswers.get(GroupSubjectToRestrictionsPage)
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
      allocatedRestrictions.setRestriction(apIdx, endDate, amount)
    }

  def getAccountingperiodRestrictions(userAnswers: UserAnswers, allocatedRestrictions: AllocatedRestrictionsModel, companyIdx: Int): AllocatedRestrictionsModel = {
    getAccountingPeriodRestriction(userAnswers, allocatedRestrictions, companyIdx, 2) match {
      case Some(restrictions) => getAccountingPeriodRestriction(userAnswers, restrictions, companyIdx, 3).getOrElse(restrictions)
      case None => allocatedRestrictions
    }
  }

}