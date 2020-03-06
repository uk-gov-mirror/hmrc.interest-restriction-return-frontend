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

package utils

import assets.messages.{ReviewAndCompleteMessages, SectionHeaderMessages}
import base.SpecBase
import viewmodels.{SummaryListRowHelper, TaskListRow}


class ReviewAndCompleteHelperSpec extends SpecBase with SummaryListRowHelper with CurrencyFormatter {

  "ReviewAndCompleteHelper().rows" when {

    "given a list of deemed companies" should {

      "return the correct summary list row models" in {

        val helper = new ReviewAndCompleteHelper()

        helper.rows mustBe Seq(
          TaskListRow(SectionHeaderMessages.aboutReturn, controllers.routes.UnderConstructionController.onPageLoad(), ReviewAndCompleteMessages.completed),
          TaskListRow(SectionHeaderMessages.reportingCompany, controllers.routes.UnderConstructionController.onPageLoad(), ReviewAndCompleteMessages.completed),
          TaskListRow(SectionHeaderMessages.groupStructure, controllers.routes.UnderConstructionController.onPageLoad(), ReviewAndCompleteMessages.completed),
          TaskListRow(SectionHeaderMessages.elections, controllers.routes.UnderConstructionController.onPageLoad(), ReviewAndCompleteMessages.completed),
          TaskListRow(SectionHeaderMessages.ukCompanies, controllers.routes.UnderConstructionController.onPageLoad(), ReviewAndCompleteMessages.completed),
          TaskListRow(SectionHeaderMessages.checkTotals, controllers.routes.UnderConstructionController.onPageLoad(), ReviewAndCompleteMessages.completed)
        )
      }
    }
  }
}
