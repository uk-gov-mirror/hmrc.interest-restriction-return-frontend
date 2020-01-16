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

import controllers.aboutReportingCompany.{routes => aboutReportingCompanyRoutes}
import controllers.aboutReturn.{routes => aboutReturnRoutes}
import javax.inject.{Inject, Singleton}
import models._
import pages._
import pages.aboutReportingCompany.{CheckAnswersReportingCompanyPage, ReportingCompanyCRNPage, ReportingCompanyCTUTRPage, ReportingCompanyNamePage}
import play.api.mvc.Call

@Singleton
class AboutReportingCompanyNavigator @Inject()() extends BaseNavigator {

  val normalRoutes: Map[Page, UserAnswers => Call] = Map(
    ReportingCompanyNamePage -> (_ => aboutReportingCompanyRoutes.ReportingCompanyCTUTRController.onPageLoad(NormalMode)),
    ReportingCompanyCTUTRPage -> (_ => aboutReportingCompanyRoutes.ReportingCompanyCRNController.onPageLoad(NormalMode)),
    ReportingCompanyCRNPage -> (_ => aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()),
    CheckAnswersReportingCompanyPage -> (_ => nextSection(NormalMode))
  )

  val checkRouteMap: Map[Page, UserAnswers => Call] = Map().withDefaultValue(_ =>
    aboutReportingCompanyRoutes.CheckAnswersReportingCompanyController.onPageLoad()
  )

  private def nextSection(mode: Mode): Call = aboutReturnRoutes.RevisingReturnController.onPageLoad(mode)

  def nextPage(page: Page, mode: Mode, userAnswers: UserAnswers): Call = mode match {
    case NormalMode => normalRoutes(page)(userAnswers)
    case CheckMode => checkRouteMap(page)(userAnswers)
  }
}
