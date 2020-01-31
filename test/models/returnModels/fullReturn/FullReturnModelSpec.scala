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
import org.scalatest.{Matchers, WordSpec}
import play.api.libs.json.Json
import assets.constants.fullReturn.UkCompanyConstants._

class FullReturnModelSpec extends WordSpec with Matchers {

  "FullReturnModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = fullReturnJsonMax
        val actualValue = Json.toJson(fullReturnModelMax)

        actualValue shouldBe expectedValue
      }

      "min values given" in {

        val expectedValue = fullReturnJsonMin
        val actualValue = Json.toJson(fullReturnModelMin)
        actualValue shouldBe expectedValue
      }
    }

    "derive the correct derived values" when {

      "deriving the numberOfUkCompanies when given one or multiple companies" in {
        fullReturnModelMin.numberOfUkCompanies shouldBe 1
        fullReturnNetTaxExpenseModelMax.numberOfUkCompanies shouldBe 4
      }

      "deriving the aggregateNetTaxInterest" when {

        "income is bigger" in {
          fullReturnNetTaxIncomeModelMax.aggregateNetTaxInterest shouldBe ((3 * netTaxInterestIncome) - netTaxInterestExpense)
        }

        "expense is bigger" in {
          fullReturnNetTaxExpenseModelMax.aggregateNetTaxInterest shouldBe (netTaxInterestIncome - (3 * netTaxInterestExpense))
        }

        "income and expense are equal" in {
          fullReturnModelMax.aggregateNetTaxInterest shouldBe 0
        }
      }

      "deriving the aggregateTaxEBITDA" when {

        "one company has a taxEBITDA" in {
          fullReturnModelMin.aggregateTaxEBITDA shouldBe taxEBITDA
        }

        "multiple companies have a taxEBITDA" in {
          fullReturnModelMax.aggregateTaxEBITDA shouldBe (2 * taxEBITDA)
        }
      }

      "deriving the aggregateAllocatedRestrictions" when {

        "no companies have a allocatedRestrictions" in {
          fullReturnModelMin.aggregateAllocatedRestrictions shouldBe None
        }

        "one company has a allocatedRestrictions" in {
          fullReturnModelMax.aggregateAllocatedRestrictions shouldBe Some(totalDisallowances)

        }

        "multiple companies have a allocatedRestrictions" in {
          fullReturnNetTaxExpenseModelMax.aggregateAllocatedRestrictions shouldBe Some(3 * totalDisallowances)
        }
      }

      "deriving the aggregateAllocatedReactivations" when {

        "no companies have a allocatedRestrictions" in {
          fullReturnModelMin.aggregateAllocatedReactivations shouldBe None
        }

        "one company has a allocatedRestrictions" in {
          fullReturnModelMax.aggregateAllocatedReactivations shouldBe Some(currentPeriodReactivation)

        }

        "multiple companies have a allocatedRestrictions" in {
          fullReturnNetTaxExpenseModelMax.aggregateAllocatedReactivations shouldBe Some(3 * currentPeriodReactivation)
        }

      }
    }
  }
}

