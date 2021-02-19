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

package models

import play.api.data.Form
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.checkboxes.CheckboxItem
import pages.QuestionPage
import pages.groupLevelInformation._
import collection.immutable.ListMap

sealed trait EstimatedFigures

object EstimatedFigures extends Enumerable.Implicits {

  case object Angie extends WithName("angie") with EstimatedFigures
  case object Qngie extends WithName("qngie") with EstimatedFigures
  case object GroupTaxEBITDA extends WithName("groupTaxEBITDA") with EstimatedFigures
  case object GroupRatioPercentage extends WithName("groupRatioPercentage") with EstimatedFigures
  case object ReactivationCap extends WithName("reactivationCap") with EstimatedFigures
  case object TotalDisallowedAmount extends WithName("totalDisallowedAmount") with EstimatedFigures
  case object InterestAllowanceBroughtForward extends WithName("interestAllowanceBroughtForward") with EstimatedFigures
  case object InterestAllowanceForThePeriodOfAccount extends WithName("interestAllowanceForThePeriodOfAccount") with EstimatedFigures
  case object InterestCapacity extends WithName("interestCapacity") with EstimatedFigures

  val values: Seq[EstimatedFigures] = Seq(
    Angie, Qngie, GroupTaxEBITDA, GroupRatioPercentage, ReactivationCap, TotalDisallowedAmount, InterestAllowanceBroughtForward, InterestAllowanceForThePeriodOfAccount, InterestCapacity
  )

  val pageToFigureMap: ListMap[QuestionPage[_], EstimatedFigures] = ListMap(
    EnterANGIEPage -> Angie,
    EnterQNGIEPage -> Qngie, 
    GroupEBITDAPage -> GroupTaxEBITDA, 
    GroupRatioPercentagePage -> GroupRatioPercentage,
    InterestReactivationsCapPage -> ReactivationCap, 
    DisallowedAmountPage -> TotalDisallowedAmount, 
    InterestAllowanceBroughtForwardPage -> InterestAllowanceBroughtForward, 
    GroupInterestAllowancePage -> InterestAllowanceForThePeriodOfAccount, 
    GroupInterestCapacityPage -> InterestCapacity
  )

  def optionsFilteredByUserAnswers(form: Form[_], userAnswers: UserAnswers)(implicit messages: Messages): Seq[CheckboxItem] = {
    for {
      (page, figure) <- pageToFigureMap.toSeq
      if userAnswers.get(page).isDefined
    } yield estimatedFigureToCheckboxItem(form, figure)
  }

  def estimatedFigureToCheckboxItem(form: Form[_], value: EstimatedFigures)(implicit messages: Messages) = 
    CheckboxItem(
      name = Some("value[]"),
      id = Some(value.toString),
      value = value.toString,
      content = Text(messages(s"estimatedFigures.${value.toString}")),
      checked = form.data.exists(_._2 == value.toString)
    )

  implicit val enumerable: Enumerable[EstimatedFigures] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit def ordering: Ordering[EstimatedFigures] = new Ordering[EstimatedFigures] {
    override def compare(x: EstimatedFigures, y: EstimatedFigures): Int = {
      val xIndex = values.indexOf(x)
      val yIndex = values.indexOf(y)
      xIndex.compareTo(yIndex)
    }
  }
}
