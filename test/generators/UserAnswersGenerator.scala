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
    arbitrary[(EstimatedFiguresPage, JsValue)] ::
    arbitrary[(CheckAnswersGroupLevelPage, JsValue)] ::
    arbitrary[(GroupEBITDAPage, JsValue)] ::
    arbitrary[(GroupRatioPercentagePage, JsValue)] ::
    arbitrary[(DisallowedAmountPage, JsValue)] ::
    arbitrary[(TellUsWhatHasChangedPage, JsValue)] ::
    arbitrary[(RestrictionAmountSameAPPage, JsValue)] ::
    arbitrary[(CompanyAccountingPeriodSameAsGroupPage, JsValue)] ::
    arbitrary[(AddRestrictionPage, JsValue)] ::
    arbitrary[(PartnershipDeletionConfirmationPage, JsValue)] ::
    arbitrary[(PartnershipsReviewAnswersListPage, JsValue)] ::
    arbitrary[(AddAnReactivationQueryPage, JsValue)] ::
    arbitrary[(ReactivationAmountPage, JsValue)] ::
    arbitrary[(AccountingPeriodPage, JsValue)] ::
    arbitrary[(InvestorGroupsDeletionConfirmationPage, JsValue)] ::
    arbitrary[(InvestmentsDeletionConfirmationPage, JsValue)] ::
    arbitrary[(InvestmentNamePage, JsValue)] ::
    arbitrary[(ConsentingCompanyPage, JsValue)] ::
    arbitrary[(EnterCompanyTaxEBITDAPage, JsValue)] ::
    arbitrary[(NetTaxInterestIncomeOrExpensePage, JsValue)] ::
    arbitrary[(DeletionConfirmationPage, JsValue)] ::
    arbitrary[(CompanyDetailsPage, JsValue)] ::
    arbitrary[(NetTaxInterestAmountPage, JsValue)] ::
    arbitrary[(PartnershipSAUTRPage, JsValue)] ::
    arbitrary[(IsUkPartnershipPage, JsValue)] ::
    arbitrary[(PartnershipNamePage, JsValue)] ::
    arbitrary[(InvestorRatioMethodPage, JsValue)] ::
    arbitrary[(InvestorGroupNamePage, JsValue)] ::
    arbitrary[(AddInvestorGroupPage, JsValue)] ::
    arbitrary[(OtherInvestorGroupElectionsPage, JsValue)] ::
    arbitrary[(InterestAllowanceConsolidatedPshipElectionPage, JsValue)] ::
    arbitrary[(ElectedInterestAllowanceConsolidatedPshipBeforePage, JsValue)] ::
    arbitrary[(InterestAllowanceNonConsolidatedInvestmentsElectionPage, JsValue)] ::
    arbitrary[(InterestAllowanceAlternativeCalcElectionPage, JsValue)] ::
    arbitrary[(ElectedInterestAllowanceAlternativeCalcBeforePage, JsValue)] ::
    arbitrary[(GroupEBITDAChargeableGainsElectionPage, JsValue)] ::
    arbitrary[(ElectedGroupEBITDABeforePage, JsValue)] ::
    arbitrary[(GroupRatioBlendedElectionPage, JsValue)] ::
    arbitrary[(EnterQNGIEPage, JsValue)] ::
    arbitrary[(CountryOfIncorporationPage, JsValue)] ::
    arbitrary[(EnterANGIEPage, JsValue)] ::
    arbitrary[(GroupRatioElectionPage, JsValue)] ::
    arbitrary[(ParentCompanySAUTRPage, JsValue)] ::
    arbitrary[(PayTaxInUkPage, JsValue)] ::
    arbitrary[(LimitedLiabilityPartnershipPage, JsValue)] ::
    arbitrary[(ParentCompanyCTUTRPage, JsValue)] ::
    arbitrary[(ParentCompanyNamePage, JsValue)] ::
    arbitrary[(HasDeemedParentPage, JsValue)] ::
    arbitrary[(ContinueSavedReturnPage, JsValue)] ::
    arbitrary[(CheckAnswersAboutReturnPage, JsValue)] ::
    arbitrary[(ReturnContainEstimatesPage, JsValue)] ::
    arbitrary[(GroupInterestAllowancePage, JsValue)] ::
    arbitrary[(GroupInterestCapacityPage, JsValue)] ::
    arbitrary[(GroupSubjectToRestrictionsPage, JsValue)] ::
    arbitrary[(ReportingCompanyAppointedPage, JsValue)] ::
    arbitrary[(ReportingCompanyNamePage, JsValue)] ::
    arbitrary[(ReportingCompanyRequiredPage, JsValue)] ::
    arbitrary[(ReportingCompanyCTUTRPage, JsValue)] ::
    arbitrary[(InterestReactivationsCapPage, JsValue)] ::
    arbitrary[(InterestAllowanceBroughtForwardPage, JsValue)] ::
    arbitrary[(GroupSubjectToReactivationsPage, JsValue)] ::
    arbitrary[(FullOrAbbreviatedReturnPage, JsValue)] ::
    arbitrary[(RevisingReturnPage, JsValue)] ::
    arbitrary[(AgentNamePage, JsValue)] ::
    arbitrary[(AgentActingOnBehalfOfCompanyPage, JsValue)] ::
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
