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

package services

import assets.constants.BaseConstants
import assets.constants.fullReturn.UkCompanyConstants.ukCompanyModelMax
import base.SpecBase
import connectors.mocks.MockCRNValidationConnector
import models.SectionStatus.InProgress
import models.returnModels.{ReviewAndCompleteModel, SectionState}
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.ukCompanies.{ConsentingCompanyPage, UkCompaniesPage}


class UpdateSectionServiceSpec extends SpecBase with MockCRNValidationConnector with BaseConstants {

  object TestUpdateSectionStateService extends UpdateSectionStateService

  "UpdateSectionService.updateState()" when {

    "given a page in the ukCompanies section" must {

      "return ukCompanies section udpated with InProgress" in {

        val userAnswers = emptyUserAnswers
          .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
          .set(ConsentingCompanyPage, true).get

        val result = TestUpdateSectionStateService.updateState(userAnswers, ConsentingCompanyPage)
        result mustBe ReviewAndCompleteModel(ukCompanies = SectionState(InProgress, Some(ConsentingCompanyPage)))
      }
    }

    "given the final page in the ukCompanies section" must {

      "return ukCompanies section udpated with InProgress" in {

        val userAnswers = emptyUserAnswers
          .set(ReviewAndCompletePage, ReviewAndCompleteModel()).get
          .set(UkCompaniesPage, ukCompanyModelMax).get

        val result = TestUpdateSectionStateService.updateState(userAnswers, ConsentingCompanyPage)
        result mustBe ReviewAndCompleteModel(ukCompanies = SectionState(InProgress, Some(ConsentingCompanyPage)))
      }
    }
  }
}
