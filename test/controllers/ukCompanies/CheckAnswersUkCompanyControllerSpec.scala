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

package controllers.ukCompanies

import assets.constants.fullReturn.UkCompanyConstants._
import assets.messages.CheckAnswersUkCompanyMessages
import base.SpecBase
import config.featureSwitch.FeatureSwitching
import controllers.actions._
import models.NormalMode
import navigation.FakeNavigators.FakeUkCompaniesNavigator
import pages.ukCompanies.{CheckAnswersUkCompanyPage, UkCompaniesPage}
import play.api.test.Helpers._
import views.ViewUtils.addPossessive
import views.html.CheckYourAnswersView

class CheckAnswersUkCompanyControllerSpec extends SpecBase with FeatureSwitching with MockDataRetrievalAction {

  val view = injector.instanceOf[CheckYourAnswersView]

  object Controller extends CheckAnswersUkCompanyController(
    messagesApi = messagesApi,
    sessionRepository = mockSessionRepository,
    questionDeletionLookupService = questionDeletionLookupService,
    updateSectionService = updateSectionService,
    navigator = FakeUkCompaniesNavigator,
    identify = FakeIdentifierAction,
    getData = mockDataRetrievalAction,
    requireData = dataRequiredAction,
    controllerComponents = messagesControllerComponents,
    view = view
  )

  "Check Your Answers Controller" when {

    "calling the onPageLoad() method" must {

      "return a OK (200) when given empty answers" in {

        lazy val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1)).success.value

        mockGetAnswers(Some(userAnswers))

        val result = Controller.onPageLoad(1)(fakeRequest)

        status(result) mustEqual OK
        titleOf(contentAsString(result)) mustEqual title(CheckAnswersUkCompanyMessages.title(addPossessive(ukCompanyModelReactivationMaxIncome.companyDetails.companyName)), Some(ukCompanyModelReactivationMaxIncome.companyDetails.companyName))
      }
    }

    "calling the onSubmit() method" when {

      "given a uk company" when {

        "redirect to the next page in the navigator" in {

          lazy val userAnswers = emptyUserAnswers.set(UkCompaniesPage, ukCompanyModelReactivationMaxIncome, Some(1)).success.value

          mockGetAnswers(Some(userAnswers))
          mockSetAnswers

          val result = Controller.onSubmit(1)(fakeRequest)

          status(result) mustBe SEE_OTHER
          redirectLocation(result) mustBe Some(FakeUkCompaniesNavigator.nextPage(
            page = CheckAnswersUkCompanyPage,
            mode = NormalMode,
            userAnswers = emptyUserAnswers,
            id = Some(1)
          ).url)
        }
      }
    }
  }
}
