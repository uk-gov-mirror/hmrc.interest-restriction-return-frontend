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

package generators

import org.scalacheck.Arbitrary
import pages._

trait PageGenerators {

  implicit lazy val arbitraryHelloWorldYesNoPage: Arbitrary[HelloWorldYesNoPageNunjucks.type] =
    Arbitrary(HelloWorldYesNoPageNunjucks)

  implicit lazy val arbitraryReportingCompanyAppointedPage: Arbitrary[ReportingCompanyAppointedPage.type] =
    Arbitrary(ReportingCompanyAppointedPage)

  implicit lazy val arbitraryReportingCompanyNamePage: Arbitrary[ReportingCompanyNamePage.type] =
    Arbitrary(ReportingCompanyNamePage)

  implicit lazy val arbitraryReportingCompanyResultPage: Arbitrary[ReportingCompanyRequiredPage.type] =
    Arbitrary(ReportingCompanyRequiredPage)

  implicit lazy val arbitraryReportingCompanyCTUTRPage: Arbitrary[ReportingCompanyCTUTRPage.type] =
    Arbitrary(ReportingCompanyCTUTRPage)

  implicit lazy val arbitraryReportingCompanyCRNPage: Arbitrary[ReportingCompanyCRNPage.type] =
    Arbitrary(ReportingCompanyCRNPage)

  implicit lazy val arbitraryInterestAllowanceBroughtForwardPage: Arbitrary[InterestAllowanceBroughtForwardPage.type] =
    Arbitrary(InterestAllowanceBroughtForwardPage)

  implicit lazy val arbitraryGroupSubjectToReactivationsPage: Arbitrary[GroupSubjectToReactivationsPage.type] =
    Arbitrary(GroupSubjectToReactivationsPage)

  implicit lazy val arbitraryFullOrAbbreviatedReturnPage: Arbitrary[FullOrAbbreviatedReturnPage.type] =
    Arbitrary(FullOrAbbreviatedReturnPage)

  implicit lazy val arbitraryRevisingReturnPage: Arbitrary[RevisingReturnPage.type] =
    Arbitrary(RevisingReturnPage)

  implicit lazy val arbitraryAgentNamePage: Arbitrary[AgentNamePage.type] =
    Arbitrary(AgentNamePage)

  implicit lazy val arbitraryAgentActingOnBehalfOfCompanyPage: Arbitrary[AgentActingOnBehalfOfCompanyPage.type] =
    Arbitrary(AgentActingOnBehalfOfCompanyPage)

  implicit lazy val arbitraryInfrastructureCompanyElectionPage: Arbitrary[InfrastructureCompanyElectionPage.type] =
    Arbitrary(InfrastructureCompanyElectionPage)
}
