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

package navigation

import base.SpecBase
import models.{Mode, UserAnswers}
import pages.Page
import play.api.mvc.Call

object FakeNavigators extends SpecBase {

  trait FakeNavigator extends Navigator {
    override def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, id: Option[Int] = None): Call = onwardRoute
  }

  object FakeAboutReturnNavigator extends AboutReturnNavigator() with FakeNavigator
  object FakeGroupLevelInformationNavigator extends GroupLevelInformationNavigator() with FakeNavigator
  object FakeUltimateParentCompanyNavigator extends UltimateParentCompanyNavigator() with FakeNavigator {
    override def addParent(numberOfParents: Int): Call = Call("GET", s"/addParent/$numberOfParents")
  }
  object FakeElectionsNavigator extends ElectionsNavigator() with FakeNavigator
  object FakeUkCompaniesNavigator extends UkCompaniesNavigator() with FakeNavigator {
    override def nextRestrictionPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = onwardRoute
  }
  object FakeCheckTotalsNavigator extends CheckTotalsNavigator() with FakeNavigator
  object FakeReviewAndCompleteNavigator extends ReviewAndCompleteNavigator() with FakeNavigator
}
