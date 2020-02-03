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

package pages

import pages.aboutReportingCompany._
import pages.aboutReturn._
import pages.groupStructure.PayTaxInUkPage
import pages.startReturn._
import play.api.libs.json.{JsPath, JsString, Reads, Writes}

import scala.language.implicitConversions

trait Page

object Page {

  implicit def toString(page: Page): String = page.toString

  val pages: Map[String, Page] = Map(
    PayTaxInUkPage.toString -> PayTaxInUkPage,
    DeemedParentPage.toString -> DeemedParentPage,
    ConfirmationPage.toString -> ConfirmationPage,
    ContinueSavedReturnPage.toString -> ContinueSavedReturnPage,
    ReportingCompanyNamePage.toString -> ReportingCompanyNamePage,
    ReportingCompanyCTUTRPage.toString -> ReportingCompanyCTUTRPage,
    ReportingCompanyCRNPage.toString -> ReportingCompanyCRNPage,
    AgentActingOnBehalfOfCompanyPage.toString -> AgentActingOnBehalfOfCompanyPage,
    AgentNamePage.toString -> AgentNamePage,
    FullOrAbbreviatedReturnPage.toString -> FullOrAbbreviatedReturnPage,
    ReportingCompanyAppointedPage.toString -> ReportingCompanyAppointedPage,
    ReportingCompanyRequiredPage.toString -> ReportingCompanyRequiredPage,
    GroupInterestAllowancePage.toString -> GroupInterestAllowancePage,
    GroupInterestCapacityPage.toString -> GroupInterestCapacityPage,
    GroupSubjectToReactivationsPage.toString -> GroupSubjectToReactivationsPage,
    GroupSubjectToRestrictionsPage.toString -> GroupSubjectToRestrictionsPage,
    InfrastructureCompanyElectionPage.toString -> InfrastructureCompanyElectionPage,
    InterestAllowanceBroughtForwardPage.toString -> InterestAllowanceBroughtForwardPage,
    InterestReactivationsCapPage.toString -> InterestReactivationsCapPage,
    RevisingReturnPage.toString -> RevisingReturnPage,
    ReturnContainEstimatesPage.toString -> ReturnContainEstimatesPage,
    IndexPage.toString -> IndexPage
  )

  def apply(page: String): Page = pages(page)

  def unapply(arg: Page): String = pages.map(_.swap).apply(arg)

  implicit val reads: Reads[Page] = JsPath.read[String].map(apply)
  implicit val writes: Writes[Page] = Writes { page => JsString(unapply(page)) }
}
