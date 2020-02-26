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

import controllers.groupStructure.{routes => groupStructureRoutes}
import controllers.ukCompanies.routes
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.ukCompanies._
import play.api.mvc.Call

@Singleton
class UkCompaniesNavigator @Inject()() extends Navigator {

  val normalRoutes: Map[Page, (Int, UserAnswers) => Call] = Map(
    AboutAddingUKCompaniesPage -> ((_, _) => routes.AboutAddingUKCompaniesController.onPageLoad()),
    CompanyDetailsPage -> ((_, _) => controllers.routes.UnderConstructionController.onPageLoad())
    //CompanyDetailsPage -> ((id, _) => nextSection(NormalMode))
  )

  val checkRouteMap: Map[Page, (Int, UserAnswers) => Call] = Map().withDefaultValue((id, _) => ???
  )

  private def nextSection(mode: Mode): Call = groupStructureRoutes.ReportingCompanySameAsParentController.onPageLoad(mode)

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, id: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(id.getOrElse[Int](1), userAnswers) //TODO: Requires Refactor
    case CheckMode => checkRouteMap(page)(id.getOrElse[Int](1), userAnswers) //TODO: Requires Refactor
  }
}
