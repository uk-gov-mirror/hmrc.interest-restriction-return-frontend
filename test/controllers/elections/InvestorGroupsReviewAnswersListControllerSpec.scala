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

package controllers.elections

import assets.constants.InvestorGroupConstants._
import assets.messages.SectionHeaderMessages
import assets.messages.elections.InvestorGroupsReviewAnswersListMessages
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.elections.InvestorGroupsReviewAnswersListFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeElectionsNavigator
import pages.elections.{InvestmentNamePage, InvestorGroupsPage}
import play.api.test.Helpers._
import views.html.elections.InvestorGroupsReviewAnswersListView

class InvestorGroupsReviewAnswersListControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[InvestorGroupsReviewAnswersListView]
  val formProvider = injector.instanceOf[InvestorGroupsReviewAnswersListFormProvider]
  val form = formProvider()

  object Controller extends InvestorGroupsReviewAnswersListController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    updateSectionService = updateSectionService,
    questionDeletionLookupService = questionDeletionLookupService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view,
    navigator = FakeElectionsNavigator,
    formProvider = formProvider
  )

  "Investor Groups Review Answers List Controller" when {

    "calling the onPageLoad() method" when {

      "there are no investor groups in the list" must {

        "return a SEE_OTHER (303)" in {

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onPageLoad()(fakeRequest)

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(FakeElectionsNavigator.addInvestorGroup(0).url)
        }
      }

      "there are investor groups in the list" must {

        "return a OK (200)" in {

          mockGetAnswers(Some(emptyUserAnswers.set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).get))

          val result = Controller.onPageLoad()(fakeRequest)

          status(result) mustEqual OK
          titleOf(contentAsString(result)) mustEqual title(InvestorGroupsReviewAnswersListMessages.title(1), Some(SectionHeaderMessages.elections))
        }
      }
    }

    "calling the onSubmit() method" when {

      "add another investorGroup answer is yes" should {

        "redirect to the Add Investment route" in {

          mockGetAnswers(Some(emptyUserAnswers.set(InvestorGroupsPage, investorGroupsGroupRatioModel, Some(1)).get))

          val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

          val result = Controller.onSubmit()(request)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeElectionsNavigator.addInvestorGroup(1).url)
        }
      }

      "add another investorGroup answer is false" should {

        "redirect to the Next Page route" in {

          mockGetAnswers(Some(emptyUserAnswers))
          mockSetAnswers

          val request = fakeRequest.withFormUrlEncodedBody(("value", "false"))

          val result = Controller.onSubmit()(request)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeElectionsNavigator.nextPage(InvestorGroupsPage, NormalMode, emptyUserAnswers).url)
        }
      }
    }
  }
}
