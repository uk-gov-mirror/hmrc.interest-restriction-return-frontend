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

package models.returnModels.fullReturn

import assets.constants.fullReturn.AllocatedRestrictionsConstants._
import assets.constants.fullReturn.AllocatedReactivationsConstants._
import assets.constants.fullReturn.FullReturnConstants._
import org.scalatest.{MustMatchers, WordSpec}
import play.api.libs.json.Json
import assets.constants.fullReturn.UkCompanyConstants._

class FullReturnModelSpec extends WordSpec with MustMatchers {

  "FullReturnModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = fullReturnJsonMax
        val actualValue = Json.toJson(fullReturnModelMax)

        actualValue mustBe expectedValue
      }

      "min values given" in {

        val expectedValue = fullReturnJsonMin
        val actualValue = Json.toJson(fullReturnModelMin)
        actualValue mustBe expectedValue
      }
    }

    "derive the correct derived values" when {

      "deriving the numberOfUkCompanies when given one or multiple companies" in {
        fullReturnModelMin.numberOfUkCompanies mustBe 1
        fullReturnNetTaxExpenseModelMax.numberOfUkCompanies mustBe 4
      }

      "deriving the aggregateNetTaxInterest" when {

        "income is bigger" in {
          fullReturnNetTaxIncomeModelMax.aggregateNetTaxInterest mustBe ((3 * netTaxInterestIncome) - netTaxInterestExpense)
        }

        "expense is bigger" in {
          fullReturnNetTaxExpenseModelMax.aggregateNetTaxInterest mustBe (netTaxInterestIncome - (3 * netTaxInterestExpense))
        }

        "income and expense are equal" in {
          fullReturnModelMax.aggregateNetTaxInterest mustBe 0
        }
      }

      "deriving the aggregateTaxEBITDA" when {

        "one company has a taxEBITDA" in {
          fullReturnModelMin.aggregateTaxEBITDA mustBe taxEBITDA
        }

        "multiple companies have a taxEBITDA" in {
          fullReturnModelMax.aggregateTaxEBITDA mustBe (2 * taxEBITDA)
        }
      }

      "deriving the aggregateAllocatedRestrictions" when {

        "no companies have a allocatedRestrictions" in {
          fullReturnModelMin.aggregateAllocatedRestrictions mustBe None
        }

        "one company has a allocatedRestrictions" in {
          fullReturnModelMax.aggregateAllocatedRestrictions mustBe Some(totalDisallowances)

        }

        "multiple companies have a allocatedRestrictions" in {
          fullReturnNetTaxExpenseModelMax.aggregateAllocatedRestrictions mustBe Some(3 * totalDisallowances)
        }
      }

      "deriving the aggregateAllocatedReactivations" when {

        "no companies have a allocatedRestrictions" in {
          fullReturnModelMin.aggregateAllocatedReactivations mustBe None
        }

        "one company has a allocatedRestrictions" in {
          fullReturnModelMax.aggregateAllocatedReactivations mustBe Some(currentPeriodReactivation)

        }

        "multiple companies have a allocatedRestrictions" in {
          fullReturnNetTaxExpenseModelMax.aggregateAllocatedReactivations mustBe Some(3 * currentPeriodReactivation)
        }

      }
    }
  }
}

