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

package utils

import controllers.ultimateParentCompany.{routes => ultimateParentCompanyRoutes}
import models.returnModels.DeemedParentModel
import models.{CheckMode, UserAnswers}
import pages.QuestionPage
import pages.ultimateParentCompany._
import play.api.i18n.Messages
import play.api.mvc.Call
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class CheckYourAnswersUltimateParentCompanyHelper(val userAnswers: UserAnswers)
                                                 (implicit val messages: Messages) extends CheckYourAnswersHelper {

  private def deemedParentModel(idx: Int): Option[DeemedParentModel] = userAnswers.get(DeemedParentPage, Some(idx))

  def reportingCompanySameAsParent: Option[SummaryListRow] =
    answer(ReportingCompanySameAsParentPage, ultimateParentCompanyRoutes.ReportingCompanySameAsParentController.onPageLoad(CheckMode))

  def deemedParent: Option[SummaryListRow] =
    answer(HasDeemedParentPage, ultimateParentCompanyRoutes.HasDeemedParentController.onPageLoad(CheckMode))

  def countryOfIncorporation(idx: Int): Option[SummaryListRow] =
    deemedParentModel(idx).flatMap(_.countryOfIncorporation.map(countryOfIncorporation =>
      deemedParentAnswer(
        page = CountryOfIncorporationPage,
        value = countryOfIncorporation.country,
        changeLinkCall = ultimateParentCompanyRoutes.CountryOfIncorporationController.onPageLoad(idx, CheckMode)
      )
    ))

  def parentCompanyCTUTR(idx: Int): Option[SummaryListRow] =
    deemedParentModel(idx).flatMap(_.ctutr.map(ctutr =>
      deemedParentAnswer(
        page = ParentCompanyCTUTRPage,
        value = ctutr.utr,
        changeLinkCall = ultimateParentCompanyRoutes.ParentCompanyCTUTRController.onPageLoad(idx, CheckMode)
      )
    ))

  def parentCompanySAUTR(idx: Int): Option[SummaryListRow] =
    deemedParentModel(idx).flatMap(_.sautr.map(sautr =>
      deemedParentAnswer(
        page = ParentCompanySAUTRPage,
        value = sautr.utr,
        changeLinkCall = ultimateParentCompanyRoutes.ParentCompanySAUTRController.onPageLoad(idx, CheckMode)
      )
    ))

  def payTaxInUk(idx: Int): Option[SummaryListRow] =
    deemedParentModel(idx).flatMap(_.payTaxInUk.map(payTaxInUk =>
      deemedParentAnswer(
        page = PayTaxInUkPage,
        value = payTaxInUk,
        changeLinkCall = ultimateParentCompanyRoutes.PayTaxInUkController.onPageLoad(idx, CheckMode)
      )
    ))

  def limitedLiabilityPartnership(idx: Int): Option[SummaryListRow] =
    deemedParentModel(idx).flatMap(_.limitedLiabilityPartnership.map(limitedLiabilityPartnership =>
      deemedParentAnswer(
        page = LimitedLiabilityPartnershipPage,
        value = limitedLiabilityPartnership,
        changeLinkCall = ultimateParentCompanyRoutes.LimitedLiabilityPartnershipController.onPageLoad(idx, CheckMode)
      )
    ))

  def parentCompanyName(idx: Int): Option[SummaryListRow] =
    deemedParentModel(idx).map( deemedParentModel =>
      deemedParentAnswer(
        ParentCompanyNamePage,
        deemedParentModel.companyName.name,
        ultimateParentCompanyRoutes.ParentCompanyNameController.onPageLoad(idx, CheckMode)
      )
    )

  def rows(idx: Int): Seq[SummaryListRow] = Seq(
    reportingCompanySameAsParent,
    deemedParent,
    parentCompanyName(idx),
    payTaxInUk(idx),
    limitedLiabilityPartnership(idx),
    parentCompanyCTUTR(idx),
    parentCompanySAUTR(idx),
    countryOfIncorporation(idx)
  ).flatten

  def deemedParentAnswer[A](page: QuestionPage[A],
                            value: A,
                            changeLinkCall: Call,
                            answerIsMsgKey: Boolean = false,
                            headingMessageArgs: Seq[String] = Seq())
                           (implicit messages: Messages, conversion: A => String): SummaryListRow =
    summaryListRow(
      label = messages(s"$page.checkYourAnswersLabel", headingMessageArgs: _*),
      value = if (answerIsMsgKey) messages(s"$page.$value") else value,
      changeLinkCall -> messages("site.edit")
    )
}
