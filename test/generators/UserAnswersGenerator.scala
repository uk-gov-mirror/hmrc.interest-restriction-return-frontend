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

package generators

import models.UserAnswers
import org.scalacheck.Arbitrary.arbitrary
import org.scalacheck.{Arbitrary, Gen}
import org.scalatest.TryValues
import pages._
import pages.aboutReturn._
import pages.groupLevelInformation._
import pages.elections._
import pages.ultimateParentCompany._
import pages.aboutReturn._
import pages.ukCompanies._
import play.api.libs.json.{JsValue, Json}

trait UserAnswersGenerator extends TryValues {
  self: Generators =>

  val generators: Seq[Gen[(QuestionPage[_], JsValue)]] =
    arbitrary[(TellUsWhatHasChangedPage.type, JsValue)] ::
    arbitrary[(RestrictionAmountSameAPPage.type, JsValue)] ::
    arbitrary[(CompanyAccountingPeriodSameAsGroupPage.type, JsValue)] ::
    arbitrary[(AddRestrictionPage.type, JsValue)] ::
    arbitrary[(PartnershipDeletionConfirmationPage.type, JsValue)] ::
    arbitrary[(PartnershipsReviewAnswersListPage.type, JsValue)] ::
    arbitrary[(AddAnReactivationQueryPage.type, JsValue)] ::
    arbitrary[(ReactivationAmountPage.type, JsValue)] ::
    arbitrary[(AccountingPeriodStartPage.type, JsValue)] ::
    arbitrary[(AccountingPeriodEndPage.type, JsValue)] ::
    arbitrary[(InvestorGroupsDeletionConfirmationPage.type, JsValue)] ::
    arbitrary[(InvestmentsDeletionConfirmationPage.type, JsValue)] ::
    arbitrary[(InvestmentNamePage.type, JsValue)] ::
    arbitrary[(ConsentingCompanyPage.type, JsValue)] ::
    arbitrary[(EnterCompanyTaxEBITDAPage.type, JsValue)] ::
    arbitrary[(NetTaxInterestIncomeOrExpensePage.type, JsValue)] ::
    arbitrary[(DeletionConfirmationPage.type, JsValue)] ::
    arbitrary[(CompanyDetailsPage.type, JsValue)] ::
    arbitrary[(NetTaxInterestAmountPage.type, JsValue)] ::
    arbitrary[(PartnershipSAUTRPage.type, JsValue)] ::
    arbitrary[(IsUkPartnershipPage.type, JsValue)] ::
    arbitrary[(PartnershipNamePage.type, JsValue)] ::
    arbitrary[(InvestorRatioMethodPage.type, JsValue)] ::
    arbitrary[(InvestorGroupNamePage.type, JsValue)] ::
    arbitrary[(AddInvestorGroupPage.type, JsValue)] ::
    arbitrary[(OtherInvestorGroupElectionsPage.type, JsValue)] ::
    arbitrary[(GroupEBITDAPage.type, JsValue)] ::
    arbitrary[(InterestAllowanceConsolidatedPshipElectionPage.type, JsValue)] ::
    arbitrary[(ElectedInterestAllowanceConsolidatedPshipBeforePage.type, JsValue)] ::
    arbitrary[(InterestAllowanceNonConsolidatedInvestmentsElectionPage.type, JsValue)] ::
    arbitrary[(GroupRatioPercentagePage.type, JsValue)] ::
    arbitrary[(InterestAllowanceAlternativeCalcElectionPage.type, JsValue)] ::
    arbitrary[(ElectedInterestAllowanceAlternativeCalcBeforePage.type, JsValue)] ::
    arbitrary[(GroupEBITDAChargeableGainsElectionPage.type, JsValue)] ::
    arbitrary[(ElectedGroupEBITDABeforePage.type, JsValue)] ::
    arbitrary[(GroupRatioBlendedElectionPage.type, JsValue)] ::
    arbitrary[(EnterQNGIEPage.type, JsValue)] ::
    arbitrary[(CountryOfIncorporationPage.type, JsValue)] ::
    arbitrary[(EnterANGIEPage.type, JsValue)] ::
    arbitrary[(GroupRatioElectionPage.type, JsValue)] ::
    arbitrary[(ParentCompanySAUTRPage.type, JsValue)] ::
    arbitrary[(PayTaxInUkPage.type, JsValue)] ::
    arbitrary[(LimitedLiabilityPartnershipPage.type, JsValue)] ::
    arbitrary[(ParentCompanyCTUTRPage.type, JsValue)] ::
    arbitrary[(ParentCompanyNamePage.type, JsValue)] ::
    arbitrary[(HasDeemedParentPage.type, JsValue)] ::
    arbitrary[(ContinueSavedReturnPage.type, JsValue)] ::
    arbitrary[(CheckAnswersReportingCompanyPage.type, JsValue)] ::
    arbitrary[(ReturnContainEstimatesPage.type, JsValue)] ::
    arbitrary[(GroupInterestAllowancePage.type, JsValue)] ::
    arbitrary[(GroupInterestCapacityPage.type, JsValue)] ::
    arbitrary[(GroupSubjectToRestrictionsPage.type, JsValue)] ::
    arbitrary[(ReportingCompanyAppointedPage.type, JsValue)] ::
    arbitrary[(ReportingCompanyNamePage.type, JsValue)] ::
    arbitrary[(ReportingCompanyRequiredPage.type, JsValue)] ::
    arbitrary[(ReportingCompanyCTUTRPage.type, JsValue)] ::
    arbitrary[(InterestReactivationsCapPage.type, JsValue)] ::
    arbitrary[(InterestAllowanceBroughtForwardPage.type, JsValue)] ::
    arbitrary[(GroupSubjectToReactivationsPage.type, JsValue)] ::
    arbitrary[(FullOrAbbreviatedReturnPage.type, JsValue)] ::
    arbitrary[(RevisingReturnPage.type, JsValue)] ::
    arbitrary[(InfrastructureCompanyElectionPage.type, JsValue)] ::
    arbitrary[(AgentNamePage.type, JsValue)] ::
    arbitrary[(AgentActingOnBehalfOfCompanyPage.type, JsValue)] ::
    arbitrary[(InfrastructureCompanyElectionPage.type, JsValue)] ::
    Nil

  implicit lazy val arbitraryUserData: Arbitrary[UserAnswers] = {

    import models._

    Arbitrary {
      for {
        id      <- nonEmptyString
        data    <- generators match {
          case Nil => Gen.const(Map[QuestionPage[_], JsValue]())
          case _   => Gen.mapOf(oneOf(generators))
        }
      } yield UserAnswers (
        id = id,
        data = data.foldLeft(Json.obj()) {
          case (obj, (path, value)) =>
            obj.setObject(path.path, value).get
        },
        lastPageSaved = data.lastOption.map(_._1)
      )
    }
  }
}
