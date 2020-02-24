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
import pages.{groupStructure, _}
import pages.groupStructure._
import play.api.mvc.Call
import controllers.groupStructure.routes
import controllers.aboutReturn.{routes => aboutReturnRoutes}

@Singleton
class GroupStructureNavigator @Inject()() extends Navigator {

  //TODO update with next page
  val normalRoutes: Map[Page, UserAnswers => Call] = Map(
    ReportingCompanySameAsParentPage -> (_.get(ReportingCompanySameAsParentPage) match {
      case Some(false) => routes.DeemedParentController.onPageLoad(NormalMode)
      case Some(true) => nextSection(NormalMode)
      case _ => routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
    }),
    DeemedParentPage -> (_ => routes.ParentCompanyNameController.onPageLoad(NormalMode)),
    ParentCompanyNamePage -> (_ => routes.PayTaxInUkController.onPageLoad(NormalMode)),
    PayTaxInUkPage -> (_.get(PayTaxInUkPage) match {
      case Some(true) => routes.LimitedLiabilityPartnershipController.onPageLoad(NormalMode)
      case Some(false) => routes.RegisteredForTaxInAnotherCountryController.onPageLoad(NormalMode)
      case _ => routes.PayTaxInUkController.onPageLoad(NormalMode)
    }),
    LimitedLiabilityPartnershipPage -> (_.get(LimitedLiabilityPartnershipPage) match {
      case Some(true) => routes.ParentCompanySAUTRController.onPageLoad(NormalMode)
      case Some(false) => routes.ParentCompanyCTUTRController.onPageLoad(NormalMode)
      case _ => routes.LimitedLiabilityPartnershipController.onPageLoad(NormalMode)
    }),
    ParentCompanyCTUTRPage -> (_ => routes.RegisteredCompaniesHouseController.onPageLoad(NormalMode)),
    RegisteredCompaniesHousePage -> (_.get(RegisteredCompaniesHousePage) match {
      case Some(true) => routes.ParentCRNController.onPageLoad(NormalMode)
      case Some(false) => checkYourAnswers
      case _ => routes.RegisteredCompaniesHouseController.onPageLoad(NormalMode)
    }),
    ParentCompanySAUTRPage -> (_ => routes.ParentCRNController.onPageLoad(NormalMode)),
    ParentCRNPage -> (_ => checkYourAnswers),
    RegisteredForTaxInAnotherCountryPage -> (_.get(RegisteredForTaxInAnotherCountryPage) match {
      case Some(true) => routes.CountryOfIncorporationController.onPageLoad(NormalMode)
      case Some(false) => checkYourAnswers
      case _ => routes.RegisteredForTaxInAnotherCountryController.onPageLoad(NormalMode)
    }),
    CountryOfIncorporationPage -> (_ => routes.LocalRegistrationNumberController.onPageLoad(NormalMode)),
    LocalRegistrationNumberPage -> (_ => checkYourAnswers),
    CheckAnswersGroupStructurePage -> (_ => nextSection(NormalMode))
  )

  val checkRouteMap: Map[Page, UserAnswers => Call] = Map().withDefaultValue(_ => checkYourAnswers)

  private def checkYourAnswers: Call = routes.CheckAnswersGroupStructureController.onPageLoad()
  private def nextSection(mode: Mode): Call = aboutReturnRoutes.RevisingReturnController.onPageLoad(mode)

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, id: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode => checkRouteMap(page)(userAnswers)
  }
}
