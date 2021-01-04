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

package models
import generators.ModelGenerators
import models.InvestorRatioMethod.{FixedRatioMethod, GroupRatioMethod}
import models.OtherInvestorGroupElections.{GroupEBITDA, GroupRatioBlended, InterestAllowanceAlternativeCalculation, InterestAllowanceConsolidatedPartnership, InterestAllowanceNonConsolidatedInvestment}
import org.scalacheck.Arbitrary.arbitrary
import org.scalatest.{MustMatchers, OptionValues, WordSpec}
import org.scalatestplus.scalacheck.ScalaCheckPropertyChecks
import play.api.libs.json.{JsError, JsString, Json}

class OtherInvestorGroupElectionsSpec extends WordSpec with MustMatchers with ScalaCheckPropertyChecks with OptionValues with ModelGenerators {

  "OtherInvestorGroupElections" must {

    "deserialise valid values" in {

      val gen = arbitrary[OtherInvestorGroupElections]

      forAll(gen) {
        otherInvestorGroupElections =>

          JsString(otherInvestorGroupElections.toString).validate[OtherInvestorGroupElections].asOpt.value mustEqual otherInvestorGroupElections
      }
    }

    "fail to deserialise invalid values" in {

      val gen = arbitrary[String] suchThat (!OtherInvestorGroupElections.values(GroupRatioMethod).map(_.toString).contains(_))

      forAll(gen) {
        invalidValue =>

          JsString(invalidValue).validate[OtherInvestorGroupElections] mustEqual JsError("error.invalid")
      }
    }

    "serialise" in {

      val gen = arbitrary[OtherInvestorGroupElections]

      forAll(gen) {
        otherInvestorGroupElections =>

          Json.toJson(otherInvestorGroupElections) mustEqual JsString(otherInvestorGroupElections.toString)
      }
    }

    "Have the correct set of options" when {

      "Investor Ratio Method is Fixed Ratio" in {
        OtherInvestorGroupElections.values(FixedRatioMethod) mustBe Seq(
          InterestAllowanceAlternativeCalculation,
          InterestAllowanceNonConsolidatedInvestment,
          InterestAllowanceConsolidatedPartnership
        )
      }

      "Investor Ratio Method is Group Ratio" in {
        OtherInvestorGroupElections.values(GroupRatioMethod) mustBe Seq(
          GroupRatioBlended,
          GroupEBITDA,
          InterestAllowanceAlternativeCalculation,
          InterestAllowanceNonConsolidatedInvestment,
          InterestAllowanceConsolidatedPartnership
        )
      }
    }
  }
}
