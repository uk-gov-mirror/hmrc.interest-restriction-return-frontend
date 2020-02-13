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

package models
import models.InvestorRatioMethod.{FixedRatioMethod, GroupRatioMethod}
import models.requests.DataRequest
import play.api.data.Form
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text

sealed trait OtherInvestorGroupElections

object OtherInvestorGroupElections extends Enumerable.Implicits {

  case object GroupRatioBlended extends WithName("groupRatioBlended") with OtherInvestorGroupElections
  case object GroupEBITDA extends WithName("groupEBITDA") with OtherInvestorGroupElections
  case object InterestAllowanceAlternativeCalculation extends WithName("interestAllowanceAlternativeCalculation") with OtherInvestorGroupElections
  case object InterestAllowanceNonConsolidatedInvestment extends WithName("interestAllowanceNonConsolidatedInvestment") with OtherInvestorGroupElections
  case object InterestAllowanceConsolidatedPartnership extends WithName("interestAllowanceConsolidatedPartnership") with OtherInvestorGroupElections

  val allValues = Seq(
    GroupRatioBlended,
    GroupEBITDA,
    InterestAllowanceAlternativeCalculation,
    InterestAllowanceNonConsolidatedInvestment,
    InterestAllowanceConsolidatedPartnership
  )

  val fixedRatioValues = Seq(
    InterestAllowanceAlternativeCalculation,
    InterestAllowanceNonConsolidatedInvestment,
    InterestAllowanceConsolidatedPartnership
  )

  def values: InvestorRatioMethod => Seq[OtherInvestorGroupElections] = {
    case FixedRatioMethod => fixedRatioValues
    case GroupRatioMethod => allValues
  }


  def options(form: Form[_], investorRatioMethod: InvestorRatioMethod)(implicit messages: Messages): Seq[CheckboxItem] =
    values(investorRatioMethod).map {
      value =>
        CheckboxItem(
          name = Some("value[]"),
          id = Some(value.toString),
          value = value.toString,
          content = Text(messages(s"otherInvestorGroupElections.${value.toString}")),
          checked = form.data.exists(_._2 == value.toString)
        )
    }

  implicit val enumerable: Enumerable[OtherInvestorGroupElections] =
    Enumerable(allValues.map(v => v.toString -> v): _*)
}
