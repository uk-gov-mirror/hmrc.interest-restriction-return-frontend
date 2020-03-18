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

package controllers.ultimateParentCompany

import assets.messages.{CheckAnswersUltimateParentCompanyMessages, SectionHeaderMessages}
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import models.NormalMode
import navigation.FakeNavigators.FakeUltimateParentCompanyNavigator
import pages.ultimateParentCompany.{CheckAnswersGroupStructurePage, HasDeemedParentPage}
import play.api.test.Helpers._
import views.html.CheckYourAnswersView

class CheckAnswersUltimateParentCompanyControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[CheckYourAnswersView]

  object Controller extends CheckAnswersGroupStructureController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    navigator = FakeUltimateParentCompanyNavigator,
    questionDeletionLookupService = questionDeletionLookupService,
    updateSectionService = updateSectionService,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "Check Your Answers Controller" when {

    "calling the onPageLoad() method" must {

      "return a OK (200) when given empty answers" in {

        mockGetAnswers(Some(emptyUserAnswers))

        val result = Controller.onPageLoad(1)(fakeRequest)

        status(result) mustEqual OK
        titleOf(contentAsString(result)) mustEqual title(CheckAnswersUltimateParentCompanyMessages.title, Some(SectionHeaderMessages.ultimateParentCompany))
      }
    }

    "calling the onSubmit() method" when {

      "given a deemed parent" when {

        "redirect to the next page in the navigator" in {

          lazy val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, true).success.value

          mockGetAnswers(Some(userAnswers))
          mockSetAnswers(true)

          val result = Controller.onSubmit(1)(fakeRequest)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeUltimateParentCompanyNavigator.nextPage(
            page = CheckAnswersGroupStructurePage,
            mode = NormalMode,
            userAnswers = emptyUserAnswers,
            id = Some(1)
          ).url)
        }
      }

      "given a ultimate parent" when {

        "redirect to the next page in the navigator" in {

          val userAnswers = emptyUserAnswers.set(HasDeemedParentPage, false).success.value
          mockGetAnswers(Some(userAnswers))
          mockSetAnswers(true)

          val result = Controller.onSubmit(1)(fakeRequest)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeUltimateParentCompanyNavigator.nextPage(
            page = CheckAnswersGroupStructurePage,
            mode = NormalMode,
            userAnswers = emptyUserAnswers,
            id = Some(1)
          ).url)
        }
      }
    }
  }
}
