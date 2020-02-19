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
  val normalRoutes: Map[Page, (Int, UserAnswers) => Call] = Map(
    ReportingCompanySameAsParentPage -> ((_, userAnswers) => userAnswers.get(ReportingCompanySameAsParentPage) match {
      case Some(false) => routes.DeemedParentController.onPageLoad(NormalMode)
      case Some(true) => nextSection(NormalMode)
      case _ => routes.ReportingCompanySameAsParentController.onPageLoad(NormalMode)
    }),
    DeemedParentPage -> ((id, _) => routes.ParentCompanyNameController.onPageLoad(id, NormalMode)),
    ParentCompanyNamePage -> ((id, _) => routes.PayTaxInUkController.onPageLoad(id, NormalMode)),
    PayTaxInUkPage -> ((id, userAnswers) => userAnswers.get(PayTaxInUkPage) match {
      case Some(true) => routes.LimitedLiabilityPartnershipController.onPageLoad(id, NormalMode)
      case Some(false) => routes.RegisteredForTaxInAnotherCountryController.onPageLoad(id, NormalMode)
      case _ => routes.PayTaxInUkController.onPageLoad(id, NormalMode)
    }),
    LimitedLiabilityPartnershipPage -> ((id, userAnswers) => userAnswers.get(LimitedLiabilityPartnershipPage) match {
      case Some(true) => routes.ParentCompanySAUTRController.onPageLoad(id, NormalMode)
      case Some(false) => routes.ParentCompanyCTUTRController.onPageLoad(id, NormalMode)
      case _ => routes.LimitedLiabilityPartnershipController.onPageLoad(id, NormalMode)
    }),
    ParentCompanyCTUTRPage -> ((id, _) => routes.RegisteredCompaniesHouseController.onPageLoad(id, NormalMode)),
    RegisteredCompaniesHousePage -> ((id, userAnswers) => userAnswers.get(RegisteredCompaniesHousePage) match {
      case Some(true) => routes.ParentCRNController.onPageLoad(id, NormalMode)
      case Some(false) => checkYourAnswers(id)
      case _ => routes.RegisteredCompaniesHouseController.onPageLoad(id, NormalMode)
    }),
    ParentCompanySAUTRPage -> ((id, _) => routes.ParentCRNController.onPageLoad(id, NormalMode)),
    ParentCRNPage -> ((id, _) => checkYourAnswers(id)),
    RegisteredForTaxInAnotherCountryPage -> ((id, userAnswers) => userAnswers.get(RegisteredForTaxInAnotherCountryPage) match {
      case Some(true) => routes.CountryOfIncorporationController.onPageLoad(id, NormalMode)
      case Some(false) => checkYourAnswers(id)
      case _ => routes.RegisteredForTaxInAnotherCountryController.onPageLoad(id, NormalMode)
    }),
    CountryOfIncorporationPage -> ((id, _) => routes.LocalRegistrationNumberController.onPageLoad(id, NormalMode)),
    LocalRegistrationNumberPage -> ((id, _) => checkYourAnswers(id)),
    CheckAnswersGroupStructurePage -> ((_,_) => nextSection(NormalMode))
  )

  val checkRouteMap: Map[Page, (Int, UserAnswers) => Call] = Map().withDefaultValue((id, _) => checkYourAnswers(id))

  private def checkYourAnswers(id: Int): Call = routes.CheckAnswersGroupStructureController.onPageLoad(id)
  private def nextSection(mode: Mode): Call = aboutReturnRoutes.RevisingReturnController.onPageLoad(mode)

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, id: Option[Int] = Some(1)): Call = (id, mode) match {
    case (Some(idValue), NormalMode) => normalRoutes(page)(idValue, userAnswers)
    case (Some(idValue), CheckMode) => checkRouteMap(page)(idValue, userAnswers)
  }
}
