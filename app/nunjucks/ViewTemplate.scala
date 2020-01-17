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

package nunjucks

import models.WithName

trait ViewTemplate
object HelloWorldYesNoTemplate extends WithName("helloWorldYesNo.njk") with ViewTemplate
object CheckYourAnswersTemplate extends WithName("checkYourAnswers.njk") with ViewTemplate
object ReportingCompanyCRNTemplate extends WithName("reportingCompanyCRN.njk") with ViewTemplate
object ReportingCompanyCTUTRTemplate extends WithName("reportingCompanyCTUTR.njk") with ViewTemplate
object ReportingCompanyRequiredTemplate extends WithName("reportingCompanyRequired.njk") with ViewTemplate
object ReportingCompanyNameTemplate extends WithName("reportingCompanyName.njk") with ViewTemplate
object ReportingCompanyAppointedTemplate extends WithName("reportingCompanyAppointed.njk") with ViewTemplate
object AgentActingOnBehalfOfCompanyTemplate extends WithName("agentActingOnBehalfOfCompany.njk") with ViewTemplate
object AgentNameTemplate extends WithName("agentName.njk") with ViewTemplate
object InterestAllowanceBroughtForwardTemplate extends WithName("interestAllowanceBroughtForward.njk") with ViewTemplate
object GroupSubjectToReactivationsTemplate extends WithName("groupSubjectToReactivations.njk") with ViewTemplate
object InfrastructureCompanyElectionTemplate extends WithName("infrastructureCompanyElection.njk") with ViewTemplate
object FullOrAbbreviatedReturnTemplate extends WithName("fullOrAbbreviatedReturn.njk") with ViewTemplate
object RevisingReturnTemplate extends WithName("revisingReturn.njk") with ViewTemplate
object GroupInterestCapacityTemplate extends WithName("groupInterestCapacity.njk") with ViewTemplate
object GroupSubjectToRestrictionsTemplate extends WithName("groupSubjectToRestrictions.njk") with ViewTemplate
object GroupInterestAllowanceTemplate extends WithName("groupInterestAllowance.njk") with ViewTemplate
object InterestReactivationsCapTemplate extends WithName("interestReactivationsCap.njk") with ViewTemplate
object ReturnContainEstimatesTemplate extends WithName("returnContainEstimates.njk") with ViewTemplate
object CheckAnswersReportingCompanyTemplate extends WithName("checkAnswersReportingCompany.njk") with ViewTemplate
object SavedReturnTemplate extends WithName("savedReturn.njk") with ViewTemplate
object UnderConstructionTemplate extends WithName("underConstruction.njk") with ViewTemplate
object ContinueSavedReturnTemplate extends WithName("continueSavedReturn.njk") with ViewTemplate
