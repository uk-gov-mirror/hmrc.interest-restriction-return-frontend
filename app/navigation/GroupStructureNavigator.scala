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
import pages.groupStructure._
import play.api.mvc.Call
import controllers.groupStructure.routes

@Singleton
class GroupStructureNavigator @Inject()() extends Navigator {

  //TODO update with next page
  val normalRoutes: Map[Page, UserAnswers => Call] = Map(
    ReportingCompanySameAsParentPage -> (_.get(ReportingCompanySameAsParentPage) match {
      case Some(false) => routes.DeemedParentController.onPageLoad(NormalMode)
      case Some(true) => controllers.routes.UnderConstructionController.onPageLoad()
      case _ => routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
    }),
    DeemedParentPage -> (_ => routes.ParentCompanyNameController.onPageLoad(NormalMode)),
    ParentCompanyNamePage -> (_ => routes.PayTaxInUkController.onPageLoad(NormalMode)),
    PayTaxInUkPage -> (_.get(PayTaxInUkPage) match {
      case Some(true) => routes.LimitedLiabilityPartnershipController.onPageLoad(NormalMode)
      case Some(false) => controllers.routes.UnderConstructionController.onPageLoad()
      case _ => routes.PayTaxInUkController.onPageLoad(NormalMode)
    }),
    LimitedLiabilityPartnershipPage -> (_.get(LimitedLiabilityPartnershipPage) match {
      case Some(true) => routes.ParentCompanySAUTRController.onPageLoad(NormalMode)
      case Some(false) => routes.ParentCompanyCTUTRController.onPageLoad(NormalMode)
      case _ => routes.LimitedLiabilityPartnershipController.onPageLoad(NormalMode)
    }),
    ParentCompanyCTUTRPage -> (_ => routes.RegisteredCompaniesHouseController.onPageLoad(NormalMode)),
    RegisteredCompaniesHousePage -> (_ => routes.ParentCRNController.onPageLoad(NormalMode)),
    ParentCompanySAUTRPage -> (_ => routes.ParentCRNController.onPageLoad(NormalMode)),
    ParentCRNPage -> (_ => controllers.routes.UnderConstructionController.onPageLoad()),
    RegisteredForTaxInAnotherCountryPage -> (_ => controllers.routes.UnderConstructionController.onPageLoad())
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
