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

package viewmodels

import base.SpecBase
import play.api.mvc._
import play.api.mvc.Responses._
import play.api.i18n.MessagesApi
import uk.gov.hmrc.govukfrontend.views.viewmodels.summarylist._
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.{HtmlContent, Text}

class SummaryListRowHelperSpec extends SpecBase {
  
  object TestHelper extends SummaryListRowHelper

  "defaultVisuallyHiddenText" must {

    "default to the label text where hidden text is not passed in" in {
      val label = "Label"
      val visuallyHiddenText = None
      TestHelper.defaultVisuallyHiddenText(label, visuallyHiddenText) mustEqual "Label"
    }

    "return the hidden text where it is passed in" in {
      val label = "Label"
      val visuallyHiddenText = Some("Hidden text")
      TestHelper.defaultVisuallyHiddenText(label, visuallyHiddenText) mustEqual "Hidden text"
    }

  }

  "summaryListRowSpecifyingClass" must  {

    val action = Action { Ok() } 

    "default to the label text where hidden text is not passed in" in {
      val label = "Label"
      val visuallyHiddenText = None
      val expectedActions = Actions(
        items = Seq(ActionItem(
          href = "Some URL",
          content = Text("Change"),
          visuallyHiddenText = Some("Label")
        )),
        classes = "govuk-!-width-one-third"
      )
      val summaryListRowResult = TestHelper.summaryListRow(label, "value", visuallyHiddenText, action -> "Change")
      summaryListRowResult.actions mustEqual expectedActions
    }

    "return the hidden text where it is passed in" in {
      val label = "Label"
      val visuallyHiddenText = Some("Hidden text")
      val expectedActions = Actions(
        items = Seq(ActionItem(
          href = "Some URL",
          content = Text("Change"),
          visuallyHiddenText = Some("Hidden text")
        )),
        classes = "govuk-!-width-one-third"
      )
      val summaryListRowResult = TestHelper.summaryListRow(label, "value", visuallyHiddenText, action -> "Change")
      summaryListRowResult.actions mustEqual expectedActions
    }  
  }

}
