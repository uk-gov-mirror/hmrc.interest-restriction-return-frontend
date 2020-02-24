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
import forms.groupStructure.DeemedParentReviewAnswersListFormProvider
import pages.groupStructure.DeemedParentPage
import play.api.data.Form
import play.twirl.api.HtmlFormat
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist.SummaryListRow
import utils.DeemedParentReviewAnswersListHelper
import viewmodels.SummaryListRowHelper
import views.behaviours.YesNoViewBehaviours
import views.html.groupStructure.DeemedParentReviewAnswersListView

class DeemedParentReviewAnswersListViewSpec extends YesNoViewBehaviours with GroupStructureCheckYourAnswersConstants with SummaryListRowHelper {

  val view = viewFor[DeemedParentReviewAnswersListView]()
  val messageKeyPrefix = "deemedParentReviewAnswersList"
  val section = Some(messages("section.groupStructure"))
  val form = new DeemedParentReviewAnswersListFormProvider()()

  s"DeemedParent view" must {

    def applyView(summaryList: Seq[SummaryListRow])(form: Form[_]): HtmlFormat.Appendable =
      view.apply(
        form,
        summaryList,
        onwardRoute
      )(fakeRequest, messages, frontendAppConfig)

    val summaryList = new DeemedParentReviewAnswersListHelper(
      emptyUserAnswers
        .set(DeemedParentPage, deemedParentModelUkCompany, Some(1)).get
        .set(DeemedParentPage, deemedParentModelUkPartnership, Some(2)).get
        .set(DeemedParentPage, deemedParentModelNonUkCompany, Some(3)).get
    ).rows

    behave like normalPage(applyView(summaryList)(form), messageKeyPrefix, section = section)

    behave like yesNoPage(
      form,
      applyView(summaryList),
      messageKeyPrefix,
      expectedFormAction = onwardRoute.url,
      section = section
    )

    behave like pageWithBackLink(applyView(summaryList)(form))

    behave like pageWithSubmitButton(applyView(summaryList)(form), BaseMessages.saveAndContinue)

    behave like pageWithSubHeading(applyView(summaryList)(form), SectionHeaderMessages.groupStructure)

    behave like pageWithSaveForLater(applyView(summaryList)(form))
  }
}
