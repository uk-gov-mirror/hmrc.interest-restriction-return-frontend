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

import javax.inject.{Inject, Singleton}
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import controllers.ultimateParentCompany.{routes => ultimateParentCompanyRoutes}
import controllers.routes
import models.FullOrAbbreviatedReturn.{Abbreviated, Full}
import models._
import pages._
import pages.aboutReturn._
import pages.groupLevelInformation.RevisingReturnPage
import play.api.mvc.Call

@Singleton
class AboutReturnNavigator @Inject()() extends Navigator {

  val normalRoutes: Map[Page, UserAnswers => Call] = Map(
    IndexPage -> (_ => aboutReturnRoutes.ReportingCompanyAppointedController.onPageLoad(NormalMode)),
    ReportingCompanyAppointedPage -> (_.get(ReportingCompanyAppointedPage) match {
      case Some(true) => aboutReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode)
      case Some(false) => aboutReturnRoutes.ReportingCompanyRequiredController.onPageLoad()
      case _ => aboutReturnRoutes.ReportingCompanyAppointedController.onPageLoad(NormalMode)
    }),
    AgentActingOnBehalfOfCompanyPage -> (_.get(AgentActingOnBehalfOfCompanyPage) match {
      case Some(true) => aboutReturnRoutes.AgentNameController.onPageLoad(NormalMode)
      case Some(false) => aboutReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(NormalMode)
      case _ => aboutReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode)
    }),
    AgentNamePage -> (_ => aboutReturnRoutes.FullOrAbbreviatedReturnController.onPageLoad(NormalMode)),
    FullOrAbbreviatedReturnPage -> (_ => aboutReturnRoutes.RevisingReturnController.onPageLoad(NormalMode)),
    RevisingReturnPage -> (_.get(RevisingReturnPage) match {
      case Some(true) => aboutReturnRoutes.TellUsWhatHasChangedController.onPageLoad(NormalMode)
      case Some(false) => aboutReturnRoutes.ReportingCompanyNameController.onPageLoad(NormalMode)
      case _ => aboutReturnRoutes.RevisingReturnController.onPageLoad(NormalMode)
    }),
    TellUsWhatHasChangedPage -> (_ => aboutReturnRoutes.ReportingCompanyNameController.onPageLoad(NormalMode)),
    ReportingCompanyNamePage -> (_ => aboutReturnRoutes.ReportingCompanyCTUTRController.onPageLoad(NormalMode)),
    ReportingCompanyCTUTRPage -> (_ => aboutReturnRoutes.AccountingPeriodController.onPageLoad(NormalMode)),
    AccountingPeriodPage -> (_ => checkAnswers),
    CheckAnswersAboutReturnPage -> (_ => nextSection(NormalMode))
  )

  val checkRouteMap: Map[Page, UserAnswers => Call] = Map[Page, UserAnswers => Call](
    ReportingCompanyAppointedPage -> normalRoutes(ReportingCompanyAppointedPage),
    FullOrAbbreviatedReturnPage -> (fullOrAbbreviatedReturnCheckRoute(_)),
    AgentActingOnBehalfOfCompanyPage -> (_.get(AgentActingOnBehalfOfCompanyPage) match {
      case Some(true) => aboutReturnRoutes.AgentNameController.onPageLoad(CheckMode)
      case Some(false) => checkAnswers
      case _ => aboutReturnRoutes.AgentActingOnBehalfOfCompanyController.onPageLoad(NormalMode)
    }),
    RevisingReturnPage -> (_.get(RevisingReturnPage) match {
      case Some(true) => aboutReturnRoutes.TellUsWhatHasChangedController.onPageLoad(CheckMode)
      case Some(false) => checkAnswers
      case _ => aboutReturnRoutes.RevisingReturnController.onPageLoad(NormalMode)
    })
  ).withDefaultValue(_ => checkAnswers)

  def fullOrAbbreviatedReturnCheckRoute(userAnswers: UserAnswers): Call = {
    if (userAnswers.get(RevisingReturnPage).isDefined) {
      checkAnswers
    } else {
      aboutReturnRoutes.RevisingReturnController.onPageLoad(NormalMode)
    }
  }

  private def checkAnswers = aboutReturnRoutes.CheckAnswersAboutReturnController.onPageLoad()
  private def nextSection(mode: Mode): Call = ultimateParentCompanyRoutes.ReportingCompanySameAsParentController.onPageLoad(mode)

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers, id: Option[Int] = None): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode => checkRouteMap(page)(userAnswers)
    case ReviewMode => normalRoutes(page)(userAnswers)
  }
}
