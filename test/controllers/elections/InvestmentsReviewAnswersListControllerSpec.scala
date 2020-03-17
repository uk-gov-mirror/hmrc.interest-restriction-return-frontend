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

import assets.constants.NonConsolidatedInvestmentConstants._
import assets.messages.SectionHeaderMessages
import assets.messages.elections.InvestmentsReviewAnswersListMessages
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.elections.InvestmentsReviewAnswersListFormProvider
import models.NormalMode
import navigation.FakeNavigators.FakeElectionsNavigator
import pages.elections.InvestmentNamePage
import play.api.test.Helpers._
import views.html.elections.InvestmentsReviewAnswersListView

class InvestmentsReviewAnswersListControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[InvestmentsReviewAnswersListView]
  val formProvider = injector.instanceOf[InvestmentsReviewAnswersListFormProvider]
  val form = formProvider()

  object Controller extends InvestmentsReviewAnswersListController(
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

  "Investments Review Answers List Controller" when {

    "calling the onPageLoad() method" when {

      "there are no investments in the list" must {

        "return a SEE_OTHER (303)" in {

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onPageLoad()(fakeRequest)

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(FakeElectionsNavigator.addInvestment(0).url)
        }
      }

      "there are investments in the list" must {

        "return a OK (200)" in {

          mockGetAnswers(Some(emptyUserAnswers.set(InvestmentNamePage, investmentName, Some(1)).get))

          val result = Controller.onPageLoad()(fakeRequest)

          status(result) mustEqual OK
          titleOf(contentAsString(result)) mustEqual title(InvestmentsReviewAnswersListMessages.title(1), Some(SectionHeaderMessages.elections))
        }
      }
    }

    "calling the onSubmit() method" when {

      "add another investment answer is yes" should {

        "redirect to the Add Investment route" in {

          mockGetAnswers(Some(emptyUserAnswers.set(InvestmentNamePage, investmentName, Some(1)).get))

          val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

          val result = Controller.onSubmit()(request)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeElectionsNavigator.addInvestment(1).url)
        }
      }

      "add another investment answer is false" should {

        "redirect to the Next Page route" in {

          mockGetAnswers(Some(emptyUserAnswers))
          mockSetAnswers(true)

          val request = fakeRequest.withFormUrlEncodedBody(("value", "false"))

          val result = Controller.onSubmit()(request)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeElectionsNavigator.nextPage(InvestmentNamePage, NormalMode, emptyUserAnswers).url)
        }
      }
    }
  }
}
