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

package models.sections

case class GroupLevelInformationSectionModel(
  restrictionReactivationJourney: RestrictionReactivationJourneyModel,
  interestAllowanceBroughtForward: BigDecimal, //DONE
  interestAllowanceForReturnPeriod: BigDecimal, //DONE
  interestCapacityForReturnPeriod: BigDecimal, //DONE
  groupRatioJourney: GroupRatioJourneyModel, //DONE
  estimates: Boolean,
  groupRatioElection: Boolean
)

case class RestrictionReactivationJourneyModel(
  subjectToRestrictions: Boolean, //DONE
  totalDisallowedAmount: Option[BigDecimal],
  subjectToReactivations: Option[Boolean],
  reactivationCap: Option[BigDecimal]
)

case class GroupRatioJourneyModel(
  angie: BigDecimal, //DONE
  qngie: Option[BigDecimal], //DONE
  groupEBITDA: Option[BigDecimal], //DONE
  groupRatioPercentage: Option[BigDecimal] //DONE
)