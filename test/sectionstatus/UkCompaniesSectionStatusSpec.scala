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
    }
    
    "return Completed" when {     
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
        subjectToRestrictions = true,
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
        subjectToRestrictions = true,
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
        subjectToRestrictions = true,
        subjectToReactivations = None
      )
      
      UkCompaniesSectionStatus.currentSection(userAnswers) mustEqual Some(expectedModel)   
    }

  }
}