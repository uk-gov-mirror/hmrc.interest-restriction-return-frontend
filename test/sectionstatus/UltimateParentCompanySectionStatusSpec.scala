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

import base.SpecBase
import models.SectionStatus._
import pages.ultimateParentCompany._
import pages.Page._
import assets.constants.BaseConstants
import models.UserAnswers
import models.FullOrAbbreviatedReturn._
import models.returnModels.DeemedParentModel
import models.returnModels._

class UltimateParentCompanySectionStatusSpec extends SpecBase with BaseConstants {

  "UltimateParentCompanySectionStatus" must {
    "return NotStarted" when {
      "no data from the section has been entered" in {
        val userAnswers = UserAnswers("id")
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual NotStarted
      }
    }
    "return InProgress" when {
      "only ReportingCompanySameAsParent is set and it's set to false" in {
        val userAnswers = UserAnswers("id").set(ReportingCompanySameAsParentPage, false).get
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual InProgress
      }
  
      "has deemed parents is set to false and all but one ultimate parent info are entered for a UK LLP" in {
        val deemedParentModel = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            limitedLiabilityPartnership = Some(true),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )
        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, false).get
            .set(DeemedParentPage, deemedParentModel, Some(1)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "has deemed parents is set to false and all but one ultimate parent info are entered for a UK Co" in {
        val deemedParentModel = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            limitedLiabilityPartnership = Some(false),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )
        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, false).get
            .set(DeemedParentPage, deemedParentModel, Some(1)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "has deemed parents is set to false and all but one ultimate parent info are entered for a non UK Co" in {
        val deemedParentModel = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            payTaxInUk = Some(false),
            reportingCompanySameAsParent = Some(false)
          )
        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, false).get
            .set(DeemedParentPage, deemedParentModel, Some(1)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "has deemed parents is set to true and only part of a deemed parent is entered" in {
        val deemedParentModel = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            payTaxInUk = Some(false),
            reportingCompanySameAsParent = Some(false)
          )
        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, true).get
            .set(DeemedParentPage, deemedParentModel, Some(1)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "has deemed parents is set to true and only one deemed parent is entered" in {
        val deemedParentModel = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            sautr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(true),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )
        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, true).get
            .set(DeemedParentPage, deemedParentModel, Some(1)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "has deemed parents is set to true and two deemed parents are added, but one is incomplete" in {
        val deemedParentModel1 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            sautr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(true),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )

        val deemedParentModel2 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            limitedLiabilityPartnership = Some(false),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )

        val userAnswers = 
          UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, true).get
            .set(DeemedParentPage, deemedParentModel1, Some(1)).get
            .set(DeemedParentPage, deemedParentModel2, Some(2)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual InProgress
      }

      "has deemed parents is set to true and three deemed parents are added, but one is incomplete" in {
        val deemedParentModel1 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            sautr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(true),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )

          val deemedParentModel2 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            ctutr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(false),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )

        val deemedParentModel3 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            payTaxInUk = Some(false),
            reportingCompanySameAsParent = Some(false)
          )

        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, true).get
            .set(DeemedParentPage, deemedParentModel1, Some(1)).get
            .set(DeemedParentPage, deemedParentModel2, Some(2)).get
            .set(DeemedParentPage, deemedParentModel3, Some(3)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual InProgress
      }
    }
    
    "return Completed" when {
      "only ReportingCompanySameAsParent is set and it's set to false" in {
        val userAnswers = UserAnswers("id").set(ReportingCompanySameAsParentPage, true).get
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "has deemed parents is set to false and all ultimate parent info are entered for a UK LLP" in {
        val deemedParentModel = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            sautr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(true),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )
        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, false).get
            .set(DeemedParentPage, deemedParentModel, Some(1)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "has deemed parents is set to false and all ultimate parent info are entered for a UK Co" in {
        val deemedParentModel = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            ctutr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(false),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )
        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, false).get
            .set(DeemedParentPage, deemedParentModel, Some(1)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "has deemed parents is set to false and all ultimate parent info are entered for a non UK Co" in {
        val deemedParentModel = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            countryOfIncorporation = Some(CountryCodeModel("ES", "Spain")),
            limitedLiabilityPartnership = Some(false),
            payTaxInUk = Some(false),
            reportingCompanySameAsParent = Some(false)
          )
        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, false).get
            .set(DeemedParentPage, deemedParentModel, Some(1)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "has deemed parents is set to true and two deemed parents are added" in {
        val deemedParentModel1 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            sautr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(true),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )

        val deemedParentModel2 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            ctutr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(false),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )

        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, true).get
            .set(DeemedParentPage, deemedParentModel1, Some(1)).get
            .set(DeemedParentPage, deemedParentModel2, Some(2)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual Completed
      }

      "has deemed parents is set to true and three deemed parents are added" in {
        val deemedParentModel1 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            sautr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(true),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )

        val deemedParentModel2 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            ctutr = Some(UTRModel("1123456789")),
            limitedLiabilityPartnership = Some(false),
            payTaxInUk = Some(true),
            reportingCompanySameAsParent = Some(false)
          )

        val deemedParentModel3 = 
          DeemedParentModel(
            companyName = CompanyNameModel("Name"),
            countryOfIncorporation = Some(CountryCodeModel("ES", "Spain")),
            payTaxInUk = Some(false),
            reportingCompanySameAsParent = Some(false)
          )

        val userAnswers = UserAnswers("id")
            .set(ReportingCompanySameAsParentPage, false).get
            .set(HasDeemedParentPage, true).get
            .set(DeemedParentPage, deemedParentModel1, Some(1)).get
            .set(DeemedParentPage, deemedParentModel2, Some(2)).get
            .set(DeemedParentPage, deemedParentModel3, Some(3)).get
        
        UltimateParentCompanySectionStatus.getStatus(userAnswers) mustEqual Completed
      }

    }
  }
}