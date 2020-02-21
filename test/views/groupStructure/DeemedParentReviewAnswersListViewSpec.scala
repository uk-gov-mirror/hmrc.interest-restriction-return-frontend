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

package views.groupStructure

import assets.constants.DeemedParentConstants.{deemedParentModelNonUkCompany, deemedParentModelUkCompany, deemedParentModelUkPartnership}
import assets.constants.GroupStructureCheckYourAnswersConstants
import assets.messages.{BaseMessages, SectionHeaderMessages}
import models.NormalMode
import pages.groupStructure.DeemedParentPage
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import utils.DeemedParentReviewAnswersListHelper
import viewmodels.SummaryListRowHelper
import views.behaviours.ViewBehaviours
import views.html.groupStructure.DeemedParentReviewAnswersListView

class DeemedParentReviewAnswersListViewSpec extends ViewBehaviours with GroupStructureCheckYourAnswersConstants with SummaryListRowHelper {

  val messageKeyPrefix = "deemedParentReviewAnswersList"
  val section = Some(messages("section.groupStructure"))

  s"DeemedParent view" must {

    def applyView(summaryList: Seq[SummaryListRow]): HtmlFormat.Appendable = {
      val view = viewFor[DeemedParentReviewAnswersListView](Some(emptyUserAnswers))
      view.apply(
        summaryList,
        NormalMode,
        onwardRoute)(fakeRequest, messages, frontendAppConfig)
    }

    val summaryList = new DeemedParentReviewAnswersListHelper(
      emptyUserAnswers
        .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).get
        .set(DeemedParentPage, deemedParentModelUkPartnership, Some(2)).get
        .set(DeemedParentPage, deemedParentModelNonUkCompany, Some(3)).get
    ).rows

    behave like normalPage(applyView(summaryList), messageKeyPrefix, section = section)

    behave like pageWithBackLink(applyView(summaryList))

    behave like pageWithSubmitButton(applyView(summaryList), BaseMessages.saveAndContinue)

    behave like pageWithSubHeading(applyView(summaryList), SectionHeaderMessages.groupStructure)

    behave like pageWithSaveForLater(applyView(summaryList))
  }
}
