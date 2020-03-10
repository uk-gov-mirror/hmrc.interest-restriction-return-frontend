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

package utils

import models.{NormalMode, UserAnswers}
import pages.elections.PartnershipsPage
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._

class PartnershipsReviewAnswersListHelper(val userAnswers: UserAnswers)
                                         (implicit val messages: Messages) extends CheckYourAnswersHelper {

  def rows: Seq[SummaryListRow] = userAnswers.getList(PartnershipsPage).zipWithIndex.map {
    case (partnership, idx) => summaryListRow(
      partnership.name,
      value = "",
      controllers.elections.routes.PartnershipNameController.onPageLoad(idx +1, NormalMode) -> messages("site.edit"))
      //controllers.elections.routes.PartnershipsDeletionConfirmationController.onPageLoad(idx + 1) -> messages("site.delete")
  }
}
