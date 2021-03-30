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
import pages.ukCompanies._
import models.returnModels.fullReturn.AllocatedRestrictionsModel

sealed trait CompanyEstimatedFigures

object CompanyEstimatedFigures extends Enumerable.Implicits {

  case object TaxEbitda extends WithName("taxEBITDA") with CompanyEstimatedFigures
  case object NetTaxInterest extends WithName("netTaxInterest") with CompanyEstimatedFigures
  case object AllocatedRestrictions extends WithName("allocatedRestrictions") with CompanyEstimatedFigures
  case object AllocatedReactivations extends WithName("allocatedReactivations") with CompanyEstimatedFigures

  val values: Seq[CompanyEstimatedFigures] = Seq(
    TaxEbitda, NetTaxInterest, AllocatedRestrictions, AllocatedReactivations
  )

  def options(form: Form[_], idx: Int, userAnswers: UserAnswers)(implicit messages: Messages): Seq[CheckboxItem] = {
    userAnswers.get(UkCompaniesPage, Some(idx)) match {
      case Some(ukCompany) => 
        Seq(
          ukCompany.taxEBITDA.map(_ => estimatedFigureToCheckboxItem(form, TaxEbitda)),
          ukCompany.netTaxInterest.map(_ => estimatedFigureToCheckboxItem(form, NetTaxInterest)),
          restrictionRow(form, ukCompany.allocatedRestrictions, idx, userAnswers),
          ukCompany.allocatedReactivations.map(_ => estimatedFigureToCheckboxItem(form, AllocatedReactivations))
        ).flatten
      case _ => Nil
    }
  }

  def estimatedFigureToCheckboxItem(form: Form[_], value: CompanyEstimatedFigures)(implicit messages: Messages) = 
    CheckboxItem(
      name = Some("value[]"),
      id = Some(value.toString),
      value = value.toString,
      content = Text(messages(s"companyEstimatedFigures.${value.toString}")),
      checked = form.data.exists(_._2 == value.toString)
    )

  implicit val enumerable: Enumerable[CompanyEstimatedFigures] =
    Enumerable(values.map(v => v.toString -> v): _*)

  implicit def ordering: Ordering[CompanyEstimatedFigures] = new Ordering[CompanyEstimatedFigures] {
    override def compare(x: CompanyEstimatedFigures, y: CompanyEstimatedFigures): Int = {
      val xIndex = values.indexOf(x)
      val yIndex = values.indexOf(y)
      xIndex.compareTo(yIndex)
    }
  }

  def restrictionRow(form: Form[_], allocatedRestrictions: Option[AllocatedRestrictionsModel], idx: Int, userAnswers: UserAnswers)(implicit messages: Messages) = {
    val apRestriction1 = userAnswers.get(RestrictionAmountForAccountingPeriodPage(idx, 1))
    val apRestriction2 = userAnswers.get(RestrictionAmountForAccountingPeriodPage(idx, 2))
    val apRestriction3 = userAnswers.get(RestrictionAmountForAccountingPeriodPage(idx, 3))
    val apTotalRestriction = Seq(apRestriction1, apRestriction2, apRestriction3).flatten.sum

    allocatedRestrictions match {
      case Some(_) => Some(estimatedFigureToCheckboxItem(form, AllocatedRestrictions))
      case None if apTotalRestriction > 0 => Some(estimatedFigureToCheckboxItem(form, AllocatedRestrictions))
      case None => None
    }
  }

}

