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

package models.returnModels

import assets.constants.DeemedParentConstants._
import base.SpecBase
import models.ErrorModel
import pages.groupStructure.{ParentCRNPage, ParentCompanyCTUTRPage, ParentCompanyNamePage, ParentCompanySAUTRPage}
import play.api.libs.json.Json

class DeemedParentModelSpec extends SpecBase {

  "DeemedParentModel" must {

    "correctly write to json" when {

      "max values given" in {

        val expectedValue = deemedParentJsonMax
        val actualValue = Json.toJson(deemedParentModelMax)

        actualValue mustBe expectedValue
      }

      "min values given" in {

        val expectedValue = deemedParentJsonMin
        val actualValue = Json.toJson(deemedParentModelMin)

        actualValue mustBe expectedValue
      }
    }

    "when constructing from a User Answers model" when {

      "company name is missing" should {

        "return a BadRequestException with the correct error message" in {
          DeemedParentModel(emptyUserAnswers) mustBe Left(ErrorModel("Cannot Construct Deemed Parent Model from User Answers as Company Name is missing"))
        }
      }

      "Only company name is supplied" should {

        "return a deemed parent model" in {

          val userAnswers = emptyUserAnswers.set(ParentCompanyNamePage, companyNameModel.name).success.value

          DeemedParentModel(userAnswers) mustBe Right(deemedParentModelMin)
        }
      }

      "UK Company with CT UTR with CRN" should {

        "return a deemed parent model" in {

          val userAnswers = emptyUserAnswers.set(ParentCompanyNamePage, companyNameModel.name).success.value
            .set(ParentCompanyCTUTRPage, ctutrModel.utr).success.value
            .set(ParentCRNPage, crnModel.crn).success.value

          DeemedParentModel(userAnswers) mustBe Right(deemedParentModelUkCompany.copy(knownAs = None))
        }
      }

      "UK Company with CT UTR and no CRN" should {

        "return a deemed parent model" in {

          val userAnswers = emptyUserAnswers.set(ParentCompanyNamePage, companyNameModel.name).success.value
            .set(ParentCompanyCTUTRPage, ctutrModel.utr).success.value

          DeemedParentModel(userAnswers) mustBe Right(deemedParentModelUkCompany.copy(knownAs = None, crn = None))
        }
      }

      "UK Company with SA UTR with CRN" should {

        "return a deemed parent model" in {

          val userAnswers = emptyUserAnswers.set(ParentCompanyNamePage, companyNameModel.name).success.value
            .set(ParentCompanySAUTRPage, sautrModel.utr).success.value
            .set(ParentCRNPage, crnModel.crn).success.value

          DeemedParentModel(userAnswers) mustBe Right(deemedParentModelUkCompany.copy(knownAs = None, ctutr = None, sautr = Some(sautrModel)))
        }
      }

      "UK Company with SA UTR and no CRN" should {

        "return a deemed parent model" in {

          val userAnswers = emptyUserAnswers.set(ParentCompanyNamePage, companyNameModel.name).success.value
            .set(ParentCompanySAUTRPage, sautrModel.utr).success.value

          DeemedParentModel(userAnswers) mustBe Right(deemedParentModelUkCompany.copy(knownAs = None, crn = None, ctutr = None, sautr = Some(sautrModel)))
        }
      }
    }
  }
}

