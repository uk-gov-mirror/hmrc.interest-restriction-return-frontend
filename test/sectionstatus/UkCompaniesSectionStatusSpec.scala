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

import java.time.LocalDate
import base.SpecBase
import models.SectionStatus._
import pages.ukCompanies._
import pages.aboutReturn._
import pages.groupLevelInformation._
import assets.constants.BaseConstants
import models.CompanyDetailsModel
import models.returnModels.fullReturn._
import models.FullOrAbbreviatedReturn
import models.sections.UkCompaniesSectionModel
import models.NetTaxInterestIncomeOrExpense._
import models.CompanyEstimatedFigures._

class UkCompaniesSectionStatusSpec extends SpecBase with BaseConstants {

  "getStatus" must {
    "return NotStarted" when {
      "no data from the section has been entered" in {
        val userAnswers = emptyUserAnswers
        UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual NotStarted
      }
    }
    "return InProgress" when {
      "has some data been entered" in {
        val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
        val userAnswers = emptyUserAnswers.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(1)).get
        UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "Abbreviated" when {

        "there is a single company and have NOT completed the base journey" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = None)
          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Abbreviated)
            ua2 <- ua.set(UkCompaniesPage, company, Some(1))
          } yield ua2).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual InProgress
        }

        "there are multiple companies and one has NOT completed the base journey" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company1 = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true))
          val company2 = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = None)
          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Abbreviated)
            ua2 <- ua.set(UkCompaniesPage, company1, Some(1))
            ua3 <- ua2.set(UkCompaniesPage, company2, Some(2))
          } yield ua3).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual InProgress
        }
      }

      "Full" when {

        "there is one company that is not subject to restrictions or reactivations and has not completed the tax interest" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(true),
              containsEstimates = Some(false))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, false)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual InProgress
        }

        "there is one company that is not subject to restrictions or reactivations and has not completed the estimates" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(false),
              containsEstimates = Some(true))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, false)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual InProgress
        }

        "there is one company that is subject to restrictions, said yes to adding one, and has not entered a restriction" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(true),
              netTaxInterestIncomeOrExpense = Some(NetTaxInterestExpense),
              netTaxInterest = Some(BigDecimal(123)),
              containsEstimates = Some(false),
              restriction = Some(true))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual InProgress
        }

        "there is one company that is subject to reactivations, said yes to adding one, and has not entered a reactivation" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(false),
              containsEstimates = Some(false),
              reactivation = Some(true))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, false)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, true)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual InProgress
        }

      }
    }
    
    "return Completed" when {  
      
      "Abbreviated" when {

        "there is a single company and has completed the base journey" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true))
          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Abbreviated)
            ua2 <- ua.set(UkCompaniesPage, company, Some(1))
          } yield ua2).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there are multiple companies that have completed the base journey" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true))
          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Abbreviated)
            ua2 <- ua.set(UkCompaniesPage, company, Some(1))
            ua3 <- ua2.set(UkCompaniesPage, company, Some(2))
          } yield ua3).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

      }

      "Full" when {

        "there is one company that is not subject to restrictions or reactivations and has completed the journey with an expense" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(true),
              netTaxInterestIncomeOrExpense = Some(NetTaxInterestExpense),
              netTaxInterest = Some(BigDecimal(123)),
              containsEstimates = Some(false))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, false)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there is one company that is not subject to restrictions or reactivations and has completed the journey without a tax interest" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(false),
              containsEstimates = Some(false))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, false)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there is one company that is not subject to restrictions or reactivations and has completed the journey with estimates" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(false),
              containsEstimates = Some(true),
              estimatedFigures = Some(Set(TaxEbitda)))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, false)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there is one company that is subject to reactivations and has completed the journey" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(false),
              containsEstimates = Some(false),
              reactivation = Some(true),
              allocatedReactivations = Some(AllocatedReactivationsModel(BigDecimal(123))))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, false)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, true)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there is one company that is subject to restrictions and the AP is the same as group PoA" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(true),
              netTaxInterestIncomeOrExpense = Some(NetTaxInterestExpense),
              netTaxInterest = Some(BigDecimal(123)),
              containsEstimates = Some(false),
              restriction = Some(true),
              accountPeriodSameAsGroup = Some(true),
              allocatedRestrictions = Some(AllocatedRestrictionsModel().setRestriction(1, LocalDate.now, BigDecimal(123))))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there is one company that is subject to restrictions and the AP is different" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(true),
              netTaxInterestIncomeOrExpense = Some(NetTaxInterestExpense),
              netTaxInterest = Some(BigDecimal(123)),
              containsEstimates = Some(false),
              restriction = Some(true),
              accountPeriodSameAsGroup = Some(false))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
            ua5 <- ua4.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.now)
            ua6 <- ua5.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(123))
          } yield ua6).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there is one company that is subject to restrictions and it has two APs" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(true),
              netTaxInterestIncomeOrExpense = Some(NetTaxInterestExpense),
              netTaxInterest = Some(BigDecimal(123)),
              containsEstimates = Some(false),
              restriction = Some(true),
              accountPeriodSameAsGroup = Some(false))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
            ua5 <- ua4.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.now)
            ua6 <- ua5.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(123))
            ua7 <- ua6.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.now)
          } yield ua7).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there is one company that is subject to restrictions and it has three APs" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(true),
              netTaxInterestIncomeOrExpense = Some(NetTaxInterestExpense),
              netTaxInterest = Some(BigDecimal(123)),
              containsEstimates = Some(false),
              restriction = Some(true),
              accountPeriodSameAsGroup = Some(false))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
            ua5 <- ua4.set(CompanyAccountingPeriodEndDatePage(1, 1), LocalDate.now)
            ua6 <- ua5.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(123))
            ua7 <- ua6.set(CompanyAccountingPeriodEndDatePage(1, 2), LocalDate.now)
            ua8 <- ua7.set(CompanyAccountingPeriodEndDatePage(1, 3), LocalDate.now)
            ua9 <- ua8.set(RestrictionAmountForAccountingPeriodPage(1, 3), BigDecimal(123))
          } yield ua9).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there is one company that is subject to restrictions but has no net tax interest" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(false),
              containsEstimates = Some(false))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

        "there is one company that is subject to restrictions but has a net tax interest income" in {
          val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
          val company = 
            UkCompanyModel(
              companyDetails = companyDetails, 
              consenting = Some(true), 
              qicElection = Some(true),
              taxEBITDA = Some(BigDecimal(123)),
              addNetTaxInterest = Some(true),
              netTaxInterestIncomeOrExpense = Some(NetTaxInterestIncome),
              netTaxInterest = Some(BigDecimal(123)),
              containsEstimates = Some(false))

          val userAnswers = (for {
            ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
            ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
            ua3 <- ua2.set(GroupSubjectToReactivationsPage, false)
            ua4 <- ua3.set(UkCompaniesPage, company, Some(1))    
          } yield ua4).get

          UkCompaniesSectionStatus.getStatus(userAnswers) mustEqual Completed
        }

      }

    }
  }

  "currentSection" must {
    "return None where nothing has been completed" in {
      UkCompaniesSectionStatus.currentSection(emptyUserAnswers) mustEqual None
    }

    "return None where we have set both FullOrAbbreviatedReturnPage and GroupSubjectToRestrictionsPage but no companies" in {
      val userAnswers = (for {
        ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
        ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
      } yield ua2).get
      
      UkCompaniesSectionStatus.currentSection(userAnswers) mustEqual None   
    }

    "return Some where we have set both FullOrAbbreviatedReturnPage and GroupSubjectToRestrictionsPage and a company" in {
      val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
      val userAnswers = (for {
        ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
        ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
        ua3 <- ua2.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(1))
      } yield ua3).get

      val expectedModel = UkCompaniesSectionModel(
        ukCompanies = Seq(UkCompanyModel(companyDetails)),
        fullOrAbbreviatedReturn = FullOrAbbreviatedReturn.Full,
        subjectToRestrictions = Some(true),
        subjectToReactivations = None
      )
      
      UkCompaniesSectionStatus.currentSection(userAnswers) mustEqual Some(expectedModel)   
    }

    "return Some where we have set both FullOrAbbreviatedReturnPage and GroupSubjectToRestrictionsPage and multiple companies" in {
      val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
      val userAnswers = (for {
        ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
        ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
        ua3 <- ua2.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(1))
        ua4 <- ua3.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(2))
      } yield ua4).get

      val expectedModel = UkCompaniesSectionModel(
        ukCompanies = Seq(UkCompanyModel(companyDetails), UkCompanyModel(companyDetails)),
        fullOrAbbreviatedReturn = FullOrAbbreviatedReturn.Full,
        subjectToRestrictions = Some(true),
        subjectToReactivations = None
      )
      
      UkCompaniesSectionStatus.currentSection(userAnswers) mustEqual Some(expectedModel)   
    }

    "return Some where we have a company with an accounting period restriction" in {
      val now = LocalDate.now
      val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
      val userAnswers = (for {
        ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
        ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
        ua3 <- ua2.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(1))
        ua4 <- ua3.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(2))
        ua5 <- ua4.set(CompanyAccountingPeriodEndDatePage(2, 1), now)
        ua6 <- ua5.set(RestrictionAmountForAccountingPeriodPage(2, 1), BigDecimal(123))
      } yield ua6).get

      val company2Restrictions = AllocatedRestrictionsModel().setRestriction(1, now, BigDecimal(123))
      val company2 = UkCompanyModel(companyDetails).copy(allocatedRestrictions = Some(company2Restrictions))

      val expectedModel = UkCompaniesSectionModel(
        ukCompanies = Seq(UkCompanyModel(companyDetails), company2),
        fullOrAbbreviatedReturn = FullOrAbbreviatedReturn.Full,
        subjectToRestrictions = Some(true),
        subjectToReactivations = None
      )
      
      UkCompaniesSectionStatus.currentSection(userAnswers) mustEqual Some(expectedModel)   
    }

    "return Some where we have a company with two accounting period restrictions" in {
      val now = LocalDate.now
      val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
      val userAnswers = (for {
        ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
        ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
        ua3 <- ua2.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(1))
        ua4 <- ua3.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(2))
        ua5 <- ua4.set(CompanyAccountingPeriodEndDatePage(2, 1), now)
        ua6 <- ua5.set(RestrictionAmountForAccountingPeriodPage(2, 1), BigDecimal(123))
        ua7 <- ua6.set(CompanyAccountingPeriodEndDatePage(2, 2), now)
        ua8 <- ua7.set(RestrictionAmountForAccountingPeriodPage(2, 2), BigDecimal(234))
      } yield ua8).get

      val company2Restrictions = 
        AllocatedRestrictionsModel()
          .setRestriction(1, now, BigDecimal(123))
          .setRestriction(2, now, BigDecimal(234))
      val company2 = UkCompanyModel(companyDetails).copy(allocatedRestrictions = Some(company2Restrictions))

      val expectedModel = UkCompaniesSectionModel(
        ukCompanies = Seq(UkCompanyModel(companyDetails), company2),
        fullOrAbbreviatedReturn = FullOrAbbreviatedReturn.Full,
        subjectToRestrictions = Some(true),
        subjectToReactivations = None
      )
      
      UkCompaniesSectionStatus.currentSection(userAnswers) mustEqual Some(expectedModel)   
    }

    "return Some where we have a company with three accounting period restrictions" in {
      val now = LocalDate.now
      val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
      val userAnswers = (for {
        ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
        ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
        ua3 <- ua2.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(1))
        ua4 <- ua3.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(2))
        ua5 <- ua4.set(CompanyAccountingPeriodEndDatePage(2, 1), now)
        ua6 <- ua5.set(RestrictionAmountForAccountingPeriodPage(2, 1), BigDecimal(123))
        ua7 <- ua6.set(CompanyAccountingPeriodEndDatePage(2, 2), now)
        ua8 <- ua7.set(RestrictionAmountForAccountingPeriodPage(2, 2), BigDecimal(234))
        ua9 <- ua8.set(CompanyAccountingPeriodEndDatePage(2, 3), now)
        ua10 <- ua9.set(RestrictionAmountForAccountingPeriodPage(2, 3), BigDecimal(345))
      } yield ua10).get

      val company2Restrictions = 
        AllocatedRestrictionsModel()
          .setRestriction(1, now, BigDecimal(123))
          .setRestriction(2, now, BigDecimal(234))
          .setRestriction(3, now, BigDecimal(345))
      val company2 = UkCompanyModel(companyDetails).copy(allocatedRestrictions = Some(company2Restrictions))

      val expectedModel = UkCompaniesSectionModel(
        ukCompanies = Seq(UkCompanyModel(companyDetails), company2),
        fullOrAbbreviatedReturn = FullOrAbbreviatedReturn.Full,
        subjectToRestrictions = Some(true),
        subjectToReactivations = None
      )
      
      UkCompaniesSectionStatus.currentSection(userAnswers) mustEqual Some(expectedModel)   
    }

    "return Some where we have multiple companies with restrictions" in {
      val now = LocalDate.now
      val companyDetails = CompanyDetailsModel(companyName = "name", ctutr = "1234567890")
      val userAnswers = (for {
        ua  <- emptyUserAnswers.set(FullOrAbbreviatedReturnPage, FullOrAbbreviatedReturn.Full)
        ua2 <- ua.set(GroupSubjectToRestrictionsPage, true)
        ua3 <- ua2.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(1))
        ua4 <- ua3.set(CompanyAccountingPeriodEndDatePage(1, 1), now)
        ua5 <- ua4.set(RestrictionAmountForAccountingPeriodPage(1, 1), BigDecimal(123))
        ua6 <- ua5.set(UkCompaniesPage, UkCompanyModel(companyDetails), Some(2))
        ua7 <- ua6.set(CompanyAccountingPeriodEndDatePage(2, 1), now)
        ua8 <- ua7.set(RestrictionAmountForAccountingPeriodPage(2, 1), BigDecimal(123))
      } yield ua8).get

      val companyRestrictions = 
        AllocatedRestrictionsModel()
          .setRestriction(1, now, BigDecimal(123))
      val company = UkCompanyModel(companyDetails).copy(allocatedRestrictions = Some(companyRestrictions))

      val expectedModel = UkCompaniesSectionModel(
        ukCompanies = Seq(company, company),
        fullOrAbbreviatedReturn = FullOrAbbreviatedReturn.Full,
        subjectToRestrictions = Some(true),
        subjectToReactivations = None
      )
      
      UkCompaniesSectionStatus.currentSection(userAnswers) mustEqual Some(expectedModel)   
    }

  }
}