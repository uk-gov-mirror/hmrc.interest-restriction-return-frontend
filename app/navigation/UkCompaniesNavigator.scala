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

package navigation

import javax.inject.{Inject, Singleton}
import models._
import pages.{ukCompanies, _}
import pages.ukCompanies._
import play.api.mvc.Call

@Singleton
class UkCompaniesNavigator @Inject()() extends Navigator {

  //TODO update with next page
  val normalRoutes: Map[Page, UserAnswers => Call] = Map(
    EnterCompanyTaxEBITDAPage -> (_ => controllers.routes.UnderConstructionController.onPageLoad()),
    ConsentingCompanyPage -> (_ => controllers.routes.UnderConstructionController.onPageLoad())
  )

  val checkRouteMap: Map[Page, UserAnswers => Call] = Map().withDefaultValue(_ => checkYourAnswers)

  //TODO update with CYA call
  private def checkYourAnswers: Call = controllers.routes.UnderConstructionController.onPageLoad()

  //TODO update with Next Section call
  private def nextSection(mode: Mode): Call = controllers.routes.UnderConstructionController.onPageLoad()

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, idx: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode => checkRouteMap(page)(userAnswers)
  }
}
