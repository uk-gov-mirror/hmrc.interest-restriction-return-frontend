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

package controllers.ukCompanies

import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.SectionHeaderMessages
import assets.messages.ukCompanies.UkCompaniesReviewAnswersListMessages
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import forms.ukCompanies.UkCompaniesReviewAnswersListFormProvider
import models.returnModels.{ReviewAndCompleteModel, SectionState}
import models.{NormalMode, SectionStatus}
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import pages.reviewAndComplete.ReviewAndCompletePage
import pages.ukCompanies.UkCompaniesPage
import play.api.test.Helpers._
import views.html.ukCompanies.UkCompaniesReviewAnswersListView

class UkCompaniesReviewAnswersListControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[UkCompaniesReviewAnswersListView]
  val formProvider = injector.instanceOf[UkCompaniesReviewAnswersListFormProvider]
  val form = formProvider()

  object Controller extends UkCompaniesReviewAnswersListController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeUkCompaniesNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    updateSectionService = updateSectionService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view,
    formProvider = formProvider
  )

  "Uk Companies Review Answers List Controller" when {

    "calling the onPageLoad() method" when {

      "there are no uk companies in the list" must {

        "return a SEE_OTHER (303)" in {

          mockGetAnswers(Some(emptyUserAnswers))

          val result = Controller.onPageLoad()(fakeRequest)

          status(result) mustEqual SEE_OTHER
          redirectLocation(result) mustBe Some(FakeUkCompaniesNavigator.addCompany(0).url)
        }
      }

      "there are uk companies in the list" must {

        "return a OK (200)" in {

          mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get))

          val result = Controller.onPageLoad()(fakeRequest)

          status(result) mustEqual OK
          titleOf(contentAsString(result)) mustEqual title(UkCompaniesReviewAnswersListMessages.title(1), Some(SectionHeaderMessages.ukCompanies))
        }
      }
    }

    "calling the onSubmit() method" when {

      "add another uk company answer is yes" should {

        "redirect to the Add Investment route" in {

          mockGetAnswers(Some(emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelMax, Some(1)).get))

          val request = fakeRequest.withFormUrlEncodedBody(("value", "true"))

          val result = Controller.onSubmit()(request)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeUkCompaniesNavigator.addCompany(1).url)
        }
      }

      "add another uk company answer is false" should {

        "redirect to the Next Page route" in {

          mockGetAnswers(Some(emptyUserAnswers
            .set(ReviewAndCompletePage, ReviewAndCompleteModel(ukCompanies = SectionState(SectionStatus.InProgress, Some(ReviewAndCompletePage)))).get
          ))
          mockSetAnswers

          val request = fakeRequest.withFormUrlEncodedBody(("value", "false"))

          val result = Controller.onSubmit()(request)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeUkCompaniesNavigator.nextPage(UkCompaniesPage, NormalMode, emptyUserAnswers).url)
        }
      }
    }
  }
}
