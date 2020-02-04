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
import pages._
import pages.groupStructure.{DeemedParentPage, ParentCompanyNamePage}
import play.api.mvc.Call

@Singleton
class GroupStructureNavigator @Inject()() extends BaseNavigator {

  //TODO update with next page
  val normalRoutes: Map[Page, UserAnswers => Call] = Map(
    DeemedParentPage -> (_ => controllers.routes.UnderConstructionController.onPageLoad()),
    ParentCompanyNamePage -> (_ => controllers.routes.UnderConstructionController.onPageLoad())
  )

  //TODO update with check your answers page
  val checkRouteMap: Map[Page, UserAnswers => Call] = Map().withDefaultValue(_ =>
    controllers.routes.UnderConstructionController.onPageLoad()
  )

  //TODO update with next section
  private def nextSection(mode: Mode): Call = controllers.routes.UnderConstructionController.onPageLoad()

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode => checkRouteMap(page)(userAnswers)
  }
}
